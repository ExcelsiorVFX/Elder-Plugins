package net.theelder.plugins.Spleef.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.excelsiorvfx.ExcelsiorBase.config.Config;
import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SignHandler implements Listener {
	
	public static List<Location> signs = new ArrayList<Location>();
	public static HashMap<Location, String> signArenas = new HashMap<Location, String>();
	
	
	public SignHandler() {
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMake(SignChangeEvent e) {
		String[] lines = e.getLines();
		if (lines[0].equalsIgnoreCase("spleef join")) {
			Arena join = Arena.getArena(lines[1]);
			if (join == null) {
				e.getBlock().setType(Material.AIR);
				e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
			} else {
				e.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "[SPLEEF]");
				e.setLine(1, ChatColor.BLACK + "Join Game");
				signs.add(e.getBlock().getLocation());
				signArenas.put(e.getBlock().getLocation(), join.name);
				e.getBlock().getRelative(0, -3, 0).setType(Material.WALL_SIGN);
				e.getBlock().getRelative(0, -3, 0).setData((byte) 1);
				Sign ne = (Sign) e.getBlock().getRelative(0, -3, 0).getState();
				ne.setLine(0, join.name);
				ne.update(true);
				refreshSigns();
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.WALL_SIGN) {
			Sign s = (Sign) e.getBlock().getState();
			if (s.getLine(0).contains("SPLEEF")) {
				if (e.getPlayer().isSneaking() && e.getPlayer().isOp()) {
					signs.remove(s.getLocation());
					signArenas.remove(s.getLocation());
					e.getPlayer().sendMessage(ChatColor.RED + "Sign removed.");
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.YELLOW + "You just attempted to destroy a spleef sign; you must be sneaking to do this.");
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Sign s = (Sign) e.getClickedBlock().getState();
				if (s.getLine(0).contains("[SPLEEF]")) {
					Arena a = Arena.getArena(signArenas.get(s.getLocation()));
					if (a == null) {
						a = Arena.getArena(
								((Sign) e.getClickedBlock().getRelative(0, -3, 0).getState()).getLine(0));
						/*signArenas = new HashMap<Location, String>();
						signs = new ArrayList<Location>();
						loadAll(Main.instance);
						Main.run(new Runnable() {
							@Override
							public void run() {
								try { Thread.sleep(1000); } catch (Exception e) { }
								onClick(e);
							}
						}); */
					} 
					a.addPlayer(e.getPlayer());
				}
			}
		}
	}
	
	public static void refreshSigns() {
		for (Location loc : signs) {
			try {
				Sign s = (Sign) loc.getBlock().getState();
				Arena a = Arena.getArena(signArenas.get(loc));
				s.setLine(2, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + a.players.size() + " Players");
				if (a.phase.canJoin) {
					s.setLine(3, ChatColor.DARK_PURPLE + a.phase.name());
					s.setLine(1, ChatColor.DARK_GREEN + "Join");
				}
				else {
					s.setLine(3, ChatColor.RED + a.phase.name());
					s.setLine(1, ChatColor.DARK_RED + "Spectate");
				}
				s.update(true);
			} catch (Exception e) {
				System.out.println("Encountered a problem refreshing a sign at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
				e.printStackTrace();
			}
		}
	}
		
	public static void saveAll(JavaPlugin p) {
		try {
		for (File f : p.getDataFolder().listFiles()) {
			if (f.getName().contains("Signs")) f.delete();
		} 
		Thread.sleep(1000); } catch (Exception e) {
			System.out.println("Could not delete signs.yml");
		}
		
		Config c = new Config(p.getDataFolder(), "Signs");
		
		List<String> names = new ArrayList<String>();
		
		for (Location loc : signs) {
			c.getConfig().set("Signs." + signArenas.get(loc) + ".x", loc.getBlockX());
			c.getConfig().set("Signs." + signArenas.get(loc) + ".y", loc.getBlockY());
			c.getConfig().set("Signs." + signArenas.get(loc) + ".z", loc.getBlockZ());
			c.saveConfig();
			names.add(signArenas.get(loc));
		}
		c.getConfig().set("List", names);
		c.saveConfig();
	}
	
	public static int loadAll(JavaPlugin p) {
		Config c = new Config(p.getDataFolder(), "Signs");
		List<String> names = c.getConfig().getStringList("List");
		if (names == null) return 0;
		int i = 0;
		for (String arena : names) {
			try {
			Location loc = new Location(Bukkit.getWorld("Spleef"), 
					c.getConfig().getInt("Signs." + arena  + ".x"),
					c.getConfig().getInt("Signs." + arena  + ".y"),
					c.getConfig().getInt("Signs." + arena  + ".z"));
			signs.add(loc);
			signArenas.put(loc, arena);
			i++;
			} catch (Exception e) {
				System.out.println("Encountered a problem while loading a spleef sign!");
			}
		} return i;
	}
	
}
