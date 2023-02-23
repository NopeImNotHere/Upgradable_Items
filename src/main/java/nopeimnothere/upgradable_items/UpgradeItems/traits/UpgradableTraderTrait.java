package nopeimnothere.upgradable_items.UpgradeItems.traits;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import nopeimnothere.upgradable_items.Upgradable_Items;
import nopeimnothere.upgradable_items.UpgradeItems.commands.CommandManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import static nopeimnothere.upgradable_items.Upgradable_Items.instance;

@TraitName("upgradableTrader")
public class UpgradableTraderTrait extends Trait {


    public UpgradableTraderTrait() {
        super("upgradableTrader");
        Upgradable_Items plugin = JavaPlugin.getPlugin(Upgradable_Items.class);
    }

    @Persist boolean isTrader = true;

    @EventHandler
    public void onRightClick(net.citizensnpcs.api.event.NPCRightClickEvent event) {
        if(event.getNPC() == this.getNPC()) {
            Player player = event.getClicker();
            if(player.hasPermission("UpgradeItems.GUI") && player.getInventory().getItemInMainHand().getType() != Material.NAME_TAG) {

                CommandManager commandManager = new CommandManager();
                commandManager.GUIOpen(player);
            }
        }
    }

    @Override
    public void onAttach() {
        if(instance.getConfig().getBoolean("CitizensTraitsDebug")) {
            instance.getServer().getLogger().info(npc.getId() + " has been assigned TraderTrait");
        }
    }
}
