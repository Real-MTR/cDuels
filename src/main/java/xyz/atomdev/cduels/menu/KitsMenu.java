package xyz.atomdev.cduels.menu;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.atomdev.cduels.CDuels;
import xyz.atomdev.cduels.model.kit.Kit;
import xyz.atomdev.cduels.model.request.DuelRequest;
import xyz.atomdev.cduels.util.CC;
import xyz.atomdev.cduels.util.ItemBuilder;
import xyz.atomdev.cduels.util.MessageBuilder;
import xyz.atomdev.cduels.util.menu.Menu;
import xyz.atomdev.cduels.util.menu.button.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * @project XeDuels is a property of MTR
 * @date 6/2/2024
 */

@RequiredArgsConstructor
public class KitsMenu extends Menu {

    private final CDuels instance;
    private final Player target;

    @Override
    public String getTitle(Player player) {
        return "&bKits";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (Kit kit : instance.getKitHandler().getKits().values()) {
            buttons.put(i, new KitsButton(kit, target));
            i++;
        }

        return buttons;
    }

    @RequiredArgsConstructor
    static class KitsButton extends Button {

        private final Kit kit;
        private final Player target;

        @Override
        public ItemStack getItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .name("&b" + kit.getName())
                    .lore("&eClick here to send a " + kit.getName() + " duel!")
                    .build();
        }

        @Override
        public boolean cancelEvent(Player player, int slot, ClickType type) {
            return true;
        }

        @Override
        public void onClick(Player player, int slot, ClickType type) {
            if(player.hasPermission(kit.getPermission())) {
                DuelRequest request = new DuelRequest(player, target, kit);
                player.closeInventory();

                CC.sendMessage(player, "&aYou have sent a duel to &b" + target.getName() + " &awith the kit &e" + kit.getName());
                CC.sendMessage(target, "&b" + player.getName() + " &asent you a duel with the kit &e" + kit.getName());
                new MessageBuilder("&2&lAccept")
                        .translate(true)
                        .clickable(true)
                        .clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "duelaccept " + player.getName()))
                        .hover(true)
                        .hoverText("Click to accept")
                        .sendMessage(target);
            } else {
                CC.sendMessage(player, "&cYou do not have permission to send a duel with this kit!");
            }
        }
    }
}