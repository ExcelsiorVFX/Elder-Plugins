package net.theelder.plugins.Spleef.threads;

import java.util.UUID;

import net.theelder.plugins.Spleef.Arena;
import net.theelder.plugins.Spleef.Message;
import net.theelder.plugins.Spleef.enums.Type;
import net.theelder.plugins.Spleef.handlers.FallListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Game implements Runnable {

	public Arena a;
	
	public Game(Arena a) {
		this.a = a;
	}
	
	@Override
	public void run() {
		Message.bar(a, ChatColor.RED + "" + ChatColor.BOLD + "Grace Period Ending!", 10);
		for (int i = 10; i >= 1; i--) {		
			for (UUID id : a.players) {
				Bukkit.getPlayer(id).sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[SPLEEF] " + ChatColor.YELLOW + "" + i + " seconds left in grace period!");
				//if (i <= 3) TitleAPI.sendTitle(Bukkit.getPlayer(id), 10, 20, 10, ChatColor.RED + "" + i, ChatColor.GRAY + "---");
				 FallListener.setLevel(Bukkit.getPlayer(id), i);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		for (UUID id : a.players) {
			Bukkit.getPlayer(id).setFallDistance(1);
			if (a.spleefType == Type.SNOW) {
				ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE, 1);
				ItemMeta meta = shovel.getItemMeta();
				meta.addEnchant(Enchantment.DURABILITY, 255, true);
				meta.setDisplayName(ChatColor.GREEN + "Spleeferizor 1000");
				shovel.setItemMeta(meta);
				Player p = Bukkit.getPlayer(id);
				p.getInventory().clear();
				p.getInventory().addItem(shovel);
				a.im.enabled = false;
			}
			if (a.spleefType == Type.TNT) {
				ItemStack shovel = new ItemStack(Material.BOW, 1);
				ItemMeta meta = shovel.getItemMeta();
				meta.addEnchant(Enchantment.DURABILITY, 255, true);
				meta.addEnchant(Enchantment.ARROW_FIRE, 255, true);
				meta.addEnchant(Enchantment.ARROW_INFINITE, 255, true);
				meta.setDisplayName(ChatColor.GREEN + "Bow-ser");
				shovel.setItemMeta(meta);
				Player p = Bukkit.getPlayer(id);
				p.getInventory().clear();
				p.getInventory().addItem(shovel);
				p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
				a.im.enabled = false;
			}
			if (a.spleefType == Type.BOMB) {
				ItemStack shovel = new ItemStack(Material.TNT, 64);
				Player p = Bukkit.getPlayer(id);
				p.getInventory().clear();
				p.getInventory().addItem(shovel);
				p.getInventory().addItem(shovel);
				p.getInventory().addItem(shovel);
				p.getInventory().addItem(shovel);
				p.getInventory().addItem(shovel);
				a.im.enabled = false;
			}
			if (a.spleefType == Type.INSTANT) {
				Player p = Bukkit.getPlayer(id);
				p.getInventory().clear();
				a.im.enabled = true;
			}
		}
		
		new Thread(new BlockCollapse(a)).start();
		/*while (true) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		} */
	}

}
