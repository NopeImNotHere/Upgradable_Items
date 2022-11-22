package nopeimnothere.upgradable_items;


import nopeimnothere.upgradable_items.UpgradeItems.UATConfigManager;
import nopeimnothere.upgradable_items.UpgradeItems.commands.CommandManager;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.config.ConfigManager;

import static org.bukkit.Bukkit.getPluginManager;

public final class Upgradable_Items extends JavaPlugin {


    public static Upgradable_Items instance;

    public static Upgradable_Items getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        ConfigManager config = ConfigManager.create(this).target(UATConfigManager.class).saveDefaults().load();

        getServer().getPluginManager().registerEvents(new CommandManager(), this);

        CommandRegister();

        instance = this;
    }

    private void CommandRegister() {
        new CommandParser(this.getResource("command.rdcml")).parse().register("UpgradableItems", new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}

