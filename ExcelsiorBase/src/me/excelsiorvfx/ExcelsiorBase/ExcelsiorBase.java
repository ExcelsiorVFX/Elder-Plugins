package me.excelsiorvfx.ExcelsiorBase;

import me.excelsiorvfx.ExcelsiorBase.config.Config;
import me.excelsiorvfx.ExcelsiorBase.minigame.ArenaJoinInventory;
import me.excelsiorvfx.ExcelsiorBase.minigame.ExcelsiorGame;
import me.excelsiorvfx.ExcelsiorBase.network.NetworkManager;
import me.excelsiorvfx.ExcelsiorBase.player.Money;
import me.excelsiorvfx.ExcelsiorBase.player.Rank;
import me.excelsiorvfx.funchat.Prefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ExcelsiorBase extends JavaPlugin {

	
	public static ExcelsiorBase instance;
	
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info("Enabling ExcelsiorVFX's API...");
		Config c = new Config(getDataFolder(), "base");
		c.enable(this);
		new NetworkManager().enable(this);
		Rank.enable(this);
		Money.loadAll();
		getLogger().info("All done! Prepare for some awesome.");
	}
	
	@Override
	public void onDisable() {
		Rank.disable(this);
		Money.saveAll();
		ArenaJoinInventory.disableAll();
		getLogger().info("Shutting down...");
	}
	
	private ItemStack n(String n) {
		ItemStack r = new ItemStack(Material.STONE, 1);
		ItemMeta meta = r.getItemMeta();
		meta.setDisplayName(n);
		r.setItemMeta(meta);
		return r;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage("v1.0");
		if (label.equalsIgnoreCase("open")) {
			if (!sender.isOp()) return true;
			try {
				Inventory inv = Bukkit.createInventory((Player)sender, Integer.valueOf(args[0]) * 9);
				for (int i = 0; i < (Integer.valueOf(args[0]) * 9); i++) inv.setItem(i, n("" + i));
				((Player)sender).openInventory(inv);
			} catch (Exception e) {
				sender.sendMessage("Usage: /open <size*9>");
			}
		}
		if (label.equalsIgnoreCase("setJoin")) {
			if (!sender.isOp()) return true;
			try {
				if (args[0].equalsIgnoreCase("all")) {
					for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
						if (p instanceof ExcelsiorGame) ((ExcelsiorGame) p).setJoinable(Boolean.valueOf(args[1]));
					}
				} else {
					((ExcelsiorGame) Bukkit.getPluginManager().getPlugin(args[0])).setJoinable(Boolean.valueOf(args[1]));
				}
			} catch(Exception e) {
				sender.sendMessage("Usage: /setJoin <name:all> <true/false>");
			}
		}
		if (label.equalsIgnoreCase("purge")) {
			if (!sender.isOp()) return true;
			try {
				if (args[0].equalsIgnoreCase("all")) {
					for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
						if (p instanceof ExcelsiorGame) ((ExcelsiorGame) p).purge();
					}
				} else {
					((ExcelsiorGame) Bukkit.getPluginManager().getPlugin(args[0])).purge();
				}
			} catch(Exception e) {
				sender.sendMessage("Usage: /purge <name:all>");
			}
		}
		if (label.equalsIgnoreCase("r")) {
			if (sender.isOp()) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.kickPlayer(ChatColor.GOLD + "The server is reloading! Come back in a few minutes.");
				}
				System.out.println("WARNING! Server reloading in 1 second!");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				Bukkit.reload();
			}
		} else if (label.equalsIgnoreCase("rank")) {
			if (!sender.isOp()) return true;
			try {
				if (args[0].equalsIgnoreCase("trans")) NetworkManager.transfer((Player) sender, args[1]);
				if (args[0].equalsIgnoreCase("check")) NetworkManager.checkMOTD(args[1]);
				if (args[0].equalsIgnoreCase("set")) {
					try {
						Player p = Bukkit.getPlayer(args[1]);
						Rank.getRank(args[2]).addPlayer(p);
					} catch (Exception e) {e.printStackTrace();}
					sender.sendMessage(ChatColor.GREEN + "Set " + args[1] + "\'s rank to " + args[2] + "!");
				} else if (args[0].equalsIgnoreCase("new")) {
					new Rank(args[1], Prefix.getPrefix(args[2]));
					sender.sendMessage(ChatColor.GREEN + "Made new rank " + args[1] + "!");
				} else if (args[0].equalsIgnoreCase("list")) {
					for (Rank r : Rank.ALL) sender.sendMessage(ChatColor.BLUE + " - " + r.name + ": Prefix = " + r.prefix.prefix);
				}
			} catch(Exception e) {
				sender.sendMessage(ChatColor.DARK_AQUA + " - /rank set <player> <rank> : Set a player's rank.");
				sender.sendMessage(ChatColor.DARK_AQUA + " - /rank new <name> <prefix> : Make a new rank.");
				sender.sendMessage(ChatColor.DARK_AQUA + " - /rank list : List ranks.");
			}
		}
		
		return true;
	}
	
}
