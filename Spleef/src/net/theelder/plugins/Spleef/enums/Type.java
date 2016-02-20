package net.theelder.plugins.Spleef.enums;

import org.bukkit.Material;

public enum Type {

	SNOW(Material.SNOW_BLOCK, Material.DIAMOND_SPADE), 
	TNT(Material.TNT, Material.BOW), 
	BOMB(Material.REDSTONE_BLOCK, Material.TNT), 
	INSTANT(Material.STAINED_CLAY, Material.AIR);
	
	public final Material material;
	
	public final Material item;
	
	Type(Material mat, Material item) {
		this.material = mat;
		this.item = item;
	}
	
}
