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

    public List<String> colorize(List<String> list) {
        list.forEach(CC::colorize);
        return list;
    }

    public String[] colorize(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = colorize(array[i]);
        }
        return array;
    }

    public void sendMessage(CommandSender sender, String str) {
        sender.sendMessage(colorize(str));
    }

    public static void sendMessageAsList(CommandSender sender, String[] msg) {
        sender.sendMessage(colorize(msg));
    }
}
