package me.Game_Crytus.EventsRunGC.util;

import me.Game_Crytus.EventsRunGC.Main;
import me.Game_Crytus.EventsRunGC.Events.MainEvents;

public class Util {
	private static Main plugin;

	public Util(Main plugin) {
		this.plugin = plugin;
	}

	public String convertSecondsToMinutes(int seconds) {
		int mins = 0;
		String formMins = "";
		String formSeconds = "";

		if (seconds >= 60) {
			mins = seconds / 60;
			seconds = seconds - (mins * 60);
		}

		if (seconds <= 9) {
			formSeconds = "0" + seconds;
		} else {
			formSeconds = seconds + "";
		}
		
		if (mins <= 9) {
			formMins = "0" + mins;
		} else {
			formMins = "" + mins;
		}
		
		return formMins + ":" + formSeconds;
	}

	public String convertIntToDecimal(int value) {
		String decimal = "";

		switch (value) {
		case 1:
			decimal = "first";
			break;
		case 2:
			decimal = "second";
			break;
		case 3:
			decimal = "third";
			break;
		case 4:
			decimal = "fourth";
			break;
		case 5:
			decimal = "fifth";
			break;
		case 6:
			decimal = "sixth";
			break;
		case 7:
			decimal = "seventh";
			break;
		case 8:
			decimal = "eighth";
			break;
		case 9:
			decimal = "ninth";
			break;
		}

		return decimal;
	}
	
	public String removeColor (String string) {
		string = string.replaceAll("(§|&)([0-9a-fA-F]|l|k|L|K|r|R)", "");
		
		return string;
	}
	
	public MainEvents converStringToMainEvents (String eventName) {
		return plugin.getEventsHandler().stringToMainEvents(eventName);
	}
}






















