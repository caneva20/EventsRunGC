package me.Game_Crytus.EventsRunGC.Listeners;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnMoveFinish implements Listener{
	private Main plugin;
	
	public OnMoveFinish (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnMove(PlayerMoveEvent event) {
		if (plugin.getEventsHandler().eventOn()) {
			MainEvents eventInside = plugin.getEventsHandler().eventInside(event.getPlayer());			
			
			if (eventInside != null) {
				if (eventInside.getState().equals(EventState.RUNNING)) {
					if ((event.getPlayer().getLocation().getWorld().equals(eventInside.getFinishLocation(0).getWorld()))
							&& (((double)Double.valueOf(event.getPlayer().getLocation().getX()).intValue()) >= eventInside.finishGetLowerX()) && (((double)Double.valueOf(event.getPlayer().getLocation().getX()).intValue()) <= eventInside.finishGetBiggerX())
							&& (((double)Double.valueOf(event.getPlayer().getLocation().getY()).intValue()) >= eventInside.finishGetLowerY()) && (((double)Double.valueOf(event.getPlayer().getLocation().getY()).intValue()) <= eventInside.finishGetBiggerY())
							&& (((double)Double.valueOf(event.getPlayer().getLocation().getZ()).intValue()) >= eventInside.finishGetLowerZ()) && (((double)Double.valueOf(event.getPlayer().getLocation().getZ()).intValue()) <= eventInside.finishGetBiggerZ())) {
						
						plugin.getEventsHandler().winner(event.getPlayer(), eventInside);
					}
				}
			}
		}
	}
}


















