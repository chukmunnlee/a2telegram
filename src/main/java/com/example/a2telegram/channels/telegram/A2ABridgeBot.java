package com.example.a2telegram.channels.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Locality;
import org.telegram.telegrambots.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

public class A2ABridgeBot extends AbilityBot {

  private static final Logger logger = LoggerFactory.getLogger(A2ABridgeBot.class);

  private final long admin;

  public A2ABridgeBot(String botName, long admin, TelegramClient client) {
    super(client, botName);
    this.admin = admin;
  }

  @Override
  public long creatorId() {
    return admin;
  }

  public Ability startSession() {
    return Ability.builder()
        .name("new_session")
        .info("Create a new session")
        .locality(Locality.ALL)
        .privacy(Privacy.PUBLIC)
        .action(ctx -> {
          var update = ctx.update();
          if (!update.hasMessage() || !update.getMessage().hasText())
            return;
          var msg = update.getMessage();
          Ulid sessionId =UlidCreator.getUlid();
          silent.send("New session started: %s".formatted(sessionId.toString()), msg.getChatId());
        })
        .build();
  }

}
