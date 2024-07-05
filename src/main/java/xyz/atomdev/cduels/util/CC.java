package xyz.atomdev.cduels.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@UtilityClass
public class CC {

    public String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void sendMessage(CommandSender sender, String str) {
        sender.sendMessage(colorize(str));
    }
}
