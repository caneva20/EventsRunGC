package me.Game_Crytus.EventsRunGC;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigFiles {
	private Main plugin;
	private MainLogger logger;
	private String fileName;
	private String jarFileName;
	
	public ConfigFiles(Main plugin, String fileName, String jarFileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.logger = new MainLogger(plugin);
		this.jarFileName = jarFileName;
	}
	
	private FileConfiguration customConfig;
	private File customConfigFile;
	
	
	public void reloadCustomConfig() {
        if (customConfigFile == null) {
        customConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(jarFileName + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
	
	/***********[GOOD]*****************/
	//$Just return the customConfig
	public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }
	
	/***********[GOOD]*****************/
	//$Save to a file, write on disk.
	public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getCustomConfig().save(customConfigFile);
        } catch (IOException ex) {
            logger.WarnConsole("Could not save config to " + customConfigFile);
            logger.WarnConsole("With exception: " + ex);
        }
    }
	
	/***********[GOOD]*****************/
	//$Get the default customConfig inside the .jar and write it to hard disk
	public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
        }
        
        if (!customConfigFile.exists()) {
             plugin.saveResource(jarFileName + ".yml", false);
             File file = new File(plugin.getDataFolder(), jarFileName + ".yml");
             
             String[] folderPath = fileName.split("/");
             String folderPath2 = "";
             
             for (int i = 0; i < folderPath.length - 1; i++) {
            	 folderPath2 += folderPath[i] + "/";
             }

             File folder = new File(plugin.getDataFolder(), folderPath2);
             folder.mkdirs();
             
             fileName = folderPath[folderPath.length - 1]; 
             file.renameTo(new File(plugin.getDataFolder(), folderPath2 + fileName + ".yml"));
             
         }
    }
}













