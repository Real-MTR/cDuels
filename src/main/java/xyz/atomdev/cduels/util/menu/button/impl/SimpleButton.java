package xyz.atomdev.cduels.util.menu.button.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.atomdev.cduels.util.menu.button.Button;

/**
 * @project MTR is a property of MTR
 * @date 5/31/2024
 */

@AllArgsConstructor
@RequiredArgsConstructor
public class SimpleButton extends Button {

    private final ItemStack item;
    private boolean cancelEvent = false, updateOnClick = false;
    private final Runnable onClick;

    @Override
    public ItemStack getItem(Player player) {
        return item;
    }

    @Override
    public void onClick(Player player, int slot, ClickType type) {
        onClick.run();
    }

    @Override
    public boolean cancelEvent(Player player, int slot, ClickType type) {
        return cancelEvent;
    }

    @Override
    public boolean updateOnClick(Player player, int slot, ClickType type) {
        return updateOnClick;
    }
}
