package me.stumper66.slimespawners;

import java.lang.reflect.Field;

public class SpawnerOptions {
    public SpawnerOptions(){
        // these are the defaults
        this.playerRequiredRange = 16;
        this.minSpawnDelay = 200;
        this.maxSpawnDelay = 800;
        this.spawnCount = 1;
        this.spawnRange = 4;
        this.maxNearbySlimes = 6;
        this.delay = 20;
    }

    public int playerRequiredRange;
    public int minSpawnDelay;
    public int maxSpawnDelay;
    public int maxNearbySlimes;
    public int spawnCount;
    public int spawnRange;
    public int delay;
    public boolean allowAirSpawning;

    public String toString(){
        final StringBuilder sb = new StringBuilder();

        try {
            for (final Field f : this.getClass().getDeclaredFields()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(f.getName());
                sb.append(": ");
                sb.append(f.get(this));
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
            return super.toString();
        }

        return sb.toString();
    }
}
