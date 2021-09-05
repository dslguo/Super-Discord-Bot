package com.example.demo.services;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

public interface MessagingService {

    void sendMessage(MessageAuthor author, String title, String description, String footer, String thumbnail, TextChannel channel);
}
