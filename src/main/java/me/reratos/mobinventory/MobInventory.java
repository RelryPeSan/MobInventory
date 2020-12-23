package me.reratos.mobinventory;

import me.reratos.mobinventory.events.PlayerListener;
import me.reratos.mobinventory.util.Constants;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MobInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        initMetrics();
        initEvents();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "MobInventory: ativado");
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }

    public void initEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void initMetrics() {
        Metrics metrics = new Metrics(this, Constants.PLUGIN_METRICS_ID);

//        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
    }
}
