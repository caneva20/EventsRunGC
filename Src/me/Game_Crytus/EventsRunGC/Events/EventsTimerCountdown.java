package me.Game_Crytus.EventsRunGC.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;
import me.confuser.barapi.BarAPI;
import net.minecraft.server.v1_7_R4.Material;

public class EventsTimerCountdown implements Listener {
	private int time = 0;
	private int fullTime;
	private MainEvents event;
	private static Main plugin;
	private int taskID = -1;
	
	private List<Integer> announcementsTimerInside = new ArrayList<Integer>();
	private List<Integer> announcementsTimerGlobal = new ArrayList<Integer>();

	private List<String> announcesInside;
	private List<String> announcesGlobal;

	private boolean announceInsideInside;
	private boolean announceInsideGlobal;
	private boolean announceGlobalInside;
	private boolean announceGlobalGlobal;

	private Sound announceInsideSound;
	private Sound announceGlobalSound;

	private boolean announceInsidePlaySound;
	private boolean announceGlobalPlaySound;

	private String announceInsideTag;
	private String announceGlobalTag;
	
	private boolean countdownPlayerExpBar;
	private boolean countdownPlayerLevel;
	private boolean countdownBarAPI;
	
	private boolean hasBarAPI = false;
	
	private List<UUID> needChooseKit;
	private Map<UUID, String> kits;
	private Inventory inventoryKits;
	
	private boolean showInvToChooseKit;
	private boolean forceToChooseKit;
  
	public EventsTimerCountdown (Main plugin, MainEvents event, int time) {
		this.plugin = plugin;
		this.event = event;
		this.time = time;
		this.fullTime = time;
		announcementsTimerInside = event.getAnnounceInsideTimer();
		announcementsTimerGlobal = event.getAnnounceGlobalTimer();
		
		announcesInside = event.getAnnouncementsInsideMessage();
		announcesGlobal = event.getAnnouncementsGlobalMessage();

		announceInsideInside = event.getAnnouncemnetsInsideAnnounceInside();
		announceInsideGlobal = event.getAnnouncementsInsideAnnounceGlobally();
		announceGlobalInside = event.getAnnouncemetsGlobalAnnounceInside();
		announceGlobalGlobal = event.getAnnouncementsGlobalAnnounceGlobally();
		
		announceInsideSound = event.getAnnouncementsInsideSound();
		announceGlobalSound = event.getAnnouncementsGlobalSound();

		announceInsidePlaySound = event.getAnnouncementsInsidePlaySound();
		announceGlobalPlaySound = event.getAnnouncementsGlobalPlaySound();

		announceInsideTag = event.getAnnouncementInsideTag();
		announceGlobalTag = event.getAnnouncementGlobalTag();
		
		countdownPlayerExpBar = event.getCountdownPlayerExpBar();
		countdownPlayerLevel = event.getCountdownPlayerLevel();
		countdownBarAPI = event.getCountdownBarAPI();
		
		if (plugin.getServer().getPluginManager().getPlugin("BarAPI") != null) {
			hasBarAPI = true;
		} else {
			countdownBarAPI = false;
		}

		needChooseKit = new ArrayList<UUID>();
		kits = new HashMap<UUID, String>();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		inventoryKits = event.getEventLoadout().getInventoryKits();
		
		startTimer();
	}
  
	public void startTimer() {
		if (this.taskID != -1) {
			stopTimer();
		}
		
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				// Announces inside
				if (announcementsTimerInside.contains(time)) {
					if (announceInsideInside) {
						for (UUID uuid : event.getJoinedPlayers()) {
							for (String string : announcesInside) {
								Player player = plugin.getServer().getPlayer(uuid);

								plugin.getMessages().sendMessage(player, replaceVariables(event, string), replaceVariables(event, announceInsideTag));

								if (announceInsidePlaySound) {
									player.playSound(player.getLocation(), announceInsideSound, 10F, 1F);
								}
							}
						}
					}

					if (announceInsideGlobal) {
						for (Player player : plugin.getServer().getOnlinePlayers()) {
							if (!(event.getJoinedPlayers().contains(player.getUniqueId()))) {
								for (String string : announcesInside) {
									plugin.getMessages().sendMessage(player, replaceVariables(event, string), replaceVariables(event, announceInsideTag));

									if (announceInsidePlaySound) {
										player.playSound(player.getLocation(), announceInsideSound, 10F, 1F);
									}
								}
							}
						}
					}
				}
				
				// Announces global
				if (announcementsTimerGlobal.contains(time)) {
					if (announceGlobalInside) {
						for (UUID uuid : event.getJoinedPlayers()) {
							for (String string : announcesGlobal) {
								Player player = plugin.getServer().getPlayer(uuid);

								plugin.getMessages().sendMessage(player, replaceVariables(event, string), replaceVariables(event, announceGlobalTag));

								if (announceGlobalPlaySound) {
									player.playSound(player.getLocation(), announceGlobalSound, 10F, 1F);
								}
							}
						}
					}

					if (announceGlobalGlobal) {
						for (Player player : plugin.getServer().getOnlinePlayers()) {
							if (!(event.getJoinedPlayers().contains(player.getUniqueId()))) {
								for (String string : announcesGlobal) {
									plugin.getMessages().sendMessage(player, replaceVariables(event, string), replaceVariables(event, announceGlobalTag));

									if (announceGlobalPlaySound) {
										player.playSound(player.getLocation(), announceGlobalSound, 10F, 1F);
									}
								}
							}
						}
					}
				}
				
				for (UUID uuid : event.getJoinedPlayers()) {
					Player player = plugin.getServer().getPlayer(uuid);
					
					if (countdownPlayerExpBar) {
						player.setExp(Integer.valueOf(time).floatValue() / Integer.valueOf(fullTime).floatValue());
					}
					
					if (countdownPlayerLevel) {
						player.setLevel(time);
					}
					
					if (hasBarAPI && countdownBarAPI) {
						BarAPI.setMessage(player, "§eStarting in §6" + plugin.getUtil().convertSecondsToMinutes(time) + " §eseconds", (((float)time / (float)fullTime) * 100F));
					}
				}				
				
				if (showInvToChooseKit) {
    				for (UUID uuid : event.getJoinedPlayers()) {
    					Player player = plugin.getServer().getPlayer(uuid);
    					
    					if (needChooseKit.contains(uuid)) {
    						if (player.getOpenInventory().getTitle() != inventoryKits.getTitle()) {
    							player.openInventory(inventoryKits);
    						}
    						
    						if (!forceToChooseKit) {
    							needChooseKit.remove(uuid);
    						}
    					}
    				}
    			}				
    		
    			if (EventsTimerCountdown.this.time == 0) {
    				EventsTimerCountdown.this.stopTimer();
    				
    				for (UUID uuid : event.getJoinedPlayers()) {
    					
    					if (hasBarAPI) {
    						BarAPI.removeBar(plugin.getServer().getPlayer(uuid));
    					}
    					
    					if (!kits.containsKey(uuid)) {
    						kits.put(uuid, event.getLoadoutShowInvConfigDefaultLoadout());
    					}
    				}
    				
    				for (UUID uuid : kits.keySet()) {
    					event.getEventLoadout().giveLoadout(plugin.getServer().getPlayer(uuid), kits.get(uuid));    					
    				}
    				
    				plugin.getEventsHandler().start(event);
    			}

    			EventsTimerCountdown.this.time--;
    		}
		}, 0L, 20L);
	}
  
	@EventHandler
	public void onPlayerClick (InventoryClickEvent event) {
		if (EventsTimerCountdown.this.time <= 0) {
			return;
		}
		
		if (!showInvToChooseKit) {
			return;
		}
		
		if (event.getInventory().getTitle() != inventoryKits.getTitle()) {
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
		
		if (!id.equals(null)) {
			player.getOpenInventory().close();			
			needChooseKit.remove(player.getUniqueId());
			kits.put(player.getUniqueId(), id);
			plugin.getMessages().sendMessage(player, "You choose the kit §6" + id + "§.", MessageLevel.INFO);
			
		} else {
			plugin.getMainLogger().WarnConsole("An error has occured while trying to give loadout. ID can't be null");
		}
	}	
	
  	public void stopTimer() {
  		if (this.taskID != -1) {
			Bukkit.getScheduler().cancelTask(this.taskID);
		}
	}
  	
  	public int getTime () {
  		return EventsTimerCountdown.this.time;
  	}
  	
  	public void setTime (int time) {
  		EventsTimerCountdown.this.time = time;
  	}

	public String replaceVariables(MainEvents event, String string) {
		string = string.replace("&", "§");
		string = string.replace("{EVENT_NAME}", event.getDisplayName());
		string = string.replace("{TIME_REMAINING}", plugin.getUtil().convertSecondsToMinutes(time));
		string = string.replace("{PLACES_REMAINING}", (event.getMaxPlayers() - event.getJoinedPlayers().size()) + "");
		string = string.replace("{PLAYERS_JOINED}", event.getJoinedPlayers().size() + "");

		return string;
	}
	
	public void setShowInvToChooseKit (boolean showInvToChooseKit) {
		this.showInvToChooseKit = showInvToChooseKit;
	}
	
	public void setForceChoosekit (boolean forceChooseKit) {
		this.forceToChooseKit = forceChooseKit;
	}
	
	public void addNeedChooseKit (Player player) {
		needChooseKit.add(player.getUniqueId());
	}
	
	public void removeNeedChooseKit (Player player) {
		if (needChooseKit.contains(player.getUniqueId())) {
			needChooseKit.remove(player.getUniqueId());
		}
	}
}


















