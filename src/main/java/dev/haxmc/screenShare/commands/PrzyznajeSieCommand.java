package dev.haxmc.screenShare.commands;

import dev.haxmc.screenShare.ScreenShare;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrzyznajeSieCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player admin)) {
            sender.sendMessage(ConfigManager.getMessage("messages.command.only-players"));
            return true;
        }

        if (!admin.hasPermission("screen.share")) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.no-permission"));
            return true;
        }

        UUID adminUUID = admin.getUniqueId();

        if (!SprawdzCommand.checking.containsKey(adminUUID)) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.not-checking"));
            return true;
        }

        UUID targetUUID = SprawdzCommand.checking.get(adminUUID);
        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) {
            admin.sendMessage(ConfigManager.getMessage("messages.gui.player-left"));
            SprawdzCommand.checking.remove(adminUUID);
            return true;
        }

        String cmd = ScreenShare.getInstance().getConfig().getString("commands.confess").replace("%target%", target.getName());
        Bukkit.dispatchCommand(admin, cmd);

        SprawdzCommand.checking.remove(adminUUID);
        admin.sendMessage(ConfigManager.getMessage("messages.command.confess-success"));

        return true;
    }
}
