package nopeimnothere.upgradable_items.UpgradeItems.commands;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import nopeimnothere.upgradable_items.UpgradeItems.Utils.ListUtil;
import nopeimnothere.upgradable_items.UpgradeItems.Utils.SkinsUtil;
import nopeimnothere.upgradable_items.UpgradeItems.traits.UpgradableTraderTrait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.lang.reflect.Field;
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

                boolean UIL_InBounds = ListUtil.GetItemAtListIndex(UIL, IT);
                boolean SI_Exists = ListUtil.ItemIsInList(SUIL, SI);
                int UII = ListUtil.GetItemAtListInt(UIL, IT);
                int SII = ListUtil.GetItemAtListInt(SUIL, SI);


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

    @CommandHook("UISpawnNPC")
    public void SpawnNPC(Player player, EntityType entityType, String customName) {
        customName += ChatColor.GOLD;
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(entityType, customName);

        Location location = player.getLocation();

        UpgradableTraderTrait upgradableTraderTrait = npc.getOrAddTrait(UpgradableTraderTrait.class);

        npc.spawn(location);
    }

    @CommandHook("UISpawnPlayerNPC")
    public void SpawnPlayerNPC(Player player, String Username, String customName) {
        if(player.hasPermission("UpgradeItems.Spawn")) {
            customName = ChatColor.GOLD + customName;
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, customName);
            Location location = player.getLocation();

            String UUID = SkinsUtil.getPlayerUUID(Username);
            String signatureValue = signatureValue = SkinsUtil.getSignature(UUID);
            String textureValue = textureValue = SkinsUtil.getTexture(UUID);

            SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
            skinTrait.setSkinPersistent(UUID, signatureValue, textureValue);

            npc.getOrAddTrait(Owner.class).setOwner(UUID);

            LookClose lookClose = npc.getOrAddTrait(LookClose.class);
            lookClose.lookClose(true);

            UpgradableTraderTrait upgradableTraderTrait = npc.getOrAddTrait(UpgradableTraderTrait.class);

            npc.spawn(location);
         }
     }

    @CommandHook("UINPCRemove")
    public void NPCRemoval(CommandSender commandSender) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return;
        }

        double radius = 5;
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        List<NPC> nearbyNPCs = new ArrayList<>();

        for(Entity entity : nearbyEntities) {
                NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                if(npc != null && npc.hasTrait(UpgradableTraderTrait.class)) {
                    nearbyNPCs.add(npc);
            }
        }

        if (nearbyNPCs.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No NPCs found nearby.");
            return;
        }

        int inventorySize = (int) Math.ceil(nearbyNPCs.size() / 9.0) * 9;
        InventoryGUI inventory = new InventoryGUI(Bukkit.createInventory(player, inventorySize, "Select an NPC to destroy: "));

        for(int i = 0; i < nearbyNPCs.size(); i++) {

            NPC npc = CitizensAPI.getNPCRegistry().getById(nearbyNPCs.get(i).getId());
            SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
            String texture = skinTrait.getTexture();

            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", texture));

            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            if(npc.getEntity().getType() == EntityType.PLAYER) {
                Field profileField = null;
                try {
                    profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, profile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException |
                         SecurityException e) {
                    e.printStackTrace();
                }
            }

            head.setItemMeta(meta);

            int nextFreeSlot = -1;

            for(int j = 0; i < inventory.getInventory().getSize(); j++) {
                if(inventory.getInventory().getItem(i) == null) {
                    nextFreeSlot = i;
                    break;
                }
            }

            Location NPC_Location = npc.getStoredLocation();
            String x = String.valueOf(NPC_Location.getBlockX());
            String y = String.valueOf(NPC_Location.getBlockY());
            String z = String.valueOf(NPC_Location.getBlockZ());


            ItemButton button = ItemButton.create(new ItemBuilder(head)
                    .setLore("Mob-Type: " + npc.getEntity().getType() + "\n","Location: " + "X: " + x + "\n", "Y: " + y + "\n", "Z: " + z + "\n", "NPC Id: " + npc.getId())
                    .setName(npc.getName()), e -> {
                npc.destroy();
                e.getWhoClicked().closeInventory();
            });
            inventory.addButton(button, nextFreeSlot);
        }
        inventory.open(player);

    }

}