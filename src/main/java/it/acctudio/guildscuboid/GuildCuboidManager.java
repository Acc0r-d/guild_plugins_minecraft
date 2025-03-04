package it.acctudio.guildscuboid;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.papermc.paper.chat.ChatRenderer;
import it.acctudio.guildscuboid.NPC.NPCManager;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class GuildCuboidManager {
    private final List<GuildCuboid> guilds = new ArrayList<>();
    private static final int GUILD_SIZE = 200; // Stały rozmiar 200x200
    private final GuildDB store;
    private Guilds plugin;

    public World guildWorld;
    public GuildCuboidManager(Guilds plugin) {
        this.plugin = plugin;
        store = new GuildDB(this, plugin);
        guildWorld = plugin.getServer().getWorld("world");
    }
    public void createCuboid(UUID partiesUUID, Location location ,World world) {
        GuildCuboid guild = new GuildCuboid(partiesUUID);
        guildWorld = world;
        createRegion(location , guild.region , guildWorld , partiesUUID);
        guild.npcID = createGuildMaster(location);

        guilds.add(guild);
        store.Save(guild);
        //leader.sendMessage("Utworzono gildię: " + name + "! Użyj różdżki, aby wybrać centrum regionu.");
    }
    public void destroyCuboid(UUID partiesUUID) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        Optional<GuildCuboid> guild = guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst();
        if (!guild.isPresent()) {
            return;
        }
        regions.removeRegion(guild.get().region);
        NPCManager.RemoveNPC(guild.get().npcID);

    }
    public void addToRegion(UUID playerUUID , UUID partiesUUID){

        GuildCuboid g =  guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst().get();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World guildWorld = plugin.getServer().getWorld("world");
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        ProtectedRegion region = regions.getRegion(g.region);
        DefaultDomain domain = region.getMembers();
        domain.addPlayer(playerUUID);
        region.setMembers(domain);


    }
    public void removeFromRegion(UUID playerUUID , UUID partiesUUID){
        GuildCuboid g =  guilds.stream().filter(r -> r.parties.equals(partiesUUID)).findFirst().get();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World guildWorld = plugin.getServer().getWorld("world");
        RegionManager regions = container.get(BukkitAdapter.adapt(guildWorld));
        ProtectedRegion region = regions.getRegion(g.region);
        DefaultDomain domain = region.getMembers();
        domain.removePlayer(playerUUID);
        region.setMembers(domain);
    }
    private void createRegion(Location center , String regionName ,  World eventWorld , UUID partiesUUID) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(eventWorld));

        if (regions == null) {
            plugin.getLogger().severe("Błąd: Nie można uzyskać menedżera regionów!");
            return;
        }
        PartiesAPI partiesAPI = Parties.getApi();

        // Oblicz granice regionu 200x200
        int halfSize = GUILD_SIZE / 2;
        int minX = center.getBlockX() - halfSize;
        int maxX = center.getBlockX() + halfSize - 1;
        int minZ = center.getBlockZ() - halfSize;
        int maxZ = center.getBlockZ() + halfSize - 1;
        int minY = eventWorld.getMinHeight(); // Od dna świata
        int maxY = eventWorld.getMaxHeight(); // Do maksymalnej wysokości

        BlockVector3 min = BlockVector3.at(minX, minY, minZ);
        BlockVector3 max = BlockVector3.at(maxX, maxY, maxZ);


        ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionName, min, max);
        regions.addRegion(region);
        DefaultDomain domain = new DefaultDomain();
        domain.addPlayer(partiesAPI.getParty(partiesUUID).getLeader());
        region.setOwners(domain);

    }
    private int createGuildMaster(Location center){
      NPC npc = NPCManager.SummonGuildMaster(center);
      return npc.getId();
    }
    //TODO
//    public List<Guild> getGuilds() {
//        return guilds;
//    }
    public void loadGuilds(List<GuildCuboid> guilds) {
        this.guilds.clear();
        this.guilds.addAll(guilds);
    }
}