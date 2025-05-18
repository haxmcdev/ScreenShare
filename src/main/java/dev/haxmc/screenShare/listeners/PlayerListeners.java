package dev.haxmc.screenShare.listeners;

import dev.haxmc.screenShare.commands.SprawdzCommand;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (SprawdzCommand.checking.containsValue(playerUUID)) {
            UUID adminUUID = SprawdzCommand.getAdminByTarget(playerUUID);
            if (adminUUID != null) {
                Player admin = player.getServer().getPlayer(adminUUID);
                if (admin != null) {
                    admin.sendMessage(ConfigManager.getMessage("messages.gui.player-left"));
                    admin.closeInventory();
                }
                SprawdzCommand.checking.remove(adminUUID);
            }
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (SprawdzCommand.checking.containsValue(playerUUID)) {
            String cmd = event.getMessage().toLowerCase();

            if (cmd.startsWith("/msg") || cmd.startsWith("/tell") || cmd.startsWith("/w") || cmd.startsWith("/helpop")) {
                return; // Pozwól te komendy
            }

            player.sendMessage(ConfigManager.getMessage("messages.command.blocked-during-check"));
            event.setCancelled(true);
        }
    }
}
