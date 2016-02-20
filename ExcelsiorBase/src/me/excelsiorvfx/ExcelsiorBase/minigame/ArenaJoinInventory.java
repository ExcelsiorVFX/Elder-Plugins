package me.excelsiorvfx.ExcelsiorBase.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaJoinInventory implements Runnable, Listener {

	public static List<ArenaJoinInventory> all = new ArrayList<ArenaJoinInventory>();
	
	public boolean enabled = true;
	
	private ExcelsiorGame game;
	
	private Inventory inv;
	
	public ArenaJoinInventory(ExcelsiorGame game) {
		this.game = game;
		all.add(this);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, game.getPlugin());
		
		inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + game.getGameName() + " Games");
		
		new Thread(this).start();
	}
	
	public static ItemStack name(Material mat, String name, int amt, int timer, String phase) {
		ItemStack r = new ItemStack(mat, amt);
		ItemMeta meta = r.getItemMeta();
		meta.setDisplayName(name);
		
		List<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.GREEN + " - " + amt + " Players");
		lore.add(" ");
		lore.add(ChatColor.GOLD + " - " + timer + " Seconds Left Until Start");
		lore.add(" ");
		lore.add(ChatColor.YELLOW + " - Phase: " + phase);
		
		meta.setLore(lore);
		r.setItemMeta(meta);
		return r;
	}
	
	public static ItemStack name(Material mat, String name, int amt, String phase) {
		ItemStack r = new ItemStack(mat, amt);
		ItemMeta meta = r.getItemMeta();
		meta.setDisplayName(name);
		
		List<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.GREEN + " - " + amt + " Players");
		lore.add(" ");
		lore.add(ChatColor.RED + " - Phase: " + phase);
		
		meta.setLore(lore);
		r.setItemMeta(meta);
		return r;
	}
	
	public static void disableAll() {
		for (ArenaJoinInventory a : all) a.enabled = false;
	}
	
	public static ArenaJoinInventory getManager(ExcelsiorGame game) {
		for (ArenaJoinInventory a : all) if (a.game == game) return a;
		return null;
	}
	
	public static Inventory getInventory(ExcelsiorGame game) {
		for (ArenaJoinInventory a : all) if (a.game == game) return a.inv;
		return null;
	}
	
	public static ExcelsiorGame getGame(String name) {
		for (ArenaJoinInventory a : all) if (a.game.getGameName().equalsIgnoreCase(name)) return a.game;
		return null;
	}

	
	// This updates the inventory
	@Override
	public void run() {
		while(enabled) {
			int joinableIndex = 9;
			int unjoinableIndex = 27;
			for (ExcelsiorArena arena : game.getAllArenas()) {
				if (arena.getJoinable()) {
					inv.setItem(joinableIndex, name(Material.SLIME_BALL, ChatColor.YELLOW + "Join Arena " + ChatColor.DARK_AQUA + arena.getName(), arena.getPlayerAmount(),
							arena.getCountdownTime(), arena.getPhaseMessage()));
					joinableIndex++;
				} else {
					inv.setItem(unjoinableIndex, name(Material.CLAY_BALL, ChatColor.GRAY + "Arena " + ChatColor.DARK_AQUA + arena.getName(), arena.getPlayerAmount(),
							arena.getPhaseMessage()));
					unjoinableIndex++;
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;
		if (e.getCurrentItem().getType() == Material.AIR) return;
		if (e.getClickedInventory().getName().contains(game.getGameName())) {
			e.setCancelled(true);
			for (ExcelsiorArena arena : game.getAllArenas()) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(arena.getName())) {
					e.getWhoClicked().closeInventory();
					arena.addPlayer((Player) e.getWhoClicked());
				}
			}
		}
	}
	
}
