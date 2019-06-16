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
import java.util.UUID;

public class ProxySuite {

    private Database db = ConnectionManager.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private BungeeConfiguration config = new BungeeConfiguration();
    private String psPrefix = config.load("converter", "config.yml").getString("proxysuite-prefix");

    private Set<Home> getHomes() {
        Set<Home> homes = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + psPrefix + "homes");
            rs = statement.executeQuery();
            while (rs.next()) {
                Home home = new Home();
                home.setId(rs.getInt("id"));
                home.setName(rs.getString("name"));
                home.setServer(rs.getString("server"));
                home.setOwner(UUID.fromString(rs.getString("player")));
                home.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                homes.add(home);
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
            statement = connection.prepareStatement("SELECT * FROM " + psPrefix + "portals");
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
            statement = connection.prepareStatement("SELECT * FROM " + psPrefix + "warps");
            rs = statement.executeQuery();
            while (rs.next()) {
                Warp warp = new Warp();
                warp.setId(rs.getInt("id"));
                warp.setName(rs.getString("name"));
                warp.setServer(rs.getString("server"));
                warp.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                warp.setHidden(rs.getBoolean("hidden"));
                warp.setGlobal(false);
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
            statement = connection.prepareStatement("SELECT * FROM " + psPrefix + "spawns");
            rs = statement.executeQuery();
            while (rs.next()) {
                Spawn spawn = new Spawn();
                spawn.setServer(rs.getString("server"));
                spawn.setLocation(new Location(rs.getString("world"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch")));
                spawn.setType(rs.getString("type").equals("normal") ? 0 : 1);
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
            statement = connection.prepareStatement("SELECT * FROM " + psPrefix + "players");
            rs = statement.executeQuery();
            while (rs.next()) {
                MaSuitePlayer msp = new MaSuitePlayer();
                String uuid = rs.getString("uuid");
                if(uuid.length() == 32) {
                    uuid = uuid.substring(0,8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
                }
                msp.setUniqueId(UUID.fromString(uuid));
                msp.setUsername(rs.getString("name"));
                msp.setNickname(null);
                LocalDate firstJoin = rs.getDate("first_join").toLocalDate();
                LocalDate lastJoin = rs.getDate("last_seen").toLocalDate();
                msp.setFirstLogin(firstJoin.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
                msp.setLastLogin(lastJoin.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

                players.add(msp);
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
