# URLPluginLoader

Bukkit plugin for loading plugins from URLs.

## Warning

This is a work in progress and very **unstable**.

## Method

By modifying the `file` argument passed to `SimplePluginManager.loadPlugin` is possible to load plugins from URLs that are not local files.

See [URLPluginLoader](https://github.com/hfleap/URLPluginLoader/blob/master/src/main/java/dev/fleap/urlpluginloader/loader/URLPluginLoader.java) class.

## Building with gradle

`./gradlew jar` should build the plugin at `./build/libs/`
