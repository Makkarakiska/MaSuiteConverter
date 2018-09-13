package fi.matiaspaavilainen.masuiteconverter.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class Convert extends Command {
    public Convert() {
        super("masuiteconvert", "masuiteconver.convert", "masuiteconverter", "msc");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String[] plugins = {"homes", "portals", "warps", "spawns", "players"};
        List<String> list = Arrays.asList(plugins);
        if(args.length == 1){
            if(list.contains(args[0].toLowerCase())){
                // TODO CONVERT
            }
        }else{
            cs.sendMessage(new TextComponent("Please specify what do you want convert:"));
            cs.sendMessage(new TextComponent("You can convert: homes, portals, warps"));
        }
    }
}
