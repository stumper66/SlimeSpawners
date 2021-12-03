package me.stumper66.slimespawners.listener;

import me.stumper66.slimespawners.SlimeSpawners;
import me.stumper66.slimespawners.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {
    public BlockPlaceListener(final SlimeSpawners main){
        this.main = main;
    }

    private final SlimeSpawners main;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockPlaceEvent(final @NotNull BlockPlaceEvent event){
        if (event.getBlockPlaced().getType() != Material.SPAWNER) return;

        final Block csBlock = event.getBlock();

        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                checkCreatureSpawnerDelayed(csBlock);
            }
        };

        // in the case of LevelledMobs spawners need to do a short delay otherwise
        // it will be detected as a pig spawner
        runnable.runTaskLater(main, 5L);
    }

    private void checkCreatureSpawnerDelayed(final @NotNull Block block){

        final CreatureSpawner cs = (CreatureSpawner) block.getState();
        if (cs.getSpawnedType() == EntityType.SLIME && !main.slimeSpawners.containsKey(cs.getLocation())) {
            main.slimeSpawners.put(cs.getLocation(), cs);
        }
    }
}
