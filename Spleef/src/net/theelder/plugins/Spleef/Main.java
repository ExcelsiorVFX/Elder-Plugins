package net.theelder.plugins.Spleef;

import me.excelsiorvfx.ExcelsiorBase.minigame.ArenaJoinInventory;
import me.excelsiorvfx.ExcelsiorBase.minigame.ExcelsiorArena;
import me.excelsiorvfx.ExcelsiorBase.minigame.ExcelsiorGame;
import me.excelsiorvfx.ExcelsiorBase.world.Area;
import me.excelsiorvfx.ExcelsiorBase.world.WorldEditHandle;
import net.theelder.plugins.Spleef.handlers.SignHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, ExcelsiorGame {

	public static Main instance;
	
	public Main() {}
	
	@Override
	public void onEnable() {
		instance = this;
		new ArenaJoinInventory(this);
		getLogger().info("Starting up...");
		getLogger().info("Loaded " + SignHandler.loadAll(this) + " signs!");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new SignHandler(), this);
		getLogger().info("Loaded " + Arena.loadAll(this) + " arenas!");
		getLogger().info("The Elder SPLEEF enabled!");
	}
	
	@Override
	public void onDisable() {
		SignHandler.saveAll(this);
		Arena.saveAll(this);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (Arena.isPlaying(e.getPlayer())) 
			Arena.getArena(e.getPlayer()).removePlayer(e.getPlayer());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		
		if (p.isOp()) {
			try {
				if (args[0].equalsIgnoreCase("refresh")) SignHandler.refreshSigns();
				if (args[0].equalsIgnoreCase("new")) {
					new Arena(args[1], null, null, null);
					p.sendMessage(ChatColor.GREEN + "Created new arena!");
				} else if (args[0].equalsIgnoreCase("list")) {
					for (Arena a : Arena.all) {
						p.sendMessage(ChatColor.YELLOW + " - " + a.name);
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					Arena a = Arena.getArena(args[1]);
					if (args[2].equalsIgnoreCase("area")) {
						WorldEditHandle we = new WorldEditHandle();
						a.spleefArea = new Area(we.GetSelectionMax(p), we.GetSelectionMin(p));
						p.sendMessage(ChatColor.GREEN + "Saved arena's area!");
					} else if (args[2].equalsIgnoreCase("center")) {
						a.centerOfArea = p.getLocation();
						p.sendMessage(ChatColor.GREEN + "Saved arena's center!");
					} else if (args[2].equalsIgnoreCase("lobby")) {
						a.lobby = p.getLocation();
						p.sendMessage(ChatColor.GREEN + "Saved arena's lobby!");
					} else if (args[2].equalsIgnoreCase("addLayer")) {
						WorldEditHandle we = new WorldEditHandle();
						a.layers.add(new Area(we.GetSelectionMax(p), we.GetSelectionMin(p)));
						p.sendMessage(ChatColor.GREEN + "Added new layer - that layer was index " + (a.layers.size() - 1));
					} else if (args[2].equalsIgnoreCase("removeLayer")) {
						a.layers.remove(Integer.valueOf(args[3]));
						p.sendMessage(ChatColor.GREEN + "Removed layer.");
					} else if (args[2].equalsIgnoreCase("tpToLayer")) {
						p.teleport(a.layers.get(Integer.valueOf(args[3])).min);
					} else if (args[2].equalsIgnoreCase("collapseTime")) {
						a.collapseTime = Integer.valueOf(args[3]);
						p.sendMessage(ChatColor.GREEN + "Set collapse time!");
					} else if (args[2].equalsIgnoreCase("debug")) {
						p.sendMessage(" - collapseTime: " + a.collapseTime);
					}
				}
			} catch (Exception e) {
				p.sendMessage(ChatColor.GREEN + "--[Spleef Help]--");
				p.sendMessage(" - /spleef new <name> : Create a new arena.");
				p.sendMessage(" - /spleef list : List arenas.");
				p.sendMessage(" - /spleef set <arena> area : Set the spleef area for the arena to you WorldEdit selection.");
				p.sendMessage(" - /spleef set <arena> center : Set the center of the arena to your location.");
				p.sendMessage(" - /spleef set <arena> lobby : Set the lobby to your location.");
				p.sendMessage(" - /spleef set <arena> addLayer : Add your WorldEdit selection as a layer.");
				p.sendMessage(" - /spleef set <arena> removeLayer <number> : Remove a layer.");
				p.sendMessage(" - /spleef set <arena> tpToLayer <number> : Teleport to a layer's origin.");
				p.sendMessage(" - /spleef set <arena> collapseTime <number> : Set the time it takes for an outer layer to collapse.");
				p.sendMessage(" - Signs: 1. \"spleef join\" 2. <Arena Name>");
			}
		} try {
			if (args[0].equalsIgnoreCase("leave")) {
				Arena a = Arena.getArena(p);
				if (a == null) {p.sendMessage(ChatColor.RED + "You are not in a game!"); return true;}
				a.removePlayer(p);
			} else {
				p.sendMessage(ChatColor.RED + "Usage: /spleef leave");
			}
		} catch(Exception e) {}
		
		return true;
	}
	
	public static void run(Runnable r) {
		Bukkit.getScheduler().runTaskAsynchronously(instance, r);
	}

	@Override
	public ExcelsiorArena[] getAllArenas() {
		ExcelsiorArena[] arenas = new ExcelsiorArena[Arena.all.size()];
		int i = 0;
		for (ExcelsiorArena a : Arena.all) {
			arenas[i] = a;
			i++;
		}
		return arenas;
	}

	@Override
	public String getGameName() {
		return "Spleef";
	}

	@Override
	public void purge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setJoinable(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JavaPlugin getPlugin() {
		return this;
	}
	
}
