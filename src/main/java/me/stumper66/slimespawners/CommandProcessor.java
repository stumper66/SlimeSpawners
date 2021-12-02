package me.stumper66.slimespawners;

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
    private CommandSender sender;
    private String commandLabel;

    @Override
    public boolean onCommand(final @NotNull CommandSender commandSender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        this.sender = commandSender;
        this.commandLabel = label;

        if (args.length == 0){
            showSyntax();
            return true;
        }

        switch (args[0].toLowerCase()){
            case "reload":
                doReload();
                break;
            default:
                showSyntax();
                break;
        }

        return true;
    }

    private void showSyntax(){
        sender.sendMessage("Syntax: " + commandLabel + " reload");
    }

    private void doReload(){
        main.loadConfig();
        sender.sendMessage("Reload complete");
    }

    @Nullable
    @Override
    public List<String> onTabComplete(final @NotNull CommandSender commandSender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return List.of("reload");

        return Collections.emptyList();
    }
}
