package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class OnPlayerQuit implements Listener {
	private static Main plugin;
	
	public OnPlayerQuit(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		MainEvents mainEvent = plugin.getEventsHandler().eventInside(event.getPlayer());
		
		if (mainEvent != null) {
			plugin.getEventsHandler().removePlayer(event.getPlayer(), mainEvent);
			
			if (mainEvent.getState().equals(EventState.RUNNING) && mainEvent.getJoinedPlayers().size() <= 0) {
				plugin.getEventsHandler().stop(mainEvent);
			}
		}
	}
	
	@EventHandler
	public void onDisconect(PlayerKickEvent event) {
		MainEvents mainEvent = plugin.getEventsHandler().eventInside(event.getPlayer());
		
		if (mainEvent != null) {
			plugin.getEventsHandler().removePlayer(event.getPlayer(), mainEvent);
			
			if (mainEvent.getState().equals(EventState.RUNNING) && mainEvent.getJoinedPlayers().size() <= 0) {
				plugin.getEventsHandler().stop(mainEvent);
			}
		}
	}
}





















