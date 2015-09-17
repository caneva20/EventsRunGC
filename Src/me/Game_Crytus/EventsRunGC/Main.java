package me.Game_Crytus.EventsRunGC;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.Game_Crytus.EventsRunGC.CommandExecutors.MainCommandExecutor;
import me.Game_Crytus.EventsRunGC.Events.EventsHandler;
import me.Game_Crytus.EventsRunGC.Events.InventoryHandler;
import me.Game_Crytus.EventsRunGC.Listeners.ChangeGamemode;
import me.Game_Crytus.EventsRunGC.Listeners.OnClickSignFinish;
import me.Game_Crytus.EventsRunGC.Listeners.OnMoveFinish;
import me.Game_Crytus.EventsRunGC.Listeners.OnMoveWarmupWaiting;
import me.Game_Crytus.EventsRunGC.Listeners.OnPlayerQuit;
import me.Game_Crytus.EventsRunGC.Listeners.OnReceiveDamage;
import me.Game_Crytus.EventsRunGC.Listeners.OnSignBreak;
import me.Game_Crytus.EventsRunGC.Listeners.OnSignChange;
import me.Game_Crytus.EventsRunGC.Listeners.PlayerDeath;
import me.Game_Crytus.EventsRunGC.Messages.Messages;
import me.Game_Crytus.EventsRunGC.util.Util;

public class Main extends JavaPlugin {
//|]=------------------=[Variables]=------------------=-[|//	
	//#Files
	//$Main config file
	private MainConfig mainConfig = new MainConfig(this);
	
	//$Language file
	private Messages messages;
	
	//#Class Main	
	private MainLogger logger;
	
	//#EventsHandler (Not bukkit/spigot events)
	private EventsHandler eventsHandler;
	
	//#InventoryHandler
	private InventoryHandler inventoryHandler;
	
	// #Utils
	private Util util;
	
//	private BarAPI barAPI;

	public void onEnable() {
//		getServer().getPluginManager().registerEvents(new MainEventsListener(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuit(this), this);
		getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
		getServer().getPluginManager().registerEvents(new ChangeGamemode(this), this);
		getServer().getPluginManager().registerEvents(new OnReceiveDamage(this), this);
		getServer().getPluginManager().registerEvents(new OnMoveWarmupWaiting(this), this);
		getServer().getPluginManager().registerEvents(new OnSignChange(this), this);
		getServer().getPluginManager().registerEvents(new OnSignBreak(this), this);
		getServer().getPluginManager().registerEvents(new OnMoveFinish(this), this);
		getServer().getPluginManager().registerEvents(new OnClickSignFinish(this), this);
		this.getCommand("EventsRun").setExecutor(new MainCommandExecutor(this));
		
		//mainConfig = new MainConfig(this);
		firstSetup();
		
		logger.InfoConsole("§5|]=---------------------------------=[|");
		logger.InfoConsole("§5|]             §eEnabled               §5[|");
		logger.InfoConsole("§5|]=---------------------------------=[|");
	}
	
	public void onDisable() {
		getEventsHandler().stopAll();
		Bukkit.getScheduler().cancelAllTasks();
		
		logger.InfoConsole("§5|]=---------------------------------=[|");
		logger.InfoConsole("§5|]             §6Disabled              §5[|");
		logger.InfoConsole("§5|]=---------------------------------=[|");
	}
	
	private void firstSetup () {
		//mainConfig = new MainConfig(this);
		logger = new MainLogger(this);
		messages = new Messages(this);
		eventsHandler = new EventsHandler(this);
		inventoryHandler = new InventoryHandler(this);
		util = new Util(this);
		
//		if (Bukkit.getPluginManager().getPlugin("BarAPI") != null) {
//			barAPI = (BarAPI) Bukkit.getPluginManager().getPlugin("BarAPI");
//		}
		
		eventsHandler.listAllEvents();
		eventsHandler.autoLoadEvents();
	}
	
	public MainConfig getMainConfig () {
		return this.mainConfig;
	}
	
	public Messages getMessages () {
		return this.messages;
	}
	
	public EventsHandler getEventsHandler () {
		return this.eventsHandler;
	}
	
	public MainLogger getMainLogger () {
		return this.logger;
	}
	
	public InventoryHandler getInventoryHandler () {
		return this.inventoryHandler;
	}
	
	public World getFirstWorld () {
		return getServer().getWorlds().get(0);
	}

	public Util getUtil() {
		return this.util;
	}
	
//	public BarAPI getBarAPI() {
//		return this.barAPI;
//	}
}



















