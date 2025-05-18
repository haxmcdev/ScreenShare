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

        SprawdzCommand sprawdzCommand = new SprawdzCommand();
        if (getCommand("sprawdz") != null)
            getCommand("sprawdz").setExecutor(sprawdzCommand);
        if (getCommand("ss") != null)
            getCommand("ss").setExecutor(sprawdzCommand);
        if (getCommand("check") != null)
            getCommand("check").setExecutor(sprawdzCommand);

        // Register przyznajesie command and aliases pointing to the same executor
        PrzyznajeSieCommand przyznajeSieCommand = new PrzyznajeSieCommand();
        if (getCommand("przyznajesie") != null)
            getCommand("przyznajesie").setExecutor(przyznajeSieCommand);
        if (getCommand("admit") != null)
            getCommand("admit").setExecutor(przyznajeSieCommand);
        if (getCommand("przyznaj") != null)
            getCommand("przyznaj").setExecutor(przyznajeSieCommand);

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
