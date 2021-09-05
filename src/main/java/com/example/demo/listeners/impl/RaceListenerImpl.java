package com.example.demo.listeners.impl;

import com.example.demo.listeners.RaceListener;
import com.example.demo.services.MessagingService;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RaceListenerImpl implements RaceListener {
    public static boolean active = false;

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if (messageCreateEvent.getMessageContent().equals("!race")) {
            if (!active) {
                active = true;
                messagingService.sendMessage(messageCreateEvent.getMessageAuthor(),
                                "The race begins!",
                                "Be the first to **react** to this message and win!",
                                null,
                                "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/openmoji/292/horse-racing_1f3c7.png",
                                messageCreateEvent.getChannel())
                        .thenAccept(message -> {
                            message.addReactionAddListener(listener -> {
                                if (active) {
                                    listener.requestUser().thenAccept(user -> {
                                        message.edit(new EmbedBuilder()
                                                .setTitle("The race ends!")
                                                .setDescription("Congratulations! **" + user.getName() + "** was first!\nThe race is now over!")
                                                .setFooter("Race again?")
                                                .setThumbnail("https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/apple/285/money-bag_1f4b0.png"));
                                        active = false;
                                    });
                                }
                            });
                        });
            }
        }
    }
}
