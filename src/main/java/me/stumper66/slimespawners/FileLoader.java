package me.stumper66.slimespawners;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileLoader {

    @NotNull
    static YamlConfiguration loadConfig(final @NotNull SlimeSpawners main){
        final File file = new File(main.getDataFolder(), "config.yml");

        if (!file.exists())
            main.saveResource(file.getName(), false);

        final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.options().copyDefaults(true);

        return cfg;
    }
}
