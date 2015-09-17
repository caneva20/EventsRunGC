package me.Game_Crytus.EventsRunGC.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.Game_Crytus.EventsRunGC.Main;
import me.confuser.barapi.BarAPI;

public class EventsTimer implements Listener {
	private int time = 0;
	private MainEvents event;
	private static Main plugin;
	private int taskID = -1;
	
	private float warmupTime;
	
	private boolean warmupDisplayWarmup = true;
	
	private boolean warmupPlayerExpBar;
	private boolean warmupPlayerLevel;
	private boolean warmupBarAPI;
	
	private boolean timerDisplayTimer = true;
	
	private boolean timerPlayerLevel;
	private boolean timerBarAPI;
	
	private boolean hasBarAPI;
	
	public EventsTimer (Main plugin, MainEvents event) {
		this.plugin = plugin;
		this.event = event;
		
		warmupTime = (float)event.getMovementWarmupTime();
		
		timerPlayerLevel = event.getTimerPlayerLevel();
		timerBarAPI = event.getTimerBarAPI();
		
		warmupPlayerExpBar = event.getWarmupPlayerExpBar();
		warmupPlayerLevel = event.getWarmupPlayerLevel();
		warmupBarAPI = event.getWarmupBarAPI();
		
		if (!timerBarAPI && !timerPlayerLevel) {
			timerDisplayTimer = false;
		}
		
		if (!warmupBarAPI && !warmupPlayerExpBar && !warmupPlayerLevel) {
			warmupDisplayWarmup = false;
		}
		
		if (plugin.getServer().getPluginManager().getPlugin("BarAPI") != null) {
			hasBarAPI = true;
		}
				
		startTimer();
	}
	
	public void startTimer() {
		if (this.taskID != -1) {
			stopTimer();
		}		
		
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if (warmupDisplayWarmup) {
					float percent = (warmupTime - time) / warmupTime;
					
					for (UUID uuid : event.getJoinedPlayers()) {
						Player player = plugin.getServer().getPlayer(uuid);
						
						if (warmupPlayerExpBar) {
							player.setExp(percent);
						}
						
						if (warmupPlayerLevel) {
							player.setLevel((int)warmupTime - time);
						}
						
						if (hasBarAPI && warmupBarAPI) {
							BarAPI.setMessage(player, "§eStarting in §6" + plugin.getUtil().convertSecondsToMinutes((int)(warmupTime - time)) + " §eseconds", (percent * 100F));
						}
						
						if (percent <= 0) {
							warmupDisplayWarmup = false;
							if (hasBarAPI) {
								BarAPI.removeBar(player);
							}
						}
					}
				} else if (timerDisplayTimer) {
					for (UUID uuid : event.getJoinedPlayers()) {
						Player player = plugin.getServer().getPlayer(uuid);
												
						if (warmupPlayerLevel) {
							player.setLevel(time);
						}
						
						if (hasBarAPI && warmupBarAPI) {
							BarAPI.setMessage(player, "§eElapsed time §6" +  plugin.getUtil().convertSecondsToMinutes(time) + " §eminutes.");
						}
					}
				}
				
    			EventsTimer.this.time++;
    		}
		}, 0L, 20L);
	}
	
  	public void stopTimer() {
  		if (this.taskID != -1) {
			Bukkit.getScheduler().cancelTask(this.taskID);
		}
	}
  	
  	public int getTime() {
  		return EventsTimer.this.time;
  	}
  	
  	public void setTime(int time) {
  		EventsTimer.this.time = time;
  	}
}


















