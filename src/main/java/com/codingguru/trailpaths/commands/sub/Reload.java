package com.codingguru.trailpaths.commands.sub;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.handlers.PathHandler;
import com.codingguru.trailpaths.utils.MessagesUtil;

public class Reload extends SubCmd {

	public Reload() {
		super();
		addAlias("rl");
		addAlias("reload");
	}

	public void performCommand(CommandSender sender, String[] args) {
		TrailPaths.getInstance().getSettingsManager().setup(TrailPaths.getInstance());
		TrailPaths.getInstance().reloadConfig();
		PathHandler.getInstance().resetMaterials();
		MessagesUtil.sendMessage(sender, MessagesUtil.RELOAD.toString());
	}

	public boolean isValidArgumentLength(int length) {
		return length == 1;
	}

	public String getHelp() {
		return "/trails reload";
	}

	public String getPermission() {
		return "trails.reload";
	}

	public String getDescription() {
		return "Reload the configuration files.";
	}

}