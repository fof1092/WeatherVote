package me.F_o_F_1092.WeatherVote.PluginManager.Spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JSONMessageListener extends me.F_o_F_1092.WeatherVote.PluginManager.JSONMessageListener {

	public static void send(Player p, String jsonString) {
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " + jsonString);
	}
}
