package me.excelsiorvfx.ExcelsiorBase.player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.excelsiorvfx.ExcelsiorBase.ExcelsiorBase;
import me.excelsiorvfx.ExcelsiorBase.API.ExcelsiorAPI;
import me.excelsiorvfx.ExcelsiorBase.config.Config;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Money implements ExcelsiorAPI {
	
	private static List<Money> all = new ArrayList<Money>();

	private HashMap<UUID,Integer> money = new HashMap<UUID,Integer>();
	
	private String name;
	
	private static File folders = ExcelsiorBase.instance.getDataFolder();
	
	public Money(String name) {
		this.name = name;
		all.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public void setMoney(Player p, int amt) {
		money.put(p.getUniqueId(), amt);
	}
	
	public boolean hasEnoughMoney(Player p, int amt) {
		try {
		return money.get(p.getUniqueId()) >= amt;
		} catch(NullPointerException e) {
			return false;
		}
	}
	
	public static Money getMoney(String name) {
		for (Money m : all) {
			if (m.getName().equalsIgnoreCase(name)) return m;
		} return null;
	}
	
	public static int saveAll() {
		if (all.size() == 0) return 0;
		int i = 0;
		List<String> names = new ArrayList<String>();
		Config co = new Config(folders, "ExcelsiorBaseMoney");
		for (Money m : all) {
			List<String> ids = new ArrayList<String>();
			for (UUID id : m.money.keySet()) {
				co.getConfig().set(m.name + "." + id.toString(), m.money.get(id));
				ids.add(id.toString());
			}
			co.getConfig().set(m.name + ".IdList", ids);
			names.add(m.getName());
			i++;
		} 
		co.getConfig().set("AllMoney", names);
		co.saveConfig();
		return i;
	}
	
	public static int loadAll() {
		int i = 0;
		Config co = new Config(folders, "ExcelsiorBaseMoney");
		List<String> names = co.getConfig().getStringList("AllMoney");
		if (names == null) return 0;
		for (String name : names) {
			Money m = new Money(name);
			List<String> ids = co.getConfig().getStringList(name + ".IdList");
			if (ids != null) {
				for (String id : ids) {
					m.money.put(UUID.fromString(id), co.getConfig().getInt(name + "." + id));
				}
			}
			i++;
		}
		return i;
	}

	@Override
	public void enable(JavaPlugin p) {
		folders = p.getDataFolder();
		p.getLogger().info("Loaded " + loadAll() + " money(s)!");
	}

}
