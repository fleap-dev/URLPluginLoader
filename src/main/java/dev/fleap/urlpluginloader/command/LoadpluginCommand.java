package dev.fleap.networkpluginloader.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.net.MalformedURLException;
import java.net.URL;

import static dev.fleap.networkpluginloader.Main.tryLoadAndEnable;

public class LoadpluginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        URL url;
        try {
            url = new URL(args[0]);
        } catch (MalformedURLException e) {
            sender.sendMessage("Invalid url!");
            return true;
        }
        if (tryLoadAndEnable(url)){
            sender.sendMessage("Plugin enabled!");
        }else {
            sender.sendMessage("Error enabling plugin. More info on the console.");
        }
        return true;
    }

}