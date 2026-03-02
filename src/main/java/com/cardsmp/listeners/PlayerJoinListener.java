package com.cardsmp.listeners;

import com.cardsmp.CardSMP;
import com.cardsmp.cards.CardType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final CardSMP plugin;

    public PlayerJoinListener(CardSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Check if player already has a card
        if (plugin.getPlayerDataManager().hasCard(player.getUniqueId())) {
            return;
        }
        
        // Give random card to new player
        CardType randomCard = plugin.getCardManager().getRandomCard().getType();
        plugin.getPlayerDataManager().savePlayerCard(player.getUniqueId(), randomCard);
        
        // Send message
        String cardName = plugin.getCardManager().getCard(randomCard).getDisplayName();
        player.sendMessage(plugin.getConfigManager().getCardReceivedMessage(cardName));
        
        // Show abilities if enabled
        if (plugin.getConfigManager().showAbilitiesOnJoin()) {
            showAbilities(player, randomCard);
        }
    }

    private void showAbilities(Player player, CardType cardType) {
        var card = plugin.getCardManager().getCard(cardType);
        if (card == null) return;
        
        player.sendMessage("§6=== §e" + card.getDisplayName() + " §6===");
        player.sendMessage("§7Lore:");
        for (String line : card.getLore()) {
            player.sendMessage(line);
        }
    }
}