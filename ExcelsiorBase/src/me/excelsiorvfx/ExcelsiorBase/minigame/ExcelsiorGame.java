package me.excelsiorvfx.ExcelsiorBase.minigame;

import org.bukkit.plugin.java.JavaPlugin;

public interface ExcelsiorGame {

	public ExcelsiorArena[] getAllArenas();
	
	public void purge();
	
	public void setJoinable(boolean joinable);
	
	public String getGameName();
	
	public JavaPlugin getPlugin();
	
}
