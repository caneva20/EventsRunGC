package me.Game_Crytus.EventsRunGC.Messages;

import org.bukkit.entity.Player;

import me.Game_Crytus.EventsRunGC.Lang;
import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.MainLogger;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;

public class Messages {
	private Main plugin;
	private MainLogger logger;
	private Lang lang;
	
	public Messages (Main plugin) {
		this.plugin = plugin;
		this.logger = new MainLogger(plugin);
		this.lang = new Lang(plugin);
	}
	
	public void DisplayHelp (String page, Player player) {
		int pageCount = 1;
		String header = "§2|]=--=[§6EventsRunGC§2]=--=[" + lang.HELP_PAGE + " §3" + page +"§b/§3" + pageCount + "§2]=--=[|";
		String foot = "§2|]=-------------------------------------------------=[|";
		String[] helpStrings = getHelp(player);
		
		switch (page) {
			case "1":
				logger.InfoNoTag(player, header);
				logger.InfoNoTag(player, "");
				logger.InfoNoTag(player, "§7 >> " + lang.ARGUMENTS_INSIDE_MUST_BE_INFORMED);
				logger.InfoNoTag(player, "§7 >> " + lang.ARGUMENTS_INSIDE_IS_OPTIONAL);
				logger.InfoNoTag(player, "");
				
				for (int i = 0; i < 2; i++) {
					logger.InfoNoTag(player, helpStrings[i]);
				}
				
				logger.InfoNoTag(player, foot);
				break;
			default:
			sendMessage(player, lang.THE_PAGE_PAGE_WAS_NOT_FOUND.replace("{PAGE}", page + ""), MessageLevel.WARN);
				break;
		}
	}
	
	public void DisplayMotd (Player player) {
		logger.InfoNoTag(player, "§2|]=----------------=[§6EventsRunGC§2]=-------------------=[|");
		logger.InfoNoTag(player, "§7 >> " + lang.CREATED_BY + " §eGame_Crytus");
		logger.InfoNoTag(player, "§7 >> " + lang.VERSION + " §e1.0");
		logger.InfoNoTag(player, "");
		logger.InfoNoTag(player, "§7 >> " + lang.TYPE_RUN_EVENTS_FOR_HELP);
		logger.InfoNoTag(player, "");
		logger.InfoNoTag(player, "§7 >> " + lang.INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1);
		logger.InfoNoTag(player, "    "   + lang.INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2);
		logger.InfoNoTag(player, "§7 >> " + lang.ALL_OF_THIS_HAS_THE_SAME_RESULT);
		logger.InfoNoTag(player, "");
		logger.InfoNoTag(player, "§7 >> " + lang.HAVE_FUN);
		logger.InfoNoTag(player, "§2|]=-------------------------------------------------=[|");
	}
	
	private String[] getHelp (Player player) {
		String[] helpStrings = new String[2];
		
		if (player.hasPermission("eventsrun.command.help")) {
			helpStrings[0] = "§7 >> §f/Eventsrun" + lang.DISPLAY_MOTD;
		}
		
		if (player.hasPermission("eventsrun.command.motd")) {
			helpStrings[1] = "§7 >> §f/Eventsrun help §7[" + lang.PAGE + "]" + lang.DISPLAY_THIS_PAGE;
		}
		
		return helpStrings;
	}
	
	public void NoPermission (Player player) {
		sendMessage(player, lang.YOU_DONT_HAVE_PERMISSION_FOR_THIS, MessageLevel.WARN);
	}
	
	public void sendMessage(Player player, String message, MessageLevel level) {
		if (level.equals(MessageLevel.WARN)) {
			replaceTags(player, message, lang.PLUGIN_WARN_FORM);
		} else {
			replaceTags(player, message, lang.PLUGIN_INFO_FORM);
		}
	}

	public void sendMessage(Player player, String message, String tag) {
		logger.log(player, tag + message);
	}

	private void replaceTags(Player player, String message, String form) {
		
		form = form.replace("{PLUGIN_INFO_TAG}", lang.PLUGIN_INFO_TAG + "");
		form = form.replace("{PLUGIN_WARN_TAG}", lang.PLUGIN_WARN_TAG + "");
		form = form.replace("{PLUGIN_TAG}", lang.PLUGIN_TAG + "");
		form = form.replace("{MESSAGE}", message + "");
		
		logger.log(player, form);
	}
	
	public Lang getLang () {
		return this.lang;
	}
}










