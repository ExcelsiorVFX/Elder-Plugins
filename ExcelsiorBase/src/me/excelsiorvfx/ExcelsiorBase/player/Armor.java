package me.excelsiorvfx.ExcelsiorBase.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Armor {

	private ItemStack boots;
	private ItemStack legs;
	private ItemStack chestplate;
	private ItemStack helmet;
	
	
	public Armor(ItemStack boots, ItemStack leggings, ItemStack chestplate, ItemStack helmet) {
		this.boots = boots;
		legs = leggings;
		this.chestplate = chestplate;
		this.helmet = helmet;
	}
	
	public void equipArmor(Player player) {
		player.getInventory().setBoots(boots);
		player.getInventory().setLeggings(legs);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setHelmet(helmet);
	}
	
	public static void clearArmor(Player player) {
		ItemStack air = new ItemStack(Material.AIR, 1);
		player.getInventory().setBoots(air);
		player.getInventory().setLeggings(air);
		player.getInventory().setChestplate(air);
		player.getInventory().setHelmet(air);
	}
	
	public static Armor getFullLeather() {
		return new Armor(new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1));
	}
	public static Armor getFullGold() {
		return new Armor(new ItemStack(Material.GOLD_BOOTS, 1), new ItemStack(Material.GOLD_LEGGINGS, 1), new ItemStack(Material.GOLD_CHESTPLATE, 1), new ItemStack(Material.GOLD_HELMET, 1));
	}
	public static Armor getFullChainmail() {
		return new Armor(new ItemStack(Material.CHAINMAIL_BOOTS, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_HELMET, 1));
	}
	
	public static Armor getFullIron() {
		return new Armor(new ItemStack(Material.IRON_BOOTS, 1), new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.IRON_HELMET, 1));
	}
	
	public static Armor getFullDiamond() {
		return new Armor(new ItemStack(Material.DIAMOND_BOOTS, 1), new ItemStack(Material.DIAMOND_LEGGINGS, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.DIAMOND_HELMET, 1));
	}
	
}
