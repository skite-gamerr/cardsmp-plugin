package com.cardsmp.abilities;

import com.cardsmp.CardSMP;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final CardSMP plugin;
    private final Map<UUID, Long> leftClickCooldowns;
    private final Map<UUID, Long> rightClickCooldowns;

    public CooldownManager(CardSMP plugin) {
        this.plugin = plugin;
        this.leftClickCooldowns = new HashMap<>();
        this.rightClickCooldowns = new HashMap<>();
        
        // Start cooldown checker task
        startCooldownChecker();
    }

    private void startCooldownChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                
                // Check left click cooldowns
                leftClickCooldowns.entrySet().removeIf(entry -> {
                    int cooldown = plugin.getConfigManager().getLeftClickCooldown();
                    return now - entry.getValue() >= cooldown * 1000L;
                });
                
                // Check right click cooldowns
                rightClickCooldowns.entrySet().removeIf(entry -> {
                    int cooldown = plugin.getConfigManager().getRightClickCooldown();
                    return now - entry.getValue() >= cooldown * 1000L;
                });
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void setLeftClickCooldown(Player player, int seconds) {
        leftClickCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public void setRightClickCooldown(Player player, int seconds) {
        rightClickCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public long getLeftClickCooldown(Player player) {
        Long cooldownEnd = leftClickCooldowns.get(player.getUniqueId());
        if (cooldownEnd == null) return 0;
        long remaining = cooldownEnd - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : 0;
    }

    public long getRightClickCooldown(Player player) {
        Long cooldownEnd = rightClickCooldowns.get(player.getUniqueId());
        if (cooldownEnd == null) return 0;
        long remaining = cooldownEnd - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : 0;
    }

    public boolean hasLeftClickCooldown(Player player) {
        return leftClickCooldowns.containsKey(player.getUniqueId());
    }

    public boolean hasRightClickCooldown(Player player) {
        return rightClickCooldowns.containsKey(player.getUniqueId());
    }

    public void removeLeftClickCooldown(Player player) {
        leftClickCooldowns.remove(player.getUniqueId());
    }

    public void removeRightClickCooldown(Player player) {
        rightClickCooldowns.remove(player.getUniqueId());
    }
}