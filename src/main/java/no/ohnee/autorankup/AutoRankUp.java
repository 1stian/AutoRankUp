package no.ohnee.autorankup;

import net.milkbowl.vault.permission.Permission;
import no.ohnee.autorankup.checks.CanRankUp;
import no.ohnee.autorankup.commands.Autorankup;
import no.ohnee.autorankup.commands.Check;
import no.ohnee.autorankup.config.Config;
import no.ohnee.autorankup.events.OnPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.logging.Logger;

public final class AutoRankUp extends JavaPlugin implements Listener {
    //Public
    public static final boolean DEBUG = false;
    public static final String CHANNEL = "Autorankup:rank";
    public static final Logger log = Logger.getLogger("Minecraft");
    // Private
    private static boolean logToConsole;
    private static boolean useVault;
    private static String getPlaceholder;
    private static boolean bungeeBroadcast;
    private static boolean playerJoin;
    private static boolean broadcastEnb;
    private static String broadcastMsg;
    private static boolean playerEnb;
    private static String playerMsg;
    private static int frequency;
    private static Permission perms = null;

    private static AutoRankUp autoRankUp;

    @Override
    public void onEnable() {
        // Plugin startup logic
        autoRankUp = this;
        // Checking for plugins
        checkForPlugins();
        // Saving default config and reloads it!
        saveDefaultConfig();
        reloadConfig();
        // Filling values
        logToConsole = Config.getBoolean("settings.console");
        useVault = Config.getBoolean("vault.enabled");
        getPlaceholder = Config.getString("placeholder.group");
        bungeeBroadcast = Config.getBoolean("bungee.broadcast");
        playerJoin = Config.getBoolean("settings.login");
        broadcastEnb = Config.getBoolean("settings.broadcast");
        broadcastMsg = Config.getString("settings.broadcastmessage");
        playerEnb = Config.getBoolean("settings.player");
        playerMsg = Config.getString("settings.playermessage");
        frequency = Config.getInteger("settings.frequency");

        if (useVault) {
            if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                log.warning("Could not find Vault!! Plugin can not work without it! - AutoRankUp disabled.");
                getServer().getPluginManager().disablePlugin(this);
            }
            setupPermissions();
            log.info("[AutoRankUp] - Using Vault system.");
        }

        // Check on player join
        if (getPlayerJoin())
            getServer().getPluginManager().registerEvents(new OnPlayer(), this);

        // Register commands
        getCommand("timecheck").setExecutor(new Check());
        getCommand("autorankup").setExecutor(new Autorankup());
        // Initiate task
        task();
    }

    private void checkForPlugins(){
        // Checking for plugins.
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(autoRankUp, this);
        } else {
            log.warning("Could not find PlaceholderAPI!! Plugin can not work without it! - AutoRankUp disabled.");
            getServer().getPluginManager().disablePlugin(this);
        }
        if (Bukkit.getPluginManager().getPlugin("Plan") == null) {
            log.warning("Could not find Plan!! Plugin can not work without it! - AutoRankUp disabled.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /***
     * Initiates the task
     */
    private void task() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                    if (players.isEmpty()) {
                        return;
                    }
                    log.info("[AutoRankUp] - checking for eligible players.");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        CanRankUp.rankUp(player);
                    }
                }, 120L, (long) getFrequency() * 1200L
        );
    }

    public static AutoRankUp getAutoRankUp() {
        return autoRankUp;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static boolean getBungeeBroadcast() {
        return bungeeBroadcast;
    }

    public static boolean getPlayerJoin() {
        return playerJoin;
    }

    public static boolean getBroadcastEnabled() {
        return broadcastEnb;
    }

    public static String getBroadcastMsg() {
        return broadcastMsg;
    }

    public static boolean getPlayerEnb() {
        return playerEnb;
    }

    public static String getPlayerMsg() {
        return playerMsg;
    }

    public static int getFrequency() {
        return frequency;
    }

    public static boolean getVault() {
        return useVault;
    }

    public static Permission getPerms() {
        return perms;
    }

    public static String getPlaceholder() {
        return getPlaceholder;
    }

    public static boolean getLogToConsole() {
        return logToConsole;
    }
}
