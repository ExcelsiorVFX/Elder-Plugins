package net.theelder.plugins.Spleef.threads;

import net.theelder.plugins.Spleef.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockTimer implements Runnable {

	public Location loc;
	
	public BlockTimer(Location loc) {
		this.loc = loc;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		loc.getBlock().setData((byte) 13);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		loc.getBlock().setData((byte) 4);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		loc.getBlock().setData((byte) 14); 
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		Bukkit.getScheduler().runTask(Main.instance, new Runnable() {
			@Override
			public void run() {
				loc.getBlock().setType(Material.AIR);
			}
		});
	}

}
