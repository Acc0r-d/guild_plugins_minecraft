package it.acctudio.guildscuboid;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.bukkit.player.BukkitPartiesPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.saicone.rtag.RtagItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class GuildListener implements Listener {
    private final Guilds plugin;

    public GuildListener(Guilds plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void flagHandler(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        if (item.getType() != Material.STICK || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        RtagItem NBT = new RtagItem(item);

        if (NBT.get("plugin_tag") != "guild_addon" || NBT.get("guild_plugin") != "guild_flag" ) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        PartiesAPI partiesAPI = Parties.getApi();
        if (!partiesAPI.isPlayerInParty(uuid)) {
            player.sendMessage("Nie jesteś w gildii");
            return;
        }
        if(player.getWorld() == plugin.getGuildManager().guildWorld) {
            player.sendMessage("Nie jesteś w odpowiednim świecie");
            return;
        }
        Party party = partiesAPI.getPartyOfPlayer(uuid);

        player.sendMessage("Lider gildii: " + party.getLeader().toString());
        player.sendMessage("Twoje UUID: " + uuid.toString());

        if (!party.getLeader().equals(uuid)) {
            player.sendMessage("Nie jesteś liderem gildii");
            return;
        }

        plugin.getGuildManager().createCuboid(party.getId(), player.getLocation());
        player.sendMessage("Założono teren gildii " + party.getName());
    }
    @EventHandler
    public void onGuildRemove(BukkitPartiesPartyPostDeleteEvent event) {
        Party party = event.getParty();
        plugin.getGuildManager().destroyCuboid(party.getId());
    }

    @EventHandler
    public void onJoinToGuild(BukkitPartiesPlayerPostJoinEvent event){
        plugin.getGuildManager().addToRegion(event.getPartyPlayer().getPlayerUUID() , event.getParty().getId());
    }

    @EventHandler
    public void onLeaveToGuild(BukkitPartiesPlayerPostLeaveEvent event){
        plugin.getGuildManager().removeFromRegion(event.getPartyPlayer().getPlayerUUID() , event.getParty().getId());
    }

}