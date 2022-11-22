package nopeimnothere.upgradable_items.UpgradeItems;

import org.bukkit.Material;
import redempt.redlib.config.annotations.ConfigMappable;

import java.util.ArrayList;
import java.util.List;

@ConfigMappable
public class UATConfigManager {
    public static List<Material> first_ingredient = new ArrayList<>();
    public static List<Material> second_ingredient = new ArrayList<>();

}
