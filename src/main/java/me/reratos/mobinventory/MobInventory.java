package me.reratos.mobinventory;

import me.reratos.mobinventory.events.PlayerListener;
import me.reratos.mobinventory.util.Constants;
import me.reratos.mobinventory.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MobInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        checkForUpdate();

        initMetrics();
        initEvents();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "MobInventory: Enabled");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MobInventory: Disabled");
    }

//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        return super.onCommand(sender, command, label, args);
//    }

    public void initEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void initMetrics() {
        Metrics metrics = new Metrics(this, Constants.PLUGIN_METRICS_ID);

//        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }

    public void checkForUpdate() {
        new UpdateChecker(this, Constants.PLUGIN_RESOURCE_ID).getVersionConsumer(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage("Plugin updated: " + version);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.BOLD +""+ ChatColor.GREEN + "New update available!" +
                        ChatColor.RESET + "\nCurrent version: " + ChatColor.LIGHT_PURPLE +
                        this.getDescription().getVersion() + ChatColor.RESET + "\nLast version: " + ChatColor.YELLOW +
                        version);
                Bukkit.getConsoleSender().sendMessage("Access: " + ChatColor.UNDERLINE + Constants.URL_PLUGIN_SPIGOT);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp()) {
                        p.sendMessage(ChatColor.BOLD +""+ ChatColor.GREEN + "New update available!" + ChatColor.RESET +
                                "\nCurrent version: " + ChatColor.LIGHT_PURPLE + this.getDescription().getVersion() +
                                ChatColor.RESET + "\nLast version: " + ChatColor.YELLOW + version);
                        p.sendMessage("Access: " + ChatColor.UNDERLINE + Constants.URL_PLUGIN_SPIGOT);
                    }
                }
            }
        });
    }
}
