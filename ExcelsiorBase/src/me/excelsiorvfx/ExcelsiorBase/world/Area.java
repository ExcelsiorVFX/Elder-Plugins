package me.excelsiorvfx.ExcelsiorBase.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Area {
	
	public List<String> blocks = new ArrayList<String>();

	public Location max;
	
	public Location min;
	
	public Area(Location max, Location min) {
		this.max = max;
		this.min = min;
	}
	
	public boolean isInThisArea(Location loc) {
		if (loc.getBlockX() <= max.getBlockX() && loc.getBlockX() >= min.getBlockX()) {
			if (loc.getBlockY() <= max.getBlockY() && loc.getBlockY() >= min.getBlockY()) {
				if (loc.getBlockZ() <= max.getBlockZ() && loc.getBlockZ() >= min.getBlockZ()) {
					return true;
				} else return false;
			} else return false;
		} else return false;
	}
	
	public Block[] getBlocks() {
		int length = 0;
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					length++;
				}
			}
		}
		Block[] blocks = new Block[length];
		int i = 0;
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					blocks[i] = new Location(max.getWorld(), x, y, z).getBlock();
					i++;
				}
			}
		}
		return blocks;
	}
	
	public void saveToRAM() {
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					blocks.add(new Location(max.getWorld(), x, y, z).getBlock().getType().name());
				}
			}
		}
	}
	
	public boolean restoreFromRAM() {
		if (blocks == null) return false;
		int i = 0;
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					new Location(max.getWorld(), x, y, z).getBlock().setType(Material.getMaterial(blocks.get(i)));
					i++;
				}
			}
		} return true;
	} 
	
	public void fillWith(Material mat) {
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					try {
					new Location(max.getWorld(), x, y, z).getBlock().setType(mat);
					} catch(Exception e) {}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void fillWith(Material mat, byte data) {
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Location loc = new Location(max.getWorld(), x, y, z);
					try { loc.getBlock().setType(mat);
					loc.getBlock().setData(data); } catch(Exception e) {}
				}
			}
		}
	}

}
