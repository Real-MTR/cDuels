package xyz.atomdev.cduels.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class MessageBuilder {

    private final String text;

    private boolean translate = false;
    private boolean clickable = false;
    private boolean hover = false;

    private String hoverText = null;
    private ClickEvent clickEvent = null;

    public MessageBuilder translate(boolean translate) {
        this.translate = translate;
        return this;
    }

    public MessageBuilder clickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public MessageBuilder hover(boolean hover) {
        this.hover = hover;
        return this;
    }

    public MessageBuilder hoverText(String text) {
        this.hoverText = text;
        return this;
    }

    public MessageBuilder clickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public TextComponent textComponent() {
        TextComponent message = new TextComponent(this.translate ? ChatColor.translateAlternateColorCodes('&', this.text) : this.text);

        if (this.hover) {
            HoverEvent.Action action = HoverEvent.Action.SHOW_TEXT;
            TextComponent[] textComponentArr = new TextComponent[]{new TextComponent(this.hoverText == null ? "" : this.hoverText)};
            message.setHoverEvent(new HoverEvent(action, textComponentArr));
        }

        if (this.clickable) {
            message.setClickEvent(this.clickEvent);
        }

        return message;
    }

    public void sendMessage(Player player) {
        player.spigot().sendMessage(this.textComponent());
    }
}