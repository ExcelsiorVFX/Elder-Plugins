package net.theelder.plugins.Spleef.handlers;

import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Main;
import net.theelder.plugins.Spleef.SpectateInventory;
import net.theelder.plugins.Spleef.enums.Phase;
import net.theelder.plugins.Spleef.enums.Type;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class FallListener implements Listener {

	public Arena a;
	
	public FallListener(Arena a) {
		this.a = a;
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (a.players.contains(e.getWhoClicked().getUniqueId())) {
			if (a.phase == Phase.IN_GAME) {
				if (a.spectating.get(e.getWhoClicked().getUniqueId())) {
					e.setCancelled(true);
					if (e.getInventory().getTitle().contains("Teleport")) {
						try {
						e.getWhoClicked().teleport(Bukkit.getPlayer(((SkullMeta)e.getCurrentItem().getItemMeta()).getOwner()));
						} catch(Exception ex) {}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		try {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		if (a.players.contains(e.getPlayer().getUniqueId())) {
			if (a.phase == Phase.IN_GAME) {
				if (a.spectating.get(e.getPlayer().getUniqueId())) {
					try {
						if (e.getPlayer().getItemInHand().getType() == Material.BLAZE_ROD) {
							SpectateInventory.openInventory(a, e.getPlayer());
						}
					} catch(Exception ex) {System.out.println(ChatColor.RED + ex.getMessage());}
				}
			}
		}
		} } catch(Exception ex2) {ex2.printStackTrace();}
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (a.players.contains(p.getUniqueId())) {
				if (a.spectating.get(p.getUniqueId())) {
					e.setCancelled(true);
					return;
				}
				if (a.phase == Phase.LOBBY || a.phase == Phase.PRE_GAME) e.setCancelled(true);
				if (e.getCause() == DamageCause.BLOCK_EXPLOSION) e.setCancelled(true);
				if (e.getCause() == DamageCause.ENTITY_EXPLOSION) e.setCancelled(true);
				if (e.getCause() == DamageCause.ENTITY_ATTACK) e.setCancelled(true);
				if (e.getCause() == DamageCause.PROJECTILE) e.setCancelled(true);
				if (e.getCause() == DamageCause.FIRE_TICK) p.setFireTicks(0);
			if (a.phase == Phase.IN_GAME && !e.isCancelled()) {
				Damageable d = p;
				if (d.getHealth() - e.getDamage() <= 0.0) {
				p.teleport(a.centerOfArea);
				e.setCancelled(true);
				d.setHealth(20d);
				d.setFireTicks(0);
					a.registerDeath(p);
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {}
							Bukkit.getScheduler().runTask(Main.instance, new Runnable() { public void run() {p.teleport(a.centerOfArea);}});
					}
					}).start();
				}
			}
			}
		}
	}
	
	/*
	@EventHandler
	public void onMove(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if (Arena.isPlaying(p)) {
				Arena a = Arena.getArena(p);
				if (a.phase == Phase.IN_GAME) {
					p.teleport(a.centerOfArea);
					a.registerDeath(p);
					Main.run(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {}
							p.teleport(a.centerOfArea);
						}
					});
				}
				
			}
		} */
	
	// TODO Changes:
	// 			- Add blocks randomly disappearing
	//			- Prevent damage on arena reset & teleport
	
	
	/*@SuppressWarnings("deprecation")
	@EventHandler
	public void onBow(CreatureSpawnEvent e) {
		if (e.getEntityType() == EntityType.PRIMED_TNT) {
			if (a.spleefArea.isInThisArea(e.getLocation())) {
				e.setCancelled(true);
				FallingBlock b = e.getLocation().getWorld().spawnFallingBlock(e.getLocation(), Material.TNT, (byte)0);
				b.setDropItem(false);
				System.out.println("Block spawned");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {}
						b.remove();
					}
				}).start();
			}
		}
	}*/
	
	@EventHandler
	public void expode(EntityExplodeEvent e) {
		if (e.getEntityType() == EntityType.PRIMED_TNT) {
			if (e.getLocation().getWorld() == a.spleefArea.max.getWorld()) {
				e.setCancelled(true);
				if (a.spleefType == Type.BOMB) {
					for (Block b : e.blockList()) {
						if (a.spleefArea.isInThisArea(b.getLocation()))
							b.setType(Material.AIR);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (a.players.contains(e.getPlayer().getUniqueId())) {
			if (a.spleefArea.isInThisArea(e.getBlock().getLocation())) {
				if (a.spleefType == Type.TNT) e.setCancelled(true);
			} else e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (a.players.contains(e.getPlayer().getUniqueId())) {
		//if (e.getBlock().getType() == Material.TNT) {
			//Location loc = new Location(e.getBlock().getLocation().getWorld(),
			//		e.getBlock().getLocation().getBlockX(),
			//		e.getBlock().getLocation().getBlockY() - 1,
			//		e.getBlock().getLocation().getBlockZ());
			if (a.spleefArea.isInThisArea(e.getBlock().getLocation())) {
				//e.setCancelled(true);

				/*Location min = new Location(e.getBlock().getLocation().getWorld(),
						e.getBlock().getLocation().getBlockX() - 3,
						e.getBlock().getLocation().getBlockY() - 1,
						e.getBlock().getLocation().getBlockZ() - 3);
				Location max = new Location(e.getBlock().getLocation().getWorld(),
						e.getBlock().getLocation().getBlockX() + 3,
						e.getBlock().getLocation().getBlockY() - 1,
						e.getBlock().getLocation().getBlockZ() + 3);
				Area all = new Area(min, max);
				for (Block b : all.getBlocks()) {
					Main.run(new BlockTimer(b.getLocation())); 
				} */
			} else e.setCancelled(true);
				
					//@Override
					//public void run() {
						//for (UUID id : a.players) Bukkit.getPlayer(id).playSound(e.getBlock().getLocation(), 
							//	Sound.FUSE, 100, 1);
						//TNTPrimed tnt = (TNTPrimed) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.PRIMED_TNT);
						//tnt.setFuseTicks(100000);
						//try {
						//	Thread.sleep(4000);
						//} catch (InterruptedException e1) {}
						//tnt.remove();
						//for (int x = -2; x < 2; x++) {
						//	for (int z = -2; z < 2; z++) {
						//		Location block = new Location(loc.getWorld(), 
						//				loc.getX() - x,
						//				loc.getY(),
						//				loc.getZ() - z);
						//		if (block.getBlock().getType() == Material.REDSTONE_BLOCK) block.getBlock().setType(Material.AIR);
						//	}
						//}
					//}
			}
		
	}
	
	public static void setLevel(Player p, int i) {
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
			
			@Override
			public void run() {
				p.setLevel(i);	
			}
		});
	}
	
}
