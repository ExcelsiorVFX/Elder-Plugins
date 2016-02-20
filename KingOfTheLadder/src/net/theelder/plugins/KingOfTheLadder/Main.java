package net.theelder.plugins.KingOfTheLadder;

import me.excelsiorvfx.excelsiorbasele.world.Area;
import me.excelsiorvfx.excelsiorbasele.world.WorldEditHandle;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main instance;
	public static Arena editing;
	
	public Main() {
		
	}
	
	@Override
	public void onEnable() {
		instance = this;
		getLogger().info("KING OF THE LADDER (Light Version) enabled!");
		int i = Arena.load(getDataFolder());
		getLogger().info("Loaded " + i + " arenas!");
	}
	
	@Override
	public void onDisable() {
		Arena.save(getDataFolder());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "Sorry! You are not allowed to use these commands.");
			return true;
		}
		try {
			Player p = (Player) sender;
			switch(args[0].toLowerCase()) {
			
			case "new": 
				editing = new Arena(args[1], null, null);
				p.sendMessage(ChatColor.GREEN + "Created new arena!");
				break;
			case "edit":
				editing = Arena.getArena(args[1]);
				if (editing == null) p.sendMessage(ChatColor.RED +"That arena does not exist.");
				else p.sendMessage(ChatColor.GREEN + "You are now editing " + args[1] + "!");
				break;
			case "delete":
				Arena.getArena(args[1]).delete();
				p.sendMessage(ChatColor.GREEN + "Deleted arena " + ChatColor.RED + args[1]);
				break;
			case "list":
				if (Arena.all.size() == 0) {
					p.sendMessage("There are no active arenas.");
					break;
				}
				p.sendMessage(ChatColor.YELLOW + "Active KOTL arenas:");
				for (Arena a : Arena.all) p.sendMessage(ChatColor.BLUE + " - " + a.name);
				break;
			//case "reload":
			//	p.sendMessage(ChatColor.YELLOW + "Reloaded " + Arena.reloadConfigs(getDataFolder()) + " arena's configurations.");
			//	break;
			case "debug":
				if (editing.listener.debug) {
					p.sendMessage(ChatColor.GRAY + "Disabled the arena's debug mode.");
					editing.listener.debug = false;
				} else {
					p.sendMessage(ChatColor.YELLOW + "Enabled the arena's debug mode.");
					editing.listener.debug = true;
				}
				break;
			case "set": 
				switch(args[1].toLowerCase()) {
					case "area":
						editing.entire = new Area(new WorldEditHandle().GetSelectionMax(p), new WorldEditHandle().GetSelectionMin(p));
						p.sendMessage(ChatColor.GREEN + "Set arena's area!");
						break;
					case "plate":
						editing.plate = new Location(p.getWorld(), p.getLocation().getBlockX(), 
								p.getLocation().getBlockY(), 
								p.getLocation().getBlockZ());
						
						p.sendMessage(ChatColor.GREEN + "Set arena's plate!");
						break;
					case "king":
						editing.king = p.getLocation();
						p.sendMessage(ChatColor.GREEN + "Set arena's king location!");
						editing.updateKing();
						break;
					case "exit":
						editing.exit = p.getLocation();
						p.sendMessage(ChatColor.GREEN + "Set arena's exit location!");
						break;
					default: sendHelp(p);
				}
				break;
			case "messages":
				if (args.length < 2) {
					sendMessagesHelp(sender);
					return true;
				}
				switch(args[1].toLowerCase()) {
					case "join":
						editing.joinMessage = args[2].replace("_", " ");
						p.sendMessage(ChatColor.GREEN + "Set join message to: \"" + args[2].replace("_", " ") + "\"");
						break;
					case "leave":
						editing.leaveMessage = args[2].replace("_", " ");
						p.sendMessage(ChatColor.GREEN + "Set leave message to: \"" + args[2].replace("_", " ") + "\"");
						break;
					case "notenoughplayers":
						editing.notEnoughPlayers = args[2].replace("_", " ");
						p.sendMessage(ChatColor.GREEN + "Set not enough players message to: \"" + args[2].replace("_", " ") + "\"");
						break;
					case "timer":
						editing.timerNotExpired = args[2].replace("_", " ");
						p.sendMessage(ChatColor.GREEN + "Set timer not expired message to: \"" + args[2].replace("_", " ") + "\"");
						break;
					case "score":
						editing.scoreIncrease = args[2].replace("_", " ");
						p.sendMessage(ChatColor.GREEN + "Set score increase message to: \"" + args[2].replace("_", " ") + "\"");
						break;
					case "joinnotify":
						editing.joinNotify = Boolean.valueOf(args[2]);
						p.sendMessage(ChatColor.GREEN + "Set join message notify to " + args[2] + ".");
						break;
					case "leavenotify":
						editing.leaveNotify = Boolean.valueOf(args[2]);
						p.sendMessage(ChatColor.GREEN + "Set leave message notify to " + args[2] + ".");
						break;
					default: sendMessagesHelp(sender);
				}
				break;
			default: sendHelp(p);
			
			}
		} catch(Exception e) {
			sendHelp(sender);
		}
		
		return true;
	}

	public static void sendMessagesHelp(CommandSender p) {
		p.sendMessage(ChatColor.YELLOW + "------ [ KING OF THE LADDER MESSAGES ] ------");
		p.sendMessage(ChatColor.ITALIC + "Note: You must be editing an arena for these commands to work.");
		p.sendMessage(ChatColor.ITALIC + "REMEMBER: USE AN UNDERSCORE (_) INSTEAD OF A SPACE WITH A CUSTOM MESSAGE!");
		p.sendMessage(ChatColor.ITALIC + "Use {name}, {timer}, and {score} when necessary.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages join <message> : Set join message.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages joinNotify <true/false> : Set if join message should notify other players.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages leave <message> : Set leave message.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages leaveNotify <true/false> : Set if leave message should notify other players.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages notEnoughPlayers <message> : Set the not enough players message.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages timer <message> : Set the timer has not expired message.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages score <message> : Set the score increased message.");
	}
	
	public static void sendHelp(CommandSender p) {
		p.sendMessage(ChatColor.YELLOW + "------ [ KING OF THE LADDER ] ------");
		p.sendMessage(ChatColor.ITALIC + "Note: You must be a player to execute these.");
		p.sendMessage(ChatColor.ITALIC + "See the SpigotMC plugin page for more information.");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl new <name>");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl edit <name>");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl set area");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl set plate");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl set king");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl set exit");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl messages");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl delete <name>");
	//	p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl reload");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl debug");
		p.sendMessage(ChatColor.LIGHT_PURPLE + " - /kotl list");
	}
}
