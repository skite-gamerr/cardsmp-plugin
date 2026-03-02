package com.cardsmp.config;

import com.cardsmp.CardSMP;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final CardSMP plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(CardSMP plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void saveDefaultConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    // General settings
    public boolean isStealingAllowed() {
        return config.getBoolean("general.allow-stealing", true);
    }

    public boolean isStackingPrevented() {
        return config.getBoolean("general.prevent-stacking", true);
    }

    public int getCardPickupTime() {
        return config.getInt("general.card-pickup-time", 10);
    }

    public boolean showAbilitiesOnJoin() {
        return config.getBoolean("general.show-abilities-on-join", true);
    }

    // Death settings
    public boolean dropCardOnPVP() {
        return config.getBoolean("death.drop-on-pvp", true);
    }

    public boolean keepCardOnNaturalDeath() {
        return config.getBoolean("death.keep-on-natural-death", true);
    }

    // Messages
    public String getMessage(String path) {
        return config.getString("messages." + path, "");
    }

    public String getCooldownMessage(String time) {
        return getMessage("cooldown").replace("{time}", time);
    }

    public String getNoCardMessage() {
        return getMessage("no-card");
    }

    public String getCardReceivedMessage(String card) {
        return getMessage("card-received").replace("{card}", card);
    }

    public String getCardDroppedMessage(String killer) {
        return getMessage("card-dropped").replace("{killer}", killer);
    }

    public String getCardPickedUpMessage(String card) {
        return getMessage("card-picked-up").replace("{card}", card);
    }

    public String getCardStolenMessage(String victim, String card) {
        return getMessage("card-stolen").replace("{victim}", victim).replace("{card}", card);
    }

    public String getNotYourCardMessage() {
        return getMessage("not-your-card");
    }

    public String getCardExpiredMessage() {
        return getMessage("card-expired");
    }

    public String getAbilityActivatedMessage(String ability) {
        return getMessage("ability-activated").replace("{ability}", ability);
    }

    public String getNoPermissionMessage() {
        return getMessage("no-permission");
    }

    public String getReloadMessage() {
        return getMessage("reload-success");
    }

    public String getInvalidCardMessage() {
        return getMessage("invalid-card");
    }

    public String getCardGivenMessage(String player, String card) {
        return getMessage("card-given").replace("{player}", player).replace("{card}", card);
    }

    public String getCardRemovedMessage(String player) {
        return getMessage("card-removed").replace("{player}", player);
    }

    // Get card config
    public ConfigurationSection getCardConfig(String cardName) {
        return config.getConfigurationSection("cards." + cardName);
    }

    public Map<String, ConfigurationSection> getAllCards() {
        Map<String, ConfigurationSection> cards = new HashMap<>();
        ConfigurationSection cardsSection = config.getConfigurationSection("cards");
        if (cardsSection != null) {
            for (String key : cardsSection.getKeys(false)) {
                cards.put(key, cardsSection.getConfigurationSection(key));
            }
        }
        return cards;
    }

    public int getLeftClickCooldown() {
        return config.getInt("cooldowns.left-click", 30);
    }

    public int getRightClickCooldown() {
        return config.getInt("cooldowns.right-click", 60);
    }
}