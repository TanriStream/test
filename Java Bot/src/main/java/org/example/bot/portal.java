package org.example.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Random;

public class portal {
    private static Random random = new Random();
    private static boolean portalStarted = false;
    private static int currentPortal = 0;

    public static void startPortal(TelegramBot bot, String chatId) {
        int chance = random.nextInt(2);
        portalStarted = true;
        currentPortal = 0; // Какой портал сейчас в данный момент

        // Вопросы в порталах
        if (chance == 0) {
            bot.sendResponse(chatId,
                    "Ты стоишь перед трещащим порталом. Что делать?\n" +
                         "1. Засунуть туда носок \n" +
                         "2. Войти с криком \"ПО МНЕ СЛЕЖАТ!\" \n" +
                         "3. Послать туда своего Морти \n", new ReplyKeyboardRemove(true));
            return;
        } else if (chance == 1) {
            currentPortal = 1;
            bot.sendResponse(chatId,
                    "Рик открыл портал. Куда ты ныряешь? \n" +
                         "В измерение разумных кактусов \n" +
                         "В логово ходячих сундуков \n" +
                         "Назад в спавн (но уже проклятый)", new ReplyKeyboardRemove(true));
        }
    }

    public static void handleUserResponce(TelegramBot bot, String chatId, String userInput) {
        if (!portalStarted) {
            return;
        }

        // Ответы на первый портал
        if (currentPortal == 0) {
            if (userInput.equals("1")) {
                bot.sendResponse(chatId, "Носок: «Ты только что основал новое измерение. Добро пожаловать, Бог носков. +5 коина.»",
                                              new ReplyKeyboardRemove(true));
                portalStarted = false;
                bot.setPortalActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("2")) {
                bot.sendResponse(chatId, "Войти: «Теперь у тебя 3 глаза. Один из них видит внутреннюю боль. Получи баф: Просветление.»",
                                              new ReplyKeyboardRemove(true));
                portalStarted = false;
                bot.setPortalActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if(userInput.equals("3")) {
                bot.sendResponse(chatId, "Теперь у тебя 3 глаза. Один из них видит внутреннюю боль. Получи баф: Просветление.",
                                              new ReplyKeyboardRemove(true));
                portalStarted = false;
                bot.setPortalActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            }
        }

        if (currentPortal == 1) {
            if (userInput.equals("1")) {
               bot.sendResponse(chatId,
                       "Они тебя признали. +10 коина и зуд в штанах.",
                            new ReplyKeyboardRemove(true));
               portalStarted = false;
               bot.setPortalActive(Long.parseLong(chatId), false);
               bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("2")) {
                bot.sendResponse(chatId,
                        "Ты украл у них душу. Получаешь временную метку \"Вор №∞\".",
                             new ReplyKeyboardRemove(true));
                portalStarted = false;
                bot.setPortalActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            } else if (userInput.equals("3")) {
                bot.sendResponse(chatId,
                        "Ты проснулся в своём же привате. Вся броня — из хлеба. Удачи.",
                             new ReplyKeyboardRemove(true));
                portalStarted = false;
                bot.setPortalActive(Long.parseLong(chatId), false);
                bot.sendMainMenu(Long.parseLong(chatId));
            }
        }

        portalStarted = false;
        bot.setQuestActive(Long.parseLong(chatId), false);

        bot.sendResponse(chatId,
                "Портал завершен! Вы можете использовать следующие команды:\n" +
                        "/привязать - привязать аккаунт\n" +
                        "/досье - получить информацию о себе\n" +
                        "/квест - начать новый квест\n" +
                        "/промо - использовать промокод" +
                        "/портал - начать портал", new ReplyKeyboardRemove(true));
        bot.removeKeyboard(Long.parseLong(chatId));
        bot.sendMainMenu(Long.parseLong(chatId));
    }
}
