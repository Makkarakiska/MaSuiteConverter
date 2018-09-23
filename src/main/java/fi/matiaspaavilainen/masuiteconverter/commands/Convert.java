package fi.matiaspaavilainen.masuiteconverter.commands;

import fi.matiaspaavilainen.masuiteconverter.BungeeSuite;
import fi.matiaspaavilainen.masuiteconverter.MaSuiteConverter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class Convert extends Command {
    public Convert() {
        super("masuiteconvert", "masuiteconverter.convert", "masuiteconverter", "msc");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        String[] plugins = {"homes", "portals", "warps", "spawns", "players"};
        List<String> list = Arrays.asList(plugins);
        if(args.length == 1){
            if(list.contains(args[0].toLowerCase())){
                BungeeSuite bs = new BungeeSuite();
                switch (args[0].toLowerCase()){
                    case("homes"):
                        if(MaSuiteConverter.homes){
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            bs.convertHomes();
                        }else{
                            System.out.println("[MaSuite] [MaSuiteConverter] Homes are not initialized");
                        }
                        break;
                    case ("portals"):
                        if(MaSuiteConverter.portals){
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                        }else{
                            System.out.println("[MaSuite] [MaSuiteConverter] Portals are not initialized");
                        }
                        break;
                    case("warps"):
                        if(MaSuiteConverter.warps){
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            bs.convertWarps();
                        }else{
                            System.out.println("[MaSuite] [MaSuiteConverter] Warps are not initialized");
                        }
                        break;
                    case("spawns"):
                        if(MaSuiteConverter.teleports){
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            bs.convertSpawns();
                        }else{
                            System.out.println("[MaSuite] [MaSuiteConverter] Spawns are not initialized");
                        }
                        break;
                    case("players"):
                        if(MaSuiteConverter.core) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            bs.convertPlayers();
                        }else{
                            System.out.println("[MaSuite] [MaSuiteConverter] Core is not initialized");
                        }
                        break;
                }
            }
        }else{
            cs.sendMessage(new TextComponent("Please specify what do you want convert:"));
            cs.sendMessage(new TextComponent("List of plugins: homes, portals, warps, spawns or players."));
        }
    }
}
