package com.codingguru.trailpaths.commands.sub;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.utils.MessagesUtil;

public class Help extends SubCmd {

	private final ArrayList<SubCmd> subCommands;

	public Help(ArrayList<SubCmd> subCommands) {
		super();
		addAlias("help");
		addAlias("?");
		this.subCommands = subCommands;
	}

	public void performCommand(CommandSender sender, String[] args) {
		MessagesUtil.sendMessage(sender, MessagesUtil.HELP_TITLE.toString());

		for (SubCmd command : subCommands) {
			MessagesUtil.sendMessage(sender, MessagesUtil.HELP_COMMAND.toString()
					.replaceAll("%command%", command.getHelp()).replaceAll("%description%", command.getDescription()));
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