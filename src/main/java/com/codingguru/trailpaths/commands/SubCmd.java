package com.codingguru.trailpaths.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.codingguru.trailpaths.utils.MessagesUtil;

public abstract class SubCmd {

	private List<String> identifiers;

	protected SubCmd() {
		this.identifiers = new ArrayList<>();
	}

	protected abstract void performCommand(CommandSender paramCommandSender, String[] paramArrayOfString);

	protected abstract boolean isValidArgumentLength(int paramInt);

	protected abstract String getPermission();

	public abstract String getHelp();

	public abstract String getDescription();

	protected boolean hasPermission(CommandSender sender) {
		if (sender.isOp() || sender.hasPermission(getPermission()) || sender.hasPermission("TRAILS.*")
				|| sender.hasPermission("TRAILS.*"))
			return true;
		return false;
	}

	protected void sendIncorrectUsage(CommandSender sender) {
		MessagesUtil.sendMessage(sender, MessagesUtil.INCORRECT_USAGE.toString().replaceAll("%command%", getHelp()));
	}

	protected void addAlias(String alias) {
		this.identifiers.add(alias);
	}

	protected List<String> getIdentifiers() {
		return this.identifiers;
	}
}