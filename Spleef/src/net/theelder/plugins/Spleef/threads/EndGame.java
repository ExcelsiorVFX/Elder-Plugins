package net.theelder.plugins.Spleef.threads;

import java.util.UUID;

import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Main;
import net.theelder.plugins.Spleef.Message;
import net.theelder.plugins.Spleef.enums.Phase;
import net.theelder.plugins.Spleef.enums.Type;
import net.theelder.plugins.Spleef.handlers.SignHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EndGame implements Runnable {

	public Arena a;
	public Player winner;

	public EndGame(Arena a, Player winner) {
		this.a = a;
		this.winner = winner;
	}
	
	@Override
	public void run() {
		BlockCollapse.get(a).enabled = false;
		a.win(winner);
		try {
		for (UUID id : a.players) {
			Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[SPLEEF] " + ChatColor.DARK_GREEN + winner.getName() + " has won the game!");
		} } catch (Exception e) {}
		
		Message.bar(a, ChatColor.GOLD + "" + winner.getName() + " has won the game! ", 5);
		Message.msgSubAndTitle(a, ChatColor.GOLD + "" + winner.getName() + " is the winner! ", ChatColor.GRAY + "Server restarting in 5 seconds...");
		Message.playSound(a, Sound.ENDERDRAGON_DEATH, 1);
		
		for (int i = 10; i > 0; i--) {
			try {
			Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {winner.getLocation().getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
			winner.getLocation().getWorld().dropItemNaturally(winner.getLocation(), new ItemStack(Material.DIAMOND, 1));
			}});
			} catch (Exception e) {}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		for (Entity e : winner.getNearbyEntities(30, 30, 30)) {
			if (e.getType() == EntityType.DROPPED_ITEM) {
				e.remove();
			}
		} winner.getInventory().clear();
		
		if (a.spleefType == Type.SNOW) a.spleefType = Type.TNT;
		else if (a.spleefType == Type.TNT) a.spleefType = Type.BOMB;
		else if (a.spleefType == Type.BOMB) a.spleefType = Type.INSTANT;
		else if (a.spleefType == Type.INSTANT) a.spleefType = Type.SNOW;
		a.im.enabled = false;
		a.phase = Phase.LOBBY;
		
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
			public void run() {
		for (int i = 0; i <= a.layers.size(); i++) {
			try {
			a.layers.get(i).fillWith(Material.AIR);
			a.layers.get(i).fillWith(a.spleefType.material, (byte) 0);
			} catch (Exception e) {}
		}
			}
		});

		
		try { Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {a.removePlayer(winner); }});} catch(Exception e) {e.printStackTrace();}
		for (UUID id : a.players) {
			try { Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {a.removePlayer(Bukkit.getPlayer(id)); }}); } catch(Exception e) {e.printStackTrace();}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {SignHandler.refreshSigns();}});
	}

}
