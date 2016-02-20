package net.theelder.plugins.Spleef.handlers;

import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Main;
import net.theelder.plugins.Spleef.enums.Phase;
import net.theelder.plugins.Spleef.threads.BlockTimer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class InstantMode implements Listener {

	public Arena a;
	public boolean enabled;
	
	public InstantMode(Arena a, boolean enabled) {
		Main.instance.getServer().getPluginManager().registerEvents(this, Main.instance);
		this.a = a;
		this.enabled = enabled;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (Arena.isPlaying(e.getPlayer())) {
			if (a.phase == Phase.IN_GAME) {
				if (enabled) {
					
					Location below = new Location(e.getFrom().getWorld(),
							e.getFrom().getBlockX(),
							e.getFrom().getBlockY() - 1,
							e.getFrom().getBlockZ());
					
					if (below.getBlock().getType() == Material.STAINED_CLAY)
						Main.run(new BlockTimer(below));
				}
			}
		}
	}
	
}
