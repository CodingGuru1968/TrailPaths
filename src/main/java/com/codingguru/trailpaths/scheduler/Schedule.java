package com.codingguru.trailpaths.scheduler;

import org.bukkit.Bukkit;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.util.ServerTypeUtil;

public abstract class Schedule implements Runnable {

	protected final TrailPaths plugin;
	private final boolean USING_FOLIA;

	public Schedule(TrailPaths plugin) {
		this.plugin = plugin;
		this.USING_FOLIA = plugin.getServerType() == ServerTypeUtil.FOLIA;
	}
	
	public void runTask() {
		if (USING_FOLIA) {
			Bukkit.getGlobalRegionScheduler().execute(plugin, this);
		} else {
			Bukkit.getScheduler().runTask(plugin, this);
		}
	}

	public void runTaskLater(long delay) {
		if (USING_FOLIA) {
			Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> this.run(), delay);
		} else {
			Bukkit.getScheduler().runTaskLater(plugin, this, delay);
		}
	}
	
}