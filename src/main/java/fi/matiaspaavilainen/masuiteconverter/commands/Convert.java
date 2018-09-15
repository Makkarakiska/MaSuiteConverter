package fi.matiaspaavilainen.masuiteconverter.commands;

import fi.matiaspaavilainen.masuiteconverter.BungeeSuite;
import fi.matiaspaavilainen.masuiteconverter.MaSuiteConverter;
import fi.matiaspaavilainen.masuiteconverter.PlayerDataFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                BungeeSuite bs = new BungeeSuite();
                switch (args[0].toLowerCase()){
                    case("homes"):
                        if(MaSuiteConverter.homes){
                            bs.convertHomes();
                        }
                        break;
                    case ("portals"):
                        if(MaSuiteConverter.portals){

                        }
                        break;
                    case("warps"):
                        if(MaSuiteConverter.warps){

                        }
                        break;
                    case("spawns"):
                        if(MaSuiteConverter.teleports){

                        }
                        break;
                    case("players"):
                        if(MaSuiteConverter.core) {
                        }
                        break;
                }
            }
        }else{
            cs.sendMessage(new TextComponent("Please specify what do you want convert:"));
            cs.sendMessage(new TextComponent("You can convert: homes, portals, warps"));
        }
    }
}
