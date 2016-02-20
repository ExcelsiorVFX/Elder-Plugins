package net.theelder.plugins.Spleef.threads;

import java.util.ArrayList;
import java.util.List;

import me.excelsiorvfx.ExcelsiorBase.world.Area;
import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Main;
import net.theelder.plugins.Spleef.Message;
import net.theelder.plugins.Spleef.enums.Phase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockCollapse implements Runnable {

	public static List<BlockCollapse> all = new ArrayList<BlockCollapse>();
	
	public boolean enabled = true;
	
	public Arena a;
	public int phase = 0;
	
	public BlockCollapse(Arena a) {
		this.a = a;
		all.add(this);
	}

	@Override
	public void run() {
		while (a.phase == Phase.IN_GAME) {
			
			try {
				Thread.sleep(500);
				Thread.sleep((a.collapseTime * 1000) - 3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (enabled) sync(new Runnable() { public void run() { Message.bar(a, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Arena shrinking...", 3); }});
	
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for (Area layer : a.layers) {
				int max_x = layer.max.getBlockX() - phase;
				int max_z = layer.max.getBlockZ() - phase;

				int min_x = layer.min.getBlockX() + phase;
				int min_z = layer.min.getBlockZ() + phase;
				
				for (Block b : layer.getBlocks()) {
					if (b.getLocation().getBlockX() == max_x) if (enabled) destroy(b); 
					if (b.getLocation().getBlockZ() == max_z) if (enabled) destroy(b); 
					if (b.getLocation().getBlockX() == min_x) if (enabled) destroy(b); 
					if (b.getLocation().getBlockZ() == min_z) if (enabled) destroy(b); 
				}
			}
			
			phase++;
		}
		all.remove(this);
	}

	public static void sync(Runnable r) {
		Bukkit.getScheduler().runTask(Main.instance, r);
	}
	
	public static void destroy(Block b) {
		sync(new Runnable() { public void run() {
			b.getWorld().playEffect(b.getLocation(), Effect.FLAME, 10);
			b.setType(Material.AIR);
		}});
	}
	
	public static BlockCollapse get(Arena a) {
		for (BlockCollapse c : all) if (c.a == a) return c;
		return null;
	}
	
}
