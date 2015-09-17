package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;

public class OnSignBreak implements Listener{
	private Main plugin;

	public OnSignBreak(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignBreak (BlockBreakEvent e) {
		Block block = e.getBlock();
		Player player = e.getPlayer();
		
		if (block.getState() instanceof Sign) {
			String[] lines = ((Sign) e.getBlock().getState()).getLines();			
			
			//plugin.getMainLogger().debug("");
			if (!(lines[0].replaceAll("(§|&)([0-9a-f]|l)", "").equalsIgnoreCase("[Events]"))) {
				return;				
			}
			
			if (player.hasPermission("eventsrun.break.sign")) {
				if (player.isSneaking()) {
					if (!(lines[2].isEmpty())) {
						String eventName = lines[2].replaceAll("(§|&)([0-9a-f]|l)", "");
						
						if (plugin.getEventsHandler().exist(eventName)) {
							plugin.getEventsHandler().removeSign(player, (Sign)e.getBlock().getState(), plugin.getEventsHandler().stringToMainEvents(eventName));
						}
					}					
					
					plugin.getMessages().sendMessage(player, "Event sign broken.", MessageLevel.INFO);
				} else {
					e.setCancelled(true);
					plugin.getMessages().sendMessage(player, "You can't break this sign for now.", MessageLevel.WARN);
					plugin.getMessages().sendMessage(player, "If you really want do this, just sneak and hit the sign again", MessageLevel.INFO);
				}
			} else {
				plugin.getMessages().sendMessage(player, "You don't have permission to do this.", MessageLevel.WARN);
				e.setCancelled(true);
			}
		}
	}
}


















