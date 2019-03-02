package fi.matiaspaavilainen.masuiteconverter.commands;

import fi.matiaspaavilainen.masuiteconverter.BungeeSuite;
import fi.matiaspaavilainen.masuiteconverter.MaSuiteConverter;
import fi.matiaspaavilainen.masuiteconverter.ProxySuite;
import fi.matiaspaavilainen.masuiteconverter.geSuit;
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
        if (args.length == 2) {
            if (list.contains(args[1].toLowerCase())) {
                BungeeSuite bs = new BungeeSuite();
                ProxySuite ps = new ProxySuite();
                geSuit gs = new geSuit();
                switch (args[1].toLowerCase()) {
                    case ("homes"):
                        if (MaSuiteConverter.homes) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            if (args[0].equalsIgnoreCase("bungeesuite")) {
                                bs.convertHomes();
                            } else if (args[0].equalsIgnoreCase("proxysuite")) {
                                ps.convertHomes();
                            } else if (args[0].equalsIgnoreCase("gesuit")) {
                                gs.convertHomes();
                            }

                        } else {
                            System.out.println("[MaSuite] [MaSuiteConverter] Homes are not initialized");
                        }
                        break;
                    case ("portals"):
                        if (MaSuiteConverter.portals) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            if (args[0].equalsIgnoreCase("bungeesuite")) {
                                bs.convertPortals();
                            } else if (args[0].equalsIgnoreCase("proxysuite")) {
                                ps.convertPortals();
                            } else if (args[0].equalsIgnoreCase("gesuit")) {
                                gs.convertPortals();
                            }
                        } else {
                            System.out.println("[MaSuite] [MaSuiteConverter] Portals are not initialized");
                        }
                        break;
                    case ("warps"):
                        if (MaSuiteConverter.warps) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            if (args[0].equalsIgnoreCase("bungeesuite")) {
                                bs.convertWarps();
                            } else if (args[0].equalsIgnoreCase("proxysuite")) {
                                ps.convertWarps();
                            } else if (args[0].equalsIgnoreCase("gesuit")) {
                                gs.convertWarps();
                            }
                        } else {
                            System.out.println("[MaSuite] [MaSuiteConverter] Warps are not initialized");
                        }
                        break;
                    case ("spawns"):
                        if (MaSuiteConverter.teleports) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            if (args[0].equalsIgnoreCase("bungeesuite")) {
                                bs.convertSpawns();
                            } else if (args[0].equalsIgnoreCase("proxysuite")) {
                                ps.convertSpawns();
                            } else if (args[0].equalsIgnoreCase("gesuit")) {
                                gs.convertSpawns();
                            }
                        } else {
                            System.out.println("[MaSuite] [MaSuiteConverter] Spawns are not initialized");
                        }
                        break;
                    case ("players"):
                        if (MaSuiteConverter.core) {
                            System.out.println("[MaSuite] [MaSuiteConverter] started converting...");
                            if (args[0].equalsIgnoreCase("bungeesuite")) {
                                bs.convertPlayers();
                            } else if (args[0].equalsIgnoreCase("proxysuite")) {
                                ps.convertPlayers();
                            } else if (args[0].equalsIgnoreCase("gesuit")) {
                                gs.convertPlayers();
                            }
                        } else {
                            System.out.println("[MaSuite] [MaSuiteConverter] Core is not initialized");
                        }
                        break;
                }
            }
        } else {
            cs.sendMessage(new TextComponent("Please specify what do you want convert:"));
            cs.sendMessage(new TextComponent("List of plugins: homes, portals, warps, spawns or players."));
        }
    }
}
