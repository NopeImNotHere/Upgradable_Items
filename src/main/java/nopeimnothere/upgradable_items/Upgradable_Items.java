package nopeimnothere.upgradable_items;


import nopeimnothere.upgradable_items.UpgradeItems.UATConfigManager;
import nopeimnothere.upgradable_items.UpgradeItems.commands.CommandManager;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.config.ConfigManager;


public final class Upgradable_Items extends JavaPlugin {


    public static Upgradable_Items instance;
    ArgType<EntityType> entityTypeArgType = ArgType.of("entityType", EntityType.class);

    public static Upgradable_Items getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {

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

