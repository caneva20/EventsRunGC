package me.Game_Crytus.EventsRunGC.Signs;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.util.Util;

public class LoadoutSign implements Listener {
	private static Main plugin;
	private static Util util;
	
	public LoadoutSign (Main plugin) {
		this.plugin = plugin;
		util = new Util(plugin);
	}
	
	
	@EventHandler
	private void OnClickSign (PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		if (!(event.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if (!util.removeColor(sign.getLine(0)).equalsIgnoreCase("[Events]")) {
			return;
		}
		
		if (!util.removeColor(sign.getLine(1)).equalsIgnoreCase("Loadout")) {
			return;
		}
		
		plugin.getEventsHandler().getEventLoadout(util.converStringToMainEvents(sign.getLine(2))).giveLoadout(event.getPlayer(), util.removeColor(sign.getLine(3)));
	}
}


















