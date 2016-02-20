package me.excelsiorvfx.uhcutils;

import static org.bukkit.ChatColor.BLUE;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.ITALIC;
import static org.bukkit.ChatColor.RESET;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Timer {

	private static boolean run;
	private static int episode;

	public static void start(Plugin plugin, final int time) {
		run = true;
		episode = 1;
		for (Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(GOLD + "" + ITALIC + "[UHC-UTILS]" + RESET + BLUE + BOLD + " END OF FIRST EPISODE IN " + time + " MINUTES");
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				while (run) {
					try {
						Thread.sleep(time * 60 * 1000);
						for (Player p : Bukkit.getOnlinePlayers())
							p.sendMessage(GOLD + "" + ITALIC + "[UHC-UTILS]" + RESET + BLUE + BOLD + " END OF EPISODE " + episode + "!");
						episode++;
					} catch(Exception e) {}
				}
			}
		});
	}


	public static void start(Plugin plugin, final int time, final int episode) {
		run = true;
		Timer.episode = episode;
		for (Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(GOLD + "" + ITALIC + "[UHC-UTILS]" + RESET + BLUE + BOLD + " END OF NEXT EPISODE IN " + time + " MINUTES");
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				while (run) {
					try {
						Thread.sleep(time * 60 * 1000);
						for (Player p : Bukkit.getOnlinePlayers())
							p.sendMessage(GOLD + "" + ITALIC + "[UHC-UTILS]" + RESET + BLUE + BOLD + " END OF EPISODE " + episode + "!");
						Timer.episode++;
					} catch(Exception e) {}
				}
			}
		});
	}

	public static void stop() {
		run = false;
	}

}
