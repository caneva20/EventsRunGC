package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class ChangeGamemode implements Listener{
	private static Main plugin;
	
	public ChangeGamemode (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnChangeGamemode (PlayerGameModeChangeEvent e) {
		MainEvents event = plugin.getEventsHandler().eventInside(e.getPlayer());
		
		if (event == null || !event.getState().equals(EventState.RUNNING)) {
			return;
		}
		
		e.setCancelled(true);
	}
}
