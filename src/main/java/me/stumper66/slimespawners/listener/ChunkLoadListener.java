package me.stumper66.slimespawners.listener;

import me.stumper66.slimespawners.SlimeSpawners;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadListener implements Listener {
    public ChunkLoadListener(final SlimeSpawners main){
        this.main = main;
    }

    private final SlimeSpawners main;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onChunkLoad(final @NotNull ChunkLoadEvent event){
        final World world = event.getWorld();
        final int yMin = world.getMinHeight();
        final int yMax = world.getMaxHeight();
        final int cx = event.getChunk().getX() << 4;
        final int cz = event.getChunk().getZ() << 4;

        for (int x = cx; x < cx + 16; x++) {
            for (int z = cz; z < cz + 16; z++) {
                for (int y = yMin; y <= yMax; y++) {
                    if (world.getBlockAt(x, y, z).getType() != Material.SPAWNER) continue;

                    final CreatureSpawner cs = (CreatureSpawner) world.getBlockAt(x, y, z).getState();
                    if (cs.getSpawnedType() == EntityType.SLIME && !main.slimeSpawners.containsKey(cs.getLocation()))
                        main.slimeSpawners.put(cs.getLocation(), cs);
                }
            }
        }
    }
}
