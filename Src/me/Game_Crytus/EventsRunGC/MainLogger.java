package me.Game_Crytus.EventsRunGC;

import org.bukkit.entity.Player;

public class MainLogger {
	private Main plugin;
	private String consoleTag = "§f[§6EventsRunGC§f] ";
	
	public MainLogger (Main plugin) {
		this.plugin = plugin;
	}
	
	public void log (Player player, String message) {
		player.sendMessage(message);
	}
	
	public void InfoConsole(String message) {
		plugin.getServer().getConsoleSender().sendMessage(consoleTag + "§e" + message);
	}
	
	public void WarnConsole(String message) {
		plugin.getServer().getConsoleSender().sendMessage(consoleTag + "§c" + message);
	}
	
	public void InfoNoTag(Player player, String message) {
		player.sendMessage("§e" + message);
	}
	
	public void WarnNoTag(Player player, String message) {
		player.sendMessage("§c" + message);
	}
	
	public void debug (String message) {
		plugin.getServer().getConsoleSender().sendMessage("§f[§bDEBUG§f]§b " + message);
	}
}




















