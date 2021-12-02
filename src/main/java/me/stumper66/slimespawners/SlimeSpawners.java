package me.stumper66.slimespawners;

import me.stumper66.slimespawners.listener.BlockBreakListener;
import me.stumper66.slimespawners.listener.BlockPlaceListener;
import me.stumper66.slimespawners.listener.ChunkLoadListener;
import me.stumper66.slimespawners.listener.PlayerInteractEventListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SlimeSpawners extends JavaPlugin {

    public Map<Location, CreatureSpawner> slimeSpawners;
    public YamlConfiguration settings;
    public boolean isEnabled;
    private SpawnerProcessor spawnerProcessor;

    @Override
    public void onEnable() {
        slimeSpawners = new HashMap<>();
        spawnerProcessor = new SpawnerProcessor(this);

        registerCommands();
        registerListeners();
        loadConfig();

        startRunnables();

        Utils.logger.info("Done loading");
    }

    @Override
    public void onDisable(){

    }

    private void registerCommands(){
        CommandProcessor cmd = new CommandProcessor(this);
        final PluginCommand slimeSpawnersCmd = getCommand("slimespawners");
        if (slimeSpawnersCmd == null)
            Utils.logger.error("Command &b/slimespawners&7 is unavailable, is it not registered in plugin.yml?");
        else
            slimeSpawnersCmd.setExecutor(cmd);
    }

    private void registerListeners(){
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockPlaceListener(this), this);
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new ChunkLoadListener(this), this);
        pm.registerEvents(new PlayerInteractEventListener(this), this);
    }

    private void startRunnables(){
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                spawnerProcessor.loopThruSpawners();
            }
        };

        runnable.runTaskTimer(this, 100, 10);
    }

    void loadConfig(){
        this.settings = FileLoader.loadConfig(this);

        this.isEnabled = settings.getBoolean("enable-slime-spawners", true);
        final SpawnerOptions defaults = new SpawnerOptions();

        final SpawnerOptions spawnerOptions = new SpawnerOptions();
        spawnerOptions.maxNearbySlimes = settings.getInt("max-nearby-slimes", defaults.maxNearbySlimes);
        spawnerOptions.spawnRange = settings.getInt("spawn-range", defaults.spawnRange);
        spawnerOptions.spawnCount = settings.getInt("spawn-count", defaults.spawnCount);
        spawnerOptions.minSpawnDelay = settings.getInt("min-spawn-delay", defaults.minSpawnDelay);
        spawnerOptions.maxSpawnDelay = settings.getInt("max-spawn-delay", defaults.maxSpawnDelay);
        spawnerOptions.playerRequiredRange = settings.getInt("player-required-range", defaults.playerRequiredRange);
        spawnerOptions.delay = settings.getInt("spawner-delay", defaults.delay);
        spawnerOptions.allowAirSpawning = settings.getBoolean("allow-air-spawning");

        this.spawnerProcessor.options = spawnerOptions;
    }
}
