package nopeimnothere.upgradable_items.UpgradeItems.commands;



import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static nopeimnothere.upgradable_items.Upgradable_Items.*;

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

            Material IT = null;
            if (gui.getInventory().getItem(11) != null) {
                IT = gui.getInventory().getItem(11).getType();
            } else {
                player.sendMessage(ChatColor.RED + "Please add the first ingredient into the GUI");
                return;
            }
            Material SI = null;
            if (gui.getInventory().getItem(12) != null) {
                SI = gui.getInventory().getItem(12).getType();
            } else {
                player.sendMessage(ChatColor.RED + "Please add the second ingredient into the GUI");
                return;
            }


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
    @CommandHook("UISpawnCreatureNPC")
    public void SpawnCreatureNPC(Player p, EntityType entityType, String customName) {
        if(customName == null) {
            customName = "Upgrade Your Items";
        }

        Entity e = p.getWorld().spawnEntity(p.getLocation(), entityType);

        if (p.hasPermission("UpgradeItems.NPC") && e.getType().isAlive()) {
            e.setInvulnerable(true);
            e.setSilent(true);

            if(e instanceof Monster) {
                Objects.requireNonNull(((Monster) e).getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(-1);
                Objects.requireNonNull(((Monster) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0);
            } else {
                Objects.requireNonNull(((Creature) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0);
            }

            e.setCustomName(ChatColor.GOLD + customName);
            e.isPersistent();
            p.sendMessage(ChatColor.GREEN + "" + e.getType() + " with Upgrade GUI created!");


        } else {
            p.sendMessage(ChatColor.RED + "Please select only a Living entity for this Operation");
        }
    }

    @CommandHook("UISpawnPlayerNPC")
    public void SpawnPlayerNPC(CommandSender player, String Player_Name, String customName) {
        Player p = (Player) player;
        String id = getUUID(Player_Name);
        Location PlayerLocation = p.getLocation();
        NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(instance, id, PlayerLocation);

        npc.setText(customName);
        npc.setShowOnTabList(false);
        npc.setCollidable(false);

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

    private String getUUID(String player_Name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+player_Name;
        try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(url));
            if(UUIDJson.isEmpty()) return "invalid name";
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if(!(p.getInventory().getItemInMainHand().getType() == Material.NAME_TAG)) {
            Entity en = e.getRightClicked();
            if (Objects.requireNonNull(en.getCustomName()).equalsIgnoreCase(ChatColor.GOLD + "Upgrade Your Items") && en.isPersistent()) {
                GUIOpen(p);
            }
        }
    }


}