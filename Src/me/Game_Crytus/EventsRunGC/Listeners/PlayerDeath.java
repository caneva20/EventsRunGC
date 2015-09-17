package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class PlayerDeath implements Listener {
	private static Main plugin;
	
	public PlayerDeath (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent e) {
		MainEvents event = plugin.getEventsHandler().eventInside(e.getEntity());
		
		if (event == null) {
			return;
		}
		
		event.removePlayerLive(e.getEntity());
		int playerLives = event.getPlayerLives(e.getEntity());		
		
		if (playerLives <= 0) {
			plugin.getEventsHandler().removePlayer(e.getEntity(), event);
		}
	}
}
