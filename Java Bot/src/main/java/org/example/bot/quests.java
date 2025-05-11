package org.example.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Random;

public class quests {
    private static Random random = new Random();
    private static boolean questStarted = false;
    private static int currentQuest = 0;

    public static void startQuest(TelegramBot bot, String chatId) {
        int chance = random.nextInt(5);
        questStarted = true;
        currentQuest = 0; // Какой квест сейчас в данный момент


        // Вопросы в квестах
        if (chance == 0) {
            bot.sendResponse(chatId,
                    "Рик открыл портал в измерение разумных носков. Что ты делаешь? \n" +
                            "1. Прыгаю туда и кричу \"АНАРХИЯ!\". \n" +
                            "2. Закрываю портал и валю от сюда \n" +
                            "3. Плюю в портал. \n" +
                            "Выбор может изменить твою судьбу, или просто слить все в унитаз.", new ReplyKeyboardRemove(true));
            return;
        } else if (chance == 1) {
            currentQuest = 1;
            bot.sendResponse(chatId,
                    "Ты находишь кирку, которая орёт при каждом ударе. Причём матом. Твои действия: \n" +
                            "1. Пользуюсь — пусть орет. \n" +
                            "2. Сдаю её в лабораторию Рика. \n" +
                            "3. Ломаю об голову зомби.\n" +
                            "Выбор может изменить твою судьбу, или просто слить все в унитаз.", new ReplyKeyboardRemove(true));
            return;
        } else if (chance == 2) {
            currentQuest = 2;
            bot.sendResponse(chatId,
                    "На полу валяется бургер. Над ним написано: \"НЕ ЕСТЬ!\". Он левитирует. Твои действия: \n" +
                            "1. Съесть. \n" +
                            "2. Спрятать от Морти. \n" +
                            "3. Пнуть в портал. \n" +
                            "Выбор может изменить твою судьбу, или просто слить все в унитаз.", new ReplyKeyboardRemove(true));
            return;
        } else if (chance == 3) {
            currentQuest = 3;
            bot.sendResponse(chatId,
                    "Унитаз издаёт сигналы Морзе. Он просит о помощи. Твои действия: \n" +
                            "1. Ответить сигналом. \n" +
                            "2. Спустить воду. \n" +
                            "3. Выломать унитаз и унести. \n" +
                            "Выбор может изменить твою судьбу, или просто слить все в унитаз.", new ReplyKeyboardRemove(true));
        } else if (chance == 4) {
            currentQuest = 4;
            bot.sendResponse(chatId,
                    "К тебе подошёл крипер в мантии. Он говорит: \"Я взрываю шаблоны, не людей.\". Твои действия: \n" +
                            "1. Обнять \n" +
                            "2. Попросить лекцию \n" +
                            "3. Убить на всякий случай \n" +
                            "Выбор может изменить твою судьбу, или просто слить все в унитаз. \n", new ReplyKeyboardRemove(true));
        } else if (chance == 5) {
            currentQuest = 5;
            bot.sendResponse(chatId,
                    "Рик открыл портал прямо в этом чате. Он пульсирует. Подпись: \"Только психи войдут\". Твои действия: \n" +
                            "1. Вхожу, не думая. \n" +
                            "2. Фоткаю на память. \n" +
                            "3. Закрываю, как будто ничего не было. \n", new ReplyKeyboardRemove(true));
        }
    }



    public static void handleUserResponce(TelegramBot bot, String chatId, String userInput) {
        if (!questStarted) {
            return;
        }


        // Ответы на первый квест
        if(currentQuest == 0) {
            if (userInput.equals("1")) {
                bot.sendResponse(chatId, "Да ты псих, обожаю это! Носки чуть не сожрали тебя, \n" +
                        "но ты вынес оттуда амулет с вонью космического уровня. +50 шизо коинов и привкус безумия. " +
                        "Добро пожаловать в клуб, анархист.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("2")) {
                bot.sendResponse(chatId, "Рационально. Скучно, но рационально. \n" +
                        "Ты не умер, зато пропустил шанс подружиться с вонючими богами. \n " +
                        "Получи +10 коинов за трусость. Можешь купить себе пакетик самоуважения", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("3")) {
                bot.sendResponse(chatId, "О, дерзкий выбор. \n" +
                        "Один из носков вылез и обосновался в твоём инвентаре." +
                        "Он постоянно ворует тебе вещи. Поздравляю, теперь у тебя компаньон..." +
                        "И -20 коинов. В следующий раз плюй в зеркало", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }


        // Ответы на второй квест
        if (currentQuest == 1) {
            if (userInput.equals("1")) {
                bot.sendResponse(chatId, "Ты просверлил шахту и психику всех поблизости. \n " +
                        "+25 коинов и бан на ближайшую свадьбу.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("2")) {
                bot.sendResponse(chatId, "Рик оценил находку, теперь у него кирка-диджей. \n" +
                        "+40 коинов и 1 случайный предмет.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("3")) {
                bot.sendResponse(chatId, "Зомби оказался философом. Кирка разбилась, а ты получил пинок в мозг. \n" +
                        "-15 коинов и странный флешбек.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }


        // Ответы на третий квест
        if(currentQuest == 2) {
            if(userInput.equals("1")) {
                bot.sendResponse(chatId, "Ты съел бургер. Теперь ты не подчиняешься гравитации. \n" +
                        "+50 коинов и минус контакт с землёй на 5 минут", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("2")) {
                bot.sendResponse(chatId, "Морти тронут... и теперь обязан тебе. \n" +
                        "+1 'долг Морти' и +20 коинов.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("3")) {
                bot.sendResponse(chatId, "Бургер врезался в галактического шеф-повара. \n + " +
                        "Ты получил его злость и +1 перец чили из ада.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }


        // Ответы на третий квест
        if(currentQuest == 3) {
            if(userInput.equals("1")) {
                bot.sendResponse(chatId, "Ты завёл переписку с разумной канализацией. \n" +
                        "+30 коинов и контакт \"Господин Унитаз\".", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("2")) {
                bot.sendResponse(chatId, "Теперь у тебя дома говорящий унитаз. \n" +
                        "Он не замолкает. +1 странный предмет", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("3")) {
                bot.sendResponse(chatId, "Теперь у тебя дома говорящий унитаз. \n" +
                        "Он не замолкает. +1 странный предмет", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }

        // Ответы на четвертый квест
        if(currentQuest == 4) {
            if(userInput.equals("1")) {
                bot.sendResponse(chatId, "Вы оба взорвались от эмоций. -10 хп, +50 коинов за гуманизм.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("2")) {
                bot.sendResponse(chatId, "Теперь ты знаешь, как устроена философия крипера. +1 мудрость, -20 интерес к жизни.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("3")) {
                bot.sendResponse(chatId, "Ты жив. Но тебя осудил Совет Криперов. -30 коинов и письмо от юриста.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }

        // Ответы на пятый квест
        if(currentQuest == 5) {
            if(userInput.equals("1")) {
                bot.sendResponse(chatId, "Ты оказался в измерении Бесконечной Токсичности. \n" +
                        "+100 коинов и иммунитет к буллингу.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("2")) {
                bot.sendResponse(chatId, "Твоя фотка взорвала внутренний портал-инстаграм. \n" +
                        "+10 лайков и +20 коинов.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("3")) {
                bot.sendResponse(chatId, "Ты — единственный, кто проигнорировал портал. \n" +
                        "Рик записал тебя в список 'Скучных'. -10 коинов.", new ReplyKeyboardRemove(true));
                questStarted = false;
                bot.setQuestActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else {
                bot.sendResponse(chatId, "Пожалуйста, напишите вариант ответа в виде числа. Или напишите: Отмена, чтобы выйти с квеста", new ReplyKeyboardRemove(true));
                if(userInput.equals("Отмена")) {
                    questStarted = false;
                    bot.setQuestActive(Long.parseLong(chatId), false);
                    bot.sendMainMenu(Long.parseLong(chatId));
                }
                return;
            }
        }



        questStarted = false;
        bot.setQuestActive(Long.parseLong(chatId), false);

        bot.sendResponse(chatId,
                "Квест завершен! Вы можете использовать следующие команды:\n" +
                        "/привязать - привязать аккаунт\n" +
                        "/досье - получить информацию о себе\n" +
                        "/квест - начать новый квест\n" +
                        "/промо - использовать промокод", new ReplyKeyboardRemove(true));
        bot.removeKeyboard(Long.parseLong(chatId));
        bot.sendMainMenu(Long.parseLong(chatId));
    }
}
