package me.Game_Crytus.EventsRunGC;


public class MainConfig {
	private Main plugin;
	private MainLogger logger;
	private ConfigFiles mainConfig;
	
	private String language;
	private boolean useBarAPI;
	
	public MainConfig (Main plugin) {
		this.plugin = plugin;
		this.logger = new MainLogger(plugin);
		
		this.mainConfig = new ConfigFiles(plugin, "MainConfig", "Config/MainConfig");
		
		setupConfig();
	}
	
	private void setupConfig () {
		mainConfig.saveDefaultConfig();
		
		language = mainConfig.getCustomConfig().getString("LANGUAGE");
		useBarAPI = mainConfig.getCustomConfig().getBoolean("USE_BAR_API");
		
//		autoLoadEvents = mainConfig.getCustomConfig().getString("AutoLoadEvents");
	}
	
	public String getLanguage () {
		return this.language;
	}
	
	public boolean getUseBarAPI () {
		return this.useBarAPI;
	}
}



















