package it.acctudio.guildscuboid.command;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Subcommand;
import it.acctudio.guildscuboid.Guilds;
import it.acctudio.guildscuboid.Items.UtilityItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
@CommandAlias("cuboidmanager")
public class GuildCommand extends BaseCommand {

    Guilds plugin;
    public GuildCommand(Guilds plugin) {
        this.plugin = plugin;
    }

    @Subcommand("create")
    @Conditions("isPlayer")
    public void onCreate(Player sender , String regionName) {
        if(!sender.isOp()) return;
        plugin.getGuildManager().createRegion(sender , "gp_" + regionName);

    }
}
