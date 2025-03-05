package it.acctudio.guildscuboid;


import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

import javax.swing.text.html.parser.Entity;

public final class Guilds extends JavaPlugin {
    private GuildCuboidManager guildCuboidManager;

    @Override
    public void onEnable() {
        // Sprawdzanie WorldGuarda
        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard nie znaleziony! OFF");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("Parties") == null) {
            getLogger().severe("Parties nie znaleziony! OFF");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Inicjalizacja managera gildii

        guildCuboidManager = new GuildCuboidManager(this);
        getCommand("guild").setExecutor(new GuildCommand(this));
        // Rejestracja listenerów

        getServer().getPluginManager().registerEvents(new GuildListener(this), this);
        getLogger().info("Włączony!");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public GuildCuboidManager getGuildManager(){
        return guildCuboidManager;
    }
}



