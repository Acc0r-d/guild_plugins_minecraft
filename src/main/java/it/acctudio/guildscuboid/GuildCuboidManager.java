package it.acctudio.guildscuboid;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GuildCuboidManager {
    private static final int GUILD_SIZE = 200; // Stały rozmiar 200x200
    private final List<GuildCuboid> guilds = new ArrayList<>();
    private final GuildDB store;
    private final Guilds plugin;
    public World guildWorld;

    public GuildCuboidManager(Guilds plugin) {
        this.plugin = plugin;
        store = new GuildDB(this, plugin);
        guildWorld = plugin.getServer().getWorld("world");
    }

    public void createCuboid(UUID partiesUUID, String region) {
        GuildCuboid guild = assignCuboid(partiesUUID, region);
        guilds.add(guild);
        store.Save(guild);
    }

    public void destroyCuboid(UUID partiesUUID) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        Optional<GuildCuboid> guild = guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst();
        if (!guild.isPresent()) {
            return;
        }
        regions.removeRegion(guild.get().region);


    }

    public String getCuboidByLocation(Location location, World world) {
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
            if (regions.getRegions().size() < 1) return null;
            BlockVector3 point = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            for (ProtectedRegion region : regions.getRegions().values()) {
                if (region.contains(point)) {
                    return region.getId();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GuildCuboid assignCuboid(UUID partiesUUID, String region) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        ProtectedRegion RegionObject = regions.getRegion(region);
        DefaultDomain domain = new DefaultDomain();
        PartiesAPI partiesAPI = Parties.getApi();
        Party party = partiesAPI.getParty(partiesUUID);
        party.getMembers().forEach(domain::addPlayer);
        RegionObject.setMembers(domain);
        return new GuildCuboid(region, partiesUUID);
    }

    public void addToRegion(UUID playerUUID, UUID partiesUUID) {

        GuildCuboid g = guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst().get();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World guildWorld = plugin.getServer().getWorld("world");
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        ProtectedRegion region = regions.getRegion(g.region);
        DefaultDomain domain = region.getMembers();
        domain.addPlayer(playerUUID);
        region.setMembers(domain);


    }

    public void removeFromRegion(UUID playerUUID, UUID partiesUUID) {
        GuildCuboid g = guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst().get();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World guildWorld = plugin.getServer().getWorld("world");
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        ProtectedRegion region = regions.getRegion(g.region);
        DefaultDomain domain = region.getMembers();
        domain.removePlayer(playerUUID);
        region.setMembers(domain);
    }

    /**
     * @param sender     Administrator serwera , tworzy terytorium gildi które można zająć
     * @param regionName nazwa stworzonego regionu
     */
    public void createRegion(Player sender, String regionName) {

        World playerWorld = sender.getWorld();
        Location center = sender.getLocation();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(playerWorld));

        if (regions == null) {
            plugin.getLogger().severe("Błąd: Nie można uzyskać menedżera regionów!");
            return;
        }
        ProtectedCuboidRegion region = getProtectedCuboidRegion(regionName, center, playerWorld);
        regions.addRegion(region);
        //sender.playEffect(sender.locale() , Effect.POTION_BREAK ,  );
        sender.sendMessage(ChatColor.GREEN + "Stworzono region");
        sender.sendMessage(ChatColor.GREEN + "Teraz aby gildia mogła go przypisać utwórz tabliczkę");
        sender.sendMessage(ChatColor.GREEN + "[Guild]");
    }

    private @NotNull ProtectedCuboidRegion getProtectedCuboidRegion(String regionName, Location center, World playerWorld) {
        int halfSize = GUILD_SIZE / 2;
        int minX = center.getBlockX() - halfSize;
        int maxX = center.getBlockX() + halfSize - 1;
        int minZ = center.getBlockZ() - halfSize;
        int maxZ = center.getBlockZ() + halfSize - 1;
        int minY = playerWorld.getMinHeight(); // Od dna świata
        int maxY = playerWorld.getMaxHeight(); // Do maksymalnej wysokości

        BlockVector3 min = BlockVector3.at(minX, minY, minZ);
        BlockVector3 max = BlockVector3.at(maxX, maxY, maxZ);


        ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionName, min, max);
        return region;
    }

    public void loadGuilds(List<GuildCuboid> guilds) {
        this.guilds.clear();
        this.guilds.addAll(guilds);
    }
}