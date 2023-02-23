package nopeimnothere.upgradable_items.UpgradeItems.Utils;

import org.bukkit.Material;

import java.util.ArrayList;

public class ListUtil {

    public static boolean GetItemAtListIndex(ArrayList OutsideList, Material SearchItem) {
        int LI = OutsideList.indexOf("" + SearchItem);
        LI = LI + 1;
        return LI < OutsideList.toArray().length;
    }

    public static boolean ItemIsInList(ArrayList OutsideList, Material SearchItem) {
        return OutsideList.contains("" + SearchItem);
    }

    public static int GetItemAtListInt(ArrayList OutsideList, Material SearchItem) {
        int LI = OutsideList.indexOf("" + SearchItem);
        LI = LI + 1;
        return LI;
    }

}
