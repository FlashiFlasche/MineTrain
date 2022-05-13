package fr.dariusmtn.minetrain.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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

		for (BlockFace face : BlockFace.values()) {
			// Check if the adjacent block relative to this face is a oak button
			if (event.getBlock().getRelative(face).getType().equals(Material.OAK_BUTTON)) {
				// Do what you want
				BlockData blockData = event.getBlock().getRelative(face).getBlockData();
				if (blockData instanceof Directional) {
					BlockFace buttonfacing = ((Directional) blockData).getFacing();

					if (face == buttonfacing) {
						if (plugin.getFileManager().isStationButton(event.getBlock().getRelative(face).getLocation())) {
							event.setCancelled(true);
							player.sendMessage("§cThis button is binded to a station!");
						}
					}
				}
				break;
			}
		}

		if (block.getType().equals(Material.OAK_BUTTON)) {
			if (plugin.getFileManager().isStationButton(bLoc)) {
				event.setCancelled(true);
				player.sendMessage("§cThis button is binded to a station!");
			}
		}
	}

}
