package it.acctudio.guildscuboid.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Item extends ItemStack implements Listener {
    NamespacedKey PLUGIN_ITEM_KEY = new NamespacedKey("guildscuboid", "guild_item");
    public Item(JavaPlugin plugin , Material material) {
        super(material);
        ItemMeta meta = getItemMeta();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        meta.setDisplayName(NAME());
        meta.getPersistentDataContainer().set(PLUGIN_ITEM_KEY , PersistentDataType.STRING , ITEM_ID() );
        meta.setCustomModelData(CUSTOM_MODEL_DATA());
        configureMeta(meta);

        setItemMeta(meta);
    }
    protected abstract void configureMeta(ItemMeta meta);
    protected abstract String NAME();
    protected abstract String ITEM_ID();
    protected abstract int CUSTOM_MODEL_DATA();

    protected boolean isItem(ItemStack item, String itemId){
        if(item == null) return false;
        if(item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        return meta.getPersistentDataContainer().has(PLUGIN_ITEM_KEY, PersistentDataType.STRING) &&
                ITEM_ID().equals(meta.getPersistentDataContainer().get(PLUGIN_ITEM_KEY, PersistentDataType.STRING));
    }
    @EventHandler
    public void onItemUse(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if(!isItem(item , ITEM_ID())) return;
        handler(event);
    }
    protected abstract void handler(PlayerInteractEvent event);
}
