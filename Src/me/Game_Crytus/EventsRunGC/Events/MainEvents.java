package me.Game_Crytus.EventsRunGC.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Game_Crytus.EventsRunGC.ConfigFiles;
import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Enums.EventState;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;

/*
 * Place to join (be teleported when join) V---
 * Place to wait (lobby) V
 * Place to leave (be teleported when lose, win or quit) V---
 * The reward (a command, with some variables) V---
 * If is on or off V---
 * Who is playing, who has joined V---
 * Last winner(s) 
 * Best time
 * Who do with the smaller and bigger time, and what was
 * Date(s)/Hour(s) to occur [?]
 * Tag for who is inside the event (Will only has inside the event, when player leaves, loses the tag)
 * A command to join/leave 
 * How much lives V
 * With what gamemode V
 * With or not fly mode
 * What is the max players count V
 * What is the min players count V
 * Win mode, area or sign V
 * Events type: just go ahead, go ahead and go back 'x' times
 */
public class MainEvents {
	private Main plugin;
	// private MainLogger logger;

	private String eventName;
	private ConfigFiles eventConfig;
	private ConfigFiles eventData;
	private EventsTimerCountdown timerCountdown;
	private EventsTimer timer;

	public MainEvents(Main plugin, String eventName) {
		this.plugin = plugin;
		this.eventConfig = new ConfigFiles(plugin, "Events/" + eventName + "/" + eventName, "Config/Events/Default");
		this.eventConfig.saveDefaultConfig();
		this.eventData = new ConfigFiles(plugin, "Events/" + eventName + "/" + eventName + "_DataBase", "Config/Events/DefaultData");
		this.eventData.saveDefaultConfig();
		this.eventName = eventName;
		Setup();
	}

	private Location[] joinLocation;
	private Location lobbyLocation;
	private Location quitLocation;
	private Location[] finishLocation;
	private List<UUID> playersJoined; // Won't save
	private EventState eventState;
//	private int minPlayers;
//	private int maxPlayers;
	private GameMode gamemode;
	private int maxWinners;
	private int winnersCount;
	private Map<Integer, List<String>> rewards;
	private Map<String, Loadout> loadouts;
	private EventsLoadout eventLoadout;
//	private List<String> announces;

	// private String lastWinner; //{PLAYER_NAME}
	// private String bestTime; //"{PLAYER}; {TIME}"
	// private String worstTime; //"{PLAYER}; {TIME}"
	// private String tagInside; //{TAG}
	// private String commandJoin; //{COMMAND}
	// private String commandLeave;//{COMMAND}
	// private int lives; //{AMOUNT}
	// private boolean flyMode; //true/false
	// private String winMode; //{WIN_MODE}
	// private String eventType; //{EVENT_TYPE}
	// private String permission; //{PERMISSION}
	// private String announceFor; //{ALL/WHO_HAS_PERMISSION}
	// -/gc set {EVENT_NAME} {VARIABLE} {VALUE1} {VALUE2} {...} {VALUEn}

	public void Setup() {
		this.lobbyLocation = getLobbyLocationSetup();
		this.quitLocation = getQuitLocationSetup();

		this.joinLocation = new Location[this.eventData.getCustomConfig().getInt("LOCATIONS.JOIN.COUNT")];
		this.joinLocation = getJoinLocationSetup();

		this.finishLocation = new Location[2];
		this.finishLocation[0] = getFinishLocationSetup(0);
		this.finishLocation[1] = getFinishLocationSetup(1);

		this.playersJoined = new ArrayList<UUID>();
		this.eventState = EventState.OFF;

		this.gamemode = configGetGamemode();

		this.maxWinners = getMaxWinnersCountSetup();
		this.winnersCount = 0;

		this.rewards = getRewardSetup();
		
		this.eventLoadout = new EventsLoadout(plugin, this);
		
//		plugin.getMainLogger().debug(eventName + "" + eventConfig.getCustomConfig().getInt("ConfigVersion") + "");
//		plugin.getMainLogger().debug(eventData.getCustomConfig().getInt("ConfigVersion") + "");
		
		
		//loadLoadouts();
	}

	// ****************************************[Config]*****************************************************************************************************
	public ConfigFiles getConfig() {
		return this.eventConfig;
	}

	public ConfigFiles getData() {
		return this.eventData;
	}

	// ****************************************[Join]*****************************************************************************************************
	public Location[] getJoinLocation() {
		return this.joinLocation;
	}

	public int setJoinLocation(Location location, boolean add) {
		int index = 0;

		if (add) {
			index = this.eventData.getCustomConfig().getInt("LOCATIONS.JOIN.LAST_INDEX");
		}

		if (location.getY() - Double.valueOf(location.getY()).intValue() < 0.5) {
			location.setY(Double.valueOf(location.getY()).intValue() + 0.5);
		}

		this.eventData.getCustomConfig().set("LOCATIONS.JOIN.LAST_INDEX", index + 1);

		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".World", location.getWorld().getName());
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".X", location.getX());
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".Y", location.getY());
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".Z", location.getZ());
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".Yaw", location.getYaw());
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN." + index + ".Pitch", location.getPitch());
		this.eventData.saveCustomConfig();

		return index;
	}

	public Location[] getJoinLocationSetup() {
		int joinCount = this.eventData.getCustomConfig().getInt("LOCATIONS.JOIN.LAST_INDEX");
		Location[] loc = new Location[joinCount];

		if (!(hasJoin())) {
			return loc = null;
		}

		for (int i = 0; i < loc.length; i++) {
			loc[i] = new Location(plugin.getFirstWorld(), 0.0, 0.0, 0.0, 0.0F, 0.0F);

		}

		for (int i = 0; i < joinCount; i++) {
			loc[i].setWorld(plugin.getServer()
					.getWorld(this.eventData.getCustomConfig().getString("LOCATIONS.JOIN." + i + ".World")));
			loc[i].setX(this.eventData.getCustomConfig().getDouble("LOCATIONS.JOIN." + i + ".X"));
			loc[i].setY(this.eventData.getCustomConfig().getDouble("LOCATIONS.JOIN." + i + ".Y"));
			loc[i].setZ(this.eventData.getCustomConfig().getDouble("LOCATIONS.JOIN." + i + ".Z"));
			loc[i].setYaw(Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.JOIN." + i + ".Yaw")));
			loc[i].setPitch(
					Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.JOIN." + i + ".Pitch")));
		}

		return loc;
	}

	public void resetJoin() {
		this.eventData.getCustomConfig().set("LOCATIONS.JOIN", "null");
		this.eventData.saveCustomConfig();
	}

	public boolean hasJoin() {
		if ((!(this.eventData.getCustomConfig().contains("LOCATIONS.JOIN")))
				|| (this.eventData.getCustomConfig().getString("LOCATIONS.JOIN").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[Lobby]*****************************************************************************************************
	public Location getLobbyLocation() {
		return this.lobbyLocation;
	}

	public void setLobbyLocation(Location location) {
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.World", location.getWorld().getName());
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.X", location.getX());
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.Y", location.getY());
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.Z", location.getZ());
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.Yaw", location.getYaw());
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY.Pitch", location.getPitch());
		this.eventData.saveCustomConfig();

	}

	public Location getLobbyLocationSetup() {
		Location loc = new Location(plugin.getFirstWorld(), 0.0, 0.0, 0.0);

		if (!(hasLobby())) {
			return loc = null;
		}

		loc.setWorld(plugin.getServer().getWorld(this.eventData.getCustomConfig().getString("LOCATIONS.LOBBY.World")));
		loc.setX(this.eventData.getCustomConfig().getDouble("LOCATIONS.LOBBY.X"));
		loc.setY(this.eventData.getCustomConfig().getDouble("LOCATIONS.LOBBY.Y"));
		loc.setZ(this.eventData.getCustomConfig().getDouble("LOCATIONS.LOBBY.Z"));
		loc.setYaw(Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.LOBBY.Yaw")));
		loc.setPitch(Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.LOBBY.Pitch")));

		return loc;
	}

	public void resetLobby() {
		this.eventData.getCustomConfig().set("LOCATIONS.LOBBY", "null");
		this.eventData.saveCustomConfig();
	}

	public boolean hasLobby() {
		if ((!(this.eventData.getCustomConfig().contains("LOCATIONS.LOBBY")))
				|| (this.eventData.getCustomConfig().getString("LOCATIONS.LOBBY").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[Quit]*****************************************************************************************************
	public Location getQuitLocation() {
		return this.quitLocation;
	}

	public void setQuitLocation(Location location) {
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.World", location.getWorld().getName());
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.X", location.getX());
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.Y", location.getY());
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.Z", location.getZ());
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.Yaw", location.getYaw());
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT.Pitch", location.getPitch());
		this.eventData.saveCustomConfig();
	}

	public Location getQuitLocationSetup() {
		Location loc = new Location(plugin.getFirstWorld(), 0.0, 0.0, 0.0);

		if (!(hasQuit())) {
			return loc = null;
		}

		loc.setWorld(plugin.getServer().getWorld(this.eventData.getCustomConfig().getString("LOCATIONS.QUIT.World")));
		loc.setX(this.eventData.getCustomConfig().getDouble("LOCATIONS.QUIT.X"));
		loc.setY(this.eventData.getCustomConfig().getDouble("LOCATIONS.QUIT.Y"));
		loc.setZ(this.eventData.getCustomConfig().getDouble("LOCATIONS.QUIT.Z"));
		loc.setYaw(Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.QUIT.Yaw")));
		loc.setPitch(Float.parseFloat(this.eventData.getCustomConfig().getString("LOCATIONS.QUIT.Pitch")));

		return loc;
	}

	public void resetQuit() {
		this.eventData.getCustomConfig().set("LOCATIONS.QUIT", "null");
		this.eventData.saveCustomConfig();
	}

	public boolean hasQuit() {
		if ((!(this.eventData.getCustomConfig().contains("LOCATIONS.QUIT")))
				|| (this.eventData.getCustomConfig().getString("LOCATIONS.QUIT").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[Finish]*****************************************************************************************************
	public void setFinish(Location location, int index) {
		if (index > 1) {
			return;
		}

		if (location.getX() < 0) {
			location.setX(location.getX() + 1);
		}

		if (location.getZ() < 0) {
			location.setZ(location.getZ() + 1);
		}

		this.eventData.getCustomConfig().set("LOCATIONS.FINISH." + index + ".World", location.getWorld().getName());
		this.eventData.getCustomConfig().set("LOCATIONS.FINISH." + index + ".X", location.getX());
		this.eventData.getCustomConfig().set("LOCATIONS.FINISH." + index + ".Y", location.getY());
		this.eventData.getCustomConfig().set("LOCATIONS.FINISH." + index + ".Z", location.getZ());
		this.eventData.saveCustomConfig();
	}

	public double finishGetLowerX() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.X") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.X")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.X");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.X");
	}

	public double finishGetLowerY() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Y") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.Y")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Y");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.Y");
	}

	public double finishGetLowerZ() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Z") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.Z")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Z");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.Z");
	}

	public double finishGetBiggerX() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.X") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.X")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.X");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.X");
	}

	public double finishGetBiggerY() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Y") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.Y")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.Y");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Y");
	}

	public double finishGetBiggerZ() {
		if (this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Z") <= this.eventData.getCustomConfig()
				.getDouble("LOCATIONS.FINISH.1.Z")) {
			return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.1.Z");
		}

		return this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH.0.Z");
	}

	public void resetFinish() {
		this.eventData.getCustomConfig().set("LOCATIONS.FINISH", "null");
		this.eventData.saveCustomConfig();
	}

	public Location getFinishLocationSetup(int index) {
		if (index > 1) {
			return null;
		}

		Location loc = new Location(plugin.getFirstWorld(), 0.0, 0.0, 0.0);

		if (!(hasFinish())) {
			return loc = null;
		}

		loc.setWorld(plugin.getServer()
				.getWorld(this.eventData.getCustomConfig().getString("LOCATIONS.FINISH." + index + ".World")));
		loc.setX(this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH." + index + ".X"));
		loc.setY(this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH." + index + ".Y"));
		loc.setZ(this.eventData.getCustomConfig().getDouble("LOCATIONS.FINISH." + index + ".Z"));

		return loc;
	}

	public Location getFinishLocation(int index) {
		if (index > 1) {
			return null;
		}

		return this.finishLocation[index];
	}

	public boolean hasFinish() {
		if ((!(this.eventData.getCustomConfig().contains("LOCATIONS.FINISH")))
				|| (this.eventData.getCustomConfig().getString("LOCATIONS.FINISH").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[EventName]*****************************************************************************************************
	public void setDisplayName(String eventName) {
		this.eventConfig.getCustomConfig().set("CONFIG.DISPLAY_NAME", eventName);
		eventConfig.saveCustomConfig();
	}

	public String getDisplayName() {
		if (!(hasDisplayName())) {
			setDisplayName(eventName);
		}

		return this.eventName;
	}

	public boolean hasDisplayName() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.DISPLAY_NAME"))) || (this.eventConfig.getCustomConfig().getString("CONFIG.DISPLAY_NAME").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[MaxPlayers]*****************************************************************************************************
	public void setMaxPlayers(int value) {
		this.eventConfig.getCustomConfig().set("CONFIG.MAX_PLAYERS", value);
		this.eventConfig.saveCustomConfig();
	}

	public int getMaxPlayers() {
		if (!(hasMaxPlayers())) {
			setMaxPlayers(10);
		}

		return this.eventConfig.getCustomConfig().getInt("CONFIG.MAX_PLAYERS");
	}

	public boolean hasMaxPlayers() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.MAX_PLAYERS")))
				|| (this.eventConfig.getCustomConfig().getString("CONFIG.MAX_PLAYERS").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[MinPlayers]*****************************************************************************************************
	public void setMinPlayers(int value) {
		this.eventConfig.getCustomConfig().set("CONFIG.MIN_PLAYERS", value);
		this.eventConfig.saveCustomConfig();
	}

	public int getMinPlayers() {
		if (!(hasMinPlayers())) {
			setMinPlayers(2);
		}

		return this.eventConfig.getCustomConfig().getInt("CONFIG.MIN_PLAYERS");
	}

	public boolean hasMinPlayers() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.MIN_PLAYERS")))
				|| (this.eventConfig.getCustomConfig().getString("CONFIG.MIN_PLAYERS").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[LobbyTime]*****************************************************************************************************
	public void setLobbyTime(int value) {
		this.eventConfig.getCustomConfig().set("CONFIG.LOBBY_WAITING_TIME", value);
		this.eventConfig.saveCustomConfig();
	}

	public int getLobbyTime() {
		if (!(hasLobbyTime())) {
			setLobbyTime(30);
		}

		return this.eventConfig.getCustomConfig().getInt("CONFIG.LOBBY_WAITING_TIME");
	}

	public boolean hasLobbyTime() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.LOBBY_WAITING_TIME"))) || (this.eventConfig
				.getCustomConfig().getString("CONFIG.LOBBY_WAITING_TIME").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	// ****************************************[JoinedPlayers]*****************************************************************************************************
	public List<UUID> getJoinedPlayers() {
		return this.playersJoined;
	}

	public void addPlayer(Player player) {
		playersJoined.add(player.getUniqueId());
		
		eventData.getCustomConfig().set("PLAYER_DATA." + player.getUniqueId() + ".lives", getLivesPlayerLives());
		timerCountdown.addNeedChooseKit(player);

	}

	public void removePlayer(Player player) {
		playersJoined.remove(player.getUniqueId());
		
		eventData.getCustomConfig().set("PLAYER_DATA." + player.getUniqueId(), null);
		
		timerCountdown.removeNeedChooseKit(player);
	}

	// ****************************************[EventState]*****************************************************************************************************
	public EventState getState() {
		return this.eventState;
	}

	public void setState(EventState state) {
		this.eventState = state;
	}

	// ****************************************[Announces inside]*********************************************************************************************
	public List<Integer> getAnnounceInsideTimer() {
		List<Integer> announces = this.eventConfig.getCustomConfig().getIntegerList("CONFIG.ANNOUNCEMENTS.INSIDE.TIMER");

		if (announces.isEmpty()) {
			announces.add(60);
			announces.add(30);
			announces.add(10);
			announces.add(5);
			announces.add(4);
			announces.add(3);
			announces.add(2);
			announces.add(1);

			// setAnnouncementsTime(new int[]{60, 30, 10, 5, 4, 3, 2, 1});

			this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.INSIDE.TIMER", announces);
			this.eventConfig.saveCustomConfig();
			return announces;
		}

		return announces;
	}

	public void setAnnouncementsInsideTime(int[] announcesTime) {
		List<Integer> announces = new ArrayList<Integer>();

		for (int time : announcesTime) {
			announces.add(time);
		}

		this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.INSIDE.TIMER", announces);
		this.eventConfig.saveCustomConfig();
	}

	public void resetAnnouncementsInsideTimer() {
		setAnnouncementsInsideTime(new int[] {});
	}

	public void addAnnouncementsInsideTime(int time) {
		List<Integer> announces = getAnnounceInsideTimer();

		announces.add(time);

		this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.INSIDE.TIMER", announces);
		this.eventConfig.saveCustomConfig();
	}

	public boolean hasAnnouncementsInsideTimer() {
		List<Integer> announces = this.eventConfig.getCustomConfig().getIntegerList("CONFIG.ANNOUNCEMENTS.INSIDE.TIMER");

		if (announces.isEmpty()) {
			return false;
		}

		return true;
	}

	public boolean getAnnouncementsInsideAnnounceGlobally() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.INSIDE.ANNOUNCE_GLOBALLY");
	}

	public boolean getAnnouncemnetsInsideAnnounceInside() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.INSIDE.ANNOUNCE_INSIDE");
	}

	public List<String> getAnnouncementsInsideMessage() {
		return eventConfig.getCustomConfig().getStringList("CONFIG.ANNOUNCEMENTS.INSIDE.MESSAGES");
	}

	public boolean getAnnouncementsInsidePlaySound() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.INSIDE.PLAY_SOUND");
	}

	public Sound getAnnouncementsInsideSound() {
		return Sound.valueOf(eventConfig.getCustomConfig().getString("CONFIG.ANNOUNCEMENTS.INSIDE.SOUND"));
	}

	public String getAnnouncementInsideTag() {
		return eventConfig.getCustomConfig().getString("CONFIG.ANNOUNCEMENTS.INSIDE.TAG");
	}

	// ****************************************[Announces Global]*********************************************************************************************
	public List<Integer> getAnnounceGlobalTimer() {
		List<Integer> announces = this.eventConfig.getCustomConfig().getIntegerList("CONFIG.ANNOUNCEMENTS.GLOBAL.TIMER");

		if (announces.isEmpty()) {
			announces.add(60);
			announces.add(30);
			announces.add(10);
			announces.add(5);

			this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.GLOBAL.TIMER", announces);
			this.eventConfig.saveCustomConfig();
			return announces;
		}

		return announces;
	}

	public void setAnnouncementsGlobalTime(int[] announcesTime) {
		List<Integer> announces = new ArrayList<Integer>();

		for (int time : announcesTime) {
			announces.add(time);
		}

		this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.GLOBAL.TIMER", announces);
		this.eventConfig.saveCustomConfig();
	}

	public void resetAnnouncementsGlobalTimer() {
		setAnnouncementsGlobalTime(new int[] {});
	}

	public void addAnnouncementsGlobalTime(int time) {
		List<Integer> announces = getAnnounceGlobalTimer();

		announces.add(time);

		this.eventConfig.getCustomConfig().set("CONFIG.ANNOUNCEMENTS.GLOBAL.TIMER", announces);
		this.eventConfig.saveCustomConfig();
	}

	public boolean hasAnnouncementsGlobalTimer() {
		List<Integer> announces = this.eventConfig.getCustomConfig().getIntegerList("CONFIG.ANNOUNCEMENTS.GLOBAL.TIMER");

		if (announces.isEmpty()) {
			return false;
		}

		return true;
	}

	public boolean getAnnouncementsGlobalAnnounceGlobally() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.GLOBAL.ANNOUNCE_GLOBALLY");
	}

	public boolean getAnnouncemetsGlobalAnnounceInside() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.GLOBAL.ANNOUNCE_INSIDE");
	}

	public List<String> getAnnouncementsGlobalMessage() {
		return eventConfig.getCustomConfig().getStringList("CONFIG.ANNOUNCEMENTS.GLOBAL.MESSAGES");
	}

	public boolean getAnnouncementsGlobalPlaySound() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.ANNOUNCEMENTS.GLOBAL.PLAY_SOUND");
	}

	public Sound getAnnouncementsGlobalSound() {
		return Sound.valueOf(eventConfig.getCustomConfig().getString("CONFIG.ANNOUNCEMENTS.GLOBAL.SOUND"));
	}

	public String getAnnouncementGlobalTag() {
		return eventConfig.getCustomConfig().getString("CONFIG.ANNOUNCEMENTS.GLOBAL.TAG");
	}

	// ****************************************[Sign]*****************************************************************************************************
	public void saveSign(Sign sign) {
		int signCount = this.eventData.getCustomConfig().getInt("LOCATIONS.WINNING_SIGN.SIGN_COUNT");

		this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN.SIGN_COUNT", signCount + 1);

		this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + signCount + ".World",
				sign.getBlock().getLocation().getWorld().getName());
		this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + signCount + ".X",
				sign.getBlock().getLocation().getX());
		this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + signCount + ".Y",
				sign.getBlock().getLocation().getY());
		this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + signCount + ".Z",
				sign.getBlock().getLocation().getZ());
		this.eventData.saveCustomConfig();
	}

	public void removeSign(Sign sign) {
		int signCount = this.eventData.getCustomConfig().getInt("LOCATIONS.WINNING_SIGN.SIGN_COUNT");
		Location signLoc = sign.getLocation();

		for (int i = 0; i < signCount; i++) {
			Location loc = getWinningSign(i);

			if ((signLoc.getWorld() == loc.getWorld()) && (signLoc.getX() == loc.getX())
					&& (signLoc.getY() == loc.getY()) && (signLoc.getZ() == loc.getZ())) {
				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + i + ".World",
						eventData.getCustomConfig().getString("LOCATIONS.WINNING_SIGN." + (signCount - 1) + ".World"));
				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + i + ".X",
						eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + (signCount - 1) + ".X"));
				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + i + ".Y",
						eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + (signCount - 1) + ".Y"));
				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + i + ".Z",
						eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + (signCount - 1) + ".Z"));

				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN." + (signCount - 1), null);
				this.eventData.getCustomConfig().set("LOCATIONS.WINNING_SIGN.SIGN_COUNT", signCount - 1);
				this.eventData.saveCustomConfig();

				return;
			}
		}
	}

	public boolean hasFinishSign () {
		if (this.eventData.getCustomConfig().getInt("LOCATIONS.WINNING_SIGN.SIGN_COUNT") <= 0) {
			return false;
		}
		
		return true;
	}
	
	// ****************************************[WinningSign]*****************************************************************************************************
	public Location getWinningSign(int index) {
		Location loc = new Location(plugin.getFirstWorld(), 0.0, 0.0, 0.0);

		loc.setWorld(plugin.getServer()
				.getWorld(this.eventData.getCustomConfig().getString("LOCATIONS.WINNING_SIGN." + index + ".World")));
		loc.setX(this.eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + index + ".X"));
		loc.setY(this.eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + index + ".Y"));
		loc.setZ(this.eventData.getCustomConfig().getDouble("LOCATIONS.WINNING_SIGN." + index + ".Z"));

		return loc;
	}

	public EventsTimerCountdown getTimerCountdown() {
		return this.timerCountdown;
	}

	// ****************************************[Gamemode]*****************************************************************************************************
	public boolean hasGamemode() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.GAMEMODE")))
				|| (this.eventConfig.getCustomConfig().getString("CONFIG.GAMEMODE").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	private void configSetGamemode(GameMode gamemode) {
		String gm = "Survial";

		if (gamemode.equals(GameMode.SURVIVAL)) {
			gm = "Survival";

		} else if (gamemode.equals(GameMode.CREATIVE)) {
			gm = "Creative";

		} else if (gamemode.equals(GameMode.ADVENTURE)) {
			gm = "Adventure";
		} else {
			plugin.getMainLogger()
					.WarnConsole("Erro while trying to set the gamemode of §4" + getDisplayName() + "§c.");
			plugin.getMainLogger().WarnConsole("Setting it to §4Survival§c.");
		}

		eventConfig.getCustomConfig().set("CONFIG.GAMEMODE", gm);
		eventConfig.saveCustomConfig();
	}

	private GameMode configGetGamemode() {
		if (!(hasGamemode())) {
			configSetGamemode(GameMode.SURVIVAL);
			return GameMode.SURVIVAL;
		}

		String gm = eventConfig.getCustomConfig().getString("CONFIG.GAMEMODE");

		if (gm.equals("Survival")) {
			return GameMode.SURVIVAL;

		} else if (gm.equals("Creative")) {
			return GameMode.CREATIVE;

		} else if (gm.equals("Adventure")) {
			return GameMode.ADVENTURE;

		} else {
			plugin.getMainLogger()
					.WarnConsole("Erro while trying to get the gamemode of §4" + getDisplayName() + "§c.");
			plugin.getMainLogger().WarnConsole("Setting it to §4Survival§c.");
		}

		return GameMode.SURVIVAL;
	}

	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
	}

	public GameMode getGamemode() {
		return this.gamemode;
	}

	// ****************************************[Broadcast]*****************************************************************************************************
	public void broadcastInsideEvent(String message, MessageLevel level) {
		if (!(playersJoined.isEmpty())) {
			for (UUID uuid : playersJoined) {
				Player p = Bukkit.getServer().getPlayer(uuid);
				plugin.getMessages().sendMessage(p, message, level);
			}
		}
	}

	// ****************************************[Start/Stop]*****************************************************************************************************
	public void startWaiting(Player player) {
		eventState = EventState.WAITING;

		timerCountdown = new EventsTimerCountdown(plugin, this, getLobbyTime());
		timerCountdown.setForceChoosekit(getLoadoutShowInvConfigForceChoose());
		timerCountdown.setShowInvToChooseKit(getLoadoutMethod().equals("SHOW_INV"));
	}

	public void start(Player player) {
		Location[] loc = getJoinLocation();

		int rand = (int) (Math.random() * loc.length);

		player.teleport(loc[rand]);
	}

	public void stop() {
		if (this.eventState.equals(EventState.RUNNING)) {
			this.timer.stopTimer();
		}
		
		if (this.timerCountdown != null) {
			this.timerCountdown.stopTimer();	
		}
		
		this.timerCountdown = null;
		this.timer = null;
		this.eventState = EventState.OFF;
		this.playersJoined = new ArrayList<UUID>();
		this.winnersCount = 0;
	}

	// ****************************************[Winners]*****************************************************************************************************
	public void firstWinner(Player player) {
		broadcastInsideEvent(
				"§6" + player.getName() + "§e was the winner of event §6" + getDisplayName() + "§e. Congratulations!",
				MessageLevel.INFO);
	}

	// ****************************************[Config Winners]**********************************************************************************************
	public int getMaxWinnersCount() {
		return this.maxWinners;
	}

	public boolean hasMaxWinnersCount() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.MAX_WINNERS_COUNT"))) || (this.eventConfig
				.getCustomConfig().getString("CONFIG.MAX_WINNERS_COUNT").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	private int getMaxWinnersCountSetup() {
		if (!(hasMaxWinnersCount())) {
			setMaxWinnersCount(1);
			return 1;
		}

		return this.eventConfig.getCustomConfig().getInt("CONFIG.MAX_WINNERS_COUNT");
	}

	public void setMaxWinnersCount(int count) {
		this.eventConfig.getCustomConfig().set("CONFIG.MAX_WINNERS_COUNT", count);
		this.eventConfig.saveCustomConfig();
	}

	public int getWinnersCount() {
		return this.winnersCount;
	}

	public void addWinner() {
		this.winnersCount++;
	}

	// ****************************************[Reward]*****************************************************************************************************

	public boolean hasReward() {
		if ((!(this.eventConfig.getCustomConfig().contains("CONFIG.REWARD")))
				|| (this.eventConfig.getCustomConfig().getString("CONFIG.REWARD").equalsIgnoreCase("null"))) {
			return false;
		}

		return true;
	}

	public Map<Integer, List<String>> getReward() {
		return this.rewards;
	}

	private Map<Integer, List<String>> getRewardSetup() {
		if (!(hasReward())) {
			this.eventConfig.getCustomConfig().set("CONFIG.REWARD.1-Winner",
					"give {PLAYER} {PICK_RANDOM(264,265,266)} {RANDOM_NUMBER(1-16)};CONSOLE");
			this.eventConfig.saveCustomConfig();
		}

		Map<Integer, List<String>> rewards = new HashMap<Integer, List<String>>();

		for (int i = 0; i < getMaxWinnersCount(); i++) {
			rewards.put(i + 1,
					this.eventConfig.getCustomConfig().getStringList("CONFIG.REWARD." + (i + 1) + "-Winner"));
		}

		return rewards;
	}

	public void setReward(int winner, String command) {
		rewards.get(winner).add(command);

		this.eventConfig.getCustomConfig().set("CONFIG.REWARD." + winner + "-Winner", rewards.get(winner));
		this.eventConfig.saveCustomConfig();
	}

//	public void giveReward(int winner) {
//
//	}
	
	// ****************************************[TIMER CONTDOWN]***********************************************************************************************
	public boolean getCountdownPlayerExpBar() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.COUNTDOWN.USE_PLAYER_EXP_BAR");
	}
	
	public boolean getCountdownPlayerLevel() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.COUNTDOWN.USE_PLAYER_LEVEL");
	}
	
	public boolean getCountdownBarAPI() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.COUNTDOWN.USE_BAR_API");
	}
	
	// ****************************************[TIMER]*************************************************************************************************
	public EventsTimer getTimer() {
		return this.timer;
	}
	
	public void setTimer(EventsTimer timer) {
		this.timer = timer;
	}
	
	public boolean getTimerPlayerLevel() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.TIMER.USE_PLAYER_LEVEL");
	}
	
	public boolean getTimerBarAPI() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.TIMER.USE_BAR_API");
	}
	
	// ****************************************[TIMER WARMUP]***********************************************************************************************
	public boolean getWarmupPlayerExpBar() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.WARMUP.USE_PLAYER_EXP_BAR");
	}
	
	public boolean getWarmupPlayerLevel() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.WARMUP.USE_PLAYER_LEVEL");
	}
	
	public boolean getWarmupBarAPI() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.WARMUP.USE_BAR_API");
	}
	
	// ****************************************[Movement]*************************************************************************************************
	public boolean getMovementMoveWaiting() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.MOVEMENT.MOVE_WHEN_WAITING");
	}
	
	public boolean getMovementMoveWarmup() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.MOVEMENT.MOVE_WHEN_WARMUPING");
	}
	
	public int getMovementWarmupTime() {
		return eventConfig.getCustomConfig().getInt("CONFIG.WARMUP_TIME");
	}
	
	public boolean getMovementSendMessageTryingMove() {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.MOVEMENT.SEND_MESSAGE_WHEN_TRYING_TO_MOVE");
	}
	
	// ****************************************[Permissions]*************************************************************************************************
	public String getPermissionsDefaultPermission () {
		return eventConfig.getCustomConfig().getString("CONFIG.PERMISSIONS.DEFAULT");
	}
	
	public String getPermissionsJoinFull () {
		return eventConfig.getCustomConfig().getString("CONFIG.PERMISSIONS.JOIN_FULL");
	}
	
	// ****************************************[Lives]*************************************************************************************************
	public int getLivesPlayerLives () {
		return eventConfig.getCustomConfig().getInt("CONFIG.PLAYER_LIVES");
	}
	
	public void removePlayerLive (Player player) {
		eventData.getCustomConfig().set("PLAYER_DATA." + player.getUniqueId() + ".lives", getPlayerLives(player) - 1);
	}
	
	public int getPlayerLives (Player player) {
		return eventData.getCustomConfig().getInt("PLAYER_DATA." + player.getUniqueId() + ".lives");
	}
	
	// ****************************************[PvP/PvE]*************************************************************************************************
	public boolean getBlockDamageByPlayer () {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.BLOCK_DAMAGE.BY_PLAYER");
	}
	
	public boolean getBlockDamageByEntity () {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.BLOCK_DAMAGE.BY_ENTITY");
	}
	
	public boolean getBlockDamageByBlocks () {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.BLOCK_DAMAGE.BY_BLOCKS");
	}
	
	public boolean getBlockDamageByOthers () {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.BLOCK_DAMAGE.BY_OTHERS");
	}
	
	
	// ****************************************[Loadout]*************************************************************************************************
	public void setLoadout (Inventory inventory, EntityEquipment equipment, String id) {
		int i = 0;
		for (ItemStack stack : inventory) {			
			eventData.getCustomConfig().set("EVENT_LOADOUT." + id + ".Stack" + i++, stack);
			
		}
		
		eventData.getCustomConfig().set("EVENT_LOADOUT." + id + ".Stack36", equipment.getHelmet());
		eventData.getCustomConfig().set("EVENT_LOADOUT." + id + ".Stack37", equipment.getChestplate());
		eventData.getCustomConfig().set("EVENT_LOADOUT." + id + ".Stack38", equipment.getLeggings());
		eventData.getCustomConfig().set("EVENT_LOADOUT." + id + ".Stack39", equipment.getBoots());
		
		eventData.saveCustomConfig();
	}
	
	public EventsLoadout getEventLoadout () {
		return this.eventLoadout;
	}
	
	public boolean getLoadoutGiveLoadouts () {
		return eventConfig.getCustomConfig().getBoolean("CONFIG.LOADOUTS.GIVE_LOADOUTS");
	}
	
	public String getLoadoutMethod () {
//		plugin.getMainLogger().debug("§e" + eventConfig.getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD"));
		
		return eventConfig.getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.METHOD");
	}
	
	public boolean getLoadoutShowInvConfigForceChoose () {		
		return eventConfig.getCustomConfig().getBoolean("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.FORCE_CHOOSE");
	}
	
	public String getLoadoutShowInvConfigDefaultLoadout () {
		return eventConfig.getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.DEFAULT_LOADOUT");
	}
	
	public String getLoadoutShowInvConfigInventoryTitle () {
		return eventConfig.getCustomConfig().getString("CONFIG.LOADOUTS.GIVE_METHOD.SHOW_INV_CONFIG.INVENTORY_TITLE");
	}
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
	
	// ****************************************[Permissions]*************************************************************************************************
}





















