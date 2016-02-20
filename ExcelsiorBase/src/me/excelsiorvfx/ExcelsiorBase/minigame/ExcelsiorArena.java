package me.excelsiorvfx.ExcelsiorBase.minigame;

import org.bukkit.entity.Player;

public interface ExcelsiorArena {

	public void addPlayer(Player p);
	
	public boolean getJoinable();
	
	public int getPlayerAmount();
	
	public String getPhaseMessage();
	
	public String getName();
	
	public int getCountdownTime();

}
