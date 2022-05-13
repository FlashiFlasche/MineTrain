package fr.dariusmtn.minetrain.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.dariusmtn.minetrain.Main;

public class BlockBreakListener implements Listener {
	private Main plugin;

	public BlockBreakListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDestroy(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location bLoc = event.getBlock().getLocation();
		Block block = event.getBlock();
		
		
		if (block.getType().equals(Material.OAK_BUTTON)) {
			if (plugin.getFileManager().isStationButton(bLoc)) {
				event.setCancelled(true);
				player.sendMessage("§cThis button is binded to a station!");
			}
		}
	}

}
