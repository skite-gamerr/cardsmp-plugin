package com.cardsmp;

import com.cardsmp.abilities.AbilityManager;
import com.cardsmp.abilities.CooldownManager;
import com.cardsmp.cards.CardManager;
import com.cardsmp.commands.CardCommand;
import com.cardsmp.config.ConfigManager;
import com.cardsmp.data.PlayerDataManager;
import com.cardsmp.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class CardSMP extends JavaPlugin {

    private static CardSMP instance;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private CardManager cardManager;
    private AbilityManager abilityManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        cardManager = new CardManager(this);
        abilityManager = new AbilityManager(this);
        cooldownManager = new CooldownManager(this);

        getCommand("card").setExecutor(new CardCommand(this));
        registerListeners();

        getLogger().info("CardSMP v1.0.0 enabled!");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            playerDataManager.saveAll();
        }
        getLogger().info("CardSMP v1.0.0 disabled!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new CardPickupListener(this), this);
    }

    public static CardSMP getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public CardManager getCardManager() { return cardManager; }
    public AbilityManager getAbilityManager() { return abilityManager; }
    public CooldownManager getCooldownManager() { return cooldownManager; }
}