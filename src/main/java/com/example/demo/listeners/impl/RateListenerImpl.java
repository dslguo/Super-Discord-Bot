package com.example.demo.listeners.impl;

import com.example.demo.listeners.RateListener;
import com.example.demo.services.MessagingService;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RateListenerImpl implements RateListener {
    private final static Pattern pattern = Pattern.compile("!rate (\\w+)");

    @Autowired
    private MessagingService messagingService;

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(messageCreateEvent.getMessageContent().startsWith("!rate")) {
            Matcher matcher = pattern.matcher(messageCreateEvent.getMessageContent());
            if (matcher.matches()) {
                // Do the rating
                int rating = (int) Math.floor(Math.random() * 100) + 1;

                messagingService.sendMessage(messageCreateEvent.getMessageAuthor(),
                        "Rate calculator",
                        messageCreateEvent.getMessageAuthor().getDisplayName() + " is " + rating + "% " + matcher.group(1),
                        "Rate again?",
                        "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/apple/285/collision_1f4a5.png",
                        messageCreateEvent.getChannel());
            } else {
                // Send helpful syntax message
                messagingService.sendMessage(messageCreateEvent.getMessageAuthor(),
                        "Rate calculator",
                        "Are you trying to use the '!rate' command? Please use the syntax '!rate [word]'. Thanks!",
                        "Rate again?",
                        "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/160/apple/285/thinking-face_1f914.png",
                        messageCreateEvent.getChannel(), true);
            }
        }

    }
}
