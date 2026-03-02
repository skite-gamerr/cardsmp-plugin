package com.cardsmp.listeners;

import com.cardsmp.CardSMP;
import com.cardsmp.cards.CardType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {

    private final CardSMP plugin;

    public PlayerInteractListener(CardSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Only handle main hand
        if (event.getHand() != EquipmentSlot.HAND) return;
        
        // Must be sneaking
        if (!player.isSneaking()) return;
        
        // Check if player has a card
        if (!plugin.getPlayerDataManager().hasCard(player.getUniqueId())) {
            player.sendMessage(plugin.getConfigManager().getNoCardMessage());
            return;
        }
        
        CardType cardType = plugin.getPlayerDataManager().getPlayerCard(player.getUniqueId());
        var card = plugin.getCardManager().getCard(cardType);
        if (card == null) return;
        
        // Left Click
        if (event.getAction().name().contains("LEFT")) {
            handleLeftClick(player, card, cardType);
        }
        // Right Click
        else if (event.getAction().name().contains("RIGHT")) {
            handleRightClick(player, card, cardType);
        }
    }

    private void handleLeftClick(Player player, com.cardsmp.cards.Card card, CardType cardType) {
        // Check cooldown
        long cooldown = plugin.getCooldownManager().getLeftClickCooldown(player);
        if (cooldown > 0) {
            player.sendMessage(plugin.getConfigManager().getCooldownMessage(String.valueOf(cooldown)));
            return;
        }
        
        // Activate ability
        boolean activated = plugin.getAbilityManager().activateLeftClickAbility(player, card, cardType);
        
        if (activated) {
            plugin.getCooldownManager().setLeftClickCooldown(player, card.getLeftClickCooldown());
            player.sendMessage(plugin.getConfigManager().getAbilityActivatedMessage(card.getLeftClickName()));
        }
    }

    private void handleRightClick(Player player, com.cardsmp.cards.Card card, CardType cardType) {
        // Check cooldown
        long cooldown = plugin.getCooldownManager().getRightClickCooldown(player);
        if (cooldown > 0) {
            player.sendMessage(plugin.getConfigManager().getCooldownMessage(String.valueOf(cooldown)));
            return;
        }
        
        // Activate ability
        boolean activated = plugin.getAbilityManager().activateRightClickAbility(player, card, cardType);
        
        if (activated) {
            plugin.getCooldownManager().setRightClickCooldown(player, card.getRightClickCooldown());
            player.sendMessage(plugin.getConfigManager().getAbilityActivatedMessage(card.getRightClickName()));
        }
    }
}