package me.imsean.clearregion;

import me.imsean.clearregion.commands.ClearRegionCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ClearRegion extends JavaPlugin {

    private Plugin plugin;

    @Override
    public void onEnable() {
        this.plugin = this;
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("clearregion").setExecutor(new ClearRegionCommand(this.plugin));
    }

}
