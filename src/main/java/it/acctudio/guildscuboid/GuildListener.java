package it.acctudio.guildscuboid;

import it.acctudio.guildscuboid.Items.FlagItem;
import it.acctudio.guildscuboid.Items.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuildListener implements Listener {
    private final Guilds plugin;

    public GuildListener(Guilds plugin) {
        this.plugin = plugin;
    }

}
