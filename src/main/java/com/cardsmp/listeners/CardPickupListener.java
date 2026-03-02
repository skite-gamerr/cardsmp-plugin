package com.cardsmp.listeners;

import com.cardsmp.CardSMP;
import com.cardsmp.cards.CardType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CardPickupListener implements Listener {

    private final CardSMP plugin;

    public CardPickupListener(CardSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCardPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        
        // Check if it's a card
        if (!plugin.getCardManager().isCardItem(item)) return;
        
        // Get card type
        CardType cardType = plugin.getCardManager().getCardTypeFromItem(item);
        if (cardType == null) return;
        
        // Check if card is expired
        long dropTime = plugin.getCardManager().getCardDropTime(item);
        long currentTime = System.currentTimeMillis();
        int pickupTime = plugin.getConfigManager().getCardPickupTime();
        
        if (currentTime - dropTime > pickupTime * 1000L) {
            player.sendMessage(plugin.getConfigManager().getCardExpiredMessage());
            event.setCancelled(true);
            return;
        }
        
        // Check if player can pick up this card
        java.util.UUID cardOwner = plugin.getCardManager().getCardOwner(item);
        boolean isOwner = cardOwner != null && cardOwner.equals(player.getUniqueId());
        boolean isAdmin = player.hasPermission("cardsmp.admin");
        
        if (!isOwner && !isAdmin) {
            player.sendMessage(plugin.getConfigManager().getNotYourCardMessage());
            event.setCancelled(true);
            return;
        }
        
        // Check stacking prevention
        if (plugin.getConfigManager().isStackingPrevented() && 
            plugin.getPlayerDataManager().hasCard(player.getUniqueId())) {
            player.sendMessage("§cYou already have a card!");
            event.setCancelled(true);
            return;
        }
        
        // Give card to player
        plugin.getPlayerDataManager().savePlayerCard(player.getUniqueId(), cardType);
        
        var card = plugin.getCardManager().getCard(cardType);
        player.sendMessage(plugin.getConfigManager().getCardPickedUpMessage(card.getDisplayName()));
        
        // Remove item from world
        event.getItem().remove();
        event.setCancelled(true);
    }
}