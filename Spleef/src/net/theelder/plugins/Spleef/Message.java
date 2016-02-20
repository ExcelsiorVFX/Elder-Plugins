package net.theelder.plugins.Spleef;

import java.util.UUID;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Message {

	public static void playSound(Arena a, Sound s, float pitch) {
		for (UUID id : a.players) {
			Player p = Bukkit.getPlayer(id);
			p.playSound(p.getLocation(), s, 100, pitch);
		}
	}
	
	public static void bar(Arena a, String msg, int seconds) {
		for (UUID id : a.players) try { BarAPI.setMessage(Bukkit.getPlayer(id), msg, seconds); } catch(Exception e) {}
	}
	
	public static void bar(Arena a, String msg) {
		for (UUID id : a.players) try { BarAPI.setMessage(Bukkit.getPlayer(id), msg); } catch(Exception e) {}
	}
	
	public static void clearBar(Arena a) {
		for (UUID id : a.players) BarAPI.removeBar(Bukkit.getPlayer(id));
	}
	
	public static void msgChat(Arena a, String msg) {
		for (UUID id : a.players) Bukkit.getPlayer(id).sendMessage(msg);
	}
	
	public static void msgTitle(Arena a, String msg) {
		for (UUID id : a.players) {
			Bukkit.getPlayer(id).sendMessage(msg);
			//try { TitleAPI.sendTitle(Bukkit.getPlayer(id), 10, 20, 10, msg, ChatColor.GRAY + "----"); } catch(Exception e) {}
		}
	}
	
	public static void msgSubAndTitle(Arena a, String msg, String subtitle) {
		for (UUID id : a.players) {
			Bukkit.getPlayer(id).sendMessage(msg + ChatColor.RESET + " " + subtitle);
			//try { TitleAPI.sendTitle(Bukkit.getPlayer(id), 10, 20, 10, msg, subtitle); } catch(Exception e) {}
		}
	}
	
}
