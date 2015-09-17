package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class OnClickSignFinish implements Listener {
	private Main plugin;
	
	public OnClickSignFinish (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onClickSign (PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		if (!(event.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if (!sign.getLine(0).replaceAll("(§|&)([0-9a-f]|l)", "").equalsIgnoreCase("[Events]")) {
			return;
		}
		
		if (sign.getLine(1).replaceAll("(§|&)([0-9a-f]|l)", "").equalsIgnoreCase("Finish")) {
			if (plugin.getEventsHandler().eventOn()) {
				MainEvents eventInside = plugin.getEventsHandler().eventInside(event.getPlayer());
						
				if (eventInside != null) {
					if (eventInside.getState().equals(EventState.RUNNING)) {
						plugin.getEventsHandler().winner(event.getPlayer(), eventInside);
					}
				}
			}
		}
	}
}




















