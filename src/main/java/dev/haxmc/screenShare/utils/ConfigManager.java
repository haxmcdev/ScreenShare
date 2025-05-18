package dev.haxmc.screenShare.utils;

import dev.haxmc.screenShare.ScreenShare;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class ConfigManager {

    public static ScreenShare plugin;

    public static void setup(ScreenShare pluginInstance) {
        plugin = pluginInstance;
    }

    public static String getMessage(String path) {
        FileConfiguration config = plugin.getConfig();
        if (config.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(path));
        }
        return ChatColor.RED + "Brak wiadomości w configu dla: " + path;
    }

    public static String getCommand(String path) {
        FileConfiguration config = plugin.getConfig();
        return config.getString("commands." + path, "");
    }

    public static Location getRoomLocation() {
        FileConfiguration config = plugin.getConfig();
        String worldName = config.getString("room.world");
        if (worldName == null) return null;

        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = config.getDouble("room.x");
        double y = config.getDouble("room.y");
        double z = config.getDouble("room.z");

        return new Location(world, x, y, z);
    }

    public static void broadcast(String path, Map<String, String> placeholders) {
        String message = plugin.getConfig().getString(path);
        if (message == null || message.isEmpty()) return;

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(message);
    }
}
