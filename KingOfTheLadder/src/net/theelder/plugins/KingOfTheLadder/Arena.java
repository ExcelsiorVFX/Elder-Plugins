package net.theelder.plugins.KingOfTheLadder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.excelsiorvfx.excelsiorbasele.config.Config;
import me.excelsiorvfx.excelsiorbasele.world.Area;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;

public class Arena {

	public String joinMessage = "{name} joined KING OF THE LADDER!";
	public boolean joinNotify = true;
	public String leaveMessage = "You left KING OF THE LADDER.";
	public boolean leaveNotify = false;
	public String notEnoughPlayers = "There need to be at least 2 players playing to increase your score.";
	public String timerNotExpired = "You need to wait {timer} more seconds before your score can increase!";
	public String scoreIncrease = "Great job! Your score is now {score}!";
	public String playerPrefix = "[KOTL]";
	public boolean prefix;
	
	public final static List<Arena> all = new ArrayList<Arena>();
	
	public Location plate;
	public Location exit;
	public Area entire;
	public String name;
	public List<UUID> players = new ArrayList<UUID>();
	public boolean playing = false;
	
	public Location king;
	public Location sign;
	
	public HighScore scoreKeeper;
	
	public UUID preKing;
	
	public DamageListener listener;
	
	public Arena(String name, Location plate, Area entire) {
		this.plate = plate;
		this.entire = entire;
		this.name = name;
		all.add(this);
		listener = new DamageListener(this);
		scoreKeeper = new HighScore(this);
	}
	
	public void updateKing() {
		if (king == null) return;
		
		if (scoreKeeper.getKing() == null) return;
		
		UUID id = scoreKeeper.getKing();
		String name = "";
		if (Bukkit.getPlayer(id) != null)
			name = Bukkit.getPlayer(id).getName();
		
	/*	if (prefix) {
			try {
				if (preKing != null) {
					if (preKing == id) {}
					else {
						Prefix.setPrefix(prePrefix, preKing);
						prePrefix = Prefix.getPrefix(id);
						Prefix.setPrefix(Prefix.getPrefixNoColor("[ KING OF THE LADDER ]"), id);
					}
				}
			} catch(Exception e) {}
		} */
		
		king.getBlock().setType(Material.SKULL);
		Skull s = (Skull) king.getBlock().getState();
		s.setSkullType(SkullType.PLAYER);
		s.setOwner(name);
		s.update();
		if (sign == null) return;
		if (sign.getBlock().getType() != Material.WALL_SIGN) sign.getBlock().setType(Material.WALL_SIGN);
		Sign si = (Sign) sign.getBlock().getState();
		si.setLine(0, ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL]");
		si.setLine(1, ChatColor.YELLOW + "" + ChatColor.BOLD + "King " + name);
		si.setLine(2, ChatColor.DARK_GREEN + "With a score of");
		si.setLine(3, ChatColor.BLUE + "" + scoreKeeper.getScore(Bukkit.getPlayer(id)));
		si.update(true);
		
		preKing = id;
	}
	
	public void delete() {
		all.remove(this);
		listener.disable();
	}
	
	public void message(String msg) {
		for (UUID id : players) Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + msg);
	}
	
	public boolean isPlaying(Player p) {
		return players.contains(p.getUniqueId());
	}
	
	public void addPlayer(Player p) {
		p.setCustomName(playerPrefix + " " + p.getName());
		players.add(p.getUniqueId());
		if (joinNotify) message(ChatColor.WHITE + "" + ChatColor.BOLD + joinMessage.replace("{name}", p.getName()));
		else p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + ChatColor.WHITE + "" + ChatColor.BOLD + joinMessage.replace("{name}", p.getName()));
		if (players.size() > 1) playing = true;
	}
	
	public void removePlayer(Player p) {
		p.setCustomName(p.getName());
		players.remove(p.getUniqueId());
		if (leaveNotify) message(ChatColor.RED + leaveMessage.replace("{name}", p.getName()));
		else p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + ChatColor.RED + leaveMessage.replace("{name}", p.getName()));
		if (players.size() < 2) playing = false;
	}
	
	public static int reloadConfigs(File folder) {
		for (Arena a : all) a.delete();
		return load(folder);
	}
	
	public static void save(File folder) {
		Config co = new Config(folder, "Arenas");
		
		List<String> names = new ArrayList<String>();
		
		for (Arena a : all) {
			try {
			names.add(a.name);
			String path = "Arena." + a.name + ".";
			
			co.getConfig().set(path + "plate.x", a.plate.getBlockX());
			co.getConfig().set(path + "plate.y", a.plate.getBlockY());
			co.getConfig().set(path + "plate.z", a.plate.getBlockZ());
			co.getConfig().set(path + "king.x", a.king.getBlockX());
			co.getConfig().set(path + "king.y", a.king.getBlockY());
			co.getConfig().set(path + "king.z", a.king.getBlockZ());
			co.getConfig().set(path + "sign.x", a.sign.getBlockX());
			co.getConfig().set(path + "sign.y", a.sign.getBlockY());
			co.getConfig().set(path + "sign.z", a.sign.getBlockZ());
			co.getConfig().set(path + "exit.x", a.exit.getBlockX());
			co.getConfig().set(path + "exit.y", a.exit.getBlockY());
			co.getConfig().set(path + "exit.z", a.exit.getBlockZ());

			co.getConfig().set(path + "area.min.x", a.entire.min.getBlockX());
			co.getConfig().set(path + "area.min.y", a.entire.min.getBlockY());
			co.getConfig().set(path + "area.min.z", a.entire.min.getBlockZ());
			co.getConfig().set(path + "area.max.x", a.entire.max.getBlockX());
			co.getConfig().set(path + "area.max.y", a.entire.max.getBlockY());
			co.getConfig().set(path + "area.max.z", a.entire.max.getBlockZ());
			
			co.getConfig().set(path + "world", a.plate.getWorld().getName());
			
			
			co.getConfig().set(path + "messages.joinMessage", a.joinMessage);
			co.getConfig().set(path + "messages.joinNotify", a.joinNotify);
			co.getConfig().set(path + "messages.leaveMessage", a.leaveMessage);
			co.getConfig().set(path + "messages.leaveNotify", a.leaveNotify);
			co.getConfig().set(path + "messages.notEnoughPlayers", a.notEnoughPlayers);
			co.getConfig().set(path + "messages.timerNotExpired", a.timerNotExpired);
			co.getConfig().set(path + "messages.scoreIncrease", a.scoreIncrease);
			co.getConfig().set(path + "prefix.enabled", a.prefix);
			co.getConfig().set(path + "prefix.prefix", a.playerPrefix);
			
			a.scoreKeeper.save(folder);
			
			co.saveConfig();
			} catch(Exception e) {
				System.out.println("----------------------");
				System.out.println("- King of the Ladder -");
				System.out.println("- Arena Save Failure -");
				System.out.println("----------------------");
				System.out.println("Did you run every command properly?");
				e.printStackTrace();
			}
			
			}
		
		co.getConfig().set("List", names);
		co.saveConfig();
		
	}
	
	public static File temp;
	
	public static void reload(File folder) {
		temp = folder;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
				System.out.println("[KOTL] Reloaded " + load(temp) + " arenas.");
			}
		}).start();
	}
	
	public static int load(File folder) {
		Config co = new Config(folder, "Arenas");
		
		List<String> names = co.getConfig().getStringList("List");
		if (names == null) return 0;
		
		int i = 0;
		
		for (String name : names) {
			try {
				String path = "Arena." + name + ".";
				
				int x = 0, y = 0, z = 0;
			
				World world = Bukkit.getWorld(co.getConfig().getString(path + "world"));
				
				if (world == null) {
					reload(folder);
					return 10000;
				}
				
				x = co.getConfig().getInt(path + "plate.x");
				y = co.getConfig().getInt(path + "plate.y");
				z = co.getConfig().getInt(path + "plate.z");
				Location plate = new Location(world, x, y, z);

				x = co.getConfig().getInt(path + "area.min.x");
				y = co.getConfig().getInt(path + "area.min.y");
				z = co.getConfig().getInt(path + "area.min.z");
				Location min = new Location(world, x, y, z);

				x = co.getConfig().getInt(path + "area.max.x");
				y = co.getConfig().getInt(path + "area.max.y");
				z = co.getConfig().getInt(path + "area.max.z");
				Location max = new Location(world, x, y, z);
				
				Arena a = new Arena(name, plate, new Area(max, min));
				try {
					x = co.getConfig().getInt(path + "king.x");
					y = co.getConfig().getInt(path + "king.y");
					z = co.getConfig().getInt(path + "king.z");
					Location king = new Location(world, x, y, z);
					a.king = king;
				} catch(Exception e) {System.err.println("[KOLT] No king location found for arena " + name);} 
				

				try {
					x = co.getConfig().getInt(path + "exit.x");
					y = co.getConfig().getInt(path + "exit.y");
					z = co.getConfig().getInt(path + "exit.z");
					Location king = new Location(world, x, y, z);
					a.exit = king;
				} catch(Exception e) {System.err.println("[KOLT] No exit location found for arena " + name);} 

				try {
					x = co.getConfig().getInt(path + "sign.x");
					y = co.getConfig().getInt(path + "sign.y");
					z = co.getConfig().getInt(path + "sign.z");
					Location sign = new Location(world, x, y, z);
					a.sign = sign;
				} catch(Exception e) {System.err.println("[KOLT] No sign location found for arena " + name);} 
				
				a.scoreKeeper.load(folder);
				
				try {
					if (co.getConfig().getString(path + "messages.joinMessage") != "" && co.getConfig().getString(path + "messages.joinMessage") != null) a.joinMessage = co.getConfig().getString(path + "messages.joinMessage");
					a.joinNotify = co.getConfig().getBoolean(path + "messages.joinNotify");
					if (co.getConfig().getString(path + "messages.leaveMessage") != "" && co.getConfig().getString(path + "messages.leaveMessage") != null) a.leaveMessage = co.getConfig().getString(path + "messages.leaveMessage");
					a.leaveNotify = co.getConfig().getBoolean(path + "messages.leaveNotify");
					if (co.getConfig().getString(path + "messages.notEnoughPlayers") != "" && co.getConfig().getString(path + "messages.notEnoughPlayers") != null) a.notEnoughPlayers = co.getConfig().getString(path + "messages.notEnoughPlayers");
					if (co.getConfig().getString(path + "messages.timerNotExpired") != "" && co.getConfig().getString(path + "messages.timerNotExpired") != null) a.timerNotExpired = co.getConfig().getString(path + "messages.timerNotExpired");
					if (co.getConfig().getString(path + "messages.scoreIncrease") != "" && co.getConfig().getString(path + "messages.scoreIncrease") != null) a.scoreIncrease = co.getConfig().getString(path + "messages.scoreIncrease");
					if (co.getConfig().getString(path + "prefix.prefix") != "" && co.getConfig().getString(path + "prefix.prefix") != null) a.playerPrefix = co.getConfig().getString(path + "prefix.prefix");
					a.prefix = co.getConfig().getBoolean(path + "prefix.enabled");
				} catch(Exception e) {}
				
				
				i++;
			} catch(Exception e) {
				System.out.println("There was a problem loading KOTL Arena " + name + ":" + e.getMessage());
			}
		}
		
		return i;
	}
	
	public static Arena getArena(String name) {
		for (Arena a : all) if (a.name.equalsIgnoreCase(name)) return a;
		return null;
	}

}
