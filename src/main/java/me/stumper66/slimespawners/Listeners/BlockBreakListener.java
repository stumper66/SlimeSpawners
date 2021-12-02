package me.stumper66.slimespawners.Listeners;

import me.stumper66.slimespawners.SlimeSpawners;
import me.stumper66.slimespawners.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreakListener implements Listener {
    public BlockBreakListener(final SlimeSpawners main){
        this.main = main;
    }

    private final SlimeSpawners main;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockBreakEvent(final @NotNull BlockBreakEvent event){
        if (event.getBlock().getType() != Material.SPAWNER) return;

        main.slimeSpawners.remove(event.getBlock().getLocation());
        Utils.logger.info("Removed the spawner if it was present");
    }
}
