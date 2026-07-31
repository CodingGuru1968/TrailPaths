package com.codingguru.trailpaths;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.codingguru.trailpaths.commands.TrailPathsCmd;
import com.codingguru.trailpaths.handlers.ManagerHandler;
import com.codingguru.trailpaths.handlers.PathHandler;
import com.codingguru.trailpaths.listeners.PlayerMove;
import com.codingguru.trailpaths.listeners.PlayerQuit;
import com.codingguru.trailpaths.managers.LanguageManager;
import com.codingguru.trailpaths.util.ConsoleUtil;
import com.codingguru.trailpaths.util.ServerTypeUtil;
import com.tchristofferson.configupdater.ConfigUpdater;

public class TrailPaths extends JavaPlugin {

	private static TrailPaths INSTANCE;
	private ServerTypeUtil serverType;

	public void onEnable() {
		INSTANCE = this;

		setupServerType();

		ConsoleUtil.sendPluginSetup();

		saveDefaultConfig();

		try {
			ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"), "paths");
		} catch (IOException e) {
			e.printStackTrace();
		}

		reloadConfig();

		registerManagers();

		ManagerHandler.getInstance().startAll();

		registerHooksAndListeners();
	}

	public void onDisable() {
		ManagerHandler.getInstance().stopAll();
	}

	public void reload() {
		ManagerHandler.getInstance().stopAll();
		reloadConfig();
		PathHandler.getInstance().resetMaterials();
		ManagerHandler.getInstance().startAll();
	}

	private void registerManagers() {
		ManagerHandler managerRegistry = ManagerHandler.getInstance();
		managerRegistry.register(LanguageManager.class, new LanguageManager(this));
	}

	private void registerHooksAndListeners() {
		getCommand("trails").setExecutor(new TrailPathsCmd(this));
		getCommand("trailpaths").setExecutor(new TrailPathsCmd(this));
		getCommand("paths").setExecutor(new TrailPathsCmd(this));
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
		getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
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

	public static TrailPaths getInstance() {
		return INSTANCE;
	}

}