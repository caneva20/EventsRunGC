package me.Game_Crytus.EventsRunGC.Events;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Game_Crytus.EventsRunGC.ConfigFiles;
import me.Game_Crytus.EventsRunGC.Main;

public class InventoryHandler {
	private static Main plugin;
	private ConfigFiles config;
	
	public InventoryHandler (Main plugin) {
		this.plugin = plugin;
		this.config = new ConfigFiles(plugin, "Events/DataBase/Inventories", "Config/EmptyFile");
	}
	
	public void saveInventory (Player player, boolean clearBefore, boolean clearAfter) {
		Inventory inventory = player.getInventory();
		UUID uuid = player.getUniqueId();
		
		if (clearBefore) {
			config.getCustomConfig().set(uuid + "", null);
			config.saveCustomConfig();
		}
		
		int i = 0;
		for (ItemStack stack : inventory) {			
			config.getCustomConfig().set(uuid + ".Stack" + i++, stack);
			
		}
		
		config.getCustomConfig().set(uuid + ".Stack36", player.getEquipment().getHelmet());
		config.getCustomConfig().set(uuid + ".Stack37", player.getEquipment().getChestplate());
		config.getCustomConfig().set(uuid + ".Stack38", player.getEquipment().getLeggings());
		config.getCustomConfig().set(uuid + ".Stack39", player.getEquipment().getBoots());
		
		config.getCustomConfig().set(uuid + ".XP", player.getExp());
		config.getCustomConfig().set(uuid + ".Level", player.getLevel());
		
		config.getCustomConfig().set(uuid + ".Health", ((Damageable)player).getHealth());
		config.getCustomConfig().set(uuid + ".Food", player.getFoodLevel());
		
		String gm = "Survival";
		
		if (player.getGameMode().equals(GameMode.SURVIVAL)) {
			gm = "Survival";
			
		} else if (player.getGameMode().equals(GameMode.ADVENTURE)) {
			gm = "Adventure";
			
		}  else if (player.getGameMode().equals(GameMode.CREATIVE)) {
			gm = "Creative";
			
		}
		
		config.getCustomConfig().set(uuid + ".Gamemode", gm);
		
//		config.getCustomConfig().set(uuid + ".PotionEffects", player.getActivePotionEffects().toArray());
//		player.getActivePotionEffects().clear();
		config.saveCustomConfig();
		
		if (clearAfter) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setExp(0);
			player.setLevel(0);
			player.setHealth(20.0);
			player.setFoodLevel(20);
		}
	}
	
	public void loadInventory (Player player, boolean clear) {
		UUID uuid = player.getUniqueId();
		ItemStack[] inventory = new ItemStack[36];
		
		for (int o = 0; o < 36; o++) {
			inventory[o] = config.getCustomConfig().getItemStack(uuid + ".Stack" + o);
		}
		
		player.getInventory().setContents(inventory);
		player.getInventory().setHelmet(config.getCustomConfig().getItemStack(uuid + ".Stack36"));
		player.getInventory().setChestplate(config.getCustomConfig().getItemStack(uuid + ".Stack37"));
		player.getInventory().setLeggings(config.getCustomConfig().getItemStack(uuid + ".Stack38"));
		player.getInventory().setBoots(config.getCustomConfig().getItemStack(uuid + ".Stack39"));
		
		player.setExp((float) config.getCustomConfig().getDouble(uuid + ".XP"));
		player.setLevel(config.getCustomConfig().getInt(uuid + ".Level"));
		
		player.setHealth(config.getCustomConfig().getDouble(uuid + ".Health"));
		player.setFoodLevel(config.getCustomConfig().getInt(uuid + ".Food"));
		
		String gm = config.getCustomConfig().getString(uuid + ".Gamemode");
		
		if (gm == "Survival") {
			player.setGameMode(GameMode.SURVIVAL);
			
		} else if (gm == "Adventure") {
			player.setGameMode(GameMode.ADVENTURE);
			
		}  else if (gm == "Creative") {
			player.setGameMode(GameMode.CREATIVE);
			
		}
		
		if (clear) {
			config.getCustomConfig().set(uuid + "", null);
			config.saveCustomConfig();
		}
	}
}





















