package com.cole.dominionpunishments.listeners;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.cole.dominionpunishments.DominionPunishments;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PunishmentListener {

    WebhookClient client;
    private final String iconURL = "https://cdn.discordapp.com/icons/1042601380944294031/b2f38cf6400927af7b70350ddf53a24c.png";

    public void registerEvents() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                if(entry == null) return;


                client = DominionPunishments.getInstance().getClient();

                WebhookEmbed.EmbedAuthor author = new WebhookEmbed.EmbedAuthor("DominionMC - Punishments", "https://minotar.net/helm/" + entry.getUuid(), iconURL);
                System.out.println(author.getIconUrl());

                long Hours = TimeUnit.MILLISECONDS.toHours(entry.getDuration());
                long Minutes = TimeUnit.MILLISECONDS.toMinutes(entry.getDuration()) % 60;
                long Seconds = TimeUnit.MILLISECONDS.toSeconds(entry.getDuration()) % 60;

                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getUuid()));

                String type =  entry.getType().substring(0,1).toUpperCase() + entry.getType().substring(1);

                WebhookEmbedBuilder embed = new WebhookEmbedBuilder()
                        .setAuthor(author)
                        .setThumbnailUrl("https://minotar.net/helm/" + entry.getUuid())
                        .addField(new WebhookEmbed.EmbedField(false,"User: ", player.getName()))
                        .addField(new WebhookEmbed.EmbedField(false, "Type: ", type));
                        if(String.format("%02d:%02d:%02d",Hours,Minutes,Seconds).equals("00:00:00"))
                            embed.addField(new WebhookEmbed.EmbedField(false,"Duration: ", "Permanent"));
                        else
                            embed.addField(new WebhookEmbed.EmbedField(false,"Duration: ", String.format("%02d:%02d:%02d",Hours,Minutes,Seconds)));
                        embed.addField(new WebhookEmbed.EmbedField(false,"Reason: ", entry.getReason()))
                        .addField(new WebhookEmbed.EmbedField(false,"Punished By: ", entry.getExecutorName()))
                        .setTimestamp(Instant.now());

                if(entry.getType().equalsIgnoreCase("kick")) {
                    return;
                }

                if(entry.getType().equalsIgnoreCase("ban") || entry.getType().equalsIgnoreCase("tempban")) {
                    embed.setColor(0xa80000);
                }

                if(entry.getType().equalsIgnoreCase("mute")) {
                    embed.setColor(0xE67E22);
                }

                client.send(embed.build());
            }

            @Override
            public void entryRemoved(Entry entry) {

                if(entry == null) return;

                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(entry.getUuid()));

                WebhookEmbedBuilder embed = new WebhookEmbedBuilder()
                        .setAuthor(new WebhookEmbed.EmbedAuthor("DominionMC - Punishments", iconURL,iconURL))
                        .setThumbnailUrl("https://minotar.net/helm/" + entry.getUuid())
                        .addField(new WebhookEmbed.EmbedField(false,"User: ", player.getName()))
                        .setColor(0x00a80b)
                        .setTimestamp(Instant.now());

                if(entry.getType().equalsIgnoreCase("ban")) embed.addField(new WebhookEmbed.EmbedField(false,"Type: ", "Unban"));

                if(entry.getType().equalsIgnoreCase("mute")) embed.addField(new WebhookEmbed.EmbedField(false,"Type: ", "Unmute"));

                WebhookEmbed finished = embed.build();
                client.send(finished);
            }
        });
    }

}
