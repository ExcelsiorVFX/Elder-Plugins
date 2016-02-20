package me.excelsiorvfx.ExcelsiorBase.world;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SaveBlocks {
	
	@SuppressWarnings("deprecation")
	public static String blocksToString(Location[] blocks, boolean saveAir, boolean threaded, boolean report) {
		String out = "";
		
		for (Location b : blocks) {
			if (b != null) {
				if (b.getBlock().getType() == Material.AIR && !saveAir) {}
				else {
					out = out + "@x:" + b.getBlockX();
					out = out + "@y:" + b.getBlockY();
					out = out + "@z:" + b.getBlockZ();
					out = out + "@w:" + b.getWorld().getUID();
					out = out + "@t:" + b.getBlock().getType().name();
					out = out + "@d:" + b.getBlock().getData();
					if (report) System.out.println("Saved block with type " + b.getBlock().getType().name());
				}
			} out = out + ";";
		}
		
		return out;
	}
	
	@SuppressWarnings("deprecation")
	public static String blocksToString(Location[] blocks, boolean saveAir) {
		String out = "";
		
		for (Location b : blocks) {
			if (b != null) {
				if (b.getBlock().getType() == Material.AIR && !saveAir) {}
				else {
					out = out + "@x:" + b.getBlockX();
					out = out + "@y:" + b.getBlockY();
					out = out + "@z:" + b.getBlockZ();
					out = out + "@w:" + b.getWorld().getUID();
					out = out + "@t:" + b.getBlock().getType().name();
					out = out + "@d:" + b.getBlock().getData();
				}
			} out = out + ";";
		}
		
		return out;
	}
	
	@Deprecated
	public static String blocksToString(Location[] blocks) {
		String out = "";
		
		for (Location b : blocks) {
			if (b != null) {
				out = out + "@x:" + b.getBlockX();
				out = out + "@y:" + b.getBlockY();
				out = out + "@z:" + b.getBlockZ();
				out = out + "@w:" + b.getWorld().getUID();
				out = out + "@t:" + b.getBlock().getType().name();
				out = out + "@d:" + b.getBlock().getData();
			} out = out + ";";
		}
		
		return out;
	}
	
	@SuppressWarnings("deprecation")
	public static int restoreBlocksFromString(String blocks) {
		String[] all = blocks.split(";");
		
		int i = 0;
		
		for (String block : all) {
			int x = 0;
			int y = 0;
			int z = 0;
			World w = Bukkit.getWorlds().get(0);
			Material m  = Material.AIR;
			byte data = 0;
			if (block != null) {
				String[] vals = block.split("@");
				for (String val : vals) {
					String[] stats = val.split(":");
						try {
							if (stats[0].equalsIgnoreCase("x")) x = Integer.valueOf(stats[1]);
							else if (stats[0].equalsIgnoreCase("y")) y = Integer.valueOf(stats[1]);
							else if (stats[0].equalsIgnoreCase("z")) z = Integer.valueOf(stats[1]);
							else if (stats[0].equalsIgnoreCase("w")) w = Bukkit.getWorld(UUID.fromString(stats[1]));
							else if (stats[0].equalsIgnoreCase("t")) m = Material.valueOf(stats[1]);
							else if (stats[0].equalsIgnoreCase("d")) data = Byte.valueOf(stats[1]);
						} catch (Exception e) {e.printStackTrace();}
				}
				Block actual = new Location(w, x, y, z).getBlock();
				actual.setType(m);
				actual.setData(data);
				i++;
			}
		} return i;
	}

}
