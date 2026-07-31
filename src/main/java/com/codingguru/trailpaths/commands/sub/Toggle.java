package com.codingguru.trailpaths.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.commands.SubCmd;
import com.codingguru.trailpaths.handlers.PathHandler;
import com.codingguru.trailpaths.util.LangDefaults;
import com.codingguru.trailpaths.util.MessageBuilder;

public class Toggle extends SubCmd {

	public Toggle(TrailPaths plugin) {
		super(plugin);
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
			new MessageBuilder.Builder("in-game-only", LangDefaults.IN_GAME_ONLY).send(sender);
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

		if (isPathDisabled) {
			new MessageBuilder.Builder("toggle-path-on", LangDefaults.TOGGLE_PATH_ON).send(player);
		} else {
			new MessageBuilder.Builder("toggle-path-off", LangDefaults.TOGGLE_PATH_OFF).send(player);
		}
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