package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;

public class OnSignChange implements Listener{
private Main plugin;
	
	public OnSignChange(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignChange (SignChangeEvent e) {
		Player player = e.getPlayer();
		if ((e.getLine(0).equalsIgnoreCase("[eventsrun]")) || (e.getLine(0).equalsIgnoreCase("[gc]")) || (e.getLine(0).equalsIgnoreCase("[events]"))) {
			if (!(player.hasPermission("eventsrun.create.signs"))) {
				e.setCancelled(true);
				plugin.getMessages().sendMessage(player, "You can't create this type of sign", MessageLevel.WARN);
				return;
			}
		}
		
		if ((e.getLine(0).equalsIgnoreCase("[Eventsrun]")) || (e.getLine(0).equalsIgnoreCase("[GC]"))) {			
			String line2 = e.getLine(1);
			String line3 = e.getLine(2);
			
			if (line2.equalsIgnoreCase("finish")) {
				if (!(plugin.getEventsHandler().isCreatedAndLoaded(player, line3))) {
					e.setLine(0, "§f[§bEvents§f]");
					e.setLine(1, "§cInvalid sign");
					e.setLine(2, "§f[§4§l!!!§f]");
					return;
				}
				
				plugin.getEventsHandler().saveSign(player, (Sign)e.getBlock().getState(), plugin.getEventsHandler().stringToMainEvents(line3));
				e.setLine(0, "§f[§bEvents§f]");
				e.setLine(1, "§aFinish");
				e.setLine(2, "§9§l" + line3);
				
				return;
			}
			
			plugin.getMessages().sendMessage(player, "Invalid sign.", MessageLevel.WARN);
			e.setLine(0, "§f[§bEvents§f]");
			e.setLine(1, "§cInvalid sign");
			e.setLine(2, "§f[§4§l!!!§f]");
		}
	}	
}

















