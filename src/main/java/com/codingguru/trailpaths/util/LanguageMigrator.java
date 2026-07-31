package com.codingguru.trailpaths.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.codingguru.trailpaths.TrailPaths;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LanguageMigrator {

	private final static TrailPaths PLUGIN = TrailPaths.getInstance();
	
    public static void migrateAndDeleteOldFile(File pluginFolder, Logger logger) {
        File oldLegacyFile = new File(PLUGIN.getDataFolder(), "lang.yml");

        if (oldLegacyFile.exists()) {
            logger.info("Found legacy TrailPaths language file. Migrating values before deletion...");
            
            FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(oldLegacyFile);
            File newLangFile = new File(pluginFolder, "lang.yml");
            FileConfiguration newConfig = YamlConfiguration.loadConfiguration(newLangFile);

            Map<String, String> migrationMap = new HashMap<>();
            migrationMap.put("HELP_TITLE", "help-title");
            migrationMap.put("HELP_COMMAND", "help-command");
            migrationMap.put("TOGGLE_PATH_ON", "toggle-path-on");
            migrationMap.put("TOGGLE_PATH_OFF", "toggle-path-off");
            migrationMap.put("RELOAD", "reload");
            migrationMap.put("NO_PERMISSION", "no-permission");
            migrationMap.put("IN_GAME_ONLY", "in-game-only");
            migrationMap.put("INCORRECT_USAGE", "incorrect-usage");

            boolean migrated = false;
            
            for (Map.Entry<String, String> entry : migrationMap.entrySet()) {
                if (oldConfig.contains(entry.getKey()) && !newConfig.contains(entry.getValue())) {
                    newConfig.set(entry.getValue(), oldConfig.get(entry.getKey()));
                    migrated = true;
                }
            }

            if (migrated) {
                try {
                    newConfig.save(newLangFile);
                    logger.info("Successfully migrated old TrailPaths values into the new format.");
                } catch (IOException e) {
                    logger.severe("Failed to save the new TrailPaths language file during migration!");
                    e.printStackTrace();
                }
            }

            if (oldLegacyFile.delete()) {
                logger.info("Successfully deleted obsolete TrailPaths legacy file.");
            } else {
                logger.warning("Could not delete old TrailPaths legacy file.");
            }
        }
    }
}