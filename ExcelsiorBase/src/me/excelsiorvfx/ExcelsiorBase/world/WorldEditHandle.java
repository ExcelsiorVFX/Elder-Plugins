package me.excelsiorvfx.ExcelsiorBase.world;

import me.excelsiorvfx.ExcelsiorBase.API.ExcelsiorAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

public class WorldEditHandle implements ExcelsiorAPI {

	@Override
	public void enable(JavaPlugin p) {
		p.getLogger().info("WorldEditHandler enabled!");
	}
	
	public WorldEditHandle() {}
	
	public Location GetSelectionMax(Player pl){
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		double x = 0;
		double y = 0;
		double z = 0;
        LocalWorld lw=BukkitUtil.getLocalWorld(pl.getWorld());
        LocalSession ls=we.getSession(pl);
        Region rs;
        try {
            rs = ls.getSelection(lw);
            x = rs.getMaximumPoint().getX();
            y = rs.getMaximumPoint().getY();
            z = rs.getMaximumPoint().getZ();
        } catch (Exception e) { 
        }
        Location rst=new Location(pl.getWorld(), x, y, z);
        return rst;
    }
	public Location GetSelectionMin(Player pl){
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		double x = 0;
		double y = 0;
		double z = 0;
        LocalWorld lw=BukkitUtil.getLocalWorld(pl.getWorld());
        LocalSession ls=we.getSession(pl);
        Region rs;
        try {
            rs = ls.getSelection(lw);
            x = rs.getMinimumPoint().getX();
            y = rs.getMinimumPoint().getY();
            z = rs.getMinimumPoint().getZ();
        } catch (Exception e) { 
        }
        Location rst=new Location(pl.getWorld(), x, y, z);
        return rst;
    }
	
	public void saveLocation(Location loc) {
		
	}
	
	
}
