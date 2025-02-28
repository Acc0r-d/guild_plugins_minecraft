package it.acctudio.guildscuboid.Items;

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

public class FlagItem  extends Item {


    public FlagItem(Guilds plugin) {
        super(plugin , Material.STICK);
    }

    @Override
    protected void configureMeta(ItemMeta meta) {

    }

    @Override
    protected String NAME() {
        return "Guild Flag";
    }

    @Override
    protected String ITEM_ID() {
        return "guild_flag";
    }

    @Override
    protected int CUSTOM_MODEL_DATA() {
        return 0;
    }

    @Override
    protected void handler(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(NAME());
    }

}
