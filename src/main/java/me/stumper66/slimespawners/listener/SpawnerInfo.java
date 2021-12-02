package me.stumper66.slimespawners.listener;

import me.stumper66.slimespawners.SpawnerOptions;
import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class SpawnerInfo {

    public SpawnerInfo(final @NotNull CreatureSpawner cs, final @NotNull SpawnerOptions options){
        this.cs = cs;
        resetTimeLeft(options);
    }

    public int delayTimeLeft;
    public final @NotNull CreatureSpawner cs;

    public void resetTimeLeft(final @NotNull SpawnerOptions options){
        this.delayTimeLeft = options.delay;
        this.delayTimeLeft += options.maxSpawnDelay <= options.minSpawnDelay ?
                options.minSpawnDelay :
                options.maxSpawnDelay + ThreadLocalRandom.current().nextInt(Math.max(options.maxSpawnDelay - options.minSpawnDelay, 1));
    }

    public String toString() {
        return "Time left: " + delayTimeLeft;
    }
}
