package net.theelder.plugins.KingOfTheLadder;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.excelsiorvfx.excelsiorbasele.config.Config;

import org.bukkit.entity.Player;

public class HighScore {
	
	public Arena a;
	
	public HighScore(Arena a) {
		this.a = a;
	}

	private final HashMap<UUID, Double> score = new HashMap<UUID, Double>();
	
	public double getScore(Player p) {
		try {
			return score.get(p.getUniqueId());
		} catch(Exception e) {return 0.0;}
	}
	
	public UUID getKing() {
		double highest = 0;
		UUID highestID = null;
		for (UUID id : score.keySet()) {
			if (score.get(id) > highest) {
				highest = score.get(id);
				highestID = id;
			}
		}
		if (highestID == null) return null;
		return highestID;
	}
	
	public void increaseScore(Player p) {
		if (getScore(p) == 0.0) score.put(p.getUniqueId(), 0.1);
		else score.put(p.getUniqueId(), 
				Double.valueOf(
				new DecimalFormat("0.0").format(score.get(p.getUniqueId()) + 0.1)));
		a.updateKing();
	}
	
	public void save(File folder) {
		Config co = new Config(folder, "Arena_" + a.name + "_HighScores");
		
		List<String> all = new ArrayList<String>();
		
		for (UUID id : score.keySet()) {
			co.getConfig().set("Scores." + id.toString(), score.get(id));
			all.add(id.toString());
		}
		co.getConfig().set("All", all);
		co.saveConfig();
	}

	public void load(File folder) {
		Config co = new Config(folder, "Arena_" + a.name + "_HighScores");
		
		List<String> all = co.getConfig().getStringList("All");
		
		if (all == null) return;
				 
		for (String id : all) {
			score.put(UUID.fromString(id), co.getConfig().getDouble("Scores." + id));
		}
	}

}
