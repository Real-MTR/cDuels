package xyz.atomdev.cduels.util.menu.button;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
    public abstract ItemStack getItem(Player player);

    public void onClick(Player player, int slot, ClickType type) {}

    public boolean cancelEvent(Player player, int slot, ClickType type) {return false;}

    public boolean updateOnClick(Player player, int slot, ClickType type) {return false;}

    public void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1F, 1F);
    }
}