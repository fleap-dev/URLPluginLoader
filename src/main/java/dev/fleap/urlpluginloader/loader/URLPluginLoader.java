package dev.fleap.networkpluginloader.loader;

import dev.fleap.networkpluginloader.exception.PluginLoadException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class URLPluginLoader {

    private final Logger logger;
    private final File configRoot;

    public URLPluginLoader(Logger logger, File configRoot) {
        this.logger = logger;
        this.configRoot = configRoot;
    }

    public Plugin loadPlugin(URL url)
            throws URISyntaxException, InvalidPluginException, InvalidDescriptionException,
            IOException, PluginLoadException {

        // Create a dummy jar with just a plugin.yml
        File dummyFile = File.createTempFile("dummy", ".jar");
        byte[] description = readPluginDescription(url);
        if (description == null) throw new PluginLoadException("Plugin does not contain a plugin.yml file!");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dummyFile));
        ZipEntry zipEntry = new ZipEntry("plugin.yml");
        zos.putNextEntry(zipEntry);
        zos.write(description, 0, description.length);
        zos.close();

        NetworkPluginFile npf = new NetworkPluginFile(dummyFile, url, configRoot);
        return Bukkit.getPluginManager().loadPlugin(npf);
    }

    // Reads plugin.yml from a jar url
    public byte[] readPluginDescription(URL url) throws IOException {
        byte[] buffer = new byte[1024];
        InputStream is = url.openStream();
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        ByteArrayOutputStream baos = null;
        int len;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals("plugin.yml")) {
                int size = (int) entry.getSize();
                baos = new ByteArrayOutputStream(size != -1 ? size : 32);
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
            }
        }
        return baos == null ? null : baos.toByteArray();
    }

    // Hacky class to override methods used by the JavaPluginLoader class
    private static class NetworkPluginFile extends File {

        private URL pluginUrl;
        private File configFolder;

        public NetworkPluginFile(String pathname) {
            super(pathname);
        }
        public NetworkPluginFile(String parent, String child) {
            super(parent, child);
        }
        public NetworkPluginFile(File parent, String child) {
            super(parent, child);
        }
        public NetworkPluginFile(URI uri) {
            super(uri);
        }

        NetworkPluginFile(File dummy, URL url, File configFolder) throws URISyntaxException {
            super(dummy.toURI());
            this.pluginUrl = url;
            this.configFolder = configFolder;
        }

        // Override toURI (method that JavaPluginLoader passes to URLClassLoader)
        @Override
        public URI toURI() {
            if (this.pluginUrl != null) {
                try {
                    return pluginUrl.toURI();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return super.toURI();
        }

        // Override getParentFile (method that JavaPluginLoader uses to set dataFolder)
        @Override
        public File getParentFile() {
            return configFolder;
        }
    }

}