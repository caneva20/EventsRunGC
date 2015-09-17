package me.Game_Crytus.EventsRunGC.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class OnReceiveDamage implements Listener {
	private static Main plugin;
	
	public OnReceiveDamage (Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnReceiveDamageByEntity (EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		MainEvents event = plugin.getEventsHandler().eventInside((Player)e.getEntity());
		
		if (event == null) {
			return;
		}
		
		if (event.getBlockDamageByEntity()) {
			if (!(e.getDamager() instanceof Player)) {
				e.setCancelled(true);
			}
		}
		
		if (event.getBlockDamageByPlayer()) {
			if (e.getDamager() instanceof Player) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void OnReceiveDamegeByBlock (EntityDamageByBlockEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		MainEvents event = plugin.getEventsHandler().eventInside((Player)e.getEntity());
		
		if (event == null) {
			return;
		}
		
		if (event.getBlockDamageByBlocks()) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void OnReceiveDamegeAll (EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		MainEvents event = plugin.getEventsHandler().eventInside((Player)e.getEntity());
		
		if (event == null) {
			return;
		}
		
		if (event.getBlockDamageByOthers()) {
			e.setCancelled(true);
		}
	}
}

















