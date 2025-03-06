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
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
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
    public void preventCreateGuildSign(SignChangeEvent event) {
        Player player = event.getPlayer();
        String line = event.getLine(0);
        if(!line.equalsIgnoreCase("[Guild]")) return;
        if(player.hasPermission("guildcuboid.create")) return;
        event.setCancelled(true);
        player.sendMessage("§cNie masz permisji, aby postawić tabliczkę [Guild]!");

    }
    @EventHandler
    public void flagHandler(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Action action = event.getAction();

        if(action !=  Action.LEFT_CLICK_BLOCK || action !=  Action.RIGHT_CLICK_BLOCK)  return;

        Block block = event.getClickedBlock();

        if(block == null) return;

        if(!isSign(block.getType())) return;

        Sign sign = (Sign) block.getState();
        String[] lines = sign.getLines();

        if(lines.length < 1) return;
        if(!lines[0].equals("[Guild")) return;
        UUID uuid = player.getUniqueId();
        PartiesAPI partiesAPI = Parties.getApi();
        if (!partiesAPI.isPlayerInParty(uuid)) {
            player.sendMessage("Nie jesteś w gildii");
            return;
        }
        Party party = partiesAPI.getPartyOfPlayer(uuid);
        if (!party.getLeader().equals(uuid)) {
            player.sendMessage("Nie jesteś liderem gildii");
            return;
        }


        plugin.getGuildManager().createCuboid(party.getId(), player.getLocation());
        player.sendMessage("Zajęto teren gildii " + party.getName());
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
    private boolean isSign(Material material) {
        return material == Material.OAK_SIGN || material == Material.SPRUCE_SIGN ||
                material == Material.BIRCH_SIGN || material == Material.JUNGLE_SIGN ||
                material == Material.ACACIA_SIGN || material == Material.DARK_OAK_SIGN ||
                material == Material.MANGROVE_SIGN || material == Material.CRIMSON_SIGN ||
                material == Material.WARPED_SIGN || material == Material.OAK_WALL_SIGN ||
                material == Material.SPRUCE_WALL_SIGN || material == Material.BIRCH_WALL_SIGN ||
                material == Material.JUNGLE_WALL_SIGN || material == Material.ACACIA_WALL_SIGN ||
                material == Material.DARK_OAK_WALL_SIGN || material == Material.MANGROVE_WALL_SIGN ||
                material == Material.CRIMSON_WALL_SIGN || material == Material.WARPED_WALL_SIGN;
    }

}