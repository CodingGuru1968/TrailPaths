package com.codingguru.trailpaths.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.api.PluginManager;
import com.codingguru.trailpaths.util.ConsoleUtil;

public final class LanguageManager implements PluginManager {

	private final TrailPaths plugin;
	private FileConfiguration langConfig;
	private File langFile;

	public LanguageManager(TrailPaths plugin) {
		this.plugin = plugin;
	}

	@Override
	public void start() {
		initializeLanguageFile();
	}

	@Override
	public void stop() {
		langFile = null;
		langConfig = null;
	}

	private void initializeLanguageFile() {
		File langDirectory = new File(plugin.getDataFolder(), "lang");

		if (!langDirectory.exists() && !langDirectory.mkdirs()) {
			ConsoleUtil.warning("Failed to create the 'lang' directory.");
		}

		String languageName = plugin.getConfig().getString("language", "en");
		String resourcePath = "lang/" + languageName + ".yml";

		langFile = new File(plugin.getDataFolder(), resourcePath);

		if (!langFile.exists()) {
			try {
				plugin.saveResource(resourcePath, false);
			} catch (IllegalArgumentException ex) {
				ConsoleUtil.info(
						"Language file " + languageName + ".yml not found in plugin jar. Creating a blank file...");
				try {
					langFile.createNewFile();
				} catch (IOException ioException) {
					ConsoleUtil.warning(
							"Could not create language file: " + langFile.getName() + ioException.getMessage());
				}
			}
		}

		migrateAndCleanOldFiles(langFile);

		langConfig = YamlConfiguration.loadConfiguration(langFile);
	}

	public FileConfiguration getLang() {
		return langConfig;
	}

	public void saveLang() {
		if (langConfig == null || langFile == null) {
			return;
		}

		try {
			langConfig.save(langFile);
		} catch (IOException ex) {
			ConsoleUtil.warning("Could not save language file to " + langFile + " : " + ex.getMessage());
		}
	}

	private void migrateAndCleanOldFiles(File activeLangFile) {
		File oldLegacyFile = new File(plugin.getDataFolder(), "lang.yml");

		if (oldLegacyFile.exists()) {
			ConsoleUtil.info("Found legacy TrailPaths language file. Migrating values before deletion...");

			FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldLegacyFile);
			FileConfiguration newConfig = YamlConfiguration.loadConfiguration(activeLangFile);

			Map<String, String> migrationMap = new HashMap<>();
			migrationMap.put("HELP_TITLE", "help-title");
			migrationMap.put("HELP_COMMAND", "help-command");
			migrationMap.put("TOGGLE_PATH_ON", "toggle-path-on");
			migrationMap.put("TOGGLE_PATH_OFF", "toggle-path-off");
			migrationMap.put("RELOAD", "reload");
			migrationMap.put("NO_PERMISSION", "no-permission");
			migrationMap.put("IN_GAME_ONLY", "in-game-only");
			migrationMap.put("INCORRECT_USAGE", "incorrect-usage");
			migrationMap.put("VERSION", "version");

			boolean migrated = false;

			for (Map.Entry<String, String> entry : migrationMap.entrySet()) {
				if (oldConfig.contains(entry.getKey())) {
					newConfig.set(entry.getValue(), oldConfig.get(entry.getKey()));
					migrated = true;
				}
			}

			if (migrated) {
				try {
					newConfig.save(activeLangFile);
					ConsoleUtil.info("Successfully migrated old TrailPaths values into the new format.");
				} catch (IOException e) {
					ConsoleUtil.warning("Failed to save the new TrailPaths language file during migration!");
					e.printStackTrace();
				}
			}

			if (oldLegacyFile.delete()) {
				ConsoleUtil.info("Successfully deleted obsolete TrailPaths legacy file.");
			} else {
				ConsoleUtil.warning("Could not delete old TrailPaths legacy file.");
			}
		}
	}
}