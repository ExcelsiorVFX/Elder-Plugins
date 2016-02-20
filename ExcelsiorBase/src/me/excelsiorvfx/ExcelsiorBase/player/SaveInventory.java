package me.excelsiorvfx.ExcelsiorBase.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SaveInventory {

	public static HashMap<UUID,List<ItemStack>> invs = new HashMap<UUID,List<ItemStack>>();
	
	public static HashMap<UUID,List<ItemStack>> armor = new HashMap<UUID,List<ItemStack>>();
	
	public static void saveInv(Player player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		List<ItemStack> arm = new ArrayList<ItemStack>();
		for (ItemStack item : player.getInventory().getContents()) {
			items.add(item);
		}
		arm.add(player.getInventory().getBoots());
		arm.add(player.getInventory().getLeggings());
		arm.add(player.getInventory().getChestplate());
		arm.add(player.getInventory().getHelmet());
		invs.put(player.getUniqueId(), items);
		armor.put(player.getUniqueId(), arm);
		
		for (PotionEffect e : player.getActivePotionEffects())
			player.removePotionEffect(e.getType());
		
		player.getInventory().clear();
		player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
		player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
		player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
		player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
	}
	
	public static void restoreInv(Player player) {
		if (hasStoredInv(player)) {
			List<ItemStack> list = invs.get(player.getUniqueId());
			ItemStack[] invents = new ItemStack[list.size()];
			list.toArray(invents);
			player.getInventory().clear();
			
			for (PotionEffect e : player.getActivePotionEffects())
				player.removePotionEffect(e.getType());
			
			player.getInventory().setBoots(new ItemStack(Material.AIR, 1));
			player.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
			player.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
			player.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
			player.getInventory().setContents(invents);
			List<ItemStack> arm = armor.get(player.getUniqueId());
			player.getInventory().setBoots(arm.get(0));
			player.getInventory().setLeggings(arm.get(1));
			player.getInventory().setChestplate(arm.get(2));
			player.getInventory().setHelmet(arm.get(3));
		} 
	}
	
	public static boolean hasStoredInv(Player player) {
		return invs.containsKey(player.getUniqueId());
	}
	
}
