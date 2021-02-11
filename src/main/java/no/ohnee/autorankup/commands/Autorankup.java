package no.ohnee.autorankup.commands;

import no.ohnee.autorankup.AutoRankUp;
import no.ohnee.autorankup.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Autorankup implements CommandExecutor {
    private String autoRankTag = "[AutoRankUp]";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("Autorankup")){
            if (args.length < 1){
                sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.WHITE + autoRankTag + ChatColor.AQUA
                        + "]" + ChatColor.GREEN + " Thank you for using AutoRankUp" + ChatColor.AQUA +" ("
                        + ChatColor.YELLOW + AutoRankUp.getAutoRankUp().
                        getDescription().getVersion() + ChatColor.AQUA + ")");
                sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.WHITE + autoRankTag + ChatColor.AQUA
                        + "]" + ChatColor.GREEN + " For config reload do " + ChatColor.AQUA +"/Autorankup reload");
                return true;
            }else if(args[0].equalsIgnoreCase("reload")){
                if (Config.reloadConfig()){
                    sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.WHITE + autoRankTag + ChatColor.AQUA
                            + "]" + ChatColor.GREEN + " Config has been" + ChatColor.AQUA +" reloaded");
                    return true;
                }else{
                    sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.WHITE + autoRankTag + ChatColor.AQUA
                            + "]" + ChatColor.RED + " Something wrong happened while reloading the config.");
                    return true;
                }
            }
        }
        return false;
    }
}
