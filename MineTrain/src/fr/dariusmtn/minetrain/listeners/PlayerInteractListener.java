package fr.dariusmtn.minetrain.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class PlayerInteractListener implements Listener {

	private Main plugin;

	public PlayerInteractListener(Main plugin) {
		this.plugin = plugin;
	}

	boolean changeNextRails(Rail clickedRail, Location clickedLoc, Material finalMaterial, Block bX, Block bZ, int mod, boolean particles,
			Shape shape) {
		if (bX != null)
			if (bX.getType().toString().contains("RAIL")) {
				Rail bXRail = (Rail) bX.getBlockData();

				if (bXRail.getShape() == clickedRail.getShape()) {
					bXRail.setShape(shape);
					bX.setBlockData(bXRail);
					bX.setType(finalMaterial);
					Block bXFar = bX.getLocation().clone().add(mod, 0, 0).getBlock();
					if (bXFar != null)
						if (bXFar.getType().toString().contains("RAIL")) {
							Rail bXFarRail = (Rail) bXFar.getLocation().getBlock().getBlockData();
							bXRail.setShape(shape);
							bXFar.setBlockData(bXFarRail);
							bXFar.setType(finalMaterial);
							if (particles)
								bXFar.getWorld().spawnParticle(Particle.END_ROD, bXFar.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1,
										0.05);
						}
					// particles (useless, but it's good :33)
					if (particles)
						bX.getWorld().spawnParticle(Particle.END_ROD, bX.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
					return true;
				}
			}
		if (bZ != null)
			if (bZ.getType().toString().contains("RAIL")) {
				Rail bZRail = (Rail) bZ.getBlockData();

				if (bZRail.getShape() == clickedRail.getShape()) {
					bZRail.setShape(shape);
					bZ.setBlockData(bZRail);
					bZ.setType(finalMaterial);
					Block bZFar = bZ.getLocation().clone().add(0, 0, mod).getBlock();
					if (bZFar != null)
						if (bZFar.getType().toString().contains("RAIL")) {
							Rail bZFarRail = (Rail) bZFar.getLocation().getBlock().getBlockData();
							bZRail.setShape(shape);
							bZFar.setBlockData(bZFarRail);
							bZFar.setType(finalMaterial);
							if (particles)
								bZFar.getWorld().spawnParticle(Particle.END_ROD, bZFar.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1,
										0.05);
						}
					// particles (useless, but it's good :33)
					if (particles)
						bZ.getWorld().spawnParticle(Particle.END_ROD, bZ.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
					return true;
				}
			}
		return false;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		// Editor
		if (plugin.editor.containsKey(player)) {
			PlayerEditor pe = plugin.editor.get(player);
			int phase = pe.getPhase();
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (e.getClickedBlock().getType().toString().contains("RAIL")) {
					Location clickedLoc = e.getClickedBlock().getLocation();
					// Rail object
					Rail clickedRail = (Rail) e.getClickedBlock().getBlockData();
					Shape clickedShape = clickedRail.getShape();
					// Setting station block phase
					if (phase == 12) {
						e.setCancelled(true);
						if (plugin.stlocEditor.containsKey(player)) {
							ArrayList<Location> startcrea = plugin.stlocEditor.get(player);
							Location stLoc = startcrea.get(0);
							if ((e.getClickedBlock().getType() == Material.POWERED_RAIL
									|| e.getClickedBlock().getType() == Material.DETECTOR_RAIL) && stLoc.distance(clickedLoc) <= 2) {
								// Remove coloration
								Material rails = Material.RAIL;
								Block bX = new Location(stLoc.getWorld(), stLoc.clone().getX() - 1, stLoc.getY(), stLoc.getZ()).getBlock();
								Block bZ = new Location(stLoc.getWorld(), stLoc.getX(), stLoc.getY(), stLoc.clone().getZ() - 1).getBlock();
								Block bX2 = new Location(stLoc.getWorld(), stLoc.clone().getX() + 1, stLoc.getY(), stLoc.getZ()).getBlock();
								Block bZ2 = new Location(stLoc.getWorld(), stLoc.getX(), stLoc.getY(), stLoc.clone().getZ() + 1).getBlock();
								Shape shape = ((Rail) stLoc.getBlock().getBlockData()).getShape();
								changeNextRails(clickedRail, stLoc, rails, bX, bZ, 2, false, shape);
								changeNextRails(clickedRail, stLoc, rails, bX2, bZ2, -2, false, shape);
								changeNextRails(clickedRail, stLoc, rails, bX, bZ, 2, false, shape);
								changeNextRails(clickedRail, stLoc, rails, bX2, bZ2, -2, false, shape);

								stLoc.getBlock().setType(Material.RAIL);
								stLoc.getBlock().setType(Material.DETECTOR_RAIL);

								// Activate direction track
								player.sendMessage(" ");
								if (e.getClickedBlock().getType() == Material.DETECTOR_RAIL) {
									player.sendMessage("§aSet this point as terminus.");
								} else {
									player.sendMessage("§aAdded direction point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o"
											+ clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
									BlockData stLocBlockData = stLoc.getBlock().getBlockData();
									shape = ((Rail) stLoc.getBlock().getBlockData()).getShape();
									((Rail) stLocBlockData).setShape(shape);
									e.getClickedBlock().setBlockData(stLocBlockData);
									e.getClickedBlock().setType(rails);
								}
								// Add direction track
								startcrea.add(1, clickedLoc);
								plugin.stlocEditor.put(player, startcrea);
								// particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								// Next phase
								pe.setPhase(13);
								// Messages
								player.sendMessage("§7§l§m-----");
								plugin.getEditorMessages().sendEditorMessage(player, 13);
								return;
							}
							player.sendMessage("§cThis direction track isn't valid.");
							return;
						}
						return;
					} else if (phase == 11) {
						e.setCancelled(true);
						Block bX = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX() - 1, clickedLoc.getY(), clickedLoc.getZ())
								.getBlock();
						Block bZ = new Location(clickedLoc.getWorld(), clickedLoc.getX(), clickedLoc.getY(), clickedLoc.clone().getZ() - 1)
								.getBlock();
						Block bX2 = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX() + 1, clickedLoc.getY(), clickedLoc.getZ())
								.getBlock();
						Block bZ2 = new Location(clickedLoc.getWorld(), clickedLoc.getX(), clickedLoc.getY(), clickedLoc.clone().getZ() + 1)
								.getBlock();
						// if track direction coloration worked station is valid :D
						if (changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX, bZ, 2, true, clickedShape)
								|| changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX2, bZ2, -2, true, clickedShape)) {
							if (!plugin.stlocEditor.containsKey(player) || pe.getStation().getStarts().size() == 0) {
								plugin.stlocEditor.remove(player);
								// Saving station track
								ArrayList<Location> startcrea = new ArrayList<Location>();
								startcrea.add(0, clickedLoc);
								plugin.stlocEditor.put(player, startcrea);
								Location stLoc = startcrea.get(0);
								// set station to detector rail
								BlockData stLocBlockData = stLoc.getBlock().getBlockData();
								Shape shape = ((Rail) stLoc.getBlock().getBlockData()).getShape();
								((Rail) stLocBlockData).setShape(shape);
								e.getClickedBlock().setBlockData(stLocBlockData);
								stLoc.getBlock().setType(Material.RAIL);
								stLoc.getBlock().setType(Material.DETECTOR_RAIL);
								// particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								// Messages
								player.sendMessage(" ");
								player.sendMessage("§aAdded station point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY()
										+ "§f, §7§o" + clickedLoc.getZ() + "§f)");
								player.sendMessage("§7§l§m-----");
								plugin.getEditorMessages().sendEditorMessage(player, 12);
								// New phase
								pe.setPhase(12);
								return;
							}
						}
						return;
					}
					return;
				}
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (phase == 11 || phase == 12)
					player.sendMessage("§bBad click! We expected a §bleft click§c ;)");
				if (phase == 15) {
					e.setCancelled(true);
					if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
						return;
					Station station = pe.getStation();

					// Setting button in front of clicked face
					Location clickedLoc = e.getClickedBlock().getLocation();

					BlockFace clickedFace = e.getBlockFace();
					Block button = clickedLoc.getBlock().getRelative(clickedFace);

					// Checking nearby starts
					int count = 0;
					for (Location starts : station.getStartLocations()) {
						if (starts.distance(button.getLocation()) <= 10) {
							count++;
						}
					}

					// No nearby starts
					if (count == 0) {
						player.sendMessage("§4§l!§c There aren't nearby minecart starts §o(< 10m)§c at this location.");
						return;
					}
					// Setting button in world
					button.setType(Material.valueOf("OAK_BUTTON"));
					Directional b = (Directional) button.getBlockData();
					b.setFacing(clickedFace);
					button.setBlockData(b);

					// Station button adding
					station.addButton(button.getLocation());

					// particles (useless, but it's good :33)
					clickedLoc.getWorld().spawnParticle(Particle.FLAME, button.getLocation().add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);

					// phase
					pe.setPhase(16);

					// messages
					player.sendMessage(" ");
					player.sendMessage("§aAdded departure button: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o"
							+ clickedLoc.getZ() + "§f)");
					player.sendMessage("§7§l§m-----");
					plugin.getEditorMessages().sendEditorMessage(player, 16);
					return;
				}
			}
		}
		// Click button destination
		else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location buttonLoc = e.getClickedBlock().getLocation();
			if (Tag.BUTTONS.isTagged(e.getClickedBlock().getType())) {
				if (e.getClickedBlock().getType().equals(Material.OAK_BUTTON))
					if (plugin.getFileManager().isStationButton(buttonLoc)) {
						if (player.hasPermission("minetrain.use")) {
							Station station = plugin.getFileManager().getStationFromButton(buttonLoc);

							// gettint starts by lines
							HashMap<Line, ArrayList<Location>> startsByLine = new HashMap<Line, ArrayList<Location>>();
							for (Location startloc : station.getStartLocations()) {
								ArrayList<Location> startlocs = new ArrayList<Location>();
								Line line = plugin.getFileManager().getLine(station.getStartLineId(startloc));
								if (startsByLine.containsKey(line))
									startlocs = startsByLine.get(line);
								startlocs.add(startloc);
								startsByLine.put(line, startlocs);
							}

							// If just one direction
							int nbstarts = station.getStartLocations().size();
							Location firstartloc = station.getStartLocations().get(0);
							if (nbstarts == 1 && firstartloc.distance(station.getStartDirectionLocation(firstartloc)) != 0) {
								player.sendMessage("§eStation§b " + station.getName());
								player.performCommand("minetrain launchfrom "
										+ plugin.getFileUtils().locationToString(station.getStartLocations().get(0)));
								return;
							}

							// Displaying destinations by lines
							player.sendMessage(
									plugin.getConfig().getString("messages.station_destinations").replace("%station%", station.getName()));
							// player.sendMessage("§eStation§b " + station.getName() + "§e - Destinations");
							boolean warn_linedestinations = false;
							int count = 0;
							for (Line line : startsByLine.keySet()) {
								for (Location start : startsByLine.get(line)) {
									if (player.getLocation().distance(start) <= 10 && station.getStartDirection(start) != null) {
										count++;
										String nextstation = "§o?";
										if (plugin.getLinesMap().getNextStop(start) != null) {
											nextstation = plugin.getLinesMap().getNextStop(start).getName();
										} else {
											warn_linedestinations = true;
										}
										int distanceToPlayer = (int) start.distance(player.getLocation());
										String destionations = plugin.getConfig().getString("messages.destinations")
												.replace("%line_acronym%", line.getAcronym()).replace("%line_name%", line.getName())
												.replace("%distance%", distanceToPlayer + "").replace("%nextstop%", nextstation);
										/*
										 * line.getAcronym() + "§ain direction of §f" + nextstation + " §7§o(" +
										 * distanceToPlayer + " m)"
										 */
										new FancyMessage(" §").color(ChatColor.GOLD).then(" ").then(destionations).color(ChatColor.GOLD)
												.tooltip(plugin.getConfig().getString("messages.direction_select_tooltip"))
												.command("/minetrain launchfrom " + plugin.getFileUtils().locationToString(start))
												.send(player);
									}
								}
							}

							// Warns
							if (warn_linedestinations)
								player.sendMessage("§c§l!§6 Some directions are missing because nobody take it for now.");
							if (count == 0)
								player.sendMessage("§9§l!§b This station has no departures.");
							return;
						}
						player.sendMessage("§cSorry! You don't have permission to do that :(");
						return;
					}
			}
		}
	}

}