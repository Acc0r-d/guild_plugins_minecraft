package it.acctudio.guildscuboid;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuildCuboid {
    public  String region;
    public final UUID parties;
    public int npcID ;
    public GuildCuboid(UUID partiesUUID) {
        PartiesAPI partiesAPI = Parties.getApi();
        this.parties = partiesUUID;
        this.region = "guild_" + partiesAPI.getParty(partiesUUID).getName();
        
    }
    public GuildCuboid(String region ,UUID partiesUUID) {
        PartiesAPI partiesAPI = Parties.getApi();
        this.parties = partiesUUID;
        this.region = region;

    }

}
