package com.cole.dominionpunishments;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import com.cole.dominionpunishments.listeners.PunishmentListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class DominionPunishments extends JavaPlugin {

    private WebhookClientBuilder builder;
    private WebhookClient client;
    private PunishmentListener punishmentListener = new PunishmentListener();

    @Getter
    private static DominionPunishments instance;

    @Override
    public void onEnable() {
        getLogger().info("Starting Punishments");
        instance = this;
        super.onEnable();
        startWebhook();
        punishmentListener.registerEvents();
    }

    @Override
    public void onDisable() {
        client.close();
    }

    public void startWebhook() {
        builder = new WebhookClientBuilder("https://discord.com/api/webhooks/1050150332216381440/E3EFbCtxffljL16hGs6bFgT7X2p6zTQ8fHrNkjTACKgviMY4IzBohQ6WJo-uWikK3Ent");
        builder.setThreadFactory(job -> {
            Thread thread = new Thread(job);
            thread.setName("Hello");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        client = builder.build();
    }

    public WebhookClient getClient() {
        return client;
    }
}
