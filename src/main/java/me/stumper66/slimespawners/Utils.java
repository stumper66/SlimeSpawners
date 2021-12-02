package me.stumper66.slimespawners;

import me.lokka30.microlib.messaging.MicroLogger;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Utils {

    @NotNull public static final MicroLogger logger = new MicroLogger("&b&lSlimeSpawners: &7");

    public static double round(final double value) {
        return Math.round(value * 100) / 100.00;
    }

    public static double round(final double value, final int digits) {
        final double scale = Math.pow(10, digits);
        return Math.round(value * scale) / scale;
    }
}
