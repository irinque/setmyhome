package irinque.setmyhome;

import irinque.setmyhome.commands.DelHomeCommand;
import irinque.setmyhome.commands.HomeCommand;
import irinque.setmyhome.commands.SetHomeCommand;
import irinque.setmyhome.config.MessageManager;
import irinque.setmyhome.handlers.HomeTeleportManager;
import irinque.setmyhome.storage.HomeManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Setmyhome extends JavaPlugin {
    private HomeManager homeManager;
    private MessageManager messageManager;
    private HomeTeleportManager homeTeleportManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        homeManager = new HomeManager(this);
        messageManager = new MessageManager(this);
        homeTeleportManager = new HomeTeleportManager(this);

        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));

        getServer().getPluginManager().registerEvents(homeTeleportManager, this);
        getLogger().info("[SetMyHome] Plugin is ready");
    }

    @Override
    public void onDisable() {
        if (homeTeleportManager != null) {
            homeTeleportManager.cancelAll();
        }
        if (messageManager != null) {
            messageManager.close();
        }
        getLogger().info("[SetMyHome] Plugin is off");
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public HomeTeleportManager getHomeTeleportManager() {
        return homeTeleportManager;
    }
}
