package it.acctudio.guildscuboid;

import org.bukkit.plugin.java.JavaPlugin;

public final class Guilds extends JavaPlugin {
    private GuildCuboidManager guildCuboidManager;
    @Override
    public void onEnable() {
        getLogger().info("Plugin Gildii włączony!");

        // Sprawdzanie WorldGuarda
        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("WorldGuard nie znaleziony! OFF");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        //Citizens check
        if(getServer().getPluginManager().getPlugin("Citizens") == null){
            getLogger().severe("Citizens nie znaleziony! OFF");
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

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new GuildListener(this), this);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public GuildCuboidManager getGuildManager(){
        return guildCuboidManager;
    }
}



