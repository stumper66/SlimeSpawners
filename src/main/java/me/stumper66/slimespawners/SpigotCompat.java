package me.stumper66.slimespawners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class SpigotCompat {
    public static @NotNull Entity spawnSlime(final @NotNull Location location){
        return location.getWorld().spawnEntity(location, EntityType.SLIME);
    }
}
