package it.acctudio.guildscuboid.command;

import co.aikar.commands.PaperCommandManager;
import it.acctudio.guildscuboid.Guilds;
import org.bukkit.command.CommandExecutor;

public class CommandManager extends PaperCommandManager {

    public CommandManager(Guilds plugin) {
        super(plugin);
        registerCommand(new GuildCommand(plugin));
    }

}
