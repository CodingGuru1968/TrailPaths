package com.codingguru.trailpaths.commands.sub;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.util.LangDefaults;
import com.codingguru.trailpaths.util.MessageBuilder;

public class Help extends SubCmd {

	private final ArrayList<SubCmd> subCommands;

	public Help(TrailPaths plugin, ArrayList<SubCmd> subCommands) {
		super(plugin);
		addAlias("help");
		addAlias("?");
		this.subCommands = subCommands;
	}

	public void performCommand(CommandSender sender, String[] args) {
		new MessageBuilder.Builder("help-title", LangDefaults.HELP_TITLE).send(sender);

		for (SubCmd command : subCommands) {
			new MessageBuilder.Builder("help-entry", LangDefaults.HELP_ENTRY).set("%cmd%", command.getHelp())
					.set("%desc%", command.getDescription()).send(sender);
		}
	}

	public boolean isValidArgumentLength(int length) {
		return length == 1;
	}

	public String getHelp() {
		return "/trails help";
	}

	public String getPermission() {
		return "trails.help";
	}

	public String getDescription() {
		return "View the help menu.";
	}

}