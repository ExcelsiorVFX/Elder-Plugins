package me.excelsiorvfx.uhcutils;

import static org.bukkit.ChatColor.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCUtils extends JavaPlugin {

	@Override
	public void onDisable() {
		Timer.stop();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (args[0].equalsIgnoreCase("starttime")) {
				if (args.length > 2) 
					Timer.start(this, Integer.valueOf(args[1]), Integer.valueOf(args[2]));
				else
					Timer.start(this, Integer.valueOf(args[1]));
			}
		} catch(Exception e) {
			sendHelp(sender);
		}
		return true;
	}
	
	public static void sendHelp(CommandSender sender) {
		sender.sendMessage(BLUE + "" + ITALIC + " - - -" + RESET + WHITE + " [ " + GOLD + BOLD + "UHC UTILS" + RESET + WHITE + "] " + BLUE + ITALIC + "- - - ");
		sender.sendMessage(BLUE + "   - /uhcutils starttime <number> | Start UHC Timer with <number> minutes");
		sender.sendMessage(BLUE + "   - /uhcutils starttime <number> <episode> | Start UHC Timer with <number> minutes on <episode>");
	}
}
