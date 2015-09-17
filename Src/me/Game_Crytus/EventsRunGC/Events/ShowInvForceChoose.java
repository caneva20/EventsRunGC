package me.Game_Crytus.EventsRunGC.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Game_Crytus.EventsRunGC.Main;
import net.minecraft.server.v1_7_R4.Material;

public class ShowInvForceChoose implements Listener {
	private static Main plugin;
	private int taskID = -1;
	private Player player;
	private Inventory inventory;
	private boolean chosen = false;
	
	public ShowInvForceChoose (Main plugin, Player player, Inventory inventory) {
		this.plugin = plugin;
		this.player = player;
		this.inventory = inventory;
		
		start();
	}
	
	
	public void start() {
		if (this.taskID != -1) {
			Bukkit.getScheduler().cancelTask(this.taskID);
		}		
		
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				
				if (player.getOpenInventory().getTitle() != inventory.getTitle()) {
					player.openInventory(inventory);
				}
    		}
		}, 0L, 20L);
	}
	
	
	@EventHandler
	public void onPlayerClick (InventoryClickEvent event) {
		//plugin.getMainLogger().debug(event.getAction() + "");
		if (event.getInventory().getTitle() != inventory.getTitle()) {
			return;
		}
		
		if (event.getAction() != InventoryAction.PICKUP_ALL) {
			return;
		}
		
		ItemStack item = event.getCurrentItem();
		
		if (item.equals(Material.AIR) || item.equals(null)) {
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		
		String id = null;
		
		for (String ID : item.getItemMeta().getLore()) {
			if (ID.matches("§eID: §6.*")) {
				id = ID.replace("§eID: §6", "");
			}
		}
		
		plugin.getMainLogger().debug("........");
		
		
		if (!id.equals(null)) {
			plugin.getEventsHandler().getEventLoadout(plugin.getEventsHandler().eventInside(player)).giveLoadout(player, id);
			HandlerList.unregisterAll(this);
			Bukkit.getScheduler().cancelTask(this.taskID);
			player.getOpenInventory().close();			
		} else {
			plugin.getMainLogger().WarnConsole("An error has occured while trying to give loadout. ID can't be null");
		}
	}
}


















