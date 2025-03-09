package it.acctudio.guildscuboid.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Subcommand;
import it.acctudio.guildscuboid.Guilds;
import org.bukkit.entity.Player;

@CommandAlias("cuboidmanager")
public class GuildCommand extends BaseCommand {

    Guilds plugin;

    public GuildCommand(Guilds plugin) {
        this.plugin = plugin;
    }

    @Subcommand("create")
    @Conditions("isPlayer")
    public void onCreate(Player sender, String regionName) {
        if (!sender.isOp()) return;
        plugin.getGuildManager().createRegion(sender, "gp_" + regionName);

    }
}
