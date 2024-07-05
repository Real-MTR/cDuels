package xyz.atomdev.cduels.util.menu.pagination;

import org.bukkit.entity.Player;
import xyz.atomdev.cduels.util.menu.Menu;
import xyz.atomdev.cduels.util.menu.button.Button;
import xyz.atomdev.cduels.util.menu.button.impl.BackButton;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

    private int page;

    public PaginatedMenu() {
        this.page = 1;
    }

    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player) + " &8(&7" + this.page + "/" + this.getPages(player) + "&8)";
    }

    public final void modPage(final Player player, final int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.openMenu(player);
    }

    public final int getPages(final Player player) {
        final int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int) Math.ceil(buttonAmount / (double) this.getMaxItemsPerPage(player));
    }

    @Override
    public final Map<Integer, Button> getButtons(final Player player) {
        final int minIndex = (int) ((this.page - 1) * (double) this.getMaxItemsPerPage(player));
        final int maxIndex = (int) (this.page * (double) this.getMaxItemsPerPage(player));
        final HashMap<Integer, Button> buttons = new HashMap<>();

        PageButton prevPage = new PageButton(-1, this);
        PageButton nextPage = new PageButton(1, this);

        if (prevPage.hasNext(player)) {
            buttons.put(39, prevPage);
        }

        if (nextPage.hasNext(player)) {
            buttons.put(41, nextPage);
        }

        for (final Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind >= minIndex && ind < maxIndex) {

                ind -= ((this.page - 1) * 21);

                switch (ind / 7) {
                    case 0:
                        ind += 10;
                        break;
                    case 1:
                        ind += 10 + 2;
                        break;
                    case 2:
                        ind += 10 + 4;
                        break;
                }
                buttons.put(ind, entry.getValue());
            }
        }
        final Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            buttons.putAll(global);
        }

        if (backButton() != null) {
            buttons.put(40, new BackButton(this.backButton()));
        }

        return buttons;
    }

    public int getMaxItemsPerPage(final Player player) {
        return 21;
    }

    public Map<Integer, Button> getGlobalButtons(final Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(final Player p0);

    public abstract Map<Integer, Button> getAllPagesButtons(final Player p0);

    public Menu backButton() {
        return null;
    }

    public int getPage() {
        return this.page;
    }
}