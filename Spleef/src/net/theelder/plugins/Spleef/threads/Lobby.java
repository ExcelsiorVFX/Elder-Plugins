package net.theelder.plugins.Spleef.threads;

import java.util.UUID;

import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Message;
import net.theelder.plugins.Spleef.enums.Phase;
import net.theelder.plugins.Spleef.handlers.FallListener;
import net.theelder.plugins.Spleef.handlers.SignHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Lobby implements Runnable {

	public Arena arena;
	
	public int countdown;
	
	public boolean running = true;
	
	public Lobby(Arena a) {
		arena = a;
	}
	
	@Override
	public void run() {
		while (arena.phase == Phase.LOBBY || arena.phase == Phase.PRE_GAME) {
			
			if (arena.players.size() >= 1) {
				countdown = 30;
				arena.phase = Phase.PRE_GAME;
				SignHandler.refreshSigns();
				while (true) {
					
					for (UUID id : arena.players) FallListener.setLevel(Bukkit.getPlayer(id), countdown);
					
					if (countdown <= 5 || countdown % 10 == 0) {
						for (UUID id : arena.players) {
							Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[SPLEEF] " + ChatColor.BLUE + "Game starting in " + countdown + " seconds!");
							Message.playSound(arena, Sound.ORB_PICKUP, 1);
						}
					} if (countdown == 0) {
						arena.phase = Phase.IN_GAME;
						SignHandler.refreshSigns();
						arena.startGame();
						break;
					}
					
					countdown--;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					
					if (arena.players.size() < 1) {
						break;
					}
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		running = false;
	}

}
