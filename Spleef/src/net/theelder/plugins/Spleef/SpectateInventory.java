package net.theelder.plugins.Spleef;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SpectateInventory {

	public static void openInventory(Arena a, Player p) {
		Inventory inv = Bukkit.createInventory(p, 45, ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Teleport to a Player");
		
		int i = 1;
		for (UUID id : a.players) {
			if (!a.spectating.get(id)) {
				ItemStack head = new ItemStack(Material.SKULL_ITEM, 1);
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setOwner(Bukkit.getPlayer(id).getName());
				meta.setDisplayName(ChatColor.YELLOW + Bukkit.getPlayer(id).getName());
				head.setItemMeta(meta);
				inv.setItem(i, head);
			}
		}
		
		p.openInventory(inv);
	}

}
