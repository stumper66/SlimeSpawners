package me.stumper66.slimespawners.Listeners;

import me.stumper66.slimespawners.SlimeSpawners;
import me.stumper66.slimespawners.Utils;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {
    public BlockPlaceListener(final SlimeSpawners main){
        this.main = main;
    }

    private final SlimeSpawners main;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockPlaceEvent(final @NotNull BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() != Material.SPAWNER) return;

        final CreatureSpawner cs = (CreatureSpawner) event.getBlockPlaced().getState();
        if (cs.getSpawnedType() == EntityType.SLIME && !main.slimeSpawners.containsKey(cs.getLocation()))
            main.slimeSpawners.put(cs.getLocation(), cs);

        Utils.logger.info("recorded the new spawner");
    }
}
