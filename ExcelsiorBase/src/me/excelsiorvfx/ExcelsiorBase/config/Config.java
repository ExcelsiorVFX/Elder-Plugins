package me.excelsiorvfx.ExcelsiorBase.config;

import java.io.File;
import java.io.IOException;
import me.excelsiorvfx.ExcelsiorBase.API.ExcelsiorAPI;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config implements ExcelsiorAPI {
	
	private FileConfiguration config1;
	
	private String string;
	
	private File folder;
	
	public Config(File folders, String name) {
		folder = folders;
		string = name + ".yml";
		File file = new File(folder, string);
		//files.add(file);
		
		if (!folder.exists()) {
			folder.mkdir();
        }
		
		if (!file.exists()) {
        	try {
        		file.createNewFile();
        	}
        	catch (IOException e) {
        		Bukkit.getServer().getLogger().severe("Could not create " + name + " in folder " + folders.getName());
        	}
        }
		config1 = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig() {
		return config1;
	}
	
	public void reloadConfig() {
		File file = new File(folder, string);
		config1 = YamlConfiguration.loadConfiguration(file);
	}
	
	public String getName() {
		return string;
	}
	
	public File getFolder() {
		return folder;
	}
	/*
	public FileConfiguration getConfig(String name) {
		File f = new File(folder, name);
		if (f.exists()) return YamlConfiguration.loadConfiguration(f);
		return null;
	} */
	
	//public Config getInstance() {
	//	return instance;
	//}
	
	public void saveConfig() {
		File f = new File(folder, string);
		if (f.exists()) {
			try {
				config1.save(f);
			} catch (IOException e1) {
				Bukkit.getLogger().severe("Oh noes! Could not save .yml file " + string);
			} 
			reloadConfig();/*
			try {
				YamlConfiguration.loadConfiguration(f).save(f);
			}
			catch (IOException e) {
				Bukkit.getLogger().severe("Could not save file " + string + ".");
			}  */
			
		} else Bukkit.getLogger().severe("Could not save file " + string + " because it did not exist.");
	}
	
	public static void saveConfig(File folder, String name) {
		File f = new File(folder, name + ".yml");
		if (f.exists()) {
			try {
				YamlConfiguration.loadConfiguration(f).save(f);
			}
			catch (IOException e) {
				Bukkit.getLogger().severe("Could not save file " + name + ".");
			} 
		} else Bukkit.getLogger().severe("Could not save file " + name + " because it did not exist.");
	}
	
	public static void createConfig(String name, File folder) {
		File file = new File(folder, name);
		
		if (!folder.exists()) {
			folder.mkdir();
        }
		
		if (!file.exists()) {
        	try {
        		file.createNewFile();
        	}
        	catch (IOException e) {
        		Bukkit.getServer().getLogger().severe("Could not create " + name + " for plugin.");
        	}
        }
	}
	
	/*public static Config getInstanceOfConfig(String name) {
		for (Config co : all) {
			if (co.getName().equalsIgnoreCase(name)) {
				return co;
			}
		} return null;
	} */

	@Override
	public void enable(JavaPlugin p) {
		
		p.getLogger().info("Config class enabled.");
	}
	
}
