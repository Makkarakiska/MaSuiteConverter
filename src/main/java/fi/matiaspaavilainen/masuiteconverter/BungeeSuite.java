package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuitecore.core.configuration.BungeeConfiguration;
import fi.matiaspaavilainen.masuitecore.core.database.ConnectionManager;
import fi.matiaspaavilainen.masuitecore.core.database.Database;
import fi.matiaspaavilainen.masuitecore.core.objects.Location;
import fi.matiaspaavilainen.masuitecore.core.objects.MaSuitePlayer;
import fi.matiaspaavilainen.masuitehomes.core.Home;
import fi.matiaspaavilainen.masuiteportals.core.Portal;
import fi.matiaspaavilainen.masuiteteleports.bungee.managers.Spawn;
import fi.matiaspaavilainen.masuitewarps.bungee.Warp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class BungeeSuite {

    private Database db = ConnectionManager.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private BungeeConfiguration config = new BungeeConfiguration();
    private String bsPrefix = config.load("converter", "config.yml").getString("bungeesuite-prefix");
    private String msPrefix = config.load(null, "config.yml").getString("database.table-prefix");

    private Set<Home> getHomes() {
        Set<Home> homes = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + bsPrefix + "homes");
            rs = statement.executeQuery();
            PlayerDataFetcher pdf = new PlayerDataFetcher();

            while (rs.next()) {
                Home home = new Home();
                String username = rs.getString("player");
                pdf.load(username);
                Thread.sleep(config.load("converter", "config.yml").getInt("wait-before-next") * 1000);
                if (pdf.getUUID(rs.getString("player")) != null) {
                    home.setName(rs.getString("home_name"));
                    home.setServer(rs.getString("server"));
                    home.setOwner(pdf.getUUID(username));
                    home.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                    homes.add(home);
                } else {
                    System.out.println("[MaSuite] [MaSuiteConverter] [Skipped] Error while converting " + username + "'s home.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return homes;
    }

    public void convertHomes() {
        for (Home home : getHomes()) {
            home.create();
        }
        System.out.println("[MaSuite] [Converter] [Homes] Converting done");
    }

    private Set<Portal> getPortals() {
        Set<Portal> portals = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + bsPrefix + "portals");
            rs = statement.executeQuery();
            while (rs.next()) {
                Portal portal = new Portal();
                portal.setName(rs.getString("name"));
                portal.setServer(rs.getString("server"));
                portal.setType("warp");
                portal.setDestination(rs.getString("destination"));
                portal.setMinLoc(new Location(rs.getString("world"), rs.getDouble("loc1_x"), rs.getDouble("loc1_y"), rs.getDouble("loc1_z")));
                portal.setMaxLoc(new Location(rs.getString("world"), rs.getDouble("loc2_x"), rs.getDouble("loc2_y"), rs.getDouble("loc2_z")));
                switch (rs.getString("type").toLowerCase()) {
                    case ("nothing"):
                        portal.setFillType("air");
                        break;
                    case ("nether"):
                        portal.setFillType("nether_portal");
                        break;
                    case ("water"):
                        portal.setFillType("water");
                        break;
                    default:
                        portal.setFillType("air");
                        break;
                }
                portals.add(portal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return portals;
    }

    public void convertPortals() {
        for (Portal portal : getPortals()) {
            portal.save();
        }
        System.out.println("[MaSuite] [Converter] [Portals] Converting done");
    }

    private Set<Warp> getWarps() {
        Set<Warp> warps = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + bsPrefix + "warps");
            rs = statement.executeQuery();
            while (rs.next()) {
                Warp warp = new Warp();
                warp.setName(rs.getString("warpname"));
                warp.setServer(rs.getString("server"));
                warp.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                warp.setHidden(rs.getBoolean("hidden"));
                warp.setGlobal(rs.getBoolean("global"));
                warps.add(warp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return warps;
    }

    public void convertWarps() {
        for (Warp warp : getWarps()) {
            warp.create();
        }
        System.out.println("[MaSuite] [Converter] [Warps] Converting done");
    }

    private Set<Spawn> getSpawns() {
        Set<Spawn> spawns = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + bsPrefix + "spawns");
            rs = statement.executeQuery();
            while (rs.next()) {
                Spawn spawn = new Spawn();
                spawn.setServer(rs.getString("server"));
                spawn.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                spawns.add(spawn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return spawns;
    }

    public void convertSpawns() {
        for (Spawn spawn : getSpawns()) {
            spawn.create(spawn);
        }
        System.out.println("[MaSuite] [Converter] [Spawns] Converting done");
    }

    private Set<MaSuitePlayer> getPlayers() {
        Set<MaSuitePlayer> players = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + msPrefix + "players");
            rs = statement.executeQuery();
            PlayerDataFetcher pdf = new PlayerDataFetcher();
            while (rs.next()) {
                String username = rs.getString("playername");
                pdf.load(username);
                Thread.sleep(config.load("converter", "config.yml").getInt("wait-before-next") * 1000);
                if (pdf.getUUID(rs.getString("playername")) != null) {
                    MaSuitePlayer msp = new MaSuitePlayer();
                    msp.setUniqueId(pdf.getUUID(username));
                    msp.setUsername(rs.getString("playername"));
                    msp.setNickname(rs.getString("nickname"));
                    LocalDate date = rs.getDate("lastonline").toLocalDate();
                    msp.setFirstLogin(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
                    msp.setLastLogin(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
                    players.add(msp);
                } else {
                    System.out.println("[MaSuite] [MaSuiteConverter] [Skipped] Error while converting " + username + "'s player profile.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return players;
    }

    public void convertPlayers() {
        for (MaSuitePlayer msp : getPlayers()) {
            msp.create();
        }
        System.out.println("[MaSuite] [Converter] [MaSuitePlayers] Converting done");
    }
}
