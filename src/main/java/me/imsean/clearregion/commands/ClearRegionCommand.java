package me.imsean.clearregion.commands;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ClearRegionCommand implements CommandExecutor {

    private final Plugin plugin;
    private final RegionContainer container;

    public ClearRegionCommand(Plugin plugin) {
        this.plugin = plugin;
        this.container = this.getWorldGuard().getRegionContainer();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (args.length < 2) return false;

            String regionName = args[0], worldName = args[1];
            World regionWorld = Bukkit.getWorld(worldName);

            if (regionWorld == null) {
                sender.sendMessage(String.format("World \"%s\" does not exist.", worldName));
                return true;
            }

            RegionManager regions = this.container.get(regionWorld);
            if (regions != null) {
                ProtectedRegion region = regions.getRegion(regionName);

                if (region != null) {
                    for (int x = region.getMinimumPoint().getBlockX(); x <= region.getMaximumPoint().getBlockX(); x++) {
                        for (int z = region.getMinimumPoint().getBlockZ(); z <= region.getMaximumPoint().getBlockZ(); z++) {
                            for (int y = region.getMinimumPoint().getBlockY(); y <= region.getMaximumPoint().getBlockY(); y++) {
                                Location loc = new Location(regionWorld, x, y, z);

                                if (loc.getBlock().getType() != Material.AIR) {
                                    loc.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }

                    sender.sendMessage(ChatColor.GREEN + "Blocks Cleared");
                    return true;
                } else {
                    sender.sendMessage(String.format("Could not load region \"%s\"", regionName));
                    return true;
                }

            } else {
                sender.sendMessage(String.format("Could not load regions in world \"%s\"", regionName));
                return true;
            }
        } else {
            sender.sendMessage("Only players or console can execute this command.");
            return true;
        }
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin wgPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(wgPlugin instanceof WorldGuardPlugin)) {
            throw new RuntimeException("WorldGuard could not be loaded");
        }

        return (WorldGuardPlugin) wgPlugin;
    }

}
