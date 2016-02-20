package net.theelder.plugins.Spleef;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.excelsiorvfx.ExcelsiorBase.config.Config;
import me.excelsiorvfx.ExcelsiorBase.minigame.ExcelsiorArena;
import me.excelsiorvfx.ExcelsiorBase.world.Area;
import net.theelder.plugins.Spleef.enums.Phase;
import net.theelder.plugins.Spleef.enums.Type;
import net.theelder.plugins.Spleef.handlers.FallListener;
import net.theelder.plugins.Spleef.handlers.InstantMode;
import net.theelder.plugins.Spleef.handlers.SignHandler;
import net.theelder.plugins.Spleef.threads.EndGame;
import net.theelder.plugins.Spleef.threads.Game;
import net.theelder.plugins.Spleef.threads.Lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Arena implements ExcelsiorArena {
	
	public static List<Arena> all = new ArrayList<Arena>();
	
	public HashMap<UUID, Location> previous = new HashMap<UUID, Location>();
	
	public String name;

	public Area spleefArea;
	public List<Area> layers = new ArrayList<Area>();
	
	public Location centerOfArea;
	public Location lobby;
	
	public List<UUID> players = new ArrayList<UUID>();
	public HashMap<UUID, Boolean> spectating = new HashMap<UUID, Boolean>();
	
	public Type spleefType;
	public Phase phase;
	
	public Lobby lobbyThread;
	
	public InstantMode im;
	
	public int collapseTime = 30;
	
	public Arena(String name, Area spleefArea, Location center, Location lobby) {
		this.name = name;
		this.spleefArea = spleefArea;
		this.centerOfArea = center;
		this.lobby = lobby;
		spleefType = Type.BOMB;
		phase = Phase.LOBBY;
		all.add(this);
		new FallListener(this);
		im = new InstantMode(this, false);
	}
	
	public void startGame() {
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
				@Override
				public void run() {
					for (UUID id : players) {
					Bukkit.getPlayer(id).teleport(centerOfArea);
					Bukkit.getPlayer(id).setGameMode(GameMode.SURVIVAL);
					}
				}
			});
		Main.run(new Game(this));
	}
	
	public void addPlayer(Player p) {
		previous.put(p.getUniqueId(), p.getLocation());
		players.add(p.getUniqueId());
		if (phase.canJoin) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100000, 1));
			spectating.put(p.getUniqueId(), false);
			p.teleport(lobby);
			p.setGameMode(GameMode.ADVENTURE);
			if (lobbyThread == null) {
				lobbyThread = new Lobby(this);
				Main.run(lobbyThread);
			} if (lobbyThread.running == false) {
				lobbyThread = new Lobby(this);
				Main.run(lobbyThread);
			}
		} else {
			spectating.put(p.getUniqueId(), true);
			if (phase == Phase.IN_GAME) p.teleport(centerOfArea);
		}
		SignHandler.refreshSigns();
	}
	
	public void win(Player p) {
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {
		p.setAllowFlight(true);
		p.setFallDistance(1);
		p.setFireTicks(0);
		p.getInventory().clear();
		if (p.getLocation().getBlockY() < 3) p.teleport(centerOfArea);
		}});
	}
	
	public void registerDeath(Player p) {
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {
		spectating.put(p.getUniqueId(), true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
		p.setAllowFlight(true);
		p.getInventory().clear();
		
		ItemStack stack = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Teleport to a Player");
		stack.setItemMeta(meta);
		p.getInventory().addItem(stack);

		}});
		int i = players.size();
		for (UUID id : players) {
			if (spectating.get(id)) i--;
		} if (i <= 1) {
			phase = Phase.ENDING;
			for (UUID id : players) {
				if (!spectating.get(id)) {
					Main.run(new EndGame(this, Bukkit.getPlayer(id)));
					return;
				}
			} Main.run(new EndGame(this, p));
		}
	}
	
	public void removePlayer(Player p) {
		p.setFallDistance(1);
		p.setFireTicks(0);
		if (p.getAllowFlight())
			p.setAllowFlight(false);
		if (previous.get(p.getUniqueId()) != null) p.teleport(previous.get(p.getUniqueId()));
		else p.teleport(Bukkit.getWorld("Spleef").getSpawnLocation());
		p.getInventory().clear();
		for (PotionEffect effect : p.getActivePotionEffects()) 
			p.removePotionEffect(effect.getType());
		players.remove(p.getUniqueId());
		spectating.remove(p.getUniqueId());
		if (phase == Phase.IN_GAME) registerDeath(p);
		SignHandler.refreshSigns();
	}
	
	
	public static boolean isPlaying(Player p) {
		for (Arena a : all) {
			if (a.players.contains(p.getUniqueId())) return true;
		} return false;
	}
	
	public static Arena getArena(Player p) {
		for (Arena a : all) {
			if (a.players.contains(p.getUniqueId())) return a;
		} return null;
	}
	
	public static Arena getArena(String name) {
		for (Arena a : all) {
			if (a.name.equalsIgnoreCase(name)) return a;
		} return null;
	}
	
	public static void saveAll(JavaPlugin p) {
		try {
		for (File f : p.getDataFolder().listFiles()) {
			if (f.getName().contains("Arenas")) f.delete();
		} 
		Thread.sleep(1000); } catch (Exception e) {
			System.out.println("Could not delete Arenas.yml");
		}
		
		Config c = new Config(p.getDataFolder(), "Arenas");
		
		List<String> names = new ArrayList<String>();
		
		for (Arena loc : all) {
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.min.x", loc.spleefArea.min.getBlockX());
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.min.y", loc.spleefArea.min.getBlockY());
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.min.z", loc.spleefArea.min.getBlockZ());
			
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.max.x", loc.spleefArea.max.getBlockX());
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.max.y", loc.spleefArea.max.getBlockY());
			c.getConfig().set("Arenas." + loc.name + ".spleefArea.max.z", loc.spleefArea.max.getBlockZ());

			c.getConfig().set("Arenas." + loc.name + ".center.x", loc.centerOfArea.getBlockX());
			c.getConfig().set("Arenas." + loc.name + ".center.y", loc.centerOfArea.getBlockY());
			c.getConfig().set("Arenas." + loc.name + ".center.z", loc.centerOfArea.getBlockZ());

			c.getConfig().set("Arenas." + loc.name + ".lobby.x", loc.lobby.getBlockX());
			c.getConfig().set("Arenas." + loc.name + ".lobby.y", loc.lobby.getBlockY());
			c.getConfig().set("Arenas." + loc.name + ".lobby.z", loc.lobby.getBlockZ());

			c.getConfig().set("Arenas." + loc.name + ".collapseTime", loc.collapseTime);

			c.saveConfig();
			
			try {
			int i = 0;
			while (true) {
				if (loc.layers.get(i) == null) break;
				try {
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".max.x", loc.layers.get(i).max.getBlockX());
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".max.y", loc.layers.get(i).max.getBlockY());
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".max.z", loc.layers.get(i).max.getBlockZ());
				
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".min.x", loc.layers.get(i).min.getBlockX());
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".min.y", loc.layers.get(i).min.getBlockY());
				c.getConfig().set("Arenas." + loc.name + ".layer." + i + ".min.z", loc.layers.get(i).min.getBlockZ());
				c.saveConfig();
				i++;
				} catch(Exception e) {System.out.println("There was a problem saving layer " + i + " in arena " + loc.name);}
			} } catch(Exception e) {System.out.println("There was a problem saving the layers in arena " + loc.name);}			
			c.saveConfig();
			names.add(loc.name);
		}
		
		c.getConfig().set("List", names);
		c.saveConfig();
	}
	
	public static int loadAll(JavaPlugin p) {
		
		Config c = new Config(p.getDataFolder(), "Arenas");
		
		List<String> names = c.getConfig().getStringList("List");
		
		if (names == null) return 0;
		
		int i = 0;
		
		for (String name : names) {
			int x = c.getConfig().getInt("Arenas." + name + ".spleefArea.min.x");
			int y = c.getConfig().getInt("Arenas." + name + ".spleefArea.min.y");
			int z = c.getConfig().getInt("Arenas." + name + ".spleefArea.min.z");
			Location min = new Location(Bukkit.getWorld("Spleef"), x, y, z);
			
			x = c.getConfig().getInt("Arenas." + name + ".spleefArea.max.x");
			y = c.getConfig().getInt("Arenas." + name + ".spleefArea.max.y");
			z = c.getConfig().getInt("Arenas." + name + ".spleefArea.max.z");
			Location max = new Location(Bukkit.getWorld("Spleef"), x, y, z);
			Area spleefArea = new Area(max, min);

			x = c.getConfig().getInt("Arenas." + name + ".center.x");
			y = c.getConfig().getInt("Arenas." + name + ".center.y");
			z = c.getConfig().getInt("Arenas." + name + ".center.z");
			Location center = new Location(Bukkit.getWorld("Spleef"), x, y, z);

			x = c.getConfig().getInt("Arenas." + name + ".lobby.x");
			y = c.getConfig().getInt("Arenas." + name + ".lobby.y");
			z = c.getConfig().getInt("Arenas." + name + ".lobby.z");
			Location lobby = new Location(Bukkit.getWorld("Spleef"), x, y, z);
			
			Arena a = new Arena(name, spleefArea, center, lobby);
			
			try { a.collapseTime = c.getConfig().getInt("Arenas." + name + ".collapseTime");
			} catch (Exception e) {a.collapseTime = 30;}
			
			int q = 0;
			
			while (true) {				
				if (c.getConfig().get("Arenas." + name + ".layer." + q + ".max.x") == null) break;
				
				x = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".max.x");
				y = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".max.y");
				z = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".max.z");
				max = new Location(Bukkit.getWorld("Spleef"), x, y, z);

				x = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".min.x");
				y = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".min.y");
				z = c.getConfig().getInt("Arenas." + name + ".layer." + q + ".min.z");
				min = new Location(Bukkit.getWorld("Spleef"), x, y, z);
				
				Area ar = new Area(max, min);
				a.layers.add(ar);
				
				System.out.println("Added layer " + q);

				ar.fillWith(Material.AIR);
				ar.fillWith(a.spleefType.material, (byte) 0);
				
				q += 1;
			}
			
			i++;
		}
		
		return i;
	}

	@Override
	public boolean getJoinable() {
		return phase.canJoin;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPhaseMessage() {
		return phase.name();
	}

	@Override
	public int getPlayerAmount() {
		return players.size();
	}

	@Override
	public int getCountdownTime() {
		if (phase == Phase.LOBBY || phase == Phase.PRE_GAME) {
			if (lobbyThread == null) return 30;
			return lobbyThread.countdown;
		} else return 0;
	}
	
}
