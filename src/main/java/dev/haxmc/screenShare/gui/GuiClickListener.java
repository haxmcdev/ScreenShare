package dev.haxmc.screenShare.gui;

import dev.haxmc.screenShare.commands.SprawdzCommand;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class GuiClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        if (title.contains("Sprawdzanie: ") || title.contains("Checking: ")) {
            event.setCancelled(true);

            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 8) {
                String targetName = title.replace("Sprawdzanie: ", "").replace("Checking: ", "");
                Player target = Bukkit.getPlayerExact(targetName);
                if (target == null) {
                    player.sendMessage(ConfigManager.getMessage("messages.command.not-found"));
                    player.closeInventory();
                    return;
                }

                switch (slot) {
                    case 1 -> {
                        if (!SprawdzCommand.checking.containsKey(player.getUniqueId())) {
                            SprawdzCommand.startChecking(player, target);
                        }
                        Location room = ConfigManager.getRoomLocation();
                        if (room != null) {
                            player.teleport(room);
                            target.teleport(room);
                            player.sendMessage(ConfigManager.getMessage("messages.gui.teleport"));
                        } else {
                            player.sendMessage("§cNie ustawiono lokacji pokoju do sprawdzania!");
                        }
                        player.closeInventory();
                    }
                    case 2 -> {
                        if (SprawdzCommand.checking.containsKey(player.getUniqueId())) {
                            SprawdzCommand.stopChecking(player.getUniqueId());
                        }
                        ConfigManager.broadcast("messages.broadcast.cleared", Map.of("player", target.getName()));
                        player.sendMessage(ConfigManager.getMessage("messages.gui.cleared"));
                        player.closeInventory();
                    }
                    case 3 -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                ConfigManager.getCommand("ban_cheats").replace("{target}", target.getName()));
                        ConfigManager.broadcast("messages.broadcast.ban-cheats", Map.of("player", target.getName()));
                        SprawdzCommand.stopChecking(player.getUniqueId());
                        player.closeInventory();
                    }
                    case 4 -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                ConfigManager.getCommand("ban_nocoop").replace("{target}", target.getName()));
                        ConfigManager.broadcast("messages.broadcast.ban-nocoop", Map.of("player", target.getName()));
                        SprawdzCommand.stopChecking(player.getUniqueId());
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
