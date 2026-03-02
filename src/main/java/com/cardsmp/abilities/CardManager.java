package com.cardsmp.cards;

import com.cardsmp.CardSMP;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CardManager {

    private final CardSMP plugin;
    private final Map<CardType, Card> cards;
    private final NamespacedKey cardKey;
    private final NamespacedKey cardOwnerKey;
    private final NamespacedKey cardDropTimeKey;

    public CardManager(CardSMP plugin) {
        this.plugin = plugin;
        this.cards = new HashMap<>();
        this.cardKey = new NamespacedKey(plugin, "card_item");
        this.cardOwnerKey = new NamespacedKey(plugin, "card_owner");
        this.cardDropTimeKey = new NamespacedKey(plugin, "card_drop_time");
        loadCards();
    }

    private void loadCards() {
        var cardConfigs = plugin.getConfigManager().getAllCards();
        
        for (CardType type : CardType.values()) {
            var config = cardConfigs.get(type.name());
            if (config != null) {
                cards.put(type, new Card(type, config));
            } else {
                plugin.getLogger().warning("No config found for card: " + type.name());
            }
        }
        
        plugin.getLogger().info("Loaded " + cards.size() + " cards!");
    }

    public void reloadCards() {
        cards.clear();
        plugin.getConfigManager().reloadConfig();
        loadCards();
    }

    public Card getCard(CardType type) {
        return cards.get(type);
    }

    public Card getRandomCard() {
        List<CardType> types = new ArrayList<>(cards.keySet());
        return cards.get(types.get(new Random().nextInt(types.size())));
    }

    public CardType getCardTypeFromString(String name) {
        for (CardType type : CardType.values()) {
            if (type.name().equals