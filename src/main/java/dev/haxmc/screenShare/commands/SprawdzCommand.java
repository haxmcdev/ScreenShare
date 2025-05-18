package dev.haxmc.screenShare.commands;

import dev.haxmc.screenShare.gui.SprawdzGui;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SprawdzCommand implements CommandExecutor {

    public static Map<UUID, UUID> checking = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ConfigManager.getMessage("messages.command.only-players"));
            return true;
        }

        Player admin = (Player) sender;

        if (!admin.hasPermission("screenshare.use")) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.no-permission"));
            return true;
        }

        if (args.length == 1 && (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("cancel"))) {
            if (checking.containsKey(admin.getUniqueId())) {
                UUID targetUUID = checking.remove(admin.getUniqueId());
                Player target = Bukkit.getPlayer(targetUUID);

                admin.sendMessage(ConfigManager.getMessage("messages.command.check-stop"));

                if (target != null) {
                    ConfigManager.broadcast("messages.broadcast.cleared", Map.of(
                            "player", target.getName()
                    ));
                }
            } else {
                admin.sendMessage(ConfigManager.getMessage("messages.command.not-checking"));
            }
            return true;
        }

        if (args.length != 1) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.usage"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !target.isOnline()) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.not-found"));
            return true;
        }

        if (target.getUniqueId().equals(admin.getUniqueId())) {
            admin.sendMessage(ConfigManager.getMessage("messages.command.cant-check-yourself"));
            return true;
        }

        SprawdzGui gui = new SprawdzGui(target);
        admin.openInventory(gui.getInventory());
        return true;
    }

    public static UUID getAdminByTarget(UUID targetUUID) {
        for (Map.Entry<UUID, UUID> entry : checking.entrySet()) {
            if (entry.getValue().equals(targetUUID)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isBeingChecked(UUID uuid) {
        return checking.containsValue(uuid);
    }

    public static void startChecking(Player admin, Player target) {
        checking.put(admin.getUniqueId(), target.getUniqueId());

        ConfigManager.broadcast("messages.broadcast.started", Map.of(
                "admin", admin.getName(),
                "target", target.getName()
        ));
    }

    public static void stopChecking(UUID adminUUID) {
        UUID targetUUID = checking.remove(adminUUID);
        if (targetUUID != null) {
            Player target = Bukkit.getPlayer(targetUUID);
            if (target != null) {
                ConfigManager.broadcast("messages.broadcast.cleared", Map.of(
                        "player", target.getName()
                ));
            }
        }
    }
}
