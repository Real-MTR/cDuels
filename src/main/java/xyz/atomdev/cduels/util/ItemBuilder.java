package xyz.atomdev.cduels.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder type(Material material) {
        this.stack.setType(material);
        return this;
    }

    public Material getType() {
        return this.stack.getType();
    }

    public ItemBuilder name(String name) {
        ItemMeta stackMeta = this.stack.getItemMeta();
        stackMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.stack.setItemMeta(stackMeta);
        return this;
    }

    public String getName() {
        if (this.stack.hasItemMeta() && this.stack.getItemMeta().hasDisplayName()) {
            return this.stack.getItemMeta().getDisplayName();
        }
        return null;
    }

    public ItemBuilder amount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return this.stack.getAmount();
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }
        ItemMeta stackMeta = this.stack.getItemMeta();
        stackMeta.setLore(lore);
        this.stack.setItemMeta(stackMeta);
        return this;
    }

    public List<String> getLore() {
        if (this.stack.hasItemMeta() && this.stack.getItemMeta().hasLore()) {
            return this.stack.getItemMeta().getLore();
        }
        return null;
    }

    public ItemBuilder color(ItemDataColor color) {
        return durability(color.getValue());
    }

    public ItemBuilder data(short data) {
        return durability(data);
    }

    public ItemBuilder durability(short durability) {
        this.stack.setDurability(durability);
        return this;
    }

    public short getDurability() {
        return this.stack.getDurability();
    }

    public ItemDataColor getColor() {
        return ItemDataColor.getByValue(this.stack.getDurability());
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder unenchant(Enchantment enchantment) {
        this.stack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder flag(ItemFlag... flag) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.addItemFlags(flag);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder deflag(ItemFlag... flag) {
        ItemMeta meta = this.stack.getItemMeta();
        meta.removeItemFlags(flag);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(String name) {
        if (this.stack.getItemMeta() instanceof SkullMeta) {
            this.stack.setDurability((short) 3);
            SkullMeta meta = (SkullMeta) this.stack.getItemMeta();
            meta.setOwner(name);
            this.stack.setItemMeta(meta);
            return this;
        }
        return this;
    }

    public ItemBuilder ifThen(Predicate<ItemBuilder> ifTrue, Function<ItemBuilder, Object> then) {
        if (ifTrue.test(this)) {
            then.apply(this);
        }
        return this;
    }

    public ItemStack build() {
        return get();
    }

    public ItemStack get() {
        return this.stack;
    }
}
