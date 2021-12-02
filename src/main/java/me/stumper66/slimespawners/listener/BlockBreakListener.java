package me.stumper66.slimespawners.listener;

import me.stumper66.slimespawners.SlimeSpawners;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {

    public BlockBreakListener(@NotNull final SlimeSpawners main){
        this.main = main;
    }
    private final SlimeSpawners main;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockBreakEvent(final @NotNull BlockBreakEvent event){
        if (event.getBlock().getType() != Material.SPAWNER) return;

        main.slimeSpawners.remove(event.getBlock().getLocation());
    }
}
