package com.example.a2telegram.channels.telegram;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class BotMessageProcessor implements LongPollingUpdateConsumer {

  private static final Logger logger = LoggerFactory.getLogger(BotMessageProcessor.class);

  @Autowired
  private TelegramClient client;

  @Override
  public void consume(final List<Update> updates) {

    for (Update update: updates) {
      if (!update.hasMessage() || !update.getMessage().hasText())
        continue;

      var msg = update.getMessage();
      var chatId = msg.getChatId();
      var text = msg.getText();

      logger.info("chatId: {} message: {}", chatId, text);

      try {
        send(SendMessage.builder()
          .chatId(chatId)
          .text("%s: %s".formatted(new Date(), text.toUpperCase()))
          .build());
      } catch (TelegramApiException ex) {
        logger.error("Failed to response to {}".formatted(chatId), ex);
      }
    }
  }

  public void send(SendMessage msg) throws TelegramApiException{
    client.execute(msg);
  }

}
