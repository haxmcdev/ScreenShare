package dev.haxmc.screenShare;

import dev.haxmc.screenShare.commands.SprawdzCommand;
import dev.haxmc.screenShare.commands.PrzyznajeSieCommand;
import dev.haxmc.screenShare.gui.GuiClickListener;
import dev.haxmc.screenShare.listeners.PlayerListeners;
import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ScreenShare extends JavaPlugin {

    private static ScreenShare instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        ConfigManager.setup(this);

        if (this.getCommand("sprawdz") != null) {
            this.getCommand("sprawdz").setExecutor(new SprawdzCommand());
        }
        if (this.getCommand("przyznajesie") != null) {
            this.getCommand("przyznajesie").setExecutor(new PrzyznajeSieCommand());
        }

        getServer().getPluginManager().registerEvents(new GuiClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    @Override
    public void onDisable() {
    }

    public static ScreenShare getInstance() {
        return instance;
    }
}
