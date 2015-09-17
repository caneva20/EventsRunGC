package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.Game_Crytus.EventsRunGC.Main;

public class MainEventsListener implements Listener{
//|]=------------------=[Variables]=------------------=-[|//	
	private Main plugin;
	
	public MainEventsListener(Main plugin) {
		this.plugin = plugin;
	}
	
//	//@EventHandler
//	public void onQuit(PlayerQuitEvent event) {
//		MainEvents mainEvent = plugin.getEventsHandler().isInsideEvent(event.getPlayer());
//		
//		if (mainEvent != null) {
//			plugin.getEventsHandler().removePlayer(event.getPlayer(), mainEvent);
//			
//			if (mainEvent.getState().equals(EventState.Running) && mainEvent.getJoinedPlayers().size() <= 0) {
//				plugin.getEventsHandler().stop(mainEvent);
//			}
//		}
//	}
	
//	@EventHandler
//	public void onPreLogin(PlayerPreLoginEvent event) {
//		event.disallow(null, "Sorry, but you can't join for now");
//		
//	}
//	
//	@EventHandler
//	public void OnLogin (PlayerJoinEvent event) {
//		event.getPlayer().kickPlayer("§2Sorry, but you can't join for now!");
//	}
	
	@EventHandler
	public void jump(PlayerInteractEvent e) {
		plugin.getMainLogger().debug(".");
		if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
			plugin.getMainLogger().debug("..");
			e.getPlayer().setVelocity(new Vector(1,2,1));
		}
	}
}


















