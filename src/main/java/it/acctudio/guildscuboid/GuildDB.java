package it.acctudio.guildscuboid;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildDB {
    private final File configFile;
    private final GuildCuboidManager guildCuboidManager;
    private final Guilds plugin;

    public GuildDB(GuildCuboidManager manager, Guilds plugin) {
        this.guildCuboidManager = manager;
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "guilds.yml");
        loadInit();
    }

    public void Save(GuildCuboid guild) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String uuidKey = guild.parties.toString();

            // Zapisz dane w strukturze UUID -> region, npc
            config.set(uuidKey + ".region", guild.region);
            config.set(uuidKey + ".npc", guild.npcID);

            config.save(configFile);
            plugin.getLogger().info("Zapisano gildię: " + uuidKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Remove(GuildCuboid guild) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String uuidKey = guild.parties.toString();
            // Zapisz dane w strukturze UUID -> region, npc
            config.set(uuidKey, null);
            config.save(configFile);
            plugin.getLogger().info("Zapisano gildię: " + uuidKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInit() {
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }

            List<GuildCuboid> guilds = new ArrayList<>();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            // Wczytaj wszystkie UUID jako główne klucze
            for (String uuidKey : config.getKeys(false)) {
                try {
                    UUID parties = UUID.fromString(uuidKey);

                    String region = config.getString(uuidKey + ".region");
                    GuildCuboid guild = new GuildCuboid(region , parties);
                    int npc = config.getInt(uuidKey + ".npc");
                    guild.npcID = npc;
                    guilds.add(guild);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Nieprawidłowy UUID w guilds.yml: " + uuidKey);
                }
            }
            guildCuboidManager.loadGuilds(guilds);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas inicjalizacji guilds.yml", e);
        }
    }
}