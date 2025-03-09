package it.acctudio.guildscuboid;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;

import java.util.UUID;

public class GuildCuboid {
    public final UUID parties;
    public String region;

    public GuildCuboid(String region, UUID partiesUUID) {
        PartiesAPI partiesAPI = Parties.getApi();
        this.parties = partiesUUID;
        this.region = region;

    }

}
