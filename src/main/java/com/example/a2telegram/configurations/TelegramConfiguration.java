package com.example.a2telegram.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfiguration {

  @Value("${telegram.bot.token}")
  private String token;

  @Bean @Primary
  public TelegramClient createTelegramClient() {
    return new OkHttpTelegramClient(token);
  }
}
