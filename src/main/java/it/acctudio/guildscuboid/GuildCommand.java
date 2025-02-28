package it.acctudio.guildscuboid;

import it.acctudio.guildscuboid.Items.FlagItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuildCommand implements CommandExecutor {

    Guilds plugin;
    public GuildCommand(Guilds plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!sender.isOp()) return false;
        if (args[0].equalsIgnoreCase("wand")) {
            ItemStack wand = new FlagItem(plugin);
            Player player = (Player) sender;
            player.getInventory().addItem(wand);
            player.sendMessage("Otrzymałeś flagę! Lewy klik, aby ustawić centrum regionu 200x200.");
            return true;
        }

        return false;
    }
}
