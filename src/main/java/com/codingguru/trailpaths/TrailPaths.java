package com.codingguru.trailpaths;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.trailpaths.commands.TrailPathsCmd;
import com.codingguru.trailpaths.listeners.PlayerMove;
import com.codingguru.trailpaths.listeners.PlayerQuit;
import com.codingguru.trailpaths.managers.SettingsManager;
import com.codingguru.trailpaths.utils.ConsoleUtil;
import com.codingguru.trailpaths.utils.ServerTypeUtil;
import com.tchristofferson.configupdater.ConfigUpdater;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class TrailPaths extends JavaPlugin {

	private static TrailPaths INSTANCE;
	private SettingsManager settingsManager;
	private BukkitAudiences adventureAPI;
	private ServerTypeUtil serverType;

	public void onEnable() {
		INSTANCE = this;

		setupServerType();
		
		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();
		
		try {
			ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		reloadConfig();

		settingsManager = new SettingsManager();
		settingsManager.setup(this);
		
		if (getConfig().getBoolean("use-mini-message", false)) {
			this.adventureAPI = BukkitAudiences.create(this);
		}

		getCommand("trails").setExecutor(new TrailPathsCmd());
		getCommand("trailpaths").setExecutor(new TrailPathsCmd());
		getCommand("paths").setExecutor(new TrailPathsCmd());

		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
		getServer().getPluginManager().registerEvents(new PlayerMove(), this);
	}
	
	private void setupServerType() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
			serverType = ServerTypeUtil.FOLIA;
			return;
		} catch (ClassNotFoundException ignored) {
		}

		try {
			Class.forName("io.papermc.paper.ServerBuildInfo");
			serverType = ServerTypeUtil.PAPER;
			return;
		} catch (ClassNotFoundException ignored) {
		}

		serverType = ServerTypeUtil.SPIGOT;
	}
	
	public ServerTypeUtil getServerType() {
		return serverType;
	}
	
	public BukkitAudiences getAdventure() {
		return this.adventureAPI;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public static TrailPaths getInstance() {
		return INSTANCE;
	}

}