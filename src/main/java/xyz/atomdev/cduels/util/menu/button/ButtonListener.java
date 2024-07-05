package xyz.atomdev.cduels.util.menu.button;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import xyz.atomdev.cduels.util.menu.Menu;
import xyz.atomdev.cduels.util.menu.MenuAPI;
import xyz.atomdev.cduels.util.menu.pagination.PaginatedMenu;

public class ButtonListener implements Listener {

    public static void onInventoryClose(Player player) {
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());

        if (openMenu != null) {
            openMenu.setInventory(player.getOpenInventory().getTopInventory());
            openMenu.onClose(player);

            MenuAPI.getOpenedMenus().remove(player.getUniqueId());
            if (openMenu instanceof PaginatedMenu) return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());
        if (openMenu != null) {
            openMenu.setInventory(event.getInventory());
            openMenu.onClose(player);
            MenuAPI.getOpenedMenus().remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onButtonPress(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Menu openMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());
        if (openMenu != null) {
            if (event.getSlot() != event.getRawSlot()) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    event.setCancelled(true);
                }
                return;
            }
            if (openMenu.getButtons().containsKey(event.getSlot())) {
                final Button button = openMenu.getButtons().get(event.getSlot());
                final boolean cancel = button.cancelEvent(player, event.getSlot(), event.getClick());
                if (!cancel && (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        player.getInventory().addItem(event.getCurrentItem());
                    }
                } else {
                    event.setCancelled(cancel);
                }
                button.onClick(player, event.getSlot(), event.getClick());
                if (MenuAPI.getOpenedMenus().containsKey(player.getUniqueId())) {
                    final Menu newMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());
                    if (newMenu == openMenu) {
                        final boolean buttonUpdate = button.updateOnClick(player, event.getSlot(), event.getClick());
                        if (buttonUpdate) {
                            openMenu.setClosedByMenu(true);
                            newMenu.openMenu(player);
                        }
                    }
                } else if (button.updateOnClick(player, event.getSlot(), event.getClick())) {
                    openMenu.setClosedByMenu(true);
                    openMenu.openMenu(player);
                }
                if (event.isCancelled()) {
                    Bukkit.getScheduler().runTaskLater(MenuAPI.getPlugin(), player::updateInventory, 2L);
                }
            }
        }
    }
}