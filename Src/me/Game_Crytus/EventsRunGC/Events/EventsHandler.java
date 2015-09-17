package me.Game_Crytus.EventsRunGC.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Game_Crytus.EventsRunGC.ConfigFiles;
import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.MainLogger;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;
import me.confuser.barapi.BarAPI;

public class EventsHandler {
	private Main plugin;
	private MainLogger logger;
	private ConfigFiles eventsHandlerConfig;
	private Map<String, MainEvents> eventsLoaded = new HashMap<String, MainEvents>();
	private List<String> eventsCreated = new ArrayList<String>();

	public EventsHandler(Main plugin) {
		this.plugin = plugin;
		this.logger = new MainLogger(plugin);
		eventsHandlerConfig = new ConfigFiles(plugin, "Events/MainEvents", "Config/Events/MainEventsDefault");
		eventsHandlerConfig.saveDefaultConfig();
	}
	
	// *****************[Utils]*************************************************************************************************************************
	public MainEvents stringToMainEvents (String name) {
		return eventsLoaded.get(name);
	}
	
	public void stopAll () {
		plugin.getMainLogger().InfoConsole("Forcing stop of ALL events...");
		
		for (int i = 0; i < eventsCreated.size(); i++) {
			if (isCreatedAndLoaded(null, eventsCreated.get(i))) {
				stop(stringToMainEvents(eventsCreated.get(i)));
				plugin.getMainLogger().InfoConsole("Event §a" + eventsCreated.get(i) + " §estoped");
			}
		}
	}

	// *****************[AutoLoad]*************************************************************************************************************************
	public void autoLoadEvents() {
		logger.InfoConsole("Starting Auto Load Events...");

		List<String> configEventsToLoad = eventsHandlerConfig.getCustomConfig().getStringList("AUTO_LOAD_EVENTS");
		if (configEventsToLoad.isEmpty()) {
			logger.InfoConsole("There is no events to load for now");
			return;
		}

		String loadedEvents = "";
		for (String event : configEventsToLoad) {
			if (!(eventsCreated.contains(event))) {
				logger.InfoConsole("The event §6" + event + " §edoesn't exist");
			} else {
				loadedEvents += "§e, §a" + event;
				eventsLoaded.put(event, new MainEvents(plugin, event));
			}
		}

		loadedEvents = loadedEvents.replaceFirst("§e, ", "");
		logger.InfoConsole("§eEvents loaded: " + loadedEvents);
	}

	
	
	// *****************[ListEvents]*************************************************************************************************************************
	public void listAllEvents() {
		List<String> configEventsCreated = eventsHandlerConfig.getCustomConfig().getStringList("EVENTS_CREATED");

		for (String event : configEventsCreated) {
			this.eventsCreated.add(event);
		}
	}

	public void listEvents(Player player) {
		if (eventsCreated.isEmpty()) {
			plugin.getMessages().sendMessage(player, "There are no events.", MessageLevel.INFO);
			return;
		}

		plugin.getMessages().sendMessage(player, "List of all events created", MessageLevel.INFO);
		plugin.getMessages().sendMessage(player, "» §cRed §enames are unloaded events.", MessageLevel.INFO);
		plugin.getMessages().sendMessage(player, "» §aGreen §enames are loaded events.", MessageLevel.INFO);
		String eventsCreated = "";
		int length = 0;
		for (String eventName : this.eventsCreated) {
			eventsCreated += "§e, " + (isLoaded(eventName) ? "§a" : "§c") + eventName;

			length++;
		}
		eventsCreated = eventsCreated.replaceFirst("§e, ", "");

		plugin.getMessages().sendMessage(player, "Events created: " + eventsCreated, MessageLevel.INFO);
		plugin.getMessages().sendMessage(player, "With a total of §6" + length + " §eevents.", MessageLevel.INFO);
	}

	// *****************[Load/Unload]*************************************************************************************************************************
	public void loadEvent(Player player, MainEvents event) {
		if (!(exist(event.getDisplayName()))) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + " §cdoesn't exist.", MessageLevel.WARN);
			}
			return;
		}

		if (isLoaded(event.getDisplayName())) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + " §cis already loaded.", MessageLevel.WARN);
			}
			return;
		}
		eventsLoaded.put(event.getDisplayName(), new MainEvents(plugin, event.getDisplayName()));
		if (player != null) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + " §ehas been loaded.", MessageLevel.INFO);
		}
	}

	public void loadAllEvents(Player player) {
		for (int i = 0; i < eventsCreated.size(); i++) {
			if (isCreatedAndLoaded(null, eventsCreated.get(i))) {
				MainEvents event = stringToMainEvents(eventsCreated.get(i));
				
				loadEvent(player, event);
			}
		}

	}

	public void unloadAllEvents(Player player) {
		for (int i = 0; i < eventsCreated.size(); i++) {
			if (isCreatedAndLoaded(null, eventsCreated.get(i))) {
				MainEvents event = stringToMainEvents(eventsCreated.get(i));
				
				unloadEvent(player, event.getDisplayName());
			}
		}
	}

	public void unloadEvent(Player player, String eventName) {
		if (!(eventsCreated.contains(eventName))) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §6" + eventName + " §edoesn't exist", MessageLevel.INFO);
			}
			return;
		}

		if (!(eventsLoaded.containsKey(eventName))) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §6" + eventName + " §eisn't loaded", MessageLevel.INFO);
			}
			return;
		}

		eventsLoaded.remove(eventName);
		if (player != null) {
			plugin.getMessages().sendMessage(player, "The event §6" + eventName + " §ehas been unloaded.", MessageLevel.INFO);
		}
	}

	public void reload(Player player, String eventName) {
		if (!isCreatedAndLoaded(player, eventName)) {
			return;
		}
		
		MainEvents event = stringToMainEvents(eventName);
		
		if (!event.getState().equals(EventState.OFF)) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e is running.", MessageLevel.WARN);
			return;
		}
		
		unloadEvent(null, event.getDisplayName());
		loadEvent(null, event);

		plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e has been reloaded.", MessageLevel.INFO);
	}

	public void reloadAll(Player player) {
		for (int i = 0; i < eventsCreated.size(); i++) {
			if (isCreatedAndLoaded(null, eventsCreated.get(i))) {
				MainEvents event = stringToMainEvents(eventsCreated.get(i));
				
				reload(player, event.getDisplayName());
			}
		}
	}

	// *****************[Create/Remove]*************************************************************************************************************************
	public void createEvent(Player player, MainEvents event) {
		if (eventsCreated.contains(event.getDisplayName())) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c yet exists.", MessageLevel.WARN);
			return;
		}

		eventsCreated.add(event.getDisplayName());

		eventsHandlerConfig.getCustomConfig().set("EVENTS_CREATED", eventsCreated);
		eventsLoaded.put(event.getDisplayName(), new MainEvents(plugin, event.getDisplayName()));
		event.getConfig().saveDefaultConfig();
		event.getConfig().saveCustomConfig();
		event.setDisplayName(event.getDisplayName());

		eventsLoaded.remove(event.getDisplayName());

		plugin.getMessages().sendMessage(player, "Event §6" + event.getDisplayName() + "§e created.", MessageLevel.INFO);
	}

	public void deleteEvent(Player player, MainEvents event) {
		if (!(eventsCreated.contains(event.getDisplayName()))) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c not exists.", MessageLevel.WARN);
			return;
		}

		if (isLoaded(event.getDisplayName())) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c is loaded.", MessageLevel.WARN);
			plugin.getMessages().sendMessage(player, "Unload it first with §6/eventsrun unload <event name>", MessageLevel.INFO);
			return;
		}

		eventsCreated.remove(event.getDisplayName());
		eventsHandlerConfig.getCustomConfig().set("EVENTS_CREATED", eventsCreated);

		List<String> configAutoLoad = eventsHandlerConfig.getCustomConfig().getStringList("AUTO_LOAD_EVENTS");
		configAutoLoad.remove(event.getDisplayName());
		eventsHandlerConfig.getCustomConfig().set("AUTO_LOAD_EVENTS", configAutoLoad);

		File eventPath = new File(plugin.getDataFolder(), "Events/" + event.getDisplayName() + "/" + event.getDisplayName() + ".yml");
		eventPath.delete();
		eventPath = new File(plugin.getDataFolder(), "Events/" + event.getDisplayName() + "/" + event.getDisplayName() + "_DataBase.yml");
		eventPath.delete();
		eventPath = new File(plugin.getDataFolder(), "Events/" + event.getDisplayName());
		eventPath.delete();

		plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e has been removed.", MessageLevel.INFO);

		eventsHandlerConfig.saveCustomConfig();
	}

	// *****************[Simple
	// Booleans]*************************************************************************************************************************
	public boolean exist(String eventName) {
		if (!(eventsCreated.contains(eventName))) {
			return false;
		}

		return true;
	}

	public boolean isLoaded(String eventName) {
		if (!(eventsLoaded.containsKey(eventName))) {
			return false;
		}

		return true;
	}

	public boolean isCreatedAndLoaded(Player player, String event) {
		if (!(exist(event))) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §4" + event + " §cdoesn't exist.", MessageLevel.WARN);
			}
			return false;
		}

		if (!(isLoaded(event))) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §4" + event + " §cisn't loaded", MessageLevel.WARN);
				plugin.getMessages().sendMessage(player, "Load it first with §6/eventsrun load <event name>§e.", MessageLevel.INFO);
			}
			return false;
		}

		return true;
	}

	public boolean eventOn() {
		for (int i = 0; i < eventsLoaded.size(); i++) {
			if (eventsLoaded.containsKey(eventsCreated.get(i))) {
				if ((eventsLoaded.get(eventsCreated.get(i)).getState().equals(EventState.OFF))) {

					return true;
				}
			}
		}

		return false;
	}

	public MainEvents eventInside(Player player) {
		for (int i = 0; i < eventsLoaded.size(); i++) {
			if (eventsLoaded.containsKey(eventsCreated.get(i))) {
				if (eventsLoaded.get(eventsCreated.get(i)).getJoinedPlayers().contains(player.getUniqueId())) {

					return eventsLoaded.get(eventsCreated.get(i));
				}
			}
		}

		return null;
	}

	// *****************[Lobby]*************************************************************************************************************************
	public void setLobby(Player player, MainEvents event, Location location) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		event.setLobbyLocation(location);
		plugin.getMessages().sendMessage(player, "Lobby location for §6" + event.getDisplayName()+ " §ehas been set.", MessageLevel.INFO);
	}

	public void resetLobby(Player player, MainEvents event) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		if (!(event.getConfig().getCustomConfig().contains("Locations.lobby"))) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + " §enot contains a lobby location, or is corrupted.", MessageLevel.INFO);
			return;
		}

		event.resetLobby();
		plugin.getMessages().sendMessage(player, "Lobby location for §6" + event.getDisplayName()+ " §ehas been reseted.", MessageLevel.INFO);
	}

	// *****************[Quit]*************************************************************************************************************************
	public void setQuit(Player player, MainEvents event, Location location) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		event.setQuitLocation(location);
		plugin.getMessages().sendMessage(player, "Quit location for §6" + event.getDisplayName()+ " §ehas been set.", MessageLevel.INFO);
	}

	public void resetQuit(Player player, MainEvents event) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		if (!(event.getConfig().getCustomConfig().contains("Locations.quit"))) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName()+ " §enot contains a quit location, or is corrupted.", MessageLevel.INFO);
			return;
		}

		event.resetQuit();
		plugin.getMessages().sendMessage(player, "Quit location for §6" + event.getDisplayName()+ " §ehas been reseted.", MessageLevel.INFO);
	}

	// *****************[Join]*************************************************************************************************************************
	public void setJoin(Player player, MainEvents event, Location location, boolean add) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		int index = event.setJoinLocation(location, add);
		plugin.getMessages().sendMessage(player, "Join location §6" + index + "§e for §6" + event.getDisplayName() + " §ehas been set.", MessageLevel.INFO);
	}

	public void resetJoin(Player player, MainEvents event) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		if (!(event.getConfig().getCustomConfig().contains("LOCATIONS.JOIN."))) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + " §enot contains a join location, or is corrupted.", MessageLevel.INFO);
			return;
		}

		event.resetJoin();
		plugin.getMessages().sendMessage(player, "All join locations for §6" + event.getDisplayName() + " §ehas been reseted.", MessageLevel.INFO);
	}

	// *****************[Finish]*************************************************************************************************************************
	public void setFinish(Player player, MainEvents event, Location location, int index) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		event.setFinish(location, index);
		plugin.getMessages().sendMessage(player, "Finish location §6" + (index + 1) + "§e for §6" + event.getDisplayName() + " §ehas been set.", MessageLevel.INFO);

	}

	// *****************[Sign]*************************************************************************************************************************
	public void saveSign(Player player, Sign sign, MainEvents event) {
		event.saveSign(sign);
		plugin.getMessages().sendMessage(player, "Winning sign for §6" + event.getDisplayName() + " §ehas been saved.", MessageLevel.INFO);
	}

	public void removeSign(Player player, Sign sign, MainEvents event) {
		event.removeSign(sign);
		plugin.getMessages().sendMessage(player, "Winning sign for §6" + event.getDisplayName() + " §ehas been removed.", MessageLevel.INFO);
	}

	// *****************[Max/Min
	// players]*************************************************************************************************************************
	public void setMaxPlayers(Player player, MainEvents event, int value) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		event.setMaxPlayers(value);
		plugin.getMessages().sendMessage(player, "Max players count for §6" + event.getDisplayName() + " §ehas been defined.", MessageLevel.INFO);
	}

	public void setMinPlayers(Player player, MainEvents event, int value) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		event.setMinPlayers(value);
		plugin.getMessages().sendMessage(player, "Min players count for §6" + event.getDisplayName() + " §ehas been defined.", MessageLevel.INFO);
	}

	// *****************[Start]*************************************************************************************************************************
	public void startWaiting(Player player, MainEvents event) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		if (!(event.getState() == EventState.OFF)) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + " §cis already started.", MessageLevel.WARN);
			return;
		}

		if (!(event.hasLobby())) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c don't have a lobby location.", MessageLevel.WARN);
			return;
		}

		if (!(event.hasJoin())) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c don't have a join location.", MessageLevel.WARN);
			return;
		}

		if (!(event.hasQuit())) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c don't have a quit location.", MessageLevel.WARN);
			return;
		}
		
		if (!event.hasFinish() && !event.hasFinishSign()) {
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + "§c don't have a finish sign or location.", MessageLevel.WARN);
			return;
		}

		event.startWaiting(player);
		plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + " §ehas been started.", MessageLevel.INFO);
	}

	public void start(MainEvents event) {
		if (event.getJoinedPlayers().size() < event.getMinPlayers()) {

			if (!(event.getJoinedPlayers().isEmpty())) {
				event.broadcastInsideEvent("The event don't reached the the minimun of players to start, stopping it.",
						MessageLevel.INFO);

				for (int i = 0; i < event.getJoinedPlayers().size(); i++) {
					removePlayer(plugin.getServer().getPlayer(event.getJoinedPlayers().get(i)), event);
				}
			}
			
			event.getJoinedPlayers().clear();
			forceStop(null, event);
			return;
		}

		for (UUID p : event.getJoinedPlayers()) {
			Player player = plugin.getServer().getPlayer(p);

			event.start(player);

			plugin.getMessages().sendMessage(player, "Starting event...", MessageLevel.INFO);
			event.setState(EventState.RUNNING);
		}
		
		event.setTimer(new EventsTimer(plugin, event));
	}

	public void forceStop(Player player, MainEvents event) {
		if (!(isCreatedAndLoaded(player, event.getDisplayName()))) {
			return;
		}

		if (event.getState() == EventState.OFF) {
			if (player != null) {
				plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + " §cis already turned off.", MessageLevel.WARN);
			}
			return;
		}
		
		stop(event);
		
		if (player != null) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + " §ehas been turned off.", MessageLevel.INFO);
		}
	}

	public void stop(MainEvents event) {

		for (int i = 0; i < event.getJoinedPlayers().size(); i++) {
			Player player = plugin.getServer().getPlayer(event.getJoinedPlayers().get(i));
			
			BarAPI.removeBar(plugin.getServer().getPlayer(event.getJoinedPlayers().get(i)));
			removePlayer(plugin.getServer().getPlayer(event.getJoinedPlayers().get(i)), event);
			
			plugin.getMessages().sendMessage(player, "The event §4" + event.getDisplayName() + " §cwas turned off.", MessageLevel.INFO);
		}

		event.stop();
	}

	// *****************[Join/Leave]*************************************************************************************************************************
	public void join(Player player, MainEvents event) {
		if (!player.hasPermission(event.getPermissionsDefaultPermission()) && !event.getPermissionsDefaultPermission().equals("null")) {
			plugin.getMessages().sendMessage(player, "You don't permission to join", MessageLevel.INFO);
			return;
		}
		
		if (!(isCreatedAndLoaded(null, event.getDisplayName()))) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e is off.", MessageLevel.INFO);
			return;
		}

		if (event.getState().equals(EventState.OFF)) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e is off.", MessageLevel.INFO);
			return;

		}

		if (event.getState().equals(EventState.RUNNING)) {
			plugin.getMessages().sendMessage(player, "The event §6" + event.getDisplayName() + "§e already began.", MessageLevel.INFO);
			return;

		}

		for (int i = 0; i < eventsLoaded.size(); i++) {
			if (eventsLoaded.containsKey(eventsCreated.get(i))) {
				if (eventsLoaded.get(eventsCreated.get(i)).getJoinedPlayers().contains(player.getUniqueId())) {
					if (eventsLoaded.get(eventsCreated.get(i)).getDisplayName().equals(event.getDisplayName())) {
						plugin.getMessages().sendMessage(player, "You're already inside this event.", MessageLevel.INFO);
						return;
					} else {
						plugin.getMessages().sendMessage(player, "You're already inside the event §6" + eventsLoaded.get(eventsCreated.get(i)).getDisplayName() + "§e.", MessageLevel.INFO);
						return;
					}
				}
			}
		}

		if (event.getJoinedPlayers().size() >= event.getMaxPlayers() && !player.hasPermission(event.getPermissionsJoinFull())) {
			plugin.getMessages().sendMessage(player, "The event reached the maximun of allowed players, sorry.", MessageLevel.INFO);
			return;
		}

		event.addPlayer(player);
		plugin.getInventoryHandler().saveInventory(player, true, true);
		player.setGameMode(event.getGamemode());
		player.teleport(event.getLobbyLocation());
		//event.loadLoadouts(player, "1");
		//event.getEventLoadout().giveLoadout(player);

		plugin.getMessages().sendMessage(player, "You joined the event §6" + event.getDisplayName(), MessageLevel.INFO);

		event.broadcastInsideEvent("§6" + player.getName() + "§e joined the event. §f(§7" + event.getJoinedPlayers().size() + "§f/§7" + event.getMaxPlayers() + "§f)", MessageLevel.INFO);
	}

	public void leave(Player player) {
		for (int i = 0; i < eventsLoaded.size(); i++) {
			if (eventsLoaded.containsKey(eventsCreated.get(i))) {
				if (eventsLoaded.get(eventsCreated.get(i)).getJoinedPlayers().contains(player.getUniqueId())) {
					removePlayer(player, eventsLoaded.get(eventsCreated.get(i)));

					eventsLoaded.get(eventsCreated.get(i)).broadcastInsideEvent(
							"§6" + player.getName() + "§e left the event. §f(§7"
									+ eventsLoaded.get(eventsCreated.get(i)).getJoinedPlayers().size() + "§f/§7"
									+ eventsLoaded.get(eventsCreated.get(i)).getMaxPlayers() + "§f)",
							MessageLevel.INFO);

					if (eventsLoaded.get(eventsCreated.get(i)).getState().equals(EventState.RUNNING)
							&& eventsLoaded.get(eventsCreated.get(i)).getJoinedPlayers().size() <= 0) {
						eventsLoaded.get(eventsCreated.get(i)).stop();
					}

					return;
				}
			}
		}
		plugin.getMessages().sendMessage(player, "You're not inside any event.", MessageLevel.INFO);
	}

	// *****************[Remove]*************************************************************************************************************************
	public void removePlayer(Player player, MainEvents event) {
		event.removePlayer(player);
		plugin.getInventoryHandler().loadInventory(player, true);
		
		if (plugin.getServer().getPluginManager().getPlugin("BarAPI") != null) {
			BarAPI.removeBar(player);
		}
		
		player.teleport(event.getQuitLocation());

		plugin.getMessages().sendMessage(player, "You left the event §6" + event.getDisplayName() + "§e.", MessageLevel.INFO);
	}

	// *****************[Winners]*************************************************************************************************************************
	public void winner(Player player, MainEvents event) {
		plugin.getMainLogger().debug("getWinnersCount(): " + event.getWinnersCount());
		plugin.getMainLogger().debug("getMaxWinnersCount(): " + event.getMaxWinnersCount());

		if (event.getWinnersCount() < event.getMaxWinnersCount()) {
			removePlayer(player, event);
			event.addWinner();
			event.broadcastInsideEvent("§6" + player.getName() + "§e was the §6" + event.getWinnersCount()
					+ "º §ewinner of event §6" + event.getDisplayName() + "§e. Congratulations!", MessageLevel.INFO);

			plugin.getMessages().sendMessage(player, "You was the §6" + event.getWinnersCount()
 + "º §ewinner of the event §6" + event.getDisplayName() + "§e. Congratulations!", MessageLevel.INFO);

			giveReward(player, event, event.getWinnersCount());
		}

		if (event.getWinnersCount() >= event.getMaxWinnersCount() || event.getJoinedPlayers().size() <= 0) {
			stop(event);
		}
	}

	// *****************[Reward]*************************************************************************************************************************
	public void setReward(MainEvents event, int winner, String command) {
		event.setReward(winner, command);
	}

	public void giveReward(Player player, MainEvents event, int winner) {
		if (event.getReward().get(winner).get(0) == "null") {
			return;
		}

		List<String> oppedPlayers = new ArrayList<String>();

		for (String cmd : event.getReward().get(winner)) {
			String command = cmd.split(";")[0];
			String mode = cmd.split(";")[1];
			String random;

			// Player {PLAYER}
			command = command.replace("{PLAYER}", player.getName());

			// Random number {RANDOM(n1-n2)}
			if (command.matches(".*\\{RANDOM_NUMBER\\(.*\\)\\}.*")) {
				random = command.split(".*\\{RANDOM_NUMBER\\(")[1];
				random = random.split("\\)\\}.*")[0];
				int rand1 = Integer.parseInt(random.split("-")[0]);
				int rand2 = Integer.parseInt(random.split("-")[1]);

				command = command.replaceAll("\\{RANDOM_NUMBER\\(\\d+-\\d+\\)\\}",
						Math.round(((Math.random() * (rand2 - rand1)) + rand1)) + "");
			}

			// Pick random {PICK_RANDOM(X1,X2,X3,...,Xn)}
			if (command.matches(".*\\{PICK_RANDOM\\(.*\\)\\}.*")) {
				random = command.split(".*\\{PICK_RANDOM\\(")[1];
				random = random.split("\\)\\}.*")[0];

				String[] rand = random.split(",");

				command = command.replaceAll("\\{PICK_RANDOM\\(.*\\)\\}",
						rand[(int) Math.round(Math.random() * (rand.length - 1))]);
			}

			if (command.matches(".*\\{PICK_RANDOM_PERCENT\\(.*\\)\\}.*")) {
				random = command.split(".*\\{PICK_RANDOM_PERCENT\\(")[1];
				random = random.split("\\)\\}.*")[0];

				String[] rand = random.split(",");// 264-10,265-25,266-65
				int[] percent = new int[rand.length];
				int maxPercent = 0;

				for (int i = 0; i < rand.length; i++) {
					percent[i] = Integer.parseInt(rand[i].split("-")[1]) + (i > 0 ? percent[i - 1] : 0);
					// plugin.getMainLogger().debug("percent[" + i + "]: " +
					// percent[i]);
					// plugin.getMainLogger().debug("rand[" + i + "]: " +
					// rand[i]);
					maxPercent += Integer.parseInt(rand[i].split("-")[1]);
				}

				// for (int i = 0; i < rand.length; i++) {
				// percent[i] = (percent[i] / maxPercent) * 100;
				// }

				float randNum = (float) (Math.random() * maxPercent);
				String raffled = "";
				plugin.getMainLogger().debug("maxPercent: " + maxPercent + " | randNum: " + randNum);
				
				for (int i = 0; i < rand.length; i++) {
					if (randNum > (i > 0 ? percent[i - 1] : 0) && randNum <= percent[i]) {
						raffled = rand[i].split("-")[0];
						continue;
					}
				}

				command = command.replaceAll("\\{PICK_RANDOM_PERCENT\\(.*\\)\\}", raffled);
			}

			// Execute the command
			if (mode.equals("OP")) {
				if (!(player.isOp())) {
					player.setOp(true);
					oppedPlayers.add(player.getName());
				}

				plugin.getServer().dispatchCommand((CommandSender) player, command);

				if (oppedPlayers.contains(player.getName())) {
					player.setOp(false);
				}
			} else if (mode.equals("CONSOLE")) {
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
			}
		}
	}
	// *****************[Loadout]*************************************************************************************************************************
	public void setLoadout (Player player, MainEvents event, String id) {
		if (!(isCreatedAndLoaded(null, event.getDisplayName()))) {
			return;
		}
		
		event.setLoadout(player.getInventory(), player.getEquipment(), id);
		
		plugin.getMessages().sendMessage(player, "Loadout for event §6" + event.getDisplayName() + " §ehas been set. §7ID: " + id, MessageLevel.INFO);
	}
	
	public EventsLoadout getEventLoadout (MainEvents event) {
		return event.getEventLoadout();
	}
}






















