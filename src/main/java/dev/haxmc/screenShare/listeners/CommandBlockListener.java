package dev.haxmc.screenShare.listeners;

import dev.haxmc.screenShare.commands.SprawdzCommand;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class CommandBlockListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!SprawdzCommand.isBeingChecked(uuid)) return;

        String cmd = event.getMessage().toLowerCase();
        if (cmd.startsWith("/msg") || cmd.startsWith("/helpop")) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ConfigManager.getMessage("messages.command.blocked-during-check"));
    }
}
