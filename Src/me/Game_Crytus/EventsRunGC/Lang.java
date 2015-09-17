package me.Game_Crytus.EventsRunGC;

public class Lang {
	private Main plugin;
	private MainLogger logger;
	private ConfigFiles messagesEn_US;
	private ConfigFiles messagesPt_BR;	
	
	public Lang (Main plugin) {
		this.plugin = plugin;
		messagesEn_US = new ConfigFiles(plugin, "Lang/Messages.en_US", "Lang/Messages.en_US");
		messagesPt_BR = new ConfigFiles(plugin, "Lang/Messages.pt_BR", "Lang/Messages.pt_BR");
		this.logger = new MainLogger(plugin);
		
		setupMessages();
	}
	
	private String lang;
	
	//#Strings
	//$CommandExecutor > case: ""
	public String CREATED_BY;
	public String VERSION;
	public String TYPE_RUN_EVENTS_FOR_HELP;
	public String INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1;
	public String INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2;
	public String ALL_OF_THIS_HAS_THE_SAME_RESULT;
	public String HAVE_FUN;
	
	//$CommandExecutor > case: "help"
	public String HELP_PAGE;
	public String ARGUMENTS_INSIDE_MUST_BE_INFORMED;
	public String ARGUMENTS_INSIDE_IS_OPTIONAL;
	public String DISPLAY_THIS_PAGE;
	public String DISPLAY_MOTD;
	
	public String THE_PAGE_PAGE_WAS_NOT_FOUND;
	//Strings for arguments
	public String PAGE;
	//$CommandExecutor
	public String ONLY_FOR_PLAYERS;
	public String YOU_DONT_HAVE_PERMISSION_FOR_THIS;
	
	//$MainLogger
	//Tags
	public String PLUGIN_TAG;
	public String PLUGIN_WARN_TAG;
	public String PLUGIN_INFO_TAG;
	public String PLUGIN_WARN_FORM;
	public String PLUGIN_INFO_FORM;
	
	public void setupMessages () {
		lang = plugin.getMainConfig().getLanguage();
		
		if (lang.equalsIgnoreCase("pt_BR")) {
			messagesPt_BR.saveDefaultConfig();
			logger.InfoConsole("Initializing setup of language: §6Portuguese brazilian (pt_BR)");
			setupPt_BR();
			logger.InfoConsole("Language:§6 Portuguese brazilian (pt_BR) §einstaled");
			
		} else if (lang.equalsIgnoreCase("en_US")) {
			messagesEn_US.saveDefaultConfig();
			logger.InfoConsole("Initializing setup of language: §6English EUA (en_US)");
			setupEn_US();
			logger.InfoConsole("Language:§6 English EUA (en_US) §einstaled");
			
		} else {
			logger.WarnConsole("An error has ocurred while reading lang '§4" + lang + "§c'");
			logger.WarnConsole("Now the default language will be instaled");
			
			messagesEn_US.saveDefaultConfig();
			logger.InfoConsole("Initializing setup of language: §6English EUA (en_US)");
			setupEn_US();
			
			logger.InfoConsole("Language:§6 English EUA (en_US) §einstaled");
		}
	}
	//Default form
	// = messagesEn_US.getCustomConfig().getString("");
	private void setupEn_US () {
		//$CommandExecutor > case: ""		
		CREATED_BY = messagesEn_US.getCustomConfig().getString("CREATED_BY");
		VERSION = messagesEn_US.getCustomConfig().getString("VERSION");
		TYPE_RUN_EVENTS_FOR_HELP = messagesEn_US.getCustomConfig().getString("TYPE_RUN_EVENTS_FOR_HELP");
		INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1 = messagesEn_US.getCustomConfig().getString("INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1");
		INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2 = messagesEn_US.getCustomConfig().getString("INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2");
		ALL_OF_THIS_HAS_THE_SAME_RESULT = messagesEn_US.getCustomConfig().getString("ALL_OF_THIS_HAS_THE_SAME_RESULT");
		HAVE_FUN = messagesEn_US.getCustomConfig().getString("HAVE_FUN");
		
		//$CommandExecutor
		ONLY_FOR_PLAYERS = messagesEn_US.getCustomConfig().getString("ONLY_FOR_PLAYERS");
		YOU_DONT_HAVE_PERMISSION_FOR_THIS = messagesEn_US.getCustomConfig().getString("YOU_DONT_HAVE_PERMISSION_FOR_THIS");
		
		//$CommandExecutor > case: "help"
		HELP_PAGE = messagesEn_US.getCustomConfig().getString("HELP_PAGE");
		ARGUMENTS_INSIDE_MUST_BE_INFORMED = messagesEn_US.getCustomConfig().getString("ARGUMENTS_INSIDE_MUST_BE_INFORMED");
		ARGUMENTS_INSIDE_IS_OPTIONAL = messagesEn_US.getCustomConfig().getString("ARGUMENTS_INSIDE_IS_OPTIONAL");
		DISPLAY_THIS_PAGE = messagesEn_US.getCustomConfig().getString("DISPLAY_THIS_PAGE");
		DISPLAY_MOTD = messagesEn_US.getCustomConfig().getString("DISPLAY_MOTD");
		
		THE_PAGE_PAGE_WAS_NOT_FOUND = messagesEn_US.getCustomConfig().getString("THE_PAGE_PAGE_WAS_NOT_FOUND");
		
		//Strings for arguments
		PAGE = messagesEn_US.getCustomConfig().getString("PAGE");
		
		//$MainLogger
		//Tags
		PLUGIN_TAG = messagesEn_US.getCustomConfig().getString("PLUGIN_TAG");
		PLUGIN_WARN_TAG = messagesEn_US.getCustomConfig().getString("PLUGIN_WARN_TAG");
		PLUGIN_INFO_TAG = messagesEn_US.getCustomConfig().getString("PLUGIN_INFO_TAG");
		PLUGIN_WARN_FORM = messagesEn_US.getCustomConfig().getString("PLUGIN_WARN_FORM");
		PLUGIN_INFO_FORM = messagesEn_US.getCustomConfig().getString("PLUGIN_INFO_FORM");
	}
	
	private void setupPt_BR () {
		//#MOTD
		CREATED_BY = messagesPt_BR.getCustomConfig().getString("CREATED_BY");
		VERSION = messagesPt_BR.getCustomConfig().getString("VERSION");
		TYPE_RUN_EVENTS_FOR_HELP = messagesPt_BR.getCustomConfig().getString("TYPE_RUN_EVENTS_FOR_HELP");
		INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1 = messagesPt_BR.getCustomConfig().getString("INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES1");
		INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2 = messagesPt_BR.getCustomConfig().getString("INSTEAD_OF_RUN_EVENTS_YOU_CAN_USE_THESE_ALIASES2");
		ALL_OF_THIS_HAS_THE_SAME_RESULT = messagesPt_BR.getCustomConfig().getString("ALL_OF_THIS_HAS_THE_SAME_RESULT");
		HAVE_FUN = messagesPt_BR.getCustomConfig().getString("HAVE_FUN");
		
		//$CommandExecutor
		ONLY_FOR_PLAYERS = messagesPt_BR.getCustomConfig().getString("ONLY_FOR_PLAYERS");
		YOU_DONT_HAVE_PERMISSION_FOR_THIS = messagesPt_BR.getCustomConfig().getString("YOU_DONT_HAVE_PERMISSION_FOR_THIS");
		
		//#Help
		HELP_PAGE = messagesPt_BR.getCustomConfig().getString("HELP_PAGE");
		ARGUMENTS_INSIDE_MUST_BE_INFORMED = messagesPt_BR.getCustomConfig().getString("ARGUMENTS_INSIDE_MUST_BE_INFORMED");
		ARGUMENTS_INSIDE_IS_OPTIONAL = messagesPt_BR.getCustomConfig().getString("ARGUMENTS_INSIDE_IS_OPTIONAL");
		DISPLAY_THIS_PAGE = messagesPt_BR.getCustomConfig().getString("DISPLAY_THIS_PAGE");
		DISPLAY_MOTD = messagesPt_BR.getCustomConfig().getString("DISPLAY_MOTD");
		
		THE_PAGE_PAGE_WAS_NOT_FOUND = messagesPt_BR.getCustomConfig().getString("THE_PAGE_PAGE_WAS_NOT_FOUND");
		
		//$Strings for arguments
		PAGE = messagesPt_BR.getCustomConfig().getString("PAGE");
		
		//$MainLogger
		//Tags
		PLUGIN_TAG = messagesPt_BR.getCustomConfig().getString("PLUGIN_TAG");
		PLUGIN_WARN_TAG = messagesPt_BR.getCustomConfig().getString("PLUGIN_WARN_TAG");
		PLUGIN_INFO_TAG = messagesPt_BR.getCustomConfig().getString("PLUGIN_INFO_TAG");
		PLUGIN_WARN_FORM = messagesPt_BR.getCustomConfig().getString("PLUGIN_WARN_FORM");
		PLUGIN_INFO_FORM = messagesPt_BR.getCustomConfig().getString("PLUGIN_INFO_FORM");
	}
}
















