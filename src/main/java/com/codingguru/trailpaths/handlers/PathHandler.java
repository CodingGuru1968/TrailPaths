package com.codingguru.trailpaths.handlers;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.util.ConsoleUtil;
import com.codingguru.trailpaths.util.WeightedRandomUtil;
import com.codingguru.trailpaths.util.XMaterialUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PathHandler {

	private PathHandler() {
		this.plugin = TrailPaths.getInstance();
		this.pathMaterials = Maps.newHashMap();
		this.disabledPaths = Sets.newHashSet();
		resetMaterials();
	}
	
	private final static PathHandler INSTANCE = new PathHandler();
	private Map<Material, WeightedRandomUtil<Material>> pathMaterials;
	private Set<UUID> disabledPaths;
	private final TrailPaths plugin;

	public void resetMaterials() {
		pathMaterials.clear();

		for (String materialName : plugin.getConfig().getConfigurationSection("paths").getKeys(false)) {

			Optional<XMaterialUtil> materialToChange = XMaterialUtil.matchXMaterial(materialName);

			if (!materialToChange.isPresent()) {
				ConsoleUtil.warning("[TrailPaths] Could not add material: " + materialName + " as it does not exist.");
				continue;
			}

			WeightedRandomUtil<Material> materialsToChangeTo = new WeightedRandomUtil<>();

			ConfigurationSection section = plugin.getConfig().getConfigurationSection("paths." + materialName);

			if (section == null) {
				ConsoleUtil.warning("[TrailPaths] Using old config format. Please update to new format.");
				continue;
			}

			for (String changeToName : section.getKeys(false)) {
				Optional<XMaterialUtil> changeMaterialToType = XMaterialUtil.matchXMaterial(changeToName);

				if (!changeMaterialToType.isPresent()) {
					ConsoleUtil
							.warning("[TrailPaths] Could not add material: " + changeToName + " as it does not exist.");
					continue;
				}

				int percentage = plugin.getConfig().getInt("paths." + materialName + "." + changeToName);

				materialsToChangeTo.addEntry(changeMaterialToType.get().parseMaterial(), percentage);
			}

			pathMaterials.put(materialToChange.get().parseMaterial(), materialsToChangeTo);
		}
	}

	public boolean isPathDisabled(UUID uuid) {
		return disabledPaths.contains(uuid);
	}

	public void enablePath(UUID uuid) {
		disabledPaths.remove(uuid);
	}

	public void disablePath(UUID uuid) {
		disabledPaths.add(uuid);
	}

	public boolean contains(Material material) {
		return pathMaterials.containsKey(material);
	}

	public Material getChangedMaterial(Material material) {
		return pathMaterials.get(material).getRandom();
	}

	public static PathHandler getInstance() {
		return INSTANCE;
	}
}