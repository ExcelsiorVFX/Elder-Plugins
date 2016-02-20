package me.excelsiorvfx.ExcelsiorBase.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.excelsiorvfx.ExcelsiorBase.config.Config;
import me.excelsiorvfx.funchat.Prefix;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Rank {
	
	public final static List<Rank> ALL = new ArrayList<Rank>();
	
	public String name;
	
	public Prefix prefix;

	public final List<UUID> PLAYERS = new ArrayList<UUID>();
	
	public Rank(String name, Prefix p) {
		this.name = name;
		prefix = p;
		ALL.add(this);
	}
	
	public void addPlayer(Player p) {
		PLAYERS.add(p.getUniqueId());
		Prefix.setPrefix(prefix, p);
	}
	
	public static Rank getRank(Player p) {
		for (Rank r : ALL) {
			if (r.PLAYERS.contains(p.getUniqueId())) return r;
		} return null;
	}
	
	public static Rank getRank(String name) {
		for (Rank r : ALL) {
			if (r.name.equalsIgnoreCase(name)) return r;
		} return null;
	}

	public static void enable(JavaPlugin p) {
		Config c = new Config(p.getDataFolder(), "Ranks");
		
		List<String> names = c.getConfig().getStringList("List");
		if (names == null) return;
		
		for (String name : names) {
			String prefix = c.getConfig().getString("Rank." + name + ".Prefix");
			Rank r = new Rank(name, Prefix.getPrefix(prefix));
			
			for (String id : c.getConfig().getStringList("Rank." + name + ".Players"))
				r.PLAYERS.add(UUID.fromString(id));
			
		}
	}
	
	public static void disable(JavaPlugin p) {
		Config c = new Config(p.getDataFolder(), "Ranks");
		
		List<String> names = new ArrayList<String>();
		 
		for (Rank r : ALL) {
			List<String> ids = new ArrayList<String>();
			for (UUID id : r.PLAYERS) ids.add(id.toString());
			
			c.getConfig().set("Rank." + r.name + ".Players", ids);
			c.saveConfig();
			c.getConfig().set("Rank." + r.name + ".Prefix", r.prefix.nocolor);
			c.saveConfig();
			
			names.add(r.name);
		}
		
		c.getConfig().set("List", names);
		c.saveConfig();
	}

}
