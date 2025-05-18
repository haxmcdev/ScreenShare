package dev.haxmc.screenShare.gui;

import dev.haxmc.screenShare.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SprawdzGui {

    private final Inventory inventory;
    private final Player target;

    public SprawdzGui(Player target) {
        this.target = target;

        inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&',
                "Checking: " + target.getName()));

        // Player head on slot 0
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(target);
            headMeta.setDisplayName(ChatColor.GREEN + target.getName());
            head.setItemMeta(headMeta);
        }
        inventory.setItem(0, head);

        ItemStack teleport = createGuiItem(Material.GREEN_WOOL, ConfigManager.getMessage("messages.gui.options.teleport"));
        inventory.setItem(1, teleport);

        ItemStack clear = createGuiItem(Material.YELLOW_WOOL, ConfigManager.getMessage("messages.gui.options.clear"));
        inventory.setItem(2, clear);

        ItemStack banCheats = createGuiItem(Material.RED_WOOL, ConfigManager.getMessage("messages.gui.options.ban_cheats"));
        inventory.setItem(3, banCheats);

        ItemStack banNoCoop = createGuiItem(Material.ORANGE_WOOL, ConfigManager.getMessage("messages.gui.options.ban_nocoop"));
        inventory.setItem(4, banNoCoop);

        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 5; i < 9; i++) {
            inventory.setItem(i, filler);
        }
    }

    private ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        return item;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
