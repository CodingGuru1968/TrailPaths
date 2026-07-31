package com.codingguru.trailpaths.commands.sub;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.util.LangDefaults;
import com.codingguru.trailpaths.util.MessageBuilder;

public class Version extends SubCmd {

	@SuppressWarnings("deprecation")
	private final String version = plugin.getDescription().getVersion();

	public Version(TrailPaths plugin) {
		super(plugin);
		addAlias("v");
		addAlias("version");
	}

	public void performCommand(CommandSender sender, String[] args) {
		new MessageBuilder.Builder("version", LangDefaults.VERSION).set("%version%", version).send(sender);
	}

	public boolean isValidArgumentLength(int length) {
		return length == 1;
	}

	public String getHelp() {
		return "/trails version";
	}

	public String getPermission() {
		return "trailpaths.version";
	}

	public String getDescription() {
		return "View the version of the plugin.";
	}

}