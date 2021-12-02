package me.stumper66.slimespawners;

import me.lokka30.microlib.messaging.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CommandProcessor implements CommandExecutor, TabCompleter {

    public CommandProcessor(final SlimeSpawners main){
        this.main = main;
    }
    private final SlimeSpawners main;

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 0){
            showSyntax(sender, label);
            return true;
        }

        switch (args[0].toLowerCase()){
            case "reload":
                doReload(sender);
                break;
            case "info":
                showInfo(sender);
                break;
            default:
                showSyntax(sender, label);
                break;
        }

        return true;
    }

    private void showSyntax(@NotNull final CommandSender sender, @NotNull final String label) {
        sender.sendMessage(MessageUtils.colorizeAll("&b&lSlimeSpawners: &7Syntax: &b/" + label + " reload | info"));
    }

    private void showInfo(@NotNull final CommandSender sender) {
        final String version = main.getDescription().getVersion();
        final String description = main.getDescription().getDescription();

        sender.sendMessage(MessageUtils.colorizeAll("\n" +
                "&b&lSlimeSpawners &fv" + version + "&r\n" +
                "&7&o" + description + "&r\n" +
                "&7Created by Stumper66"));
    }

    private void doReload(@NotNull final CommandSender sender) {
        if (!sender.hasPermission("slimespawners.reload")){
            sender.sendMessage(MessageUtils.colorizeAll("&b&lSlimeSpawners: &7You don't have permissions to run this command"));
            return;
        }

        sender.sendMessage(MessageUtils.colorizeAll("&b&lSlimeSpawners: &7Reloading config..."));
        main.loadConfig();
        sender.sendMessage(MessageUtils.colorizeAll("&b&lSlimeSpawners: &7Reload complete."));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(final @NotNull CommandSender commandSender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return List.of("reload", "info");

        return Collections.emptyList();
    }
}
