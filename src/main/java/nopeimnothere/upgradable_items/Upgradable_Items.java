package nopeimnothere.upgradable_items;


import nopeimnothere.upgradable_items.UpgradeItems.UATConfigManager;
import nopeimnothere.upgradable_items.UpgradeItems.commands.CommandManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.config.ConfigManager;

import java.util.Arrays;

import static org.bukkit.Bukkit.getPluginManager;

public final class Upgradable_Items extends JavaPlugin {


    public static Upgradable_Items instance;
    ArgType<EntityType> entityTypeArgType = ArgType.of("entityType", EntityType.class);
            //.tabStream(c -> Arrays.toString(EntityType.values()).lines());

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
        new CommandParser(this.getResource("command.rdcml")).setArgTypes(entityTypeArgType).parse().register("UpgradableItems", new CommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}

