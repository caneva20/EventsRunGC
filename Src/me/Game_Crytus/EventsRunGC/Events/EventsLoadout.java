package me.Game_Crytus.EventsRunGC.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.GiveLoadoutMethod;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;

public class EventsLoadout {
	private static Main plugin;
	private MainEvents event;
	
	private boolean giveLoadouts;
	private GiveLoadoutMethod method;	
	private List<Loadout> loadouts = new ArrayList<Loadout>(); 
	
	private boolean showInvForceChoose;
	private Loadout showInvDefaultLoadout;
	private String showInventoryTitle;
	
	
	public EventsLoadout (Main plugin, MainEvents event) {
		this.plugin = plugin;
		this.event = event;
		setup();
	}
	
	public EventsLoadout (Main plugin, List<Loadout> loadouts, MainEvents event) {
		this.plugin = plugin;
		this.loadouts = loadouts;
		this.event = event;
		setup();
	}
	
	
	public void setGiveLoadouts (boolean giveLoadouts) {
		this.giveLoadouts = giveLoadouts;
	}
	
	public boolean getGiveLoadouts () {
		return this.giveLoadouts;
	}
	
	public GiveLoadoutMethod getGiveMethod () {
		return this.method;
	}
	
	public void setGiveMethod (GiveLoadoutMethod method) {
		this.method = method;
	}
	
	private void setup () {
		ItemStack[] itemStack = new ItemStack[36];
		String[] loadouts = event.getConfig().getCustomConfig().getConfigurationSection("CONFIG.LOADOUTS.LOADOUTS").getKeys(false).toArray(new String[0]);
			
		for (String name : loadouts) {
			Loadout loadout = new Loadout();
			String id = event.getConfig().getCustomConfig().getString("CONFIG.LOADOUTS.LOADOUTS." + name + ".ID");
				
			loadout.setId(id);
			loadout.setName(name);
			loadout.setPermission(event.getConfig().getCustomConfig().getString("CONFIG.LOADOUTS.LOADOUTS." + name + ".PERMISSION"));
			loadout.setPriority(event.getConfig().getCustomConfig().getInt("CONFIG.LOADOUTS.LOADOUTS." + name + ".PRIORITY"));
				
			for (int o = 0; o < 36; o++) {
				itemStack[o] = event.getData().getCustomConfig().getItemStack("EVENT_LOADOUT." + id + ".Stack" + o);
			}
				
			Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
			inv.setContents(itemStack);
				
			Villager npc = (Villager) plugin.getFirstWorld().spawnEntity(new Location(plugin.getFirstWorld(), 0, 1, 0), EntityType.VILLAGER);
				
			npc.getEquipment().setHelmet(event.getData().getCustomConfig().getItemStack("EVENT_LOADOUT." + id + ".Stack36"));
			npc.getEquipment().setChestplate(event.getData().getCustomConfig().getItemStack("EVENT_LOADOUT." + id + ".Stack37"));
			npc.getEquipment().setLeggings(event.getData().getCustomConfig().getItemStack("EVENT_LOADOUT." + id + ".Stack38"));
			npc.getEquipment().setBoots(event.getData().getCustomConfig().getItemStack("EVENT_LOADOUT." + id + ".Stack39"));
				
			loadout.setInventory(inv);
			loadout.setEquipment(npc.getEquipment());
				
			this.loadouts.add(loadout);
			npc.setHealth(0D);
		}
		
		setGiveMethod(event.getConfig().getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.METHOD"));
		setShowInvForceChoose(event.getConfig().getCustomConfig().getBoolean("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.FORCE_CHOOSE"));
		
		for (Loadout load : this.loadouts) {
			if (load.getName().equals(event.getConfig().getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.DEFAULT_LOADOUT"))) {
				setShowInvDefaultLoadout(load);
			}
		}
		
		setShowInventoryTitle(event.getConfig().getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.INVENTORY_TITLE"));
	}
	
	public void setGiveMethod (String method) {
		if (method.equals("RANDOM")) {
			this.method = GiveLoadoutMethod.RANDOM;
			
		} else if (method.equals("RANDOM_NO_CHECK_PERM")) {
			this.method = GiveLoadoutMethod.RANDOM_NO_CHECK_PERM;
			
		} else if (method.equals("SHOW_INV")) {
			this.method = GiveLoadoutMethod.SHOW_INVENTORY;
			
		} else if (method.equals("SIGN")) {
			this.method = GiveLoadoutMethod.SIGN;
			
		} else {
			this.method = GiveLoadoutMethod.RANDOM;
		}
	}
	
	public void setLoadouts (List<Loadout> loadouts) {
		this.loadouts = loadouts;
	}
	
	public List<Loadout> getLoadouts () {
		return this.loadouts;
	}
	
	public void setShowInvForceChoose (boolean showInvForceLoadout) {
		this.showInvForceChoose = showInvForceLoadout;
	}
	
	public boolean getShoeInvForceChoose () {
		return this.showInvForceChoose;
	}
	
	public void setShowInvDefaultLoadout (Loadout showInvDefaultLoadout) {
		this.showInvDefaultLoadout = showInvDefaultLoadout;
	}
	
	public Loadout getShowInvDefaultLoadout () {
		return this.showInvDefaultLoadout;
	}
	
	public void setShowInventoryTitle (String showInventoryTitle) {
		this.showInventoryTitle = showInventoryTitle;
	}
	
	public String getShowInventoryTitle () {
		return this.showInventoryTitle;
	}
	
	public void giveLoadout (Player player) {		
		if (method.equals(GiveLoadoutMethod.RANDOM)) {
			boolean hasAnyPerm = false;
			for (Loadout load : loadouts) {
				if (player.hasPermission(load.getPermission()) || load.getPermission().equals("null")) {
					hasAnyPerm = true;
				}
			}
			
			if (!hasAnyPerm) {
				return;
			}
			
			boolean give = false;
			while (!give) {
				plugin.getMainLogger().debug("5");
				int random = (int)(Math.round((Math.random() * loadouts.size()) - 1));

				if (random < 0) {
					random = 0;
				}
				
				Loadout loadout = loadouts.get(random);
				
				if (player.hasPermission(loadout.getPermission()) || loadout.getPermission().equals("null")) {
					plugin.getMainLogger().debug("6");
					player.getInventory().setContents(loadout.getInventory().getContents());
					player.getEquipment().setArmorContents(loadout.getEquipment().getArmorContents());
					
					plugin.getMessages().sendMessage(player, "You received the kit §6" + loadout.getName() + "§e.", MessageLevel.INFO);
					
					give = true;
				}
			}
			plugin.getMainLogger().debug("7");
			plugin.getMessages().sendMessage(player, "You don't have permission to receive any kit, sorry :(", MessageLevel.INFO);
			
		} else if (method.equals(GiveLoadoutMethod.RANDOM_NO_CHECK_PERM)) {
			int random = (int)(Math.round((Math.random() * loadouts.size()) - 1));

			if (random < 0) {
				random = 0;
			}
			
			Loadout loadout = loadouts.get(random);
			
			player.getInventory().setContents(loadout.getInventory().getContents());
			player.getEquipment().setArmorContents(loadout.getEquipment().getArmorContents());
			
			plugin.getMessages().sendMessage(player, "You received the kit §6" + loadout.getName() + "§e.", MessageLevel.INFO);
			
		}
	}
	
	public Inventory getInventoryKits () {
		int size = loadouts.size() / 9;
		
		size *= 9;
		
		if (loadouts.size() % 9 > 0) {
			size += 9;
		}
		
		Inventory inventory = Bukkit.createInventory(null, size, showInventoryTitle.replace("&", "§"));
		
		int slot = 0;
		for (Loadout load : loadouts) {
			ItemStack item = new ItemStack(Material.CHEST);
				
			ItemMeta meta = item.getItemMeta();						
			meta.setDisplayName("§3" + load.getName());
				
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§eID: §6" + load.getId());
			lore.add("§eName: §6" + load.getName());
			lore.add("§ePermission: §6" + load.getPermission());
			lore.add("");
				
			meta.setLore(lore);
			item.setItemMeta(meta);
				
			inventory.setItem(slot++, item);					
		}
		
		return inventory;
	}
	
	public void giveLoadout (Player player, String id) {
		if (id.equals("null") || id.equals(null)) {
			plugin.getMessages().sendMessage(player, "You received an empty kit", MessageLevel.INFO);
			return;
		}
		
		for (Loadout load : loadouts) {
			if (load.getId().equals(id)) {
				player.getInventory().setContents(load.getInventory().getContents());
				player.getEquipment().setArmorContents(load.getEquipment().getArmorContents());
				
				plugin.getMessages().sendMessage(player, "You received the kit §6" + load.getName() + "§e.", MessageLevel.INFO);
				return;
			}
		}
	}
}

























