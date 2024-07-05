package xyz.atomdev.cduels.util.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.atomdev.cduels.util.menu.button.Button;
import xyz.atomdev.cduels.util.menu.button.ButtonListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
@Setter
public abstract class Menu {
    public abstract String getTitle(Player player);
    public abstract Map<Integer, Button> getButtons(Player player);
    private Map<Integer, Button> buttons = new HashMap<>();
    private boolean closedByMenu;
    private boolean autoUpdate;
    private Inventory inventory;

    public void onOpen(Player player) {}
    public void onClose(Player player) {}

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getItem(player);

        if (item.getType() != Material.ARROW) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null && meta.hasDisplayName()) meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            item.setItemMeta(meta);
        }

        return item;
    }

    public void openMenu(Player player) {
        this.buttons = getButtons(player);
        Menu previousMenu = MenuAPI.getOpenedMenus().get(player.getUniqueId());
        this.inventory = null;
        int size = (getSize() == -1) ? size(this.buttons) : getSize();
        boolean update = false;
        String title = getTitle(player);
        if (title.length() > 32)
            title = title.substring(0, 32);
        if (player.getOpenInventory() != null)
            if (previousMenu == null) {
                ButtonListener.onInventoryClose(player);
            } else {
                int previousSize = player.getOpenInventory().getTopInventory().getSize();
                if (previousSize == size && Arrays.equals(player.getOpenInventory().getTopInventory().getContents(), getButtons().values().toArray())) {
                    this.inventory = player.getOpenInventory().getTopInventory();
                    update = true;
                } else {
                    previousMenu.setClosedByMenu(true);
                    ButtonListener.onInventoryClose(player);
                }
            }
        onOpen(player);
        if (this.inventory == null)
            this.inventory = Bukkit.createInventory(player, size, ChatColor.translateAlternateColorCodes('&', title));
        this.inventory.setContents(new ItemStack[this.inventory.getSize()]);
        MenuAPI.getOpenedMenus().put(player.getUniqueId(), this);
        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet())
            try {
                this.inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
            } catch (Exception ignored) {
            }
        if (update) {
            player.updateInventory();
        } else {
            if (this.inventory instanceof CraftingInventory) return;
            player.openInventory(this.inventory);
        }
        setClosedByMenu(false);
        Bukkit.getScheduler().runTaskLaterAsynchronously(MenuAPI.getPlugin(), () -> MenuAPI.getOpenedMenus().put(player.getUniqueId(), this), 1L);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest)
                highest = buttonValue;
        }
        return (int) (Math.ceil((highest + 1) / 9.0D) * 9.0D);
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public void surroundButtons(boolean full, Map<Integer, Button> buttons, final ItemStack itemStack) {
        IntStream.range(0, getSize()).filter(slot -> (buttons.get(slot) == null)).forEach(slot -> {
            if (slot < 9 || slot > getSize() - 10 || (full && (slot % 9 == 0 || (slot + 1) % 9 == 0)))
                buttons.put(slot, new Button() {
                    public ItemStack getItem(Player player) {
                        return itemStack;
                    }
                });
        });
    }

    public int getSize() {
        return -1;
    }
}