package nopeimnothere.upgradable_items;


import nopeimnothere.upgradable_items.UpgradeItems.UATConfigManager;
import nopeimnothere.upgradable_items.UpgradeItems.commands.CommandManager;
import nopeimnothere.upgradable_items.UpgradeItems.traits.UpgradableTraderTrait;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.config.ConfigManager;

import java.util.Objects;
import java.util.logging.Level;


public final class Upgradable_Items extends JavaPlugin {


    public static Upgradable_Items instance;


    ArgType<EntityType> entityTypeArgType = ArgType.of("entityType", EntityType.class);

    @Override
    public void onEnable() {

        if(getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(UpgradableTraderTrait.class));

        this.saveDefaultConfig();
        ConfigManager configManager = ConfigManager.create(this).target(UATConfigManager.class).saveDefaults().load();


        getServer().getPluginManager().registerEvents(new UpgradableTraderTrait(), this);

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

