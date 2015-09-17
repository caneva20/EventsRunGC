package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class OnMoveWarmupWaiting implements Listener {
	private static Main plugin;
	
	
	public OnMoveWarmupWaiting (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled=true)
	private void OnMove(PlayerMoveEvent event) {
		MainEvents mainEvent = plugin.getEventsHandler().eventInside(event.getPlayer());
		
//		plugin.getMainLogger().debug(".....");
//		plugin.getMainLogger().debug("Time: " + mainEvent.getTimer().getTime());
//		plugin.getMainLogger().debug("WarmupTime: " + mainEvent.getMovementWarmupTime());
//		plugin.getMainLogger().debug("Move: " + mainEvent.getMovementMoveWarmup());	
		
		if (mainEvent != null) {
			if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
				if (mainEvent.getState().equals(EventState.WAITING)) {
					if (!mainEvent.getMovementMoveWaiting()) {
						event.getPlayer().teleport(event.getFrom());
					
						if (mainEvent.getMovementSendMessageTryingMove()) {
							plugin.getMessages().sendMessage(event.getPlayer(), "You can't move while waiting", MessageLevel.WARN);
						}
					}
				} else if (mainEvent.getState().equals(EventState.RUNNING)) {
					if (!mainEvent.getMovementMoveWarmup()) {
						if (mainEvent.getTimer().getTime() <= mainEvent.getMovementWarmupTime()) {
							event.getPlayer().teleport(event.getFrom());
						
							if (mainEvent.getMovementSendMessageTryingMove()) {
								plugin.getMessages().sendMessage(event.getPlayer(), "You can't move while warmuping", MessageLevel.WARN);
							}
						}
					}
				}
			}
		}
	}
}















