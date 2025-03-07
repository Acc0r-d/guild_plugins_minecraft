package it.acctudio.guildscuboid;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuildDB {
    private final File configFile;
    private final GuildCuboidManager guildCuboidManager;
    private final Guilds plugin;
    private YamlConfiguration config;

    public GuildDB(GuildCuboidManager manager, Guilds plugin) {
        this.guildCuboidManager = manager;
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "guilds.yml");
        loadInit();
    }

    public void Save(GuildCuboid guild) {
        try {
            String uuidKey = guild.parties.toString();

            // Zapisz dane w strukturze UUID -> region, npc
            config.set(uuidKey , guild.region);

            config.save(configFile);
            plugin.getLogger().info("Zapisano gildię: " + uuidKey);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie udało się zapisać gildii " + guild.parties + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void remove(GuildCuboid guild) {
        try {
            String uuidKey = guild.parties.toString();
            config.set(uuidKey, null); // Usuń sekcję dla danej gildii
            config.save(configFile);
            plugin.getLogger().info("Usunięto gildię: " + uuidKey);
        } catch (IOException e) {
            plugin.getLogger().severe("Nie udało się usunąć gildii " + guild.parties + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadInit() {
        try {
            // Utwórz folder nadrzędny, jeśli nie istnieje
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) {
                if (!dataFolder.mkdirs()) {
                    plugin.getLogger().severe("Nie udało się utworzyć folderu pluginu!");
                    return; // Nie rzucamy wyjątku, aby plugin mógł kontynuować działanie
                }
            }

            // Utwórz plik guilds.yml, jeśli nie istnieje
            if (!configFile.exists()) {
                configFile.createNewFile();
                plugin.getLogger().info("Utworzono nowy plik guilds.yml.");
            }

            // Wczytaj konfigurację
            config = YamlConfiguration.loadConfiguration(configFile);

            List<GuildCuboid> guilds = new ArrayList<>();
            for (String uuidKey : config.getKeys(false)) {
                try {
                    UUID parties = UUID.fromString(uuidKey);
                    String region = config.getString(uuidKey);

                    GuildCuboid guild = new GuildCuboid(region, parties);

                    guilds.add(guild);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Nieprawidłowy UUID w guilds.yml: " + uuidKey);
                }
            }
            guildCuboidManager.loadGuilds(guilds);
        } catch (IOException e) {
            plugin.getLogger().severe("Błąd podczas inicjalizacji guilds.yml: " + e.getMessage());
            e.printStackTrace();
            // Nie rzucamy RuntimeException, aby plugin mógł działać dalej
        }
    }

    // Metoda do ręcznego przeładowania konfiguracji (opcjonalna)
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        loadInit();
    }
}