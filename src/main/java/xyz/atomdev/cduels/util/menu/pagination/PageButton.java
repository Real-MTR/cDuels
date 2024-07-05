package xyz.atomdev.cduels.util.menu.pagination;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.atomdev.cduels.util.ItemBuilder;
import xyz.atomdev.cduels.util.menu.button.Button;

public class PageButton extends Button {
    private final int mod;
    private final PaginatedMenu menu;

    public PageButton(final int mod, final PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getItem(final Player player) {
        ItemBuilder builder = new ItemBuilder(Material.ARROW)
                .name(ChatColor.translateAlternateColorCodes('&', (this.mod > 0) ? "&8» &7Next Page" : "&8« &7Previous Page"));
        return hasNext(player) ? builder.build() : builder.lore("&cNo more pages.").build();
    }
    @Override
    public void onClick(Player player, int i, ClickType clickType) {
        if (this.hasNext(player)) {
            this.menu.modPage(player, this.mod);
            player.openInventory(menu.getInventory());
        }
    }

    public boolean hasNext(final Player player) {
        final int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
}