package com.codingguru.trailpaths.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.handlers.PathHandler;
import com.codingguru.trailpaths.utils.MessagesUtil;

public class Toggle extends SubCmd {

	public Toggle() {
		super();
		addAlias("toggle");
		addAlias("on");
		addAlias("enabled");
		addAlias("enable");
		addAlias("off");
		addAlias("disabled");
		addAlias("disable");
	}

	public void performCommand(CommandSender sender, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			MessagesUtil.sendMessage(sender, MessagesUtil.IN_GAME_ONLY.toString());
			return;
		}

		Player player = (Player) sender;

		boolean isPathDisabled = PathHandler.getInstance().isPathDisabled(player.getUniqueId());

		if (args.length == 0) {
			togglePath(player, isPathDisabled);
			return;
		}

		String alias = args[0];

		if (alias.equalsIgnoreCase("enabled") || alias.equalsIgnoreCase("enable") || alias.equalsIgnoreCase("on")) {
			togglePath(player, true);
		} else if (alias.equalsIgnoreCase("disabled") || alias.equalsIgnoreCase("disable")
				|| alias.equalsIgnoreCase("off")) {
			togglePath(player, false);
		} else {
			togglePath(player, isPathDisabled);
		}
	}

	private void togglePath(Player player, boolean isPathDisabled) {
		PathHandler INSTANCE = PathHandler.getInstance();

		if (isPathDisabled) {
			INSTANCE.enablePath(player.getUniqueId());
		} else {
			INSTANCE.disablePath(player.getUniqueId());
		}

		MessagesUtil.sendMessage(player,
				isPathDisabled ? MessagesUtil.TOGGLE_PATH_ON.toString() : MessagesUtil.TOGGLE_PATH_OFF.toString());
	}

	public boolean isValidArgumentLength(int length) {
		return length == 1 || length == 2;
	}

	public String getHelp() {
		return "/trails [on | off]";
	}

	public String getPermission() {
		return "trails.toggle";
	}

	public String getDescription() {
		return "Toggle your trail on or off.";
	}

}