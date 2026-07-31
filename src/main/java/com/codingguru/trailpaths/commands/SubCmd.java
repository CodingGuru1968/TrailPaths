package com.codingguru.trailpaths.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.util.LangDefaults;
import com.codingguru.trailpaths.util.MessageBuilder;

public abstract class SubCmd {

	protected final TrailPaths plugin;
	private List<String> identifiers;

	protected SubCmd(TrailPaths plugin) {
		this.plugin = plugin;
		this.identifiers = new ArrayList<>();
	}

	protected abstract void performCommand(CommandSender paramCommandSender, String[] paramArrayOfString);

	protected abstract boolean isValidArgumentLength(int paramInt);

	protected abstract String getPermission();

	public abstract String getHelp();

	public abstract String getDescription();

	protected boolean hasPermission(CommandSender sender) {
		if (sender.isOp() || sender.hasPermission(getPermission()) || sender.hasPermission("trailpaths.*")
				|| sender.hasPermission("trailpaths.*"))
			return true;
		return false;
	}

	protected void sendIncorrectUsage(CommandSender sender) {
		new MessageBuilder.Builder("incorrect-usage", LangDefaults.INCORRECT_USAGE).set("%command%", getHelp())
				.send(sender);
	}

	protected void addAlias(String alias) {
		this.identifiers.add(alias);
	}

	protected List<String> getIdentifiers() {
		return this.identifiers;
	}
}