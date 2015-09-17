package me.Game_Crytus.EventsRunGC.CommandExecutors;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Game_Crytus.EventsRunGC.ConfigFiles;
import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.MainLogger;
import me.Game_Crytus.EventsRunGC.Enums.MessageLevel;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class MainCommandExecutor implements CommandExecutor{
//|]=------------------=[Variables]=------------------=-[|//
	private Main plugin;
	private MainLogger logger;
	
	public MainCommandExecutor(Main plugin) {
		this.plugin = plugin;
		this.logger = new MainLogger(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		String firstArg;
		
		if (args.length == 0) {
			firstArg = "";
		} else {
			firstArg = args[0].toLowerCase();
		}
		
		List<String> oppedPlayers = new ArrayList<String>();
		
		if (cmd.getName().equalsIgnoreCase("EventsRun")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				
				// Inventory inv = player.getInventory();
				ConfigFiles invConfig = new ConfigFiles(plugin, "Test", "log");
				invConfig.reloadCustomConfig();
				
				switch (firstArg) {
					default:
					plugin.getMessages().sendMessage(player, "Unknown command.", MessageLevel.WARN);
						break;
					
					case "":
						if (player.hasPermission("eventsrun.motd")) {
							plugin.getMessages().DisplayMotd(player);
						} else {
							plugin.getMessages().NoPermission(player);
						}						
						break;
						
					case "help":
						if ((args.length == 1) || (args[1].equalsIgnoreCase("1"))) {
							plugin.getMessages().DisplayHelp("1", player);
						} else {
							plugin.getMessages().DisplayHelp(args[1], player);
						}
						break;
						
					case "create":
						if (!(args.length == 2)) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try: §6/runevents create <event name>", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().createEvent(player, stringToEvent(args[1]));
						break;
						
					case "remove":
						if (!(args.length == 2)) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try: §6/runevents remove <event name>", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().deleteEvent(player, stringToEvent(args[1]));
						break;
					case "load":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try: §6/runevents load <event name:all>", MessageLevel.INFO);
							break;
						}
						
						if (args[1].equalsIgnoreCase("all")) {
							plugin.getEventsHandler().loadAllEvents(player);
							break;
						}
						
						plugin.getEventsHandler().loadEvent(player, stringToEvent(args[1]));
						
						break;
					case "unload":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try: §6/runevents unload <event name>", MessageLevel.INFO);
							break;
						}
						
						if (args[1].equalsIgnoreCase("all")) {
							plugin.getEventsHandler().unloadAllEvents(player);
							break;
						}
						
						plugin.getEventsHandler().unloadEvent(player, args[1]);
						break;
					case "list":
						if (args.length != 1) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun list§e.", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().listEvents(player);
						break;
						
					case "set":
						String thirdArg;
						
						if ((args.length == 1) || (args.length == 2)) {
							thirdArg = "";
						} else {
							thirdArg = args[2].toLowerCase();
						}
						
						switch (thirdArg) {
							default:
						plugin.getMessages().sendMessage(player, "Wrong arguments.", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Possibles Arguments: §6lobby§e, §6quit§e, §6join§e.", MessageLevel.INFO);
								break;
							case "":
						plugin.getMessages().sendMessage(player, "Not enough arguments.", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> <argument>§e.", MessageLevel.INFO);
						plugin.getMessages().sendMessage(player, "Possibles Arguments: §6lobby§e, §6quit§e.", MessageLevel.INFO);
								break;
							case "lobby":
								if (args.length != 3) {
							plugin.getMessages().sendMessage(player, "Incorrect use.", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> lobby§e.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().setLobby(player, stringToEvent(args[1]), player.getLocation());
								
								break;
							case "quit":
								if (args.length != 3) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> quit§e.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().setQuit(player, stringToEvent(args[1]), player.getLocation());
								break;
								
							case "join":
								if ((args.length <= 3) && (args.length >= 4)) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> join §7[add]§e.", MessageLevel.INFO);
									break;
								}
								
								if (args.length == 3) {
									plugin.getEventsHandler().setJoin(player, stringToEvent(args[1]), player.getLocation(), false);
									break;
								}
								
								if (args.length == 4) {
									if (args[3].equalsIgnoreCase("add")) {
										plugin.getEventsHandler().setJoin(player, stringToEvent(args[1]), player.getLocation(), true);
										break;
									}
									
									plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
									plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> join §7[add]§e.", MessageLevel.INFO);
									
									break;
									/*
							 * try { int value = Integer.parseInt(args[3]); if (!(value >= 0)) { plugin.getMessages().sendMessage(player, "You must type a number bigger or equals to 0."); break; } plugin.getEventsHandler().setJoin(player, args[1], player.getLocation(), true); } catch
							 * (NumberFormatException ex) { plugin.getMessages().sendMessage(player, "You must type a number, or decrease the value"); } break;
							 */
								}
								
								break;
							case "maxplayers":
								if (args.length != 4) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> maxplayers §7[value]§e.", MessageLevel.INFO);
									break;
								}
								
								try {
									int value = Integer.parseInt(args[3]);
									if (!(value > 0)) {
								plugin.getMessages().sendMessage(player, "You must type a number bigger than 0.", MessageLevel.WARN);
										break;
									}
									
									plugin.getEventsHandler().setMaxPlayers(player, stringToEvent(args[1]), value);
									
								} catch (NumberFormatException ex) {
							plugin.getMessages().sendMessage(player, "You must type a number, or decrease the value", MessageLevel.WARN);
								}
								
								break;
								
							case "minplayers":
								if (args.length != 4) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> minplayers §7[value]§e.", MessageLevel.INFO);
									break;
								}
								
								try {
									int value = Integer.parseInt(args[3]);
									if (!(value > 0)) {
										plugin.getMessages().sendMessage(player, "You must type a number bigger than 0.", MessageLevel.WARN);
										break;
									}
									
									plugin.getEventsHandler().setMinPlayers(player, stringToEvent(args[1]), value);
									
								} catch (NumberFormatException ex) {
									plugin.getMessages().sendMessage(player, "You must type a number, or decrease the value", MessageLevel.WARN);
								}
								break;
							case "finish":
								if (args.length != 4) {
									plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
									plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> finish §7[1|2]§e.", MessageLevel.INFO);
									break;
								}
								
								try {
									int value = Integer.parseInt(args[3]);
									if (!((value == 1) || (value == 2))) {
										plugin.getMessages().sendMessage(player, "You must type 1 or 2.", MessageLevel.WARN);
										break;
									}
									
									plugin.getEventsHandler().setFinish(player, stringToEvent(args[1]), player.getTargetBlock(null, 50).getLocation(), value - 1);
									
								} catch (NumberFormatException ex) {
									plugin.getMessages().sendMessage(player, "You must type a number.", MessageLevel.WARN);
								}
								
								break;
							case "loadout" :
								if (args.length != 4) {
									plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
									plugin.getMessages().sendMessage(player, "Try §6/eventsrun set <event name> loadout <name/id>.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().setLoadout(player, stringToEvent(args[1]), args[3]);
								
								break;
						}						
						break;
					case "reset":
						String thirdArg2;
						
						if ((args.length == 1) || (args.length == 2)) {
							thirdArg2 = "";
						} else {
							thirdArg2 = args[2].toLowerCase();
						}
						
						switch (thirdArg2) {
							default:
						plugin.getMessages().sendMessage(player, "Wrong command usage.", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun reset <event name> <argument>", MessageLevel.INFO);
						plugin.getMessages().sendMessage(player, "Possibles Arguments: §6lobby§e, §6quit§e and §6join§e.", MessageLevel.INFO);
								break;
							case "lobby":
								if (args.length != 3) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun reset <event name> lobby§e.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().resetLobby(player, stringToEvent(args[1]));
								break;
								
							case "join":
								if (args.length != 3) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun reset <event name> join§e.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().resetJoin(player, stringToEvent(args[1]));
								break;
								
							case "quit":								
								if (args.length != 3) {
							plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
							plugin.getMessages().sendMessage(player, "Try §6/eventsrun reset <event name> quit§e.", MessageLevel.INFO);
									break;
								}
								
								plugin.getEventsHandler().resetQuit(player, stringToEvent(args[1]));
								break;
						}
						break;
					
					case "reload":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun reload <event name|all>§e.", MessageLevel.INFO);
							break;
						}
						
						if (args[1].equalsIgnoreCase("all")) {
							plugin.getEventsHandler().reloadAll(player);
							break;
						}
						
						if (!plugin.getEventsHandler().exist(args[1])) {
							plugin.getMessages().sendMessage(player, "The event §4" + args[1] + " §cdoesn't exist.", MessageLevel.WARN);
							break;
						}
						
						plugin.getEventsHandler().reload(player, args[1]);
						break;
					
					case "start":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun start <event name>§e.", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().startWaiting(player, stringToEvent(args[1]));
						break;
						
					case "stop":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun stop <event name>§e.", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().forceStop(player, stringToEvent(args[1]));
						break;
						
					case "join":
						if (args.length != 2) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun join <event name>§e.", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().join(player, stringToEvent(args[1]));
						break;
						
					case "leave":
						if (args.length != 1) {
						plugin.getMessages().sendMessage(player, "Incorrect use", MessageLevel.WARN);
						plugin.getMessages().sendMessage(player, "Try §6/eventsrun leave§e.", MessageLevel.INFO);
							break;
						}
						
						plugin.getEventsHandler().leave(player);
						break;
						
//					case "flyon":
//						player.setAllowFlight(true);
//						player.teleport(player.getLocation().add(0, 1, 0));
//						player.setFlying(true);
//						player.setFlySpeed(0.1F);
//						break;
//						
//					case "flyoff":
//						player.setAllowFlight(false);
//						player.setFlying(false);
//						player.setFlySpeed(0.0F);
//						break;
						
//					case "config":
//						invConfig.reloadCustomConfig();
////						invConfig.getCustomConfig().set("A.b.c.d.e.f", "asdasd");
////						invConfig.saveCustomConfig();
//						
//						String[] sections = invConfig.getCustomConfig().getConfigurationSection("A").getKeys(false).toArray(new String[0]);
//						
//						for (String s : sections) {
//							plugin.getMainLogger().debug(s);
//						}
//						
//						
//						break;
					case "debug": 
						
						
						
						break;
				}
			} else {
				logger.WarnConsole(plugin.getMessages().getLang().ONLY_FOR_PLAYERS);
			}
		}
		return true;
	}
	
	private MainEvents stringToEvent (String name) {
		return plugin.getEventsHandler().stringToMainEvents(name);
	}
}
















