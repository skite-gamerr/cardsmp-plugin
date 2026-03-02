package com.cardsmp.listeners;

import com.cardsmp.CardSMP;
import com.cardsmp.cards.CardType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

    private final CardSMP plugin;

    public PlayerDeathListener(CardSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        
        // Check if victim has a card
        if (!plugin.getPlayerDataManager().hasCard(victim.getUniqueId())) {
            return;
        }
        
        Player killer = victim.getKiller();
        
        // PvP Kill - Drop card
        if (killer != null && plugin.getConfigManager().dropCardOnPVP()) {
            handlePVPCardDrop(victim, killer);
        }
        // Natural Death - Keep card
        else if (plugin.getConfigManager().keepCardOnNaturalDeath()) {
            // Card stays with player - no action needed
        }
    }

    private void handlePVPCardDrop(Player victim, Player killer) {
        CardType cardType = plugin.getPlayerDataManager().getPlayerCard(victim.getUniqueId());
        if (cardType == null) return;
        
        var card = plugin.getCardManager().getCard(cardType);
        if (card == null) return;
        
        // Create dropped card item
        ItemStack cardItem = plugin.getCardManager().createCardItem(cardType);
        
        // Set card owner (killer can only pick up)
        plugin.getCardManager().setCardOwner(cardItem, killer.getUniqueId());
        plugin.getCardManager().setCardDropTime(cardItem);
        
        // Drop the card at victim location
        victim.getWorld().dropItemNaturally(victim.getLocation(), cardItem);
        
        // Remove card from victim
        plugin.getPlayerDataManager().removePlayerCard(victim.getUniqueId());
        
        // Send messages
        victim.sendMessage(plugin.getConfigManager().getCardDroppedMessage(killer.getName()));
        killer.sendMessage(plugin.getConfigManager().getCardStolenMessage(victim.getName(), card.getDisplayName()));
    }
}