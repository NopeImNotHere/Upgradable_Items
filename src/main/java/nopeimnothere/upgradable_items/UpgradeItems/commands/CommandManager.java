package nopeimnothere.upgradable_items.UpgradeItems.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.*;

import static nopeimnothere.upgradable_items.Upgradable_Items.instance;

public class CommandManager implements Listener {



    @CommandHook("UIGuiOpen")
    public void GUIOpen(CommandSender player) {
        InventoryGUI gui = new InventoryGUI(Bukkit.createInventory((InventoryHolder) player, 27, "Upgrade Table"));
        gui.fill(0, 9, InventoryGUI.FILLER);
        gui.fill(18, 27, InventoryGUI.FILLER);
        gui.fill(9, 11, InventoryGUI.FILLER);
        gui.fill(16, 18, InventoryGUI.FILLER);

        gui.openSlot(11);
        gui.openSlot(12);
        gui.openSlot(14);
        gui.openSlot(15);


        ArrayList UIL = (ArrayList) instance.getConfig().get("first_ingredient");
        ArrayList SUIL = (ArrayList) instance.getConfig().get("second_ingredient");

        ItemButton button = ItemButton.create(new ItemBuilder(Material.EMERALD_BLOCK).setName(ChatColor.GOLD + "" + ChatColor.ITALIC + "Upgrade"), event -> {

            Material IT = gui.getInventory().getItem(11).getType();
            Material SI = gui.getInventory().getItem(12).getType();

            assert UIL != null;
            assert SUIL != null;
            if (UIL.contains("" + IT)) {

                boolean UIL_InBounds = GetItemAtListIndex(UIL, IT);
                boolean SI_Exists = ItemIsInList(SUIL, SI);
                int UII = GetItemAtListInt(UIL, IT);
                int SII = GetItemAtListInt(SUIL, SI);


                if (UIL_InBounds && SI_Exists) {

                    Object UpgradeItem = UIL.get(UII);
                    Object SecondaryItem = SUIL.get(SII - 1);

                    String[] UpgradeItemStringArray = UpgradeItem.toString().split("_");
                    String[] SecondaryItemStringArray = SecondaryItem.toString().split("_");

                    String[] UpgradeItemStringArrayM = Arrays.copyOfRange(UpgradeItemStringArray, 0, 1);
                    String[] SecondaryItemStringArrayM = Arrays.copyOfRange(SecondaryItemStringArray, 0, 1);

                    boolean ItemMatched = Arrays.equals(UpgradeItemStringArrayM, SecondaryItemStringArrayM);

                    if (ItemMatched) {
                        UpgradeTheItem(gui, UpgradeItem);
                    }
                }
            }
        });
        gui.addButton(button, 13);
        gui.open((Player) player);
    }
    private void UpgradeTheItem(InventoryGUI gui, Object UpgradeItem) {

        ItemMeta UpgradeItemMeta = Objects.requireNonNull(gui.getInventory().getItem(11)).getItemMeta();
        gui.clearSlot(11);

        ItemStack SecondaryItem = gui.getInventory().getItem(12);
        int SecondaryItemAmount = gui.getInventory().getItem(12).getAmount();
        if (SecondaryItemAmount > 1) {
            SecondaryItem.setAmount(SecondaryItemAmount - 1);
            gui.getInventory().setItem(12, new ItemStack(SecondaryItem));
        } else {
            gui.clearSlot(12);
        }

        gui.fill(14, 15, new ItemStack(Material.valueOf("" + UpgradeItem)));
        Objects.requireNonNull(gui.getInventory().getItem(14)).setItemMeta(UpgradeItemMeta);
    }

    private boolean GetItemAtListIndex(ArrayList OutsideList, Material SearchItem) {
        int LI = OutsideList.indexOf("" + SearchItem);
        LI = LI + 1;
        return LI < OutsideList.toArray().length;
    }

    private boolean ItemIsInList(ArrayList OutsideList, Material SearchItem) {
        return OutsideList.contains("" + SearchItem);
    }

    private int GetItemAtListInt(ArrayList OutsideList, Material SearchItem) {
        int LI = OutsideList.indexOf("" + SearchItem);
        LI = LI + 1;
        return LI;
    }



    @CommandHook("UISpawnVillager")
    public void SpawnNPC(Player p) {
        if (p.hasPermission("UpgradeItems.SpawnVillager")) {
            Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
            v.setCustomName(ChatColor.GOLD + "Upgrade Your Items");
            p.sendMessage("Villager created!");

        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void on(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        Villager v = (Villager) e.getRightClicked();
        if(e.getRightClicked() instanceof Villager && Objects.requireNonNull(v.getCustomName()).equalsIgnoreCase(ChatColor.GOLD + "Upgrade Your Items")) {
                GUIOpen(p);
        }
    }
}