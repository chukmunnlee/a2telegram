package com.example.a2telegram.channels.telegram;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class BotMain {

  private Logger logger = LoggerFactory.getLogger(BotMain.class);

  @Value("${telegram.bot.token}")
  private String botToken;

  @Value("${telegram.bot.name}")
  private String botName;

  @Value("${telegram.bot.admin}")
  private String admin;

  @Autowired
  private TelegramClient telegramClient;

  private TelegramBotsLongPollingApplication app = null;
  private A2ABridgeBot bot = null;

  @EventListener(ApplicationReadyEvent.class)
  public void altStartup() {
    logger.info("Starting Telegram long poll");
    app = new TelegramBotsLongPollingApplication();

    bot = new A2ABridgeBot(botName, Long.parseLong(admin), telegramClient);
    bot.onRegister();

    try {
      app.registerBot(botToken, bot);
      SendMessage msg = SendMessage.builder()
          .chatId(admin)
          .text("Bot starting on %s".formatted(new Date()))
          .build();
      telegramClient.execute(msg);
    } catch (TelegramApiException ex) {
      logger.error("Fail to register bot message processor", ex);
    }

  }

  @EventListener(ContextClosedEvent.class)
  public void shutdown() {
    logger.info("Shutting down bot");
    try {
      SendMessage msg = SendMessage.builder()
          .chatId(admin)
          .text("Bot stopping on %s".formatted(new Date()))
          .build();
      telegramClient.execute(msg);

      bot.close();
    } catch (Exception ex) {
      logger.error("Fail to shutdown bot", ex);
    }
  }

}
