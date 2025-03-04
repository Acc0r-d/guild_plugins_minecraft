package it.acctudio.guildscuboid.Items;

import com.saicone.rtag.RtagItem;
import io.papermc.paper.datacomponent.item.ItemLore;
import it.acctudio.guildscuboid.Guilds;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class UtilityItems {


    public static ItemStack FlagItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Guild Flag");
        meta.setCustomModelData(10);
        item.setItemMeta(meta);
        RtagItem NBT = new RtagItem(item);
        NBT.set("guild_addon" , "plugin_tag");
        NBT.set("guild_flag" , "guild_plugin");
        NBT.update();
        return item;
    }



}
