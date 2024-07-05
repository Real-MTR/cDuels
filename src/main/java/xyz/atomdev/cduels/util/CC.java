package xyz.atomdev.cduels.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @project cDuels is a property of MTR
 * @date 7/5/2024
 */

@UtilityClass
public class CC {

    public String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public String[] colorize(String[] str) {
        List<String> list = new ArrayList<>();

        for (String string : str) {
            list.add(colorize(string));
        }

        return (String[]) list.toArray();
    }

    public void sendMessage(CommandSender sender, String str) {
        sender.sendMessage(colorize(str));
    }

    public static void sendMessageAsList(CommandSender sender, String[] msg) {
        sender.sendMessage(colorize(msg));
    }
}
