package me.F_o_F_1092.WeatherVote;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

	private Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();

		if (plugin.updateAvailable) {
			if (p.hasPermission("WeatherVote.UpdateMessage")) {
				p.sendMessage(plugin.msg.get("[WeatherVote]") + plugin.msg.get("msg.16"));
			}
		}

		if (WeatherVoteManager.isVotingAtWorld(p.getWorld().getName())) {
			WeatherVote wv = WeatherVoteManager.getVotingAtWorld(p.getWorld().getName());
			if (!wv.timeoutPeriod) {
				String text = plugin.msg.get("msg.3");
				if (wv.getWeather().equals("Sunny")) {
					text = text.replace("[WEATHER]", plugin.msg.get("text.1"));
				} else {
					text = text.replace("[WEATHER]", plugin.msg.get("text.2"));
				}

				p.sendMessage(plugin.msg.get("[WeatherVote]") + text);

				if (plugin.useScoreboard) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).setScoreboard(p.getName());
							WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).updateScore();
						}
					}, 1L);
				}
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (WeatherVoteManager.containsOpenVoteingGUI(p.getName())) {
			WeatherVoteManager.closeVoteingGUI(p.getName(), true);
		}

		if (WeatherVoteManager.isVotingAtWorld(p.getWorld().getName())) {
			WeatherVote wv = WeatherVoteManager.getVotingAtWorld(p.getWorld().getName());
			if (!wv.timeoutPeriod) {
				if (plugin.useScoreboard) {
					WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).removeScoreboard(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();

		if (WeatherVoteManager.containsOpenVoteingGUI(p.getName())) {
			WeatherVoteManager.closeVoteingGUI(p.getName(), true);
		}

		if (WeatherVoteManager.isVotingAtWorld(p.getWorld().getName())) {
			WeatherVote wv = WeatherVoteManager.getVotingAtWorld(p.getWorld().getName());
			if (!wv.timeoutPeriod) {
				if (plugin.useScoreboard) {
					WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).removeScoreboard(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();

		if (WeatherVoteManager.containsOpenVoteingGUI(p.getName())) {
			WeatherVoteManager.closeVoteingGUI(p.getName(), true);
		}

		if (!e.getFrom().getName().equals(p.getWorld().getName())) {
			if (WeatherVoteManager.isVotingAtWorld(e.getFrom().getName())) {
				if (plugin.useScoreboard) {
					WeatherVoteManager.getVotingAtWorld(e.getFrom().getName()).removeScoreboard(p.getName());
				}
			}
			if (WeatherVoteManager.isVotingAtWorld(p.getWorld().getName())) {
				WeatherVote wv = WeatherVoteManager.getVotingAtWorld(p.getWorld().getName());
				if (!wv.timeoutPeriod) {
					String text = plugin.msg.get("msg.3");
					if (wv.getWeather().equals("Sunny")) {
						text = text.replace("[WEATHER]", plugin.msg.get("text.1"));
					} else {
						text = text.replace("[WEATHER]", plugin.msg.get("text.2"));
					}

					p.sendMessage(plugin.msg.get("[WeatherVote]") + text);

					if (plugin.useScoreboard) {
						WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).setScoreboard(p.getName());
						WeatherVoteManager.getVotingAtWorld(p.getWorld().getName()).updateScore();
					}
				}
			}
		}
	}

	@EventHandler
	public void onCloseVoteingGUI(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (WeatherVoteManager.containsOpenVoteingGUI(p.getName())) {
			WeatherVoteManager.closeVoteingGUI(p.getName(), false);
		}
	}

	@EventHandler
	public void onVoteingGUIVote(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();

		if (WeatherVoteManager.containsOpenVoteingGUI(p.getName())) {
			if (e.getRawSlot() == e.getSlot()) {
				e.setCancelled(true);
				if (!WeatherVoteManager.isVotingAtWorld(p.getWorld().getName())) {
					if (e.getSlot() == 3) {
						p.chat("/wv sun");
					} else if (e.getSlot() == 5) {
						p.chat("/wv rain");
					}
				} else {
					if (e.getSlot() == 3) {
						p.chat("/wv yes");
					} else if (e.getSlot() == 5) {
						p.chat("/wv no");
					}
					WeatherVoteManager.closeVoteingGUI(p.getName(), true);
				}
			}
		}
	}
}
