package me.stumper66.slimespawners;

import me.lokka30.microlib.other.VersionUtils;
import me.stumper66.slimespawners.listener.SpawnerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnerProcessor {
    public SpawnerProcessor(final SlimeSpawners main) {
        this.main = main;
        this.lastSpawnTimes = new HashMap<>();
    }

    private final SlimeSpawners main;
    public SpawnerOptions options;
    private final Map<Location, SpawnerInfo> lastSpawnTimes;
    private final static int ticksPerCall = 10;

    public void loopThruSpawners(){
        if (main.slimeSpawners.isEmpty()) return;
        if (Bukkit.getOnlinePlayers().size() == 0) return;
        if (options == null) return;

        for (final CreatureSpawner cs : main.slimeSpawners.values()) {
            if (shouldSpawnerSpawnNow(cs)) {
                spawnSlimes(cs);

                lastSpawnTimes.get(cs.getLocation()).resetTimeLeft(options);
            }
        }
   }

   private boolean shouldSpawnerSpawnNow(final @NotNull CreatureSpawner cs){
       SpawnerInfo info;

        if (!this.lastSpawnTimes.containsKey(cs.getLocation())){
            info = new SpawnerInfo(cs, options);
            this.lastSpawnTimes.put(cs.getLocation(), info);
        }
        else {
            info = this.lastSpawnTimes.get(cs.getLocation());
            info.delayTimeLeft -= ticksPerCall;
        }

        return info.delayTimeLeft <= 0;
   }

   private void makeParticles(final @NotNull Location location){
        if (location.getWorld() == null) return;

        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 1, null);
        location.getWorld().spawnParticle(Particle.FLAME, location, 1, null);
   }

   private void spawnSlimes(final @NotNull CreatureSpawner cs){
        int slimeCount = 0;
        boolean foundPlayer = false;

        for (final Entity entity : cs.getWorld().getNearbyEntities(cs.getLocation(), options.playerRequiredRange, options.playerRequiredRange, options.playerRequiredRange)){
            if (entity instanceof Slime)
                slimeCount++;
            else if (entity instanceof Player)
                foundPlayer = true;
        }

        if (!foundPlayer) {
            //Utils.logger.info("no player found");
            return;
        }
        if (slimeCount >= options.maxNearbySlimes) {
            //Utils.logger.info("too many slimes - " + slimeCount);
            return;
        }

        for (int i = 0; i < options.spawnCount; i++) {
            final Location spawnLocation = getSpawnLocation(cs.getLocation());
            if (spawnLocation == null)
                continue;

            // if you're running spigot then they will spawn in with default spawn reason
            // if running paper they will spawn in with spawner spawn reason
            final Entity entity = VersionUtils.isRunningPaper() ?
                cs.getWorld().spawnEntity(spawnLocation, EntityType.SLIME, CreatureSpawnEvent.SpawnReason.SPAWNER) :
                SpigotCompat.spawnSlime(spawnLocation);

            makeParticles(entity.getLocation());

            slimeCount++;
            if (slimeCount >= options.maxNearbySlimes) break;
        }
    }

    @Nullable
    private Location getSpawnLocation(final @NotNull Location spawnerLocation){
        if (spawnerLocation.getWorld() == null) return null;

        final World world = spawnerLocation.getWorld();
        final int x1 = spawnerLocation.getBlockX();
        final int y1 = spawnerLocation.getBlockY();
        final int z1 = spawnerLocation.getBlockZ();
        final List<Block> blockCandidates = new LinkedList<>();

        for (int i = 0; i < 50; i++) {
            int i0 = (int) (x1 + ThreadLocalRandom.current().nextDouble() - ThreadLocalRandom.current().nextDouble() * (double) options.spawnRange + 0.5D);
            int i1 = y1 + ThreadLocalRandom.current().nextInt(options.spawnRange) - 1;
            int i2 = (int) (z1 + (ThreadLocalRandom.current().nextDouble()) - ThreadLocalRandom.current().nextDouble() * (double) options.spawnRange + 0.5D);
            final Block block = world.getBlockAt(i0, i1, i2);
            if (    options.allowAirSpawning && block.getType().isAir() ||
                    block.getType().isSolid() && !blockCandidates.contains(block)) {
                blockCandidates.add(block);
            }

            if (blockCandidates.size() >= 6) break;
        }

        if (blockCandidates.size() == 0)
            return null;

        Collections.shuffle(blockCandidates);
        for (final Block block : blockCandidates){
            if (world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).getType().isAir() &&
                    world.getBlockAt(block.getX(), block.getY() + 2, block.getZ()).getType().isAir())
                return block.getLocation().add(0, 1, 0);
        }

        return null;
    }
}
