package com.cardsmp.data;

import com.cardsmp.CardSMP;
import com.cardsmp.cards.CardType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final CardSMP plugin;
    private final Map<UUID, CardType> playerCards;
    private FileConfiguration dataConfig;
    private File dataFile;

    public PlayerDataManager(CardSMP plugin) {
        this.plugin = plugin;
        this.playerCards = new HashMap<>();
        loadData();
    }

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create playerdata.yml: " + e.getMessage());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load all player cards from config
        for (String uuidStr : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                String cardName = dataConfig.getString(uuidStr, "");
                if (!cardName.isEmpty()) {
                    CardType type = CardType.valueOf(cardName);
                    playerCards.put(uuid, type);
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid card type for player: " + uuidStr);
            }
        }
        
        plugin.getLogger().info("Loaded data for " + playerCards.size() + " players!");
    }

    public void savePlayerCard(UUID playerUUID, CardType cardType) {
        playerCards.put(playerUUID, cardType);
        dataConfig.set(playerUUID.toString(), cardType.name());
        saveData();
    }

    public void removePlayerCard(UUID playerUUID) {
        playerCards.remove(playerUUID);
        dataConfig.set(playerUUID.toString(), null);
        saveData();
    }

    public CardType getPlayerCard(UUID playerUUID) {
        return playerCards.get(playerUUID);
    }

    public boolean hasCard(UUID playerUUID) {
        return playerCards.containsKey(playerUUID);
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save playerdata.yml: " + e.getMessage());
        }
    }

    public void saveAll() {
        saveData();
    }
}