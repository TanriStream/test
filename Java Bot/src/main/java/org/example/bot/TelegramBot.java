package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TelegramBot extends TelegramLongPollingBot {
    private Map<Long, Boolean> questActive = new HashMap<>();
    private Map<Long, Boolean> portalActive = new HashMap<>();
    private Map<Long, Long> lastCommandTime = new HashMap<>();
    private final long delayInSeconds = 3;
    private Random random = new Random();
    private Map<Long, Integer> meaningfulMessageCount = new HashMap<>();
    private userState userStateInstance = new userState();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Set<Long> welcomedUsers = new HashSet<>();
    private Long chatId;
    private String lastMessage = "";
    private Map<Long, Integer> messageCount = new HashMap<>();
    private Map<Long, Integer> userStartCount = new HashMap<>();

    public TelegramBot() {
        startScheduler();
    }

    @Override
    public String getBotUsername() {
        return "RAMC137_bot";
    }

    @Override
    public String getBotToken() {
        return "7852055428:AAEuG6aCPNapZDIgLmFTkTW28uuH2QDyTQc";
    }

    private void handleStartCommand(long chatId) {
        if (!welcomedUsers.contains(chatId)) {
            SendMessage welcomeMessage = new SendMessage();
            welcomeMessage.setChatId(String.valueOf(chatId));
            welcomeMessage.setText("Ты зашёл — назад пути нет. Это не просто бот. Это — Рик-Бот!\n" +
                                   "50% хаоса, 50% сарказма и 100% анархии.\n" +
                                   "\n" +
                                   "Команды, без которых ты просто тупой крипер:\n" +
                                   "\n" +
                                   "/привязать — \uD83D\uDD17 Привязка телеги к серверу R.A.M.C. — ты теперь официально в замесе\n" +
                                   "\n" +
                                   "/досье — \uD83D\uDCCB Твоё шизо-досье: коины, ранг, уровень психоза\n" +
                                   "\n" +
                                   "/квест — ⚠\uFE0F Рандомный квест от Рика. Пахнет смертельно… и весело\n" +
                                   "\n" +
                                   "/промо — \uD83C\uDF81 Вводишь код — получаешь ништяк. Или бан. Смотря какой код\n" +
                                   "\n" +
                                   "Этот бот не поможет тебе выжить. Он поможет тебе ПРОКАЧАТЬ СМЕРТЬ.\n" +
                                   "Добро пожаловать в R.A.M.C. — измерение, где логика отдыхает, а безумие работает.");

            try {
                execute(welcomeMessage);
                welcomedUsers.add(chatId);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        sendMainMenu(chatId);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();

            messageCount.put(chatId, messageCount.getOrDefault(chatId, 0) + 1);
            int count = messageCount.get(chatId);

            if (count % 69 == 0) {
                sendResponse(chatId.toString(), "Ты написал " + count + " сообщений за день. Это… сексуально странно. +6.9 коина.", new ReplyKeyboardRemove(true));
            }
        }

        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            switch (callbackData) {
                case "button_dosie":
                    handleDosieCommand(update);
                    break;
                case "button_kvest":
                    if (!questActive.getOrDefault(chatId, false)) {
                        handleQuestCommand(update);
                    }
                    break;
                case "button_portal":
                    if(!portalActive.getOrDefault(chatId, false)) {
                        handlePortalCommand(update);
                    }
                default:
                    sendResponse(chatId.toString(), "Неизвестная команда.", new ReplyKeyboardRemove(true));
                    break;
            }
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String[] parts = command.split(" ");
            String userInput = update.getMessage().getText();
            long currentTime = System.currentTimeMillis();

            if (command.equals("/start")) {
                handleStartCommand(chatId);
                sendResponse(chatId.toString(), getMessageForTimeOfDay(), new ReplyKeyboardRemove(true));

                int count = userStartCount.getOrDefault(chatId, + 0) + 1;
                userStartCount.put(chatId, count);
                int chance = random.nextInt(2);

                if(count != 1 && chance == 0) {
                    sendResponse(chatId.toString(), "О, ты снова тут. Не могу поверить, что мультивселенная породила ТЕБЯ.", new ReplyKeyboardRemove(true));
                } else if (count != 1 && chance == 1) {
                    sendResponse(chatId.toString(), "Сервер ждёт тебя. А я — твоего фейла.", new ReplyKeyboardRemove(true));
                }
            }

            // Проверка времени последней команды
            if (lastCommandTime.containsKey(chatId) &&
                    (currentTime - lastCommandTime.get(chatId)) < TimeUnit.SECONDS.toMillis(delayInSeconds)) {
                long remainingTime = delayInSeconds - (currentTime - lastCommandTime.get(chatId)) / 1000;
                sendResponse(chatId.toString(), "Пожалуйста, не спамьте и подождите еще " + remainingTime + " секунд.", new ReplyKeyboardRemove(true));
                int chance = random.nextInt(3);
                if(chance == 0) {
                    sendResponse(chatId.toString(), "За активность +3 коина и виртуальный подзатыльник.", new ReplyKeyboardRemove(true));
                } else if(chance == 1) {
                    sendResponse(chatId.toString(), "Ты так много пишешь, что даже я устал читать. Молодец.", new ReplyKeyboardRemove(true));
                } else if (chance == 2) {
                    sendResponse(chatId.toString(), "Если бы слова были валютой — ты бы купил себе отдельную вселенную.", new ReplyKeyboardRemove(true));
                } else if (chance == 3) {
                    sendResponse(chatId.toString(), "Ты заслужил респект Рика. Лови временный титул: Флудоносец Вселенной-137", new ReplyKeyboardRemove(true));
                }
                return;
            } else {
                lastCommandTime.put(chatId, currentTime);

                if(isMeaningfulMessage(command)) {
                    int count = meaningfulMessageCount.getOrDefault(chatId, 0) + 1;
                    meaningfulMessageCount.put(chatId, count);

                    if (count == 3) {
                        sendResponse(chatId.toString(), "О, ты не флудишь, ты живёшь. Держи бонус, +2 коина.", new ReplyKeyboardRemove(true));
                        meaningfulMessageCount.put(chatId, 0);
                    }
                }
            }
            lastCommandTime.put(chatId, currentTime);

            if (questActive.getOrDefault(chatId, false)) {
                quests.handleUserResponce(this, chatId.toString(), command);
                return;
            }

            if (portalActive.getOrDefault(chatId, false)) {
                portal.handleUserResponce(this, chatId.toString(), command);
                return;
            }

            switch (parts[0]) {
                case "/start":
                    handleStartCommand(chatId);
                    break;
                case "/привязать":
                    handleBindCommand(update);
                    break;
                case "/досье":
                    handleDosieCommand(update);
                    break;
                case "/квест":
                    handleQuestCommand(update);
                    removeKeyboard(chatId);
                    break;
                case "/портал":
                    handlePortalCommand(update);
                    break;
                case "/пасхалка":
                    if(parts.length > 1 && parts[1].equals("шизо_шахта")) {
                        sendResponse(update.getMessage().getChatId().toString(),
                                "Ты нашёл древнюю кирку ПсихоРика. +3 коина и аудиофайл с его криком.", new ReplyKeyboardRemove(true));
                    } else if (parts.length > 1 && parts[1].equals("я_не_бот")) {
                        sendResponse(update.getMessage().getChatId().toString(),
                                "Конечно, не бот. Вот тебе случайный предмет: Жидкость из носа Морти. Не благодари.", new ReplyKeyboardRemove(true));
                    } else if(parts.length > 1 && parts[1].equals("pickle_rick")) {
                        sendResponse(update.getMessage().getChatId().toString(),
                                "ДА! Я — солёный огурец! И ты тоже теперь. Выдаётся роль Огуречный фанат на 24 часа.", new ReplyKeyboardRemove(true));
                    } else if(parts.length > 1 && parts[1].equals("портал_в_ботинок")) {
                        sendResponse(update.getMessage().getChatId().toString(), "Ты нашёл скрытую ссылку на... ботинок. Вот тебе +13 коина и титул Тот, кто нюхал пыль измерений.", new ReplyKeyboardRemove(true));
                    } else {
                        sendResponse(update.getMessage().getChatId().toString(), "Пожалуйста, укажите корректную пасхалку, например: " +
                                                                                      "/пасхалка портал_в_ботинок", new ReplyKeyboardRemove(true));
                    }
                    break;
                case "/промо":
                    if(parts.length > 1) {
                        sendResponse(update.getMessage().getChatId().toString(), "Награда за промокод выдана.", new ReplyKeyboardRemove(true));
                    } else {
                        sendResponse(update.getMessage().getChatId().toString(), "Пожалуйста, укажите корректный промокод, например: \n" +
                                "/промо *промокод*", new ReplyKeyboardRemove(true));
                    }
                    break;
                default:
                    sendResponse(update.getMessage().getChatId().toString(), "Неизвестная команда. Вот список всех команд:\n " +
                            "1. /привязать - привяать аккаунт в телеграм на сервер\n" +
                            "2. /досье - \n" +
                            "3. /квест - \n" +
                            "4. /промо - \n" +
                            "5. /пасхалка - \n" +
                            "6. /портал - ", new ReplyKeyboardRemove(true));
                    // Нужно дописать/переписать, хз

                    int chance = random.nextInt(3);

                    if(chance == 0) {
                        sendResponse(update.getMessage().getChatId().toString(), "Ты серьёзно? Это не команда. Это позор на клавиатуре.", new ReplyKeyboardRemove(true));
                    } else if (chance == 1) {
                        sendResponse(update.getMessage().getChatId().toString(), "Эта команда настолько мертва, что даже Морти её не воскресит.", new ReplyKeyboardRemove(true));
                    } else if (chance == 2) {
                        sendResponse(update.getMessage().getChatId().toString(), "Мда... Лучше бы ты промолчал. Но ладно, ты хотя бы пытался.", new ReplyKeyboardRemove(true));
                    }
            }
        }
    }

    private void startScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            if(chatId != null) {
                String message = getMessageForTimeOfDay();
                if(!message.equals(lastMessage)) {
                    sendResponse(chatId.toString(), message, new ReplyKeyboardRemove(true));
                    lastMessage = message;
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private String getMessageForTimeOfDay() {
        LocalTime now = LocalTime.now();

        now = now.plusHours(3);

        if (now.isBefore(LocalTime.NOON)) {
            return "Проснулся? Мир снова в заднице. Давай /квест — и может станет веселее.";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            return "Полдень — время для квестов, шизо и бессмысленного насилия. Вперёд!";
        } else if (now.isAfter(LocalTime.of(21, 0)) || now.isBefore(LocalTime.of(6, 0))) {
            return "Ночь. Самое время устроить анархию. Или хотя бы посмотреть, кто сдох сегодня.";
        }
        return "";
    }

    private boolean isMeaningfulMessage(String message) {
        return message.length() > 5;
    }


    private void handlePortalCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        portal.startPortal(this, update.getMessage().getChatId().toString());
        portalActive.put(update.getMessage().getChatId(), true);
        sendQuestKeyboard(chatId);
    }

    // Меню команд
    public void sendMainMenu(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add("/привязать");
        row1.add("/досье");

        row2.add("/квест");
        row2.add("/промо");

        row3.add("/портал");
        row3.add("/пасхалка");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите команду:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    // Меню выбора ответов
    private void sendQuestKeyboard(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("1");
        row.add("2");
        row.add("3");

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Выберите вариант ответа:");
        message.setReplyMarkup(keyboardMarkup);

        try {
            this.execute(message);
        } catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }

    public void removeKeyboard(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setReplyMarkup(new ReplyKeyboardRemove(true));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setQuestActive(Long chatId, boolean isActive) {
        questActive.put(chatId, isActive);
    }

    public void setPortalActive(Long chatId, boolean isActive) {
        portalActive.put(chatId, isActive);
    }

    private void handleQuestCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        quests.startQuest(this, update.getMessage().getChatId().toString());
        questActive.put(update.getMessage().getChatId(), true);
        sendQuestKeyboard(chatId);
    }

    private void handleDosieCommand(Update update) {
        sendResponse(update.getMessage().getChatId().toString(),
                "Личное досье: \n" +
                "Ник: \n" /* Плейсхолдер ника */ +
                "Ранг: \n" +
                "Баланс шизо коинов: \n" +
                "Количество убийст: \n" +
                "Розыск: \n" +
                "Прогресс квеста: \n", new ReplyKeyboardRemove(true));

    }

    private void handleBindCommand(Update update) {
        // Логика привязки аккаунта
        String uniqueCode = generateUniqueCode();
        sendResponse(update.getMessage().getChatId().toString(), "Вот твой уникальный код: " + uniqueCode + ". Введи его на сервере.", new ReplyKeyboardRemove(true));
    }

    void sendResponse(String chatId, String text, ReplyKeyboardRemove replyKeyboardRemove) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUniqueCode() {
        // Генерация кода
        return "/code " + System.currentTimeMillis();
    }
}

class userState {
    private static Map<Long, String> userStates = new HashMap<>(); // Сделать это статическим

    public static void setUserState(Long userId, String state) {
        userStates.put(userId, state);
    }

    public String getUserState(Long userId) {
        return userStates.get(userId);
    }

    public void clearUserState(Long userId) {
        userStates.remove(userId);
    }
}