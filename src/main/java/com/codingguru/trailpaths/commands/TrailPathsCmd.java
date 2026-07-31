package com.codingguru.trailpaths.commands;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.codingguru.trailpaths.TrailPaths;
import com.codingguru.trailpaths.commands.sub.Help;
import com.codingguru.trailpaths.commands.sub.Reload;
import com.codingguru.trailpaths.commands.sub.Toggle;
import com.codingguru.trailpaths.commands.sub.Version;
import com.codingguru.trailpaths.util.LangDefaults;
import com.codingguru.trailpaths.util.MessageBuilder;

public class TrailPathsCmd implements CommandExecutor {

	private final ArrayList<SubCmd> subCommands;

	public TrailPathsCmd(TrailPaths plugin) {
		this.subCommands = new ArrayList<>();
		this.subCommands.add(new Reload(plugin));
		this.subCommands.add(new Toggle(plugin));
		this.subCommands.add(new Version(plugin));
		this.subCommands.add(new Help(plugin, subCommands));
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if (args.length >= 1) {
			SubCmd subCommand = null;

			for (SubCmd cmd : this.subCommands) {
				if (cmd.getIdentifiers().contains(args[0].toLowerCase())) {
					subCommand = cmd;
					break;
				}
			}

			if (subCommand != null) {
				if (!subCommand.isValidArgumentLength(args.length)) {
					subCommand.sendIncorrectUsage(commandSender);
					return true;
				}

				if (commandSender instanceof Player) {
					Player player = (Player) commandSender;

					if (!subCommand.hasPermission(player)) {
						new MessageBuilder.Builder("no-permission", LangDefaults.NO_PERMISSION).send(player);
						return false;
					}

					subCommand.performCommand((CommandSender) player, args);
					return true;
				}
				subCommand.performCommand(commandSender, args);
				return true;
			}
		}

		Optional<SubCmd> subCommand = subCommands.stream()
				.filter(paramSubcommand -> paramSubcommand.getIdentifiers().contains("help")).findAny();

		if (subCommand.isPresent()) {
			SubCmd helpCommand = subCommand.get();

			if (commandSender instanceof Player) {
				Player player = (Player) commandSender;
				if (!helpCommand.hasPermission(player)) {
					new MessageBuilder.Builder("no-permission", LangDefaults.NO_PERMISSION).send(player);
					return false;
				}
			}

			helpCommand.performCommand(commandSender, args);
		}

		return false;
	}

	protected ArrayList<SubCmd> getAllCommands() {
		return subCommands;
	}
}