package irinque.setmyhome.commands;

import irinque.setmyhome.Setmyhome;
import org.bukkit.command.CommandSender;

public final class CommandSupport {
    private CommandSupport() {
    }

    public static boolean hasPermission(Setmyhome plugin, CommandSender commandSender, String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        }

        plugin.getMessageManager().send(commandSender, "error-no-permission");
        return false;
    }
}
