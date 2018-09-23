package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.database.Database;
import fi.matiaspaavilainen.masuitecore.managers.Location;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import fi.matiaspaavilainen.masuitehomes.Home;
import fi.matiaspaavilainen.masuiteteleports.managers.Spawn;
import fi.matiaspaavilainen.masuitewarps.Warp;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BungeeSuite {

    private Database db = MaSuiteCore.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private Configuration config = new Configuration();

    private Set<Home> getHomes() {
        Set<Home> homes = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = MaSuiteCore.db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM bungeehomes");
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
                    home.setWorld(rs.getString("world"));
                    home.setX(rs.getDouble("x"));
                    home.setY(rs.getDouble("y"));
                    home.setZ(rs.getDouble("z"));
                    home.setYaw(rs.getFloat("yaw"));
                    home.setPitch(rs.getFloat("pitch"));
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
            home.set(home);
        }
        System.out.println("[MaSuite] [MaSuiteConverter] [Homes] Converting done");
    }

    private Set<Warp> getWarps() {
        Set<Warp> warps = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = MaSuiteCore.db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM bungeewarps");
            rs = statement.executeQuery();
            while (rs.next()) {
                Warp warp = new Warp();
                warp.setName(rs.getString("warpname"));
                warp.setServer(rs.getString("server"));
                warp.setWorld(rs.getString("world"));
                warp.setX(rs.getDouble("x"));
                warp.setY(rs.getDouble("y"));
                warp.setZ(rs.getDouble("z"));
                warp.setYaw(rs.getFloat("yaw"));
                warp.setPitch(rs.getFloat("pitch"));
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
            warp.create(warp);
        }
        System.out.println("[MaSuite] [MaSuiteConverter] [Warps] Converting done");
    }

    private Set<Spawn> getSpawns() {
        Set<Spawn> spawns = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = MaSuiteCore.db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM bungeespawns");
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
        System.out.println("[MaSuite] [MaSuiteConverter] [Spawns] Converting done");
    }

    private Set<MaSuitePlayer> getPlayers() {
        Set<MaSuitePlayer> players = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = MaSuiteCore.db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM bungeeplayers");
            rs = statement.executeQuery();
            PlayerDataFetcher pdf = new PlayerDataFetcher();
            while (rs.next()) {
                String username = rs.getString("playername");
                pdf.load(username);
                Thread.sleep(config.load("converter", "config.yml").getInt("wait-before-next") * 1000);
                if (pdf.getUUID(rs.getString("playername")) != null) {
                    MaSuitePlayer msp = new MaSuitePlayer();
                    msp.setUUID(pdf.getUUID(username));
                    msp.setUsername(rs.getString("playername"));
                    msp.setNickname(rs.getString("nickname"));
                    msp.setIpAddress(rs.getString("ipaddress"));
                    LocalDate date = rs.getDate("lastonline").toLocalDate();
                    msp.setLastLogin(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
                    msp.setFirstLogin(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
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
            msp.insert();
        }
        System.out.println("[MaSuite] [MaSuiteConverter] [MaSuitePlayers] Converting done");
    }
}
