package irinque.setmyhome.config;

import java.io.File;
import java.util.Map;
import irinque.setmyhome.Setmyhome;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageManager {
    private final YamlConfiguration messages;
    private final BukkitAudiences audiences;

    public MessageManager(Setmyhome plugin) {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        audiences = BukkitAudiences.create(plugin);
    }

    public void send(CommandSender sender, String messageKey) {
        send(sender, messageKey, Map.of());
    }

    public void send(CommandSender sender, String messageKey, Map<String, String> placeholders) {
        String message = messages.getString(messageKey, "<red>Missing message: " + messageKey + "</red>");
        TagResolver.Builder resolver = TagResolver.builder();
        placeholders.forEach((key, value) -> resolver.resolver(Placeholder.unparsed(key, value)));
        audiences.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(message, resolver.build()));
    }

    public void close() {
        audiences.close();
    }
}
