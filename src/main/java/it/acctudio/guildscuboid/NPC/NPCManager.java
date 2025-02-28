package it.acctudio.guildscuboid.NPC;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class NPCManager {

    static public NPC SummonGuildMaster(Location loc) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER , "Guild Master");
        npc.spawn(loc);
        npc.data().set(NPC.Metadata.DEFAULT_PROTECTED, true);
        return npc;
    }
    static public void RemoveNPC(int id) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.getById(id);
        npc.destroy();

    }
}
