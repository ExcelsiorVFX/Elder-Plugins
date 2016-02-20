package net.theelder.plugins.KingOfTheLadder;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DamageListener implements Listener {

	public Arena a;
	
	public DamageListener(Arena a) {
		this.a = a;
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	public int playerTimer = 5;
	public boolean playerOnPlate = false;
	
	public UUID onPlate;
	
	public long timeMillis = 0;
	public long timeout = 0;
	
	public boolean debug = false;
	
	public boolean enabled = true;
	
	public void disable() {
		enabled = false;
	}
	
	@EventHandler
	public void plate(PlayerInteractEvent e) {
		if (!enabled) return;
		if (e.getAction() == Action.PHYSICAL) {
			if (a.isPlaying(e.getPlayer())) {
				e.setCancelled(true);
				if (a.plate.getBlockX() == e.getPlayer().getLocation().getBlockX() && a.plate.getBlockZ() == e.getPlayer().getLocation().getBlockZ()) {
					if (debug || a.playing) {
						if (playerOnPlate) {
							if (e.getPlayer().getUniqueId() != onPlate) {
								onPlate = e.getPlayer().getUniqueId();
								playerOnPlate = true;
								playerTimer = 5;
								timeMillis = System.currentTimeMillis();
							}
							if (System.currentTimeMillis() - timeMillis >= 1000) {
								if (System.currentTimeMillis() - timeMillis >= 5000) {
									playerOnPlate = false;
								} else {
									if (playerTimer == 0) {
										a.scoreKeeper.increaseScore(e.getPlayer());
										e.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + ChatColor.GREEN + ChatColor.BOLD + a.scoreIncrease.replace("{score}", "" + a.scoreKeeper.getScore(e.getPlayer())));
									} else {
										e.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + ChatColor.YELLOW + a.timerNotExpired.replace("{timer}", "" + playerTimer));
										playerTimer--;
									}
									timeMillis = System.currentTimeMillis();
								}
							} 
						} else {
							onPlate = e.getPlayer().getUniqueId();
							playerOnPlate = true;
							playerTimer = 5;
							timeMillis = System.currentTimeMillis();
						}
					} else {
						if (System.currentTimeMillis() - timeout > 5000) {
							e.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "[KOTL] " + ChatColor.RESET + ChatColor.RED + a.notEnoughPlayers);
							timeout = System.currentTimeMillis();
							e.getPlayer().teleport(a.exit);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!enabled) return;
		if (a.entire == null) return;
		if (a.entire.isInThisArea(e.getBlock().getLocation())) {
			if (!e.getPlayer().isOp()) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onSign(SignChangeEvent e) {
		if (!enabled) return;
		if (e.getPlayer().isOp()) {
			if (e.getLine(0).equalsIgnoreCase("[kotl]") && e.getLine(1).equalsIgnoreCase("king") && e.getLine(2).equalsIgnoreCase(a.name)) {
				a.sign = e.getBlock().getLocation();
				a.updateKing();
				e.getPlayer().sendMessage(ChatColor.GREEN + "Set king sign!");
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (!enabled) return;
		if (a.entire == null || a.plate == null) return;
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase(a.plate.getWorld().getName())) {
			if (a.entire.isInThisArea(e.getPlayer().getLocation())) {
				if (!a.isPlaying(e.getPlayer())) a.addPlayer(e.getPlayer());
			} else {
				if (a.isPlaying(e.getPlayer())) a.removePlayer(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!enabled) return;
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (a.isPlaying(p)) {
				if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (!enabled) return;
		if (a.isPlaying(e.getPlayer())) a.removePlayer(e.getPlayer());
	}

}
