package dev.fleap.networkpluginloader;

import dev.fleap.networkpluginloader.command.LoadpluginCommand;
import dev.fleap.networkpluginloader.loader.URLPluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Logger logger;

    static URLPluginLoader pluginLoader;

    @Override
    public void onEnable() {
        getCommand("loadplugin").setExecutor(new LoadpluginCommand());
        pluginLoader = new URLPluginLoader(getLogger(), getDataFolder().getParentFile());
    }

    @Override
    public void onLoad() {
        logger = getLogger();
    }

    public static boolean tryLoadAndEnable(URL url) {
        try {
            logger.info("Loading plugin: " + url);
            Plugin plugin = pluginLoader.loadPlugin(url);
            Bukkit.getPluginManager().enablePlugin(plugin);
            return true;
        } catch (Exception ex) {
            logger.severe("Failed to load and enable plugin: " + url);
            ex.printStackTrace();
            return false;
        }
    }

}