package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.database.Database;
import fi.matiaspaavilainen.masuitecore.managers.Location;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import fi.matiaspaavilainen.masuitehomes.Home;
import fi.matiaspaavilainen.masuiteteleports.managers.Spawn;
import fi.matiaspaavilainen.masuitewarps.Warp;

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

    private Database db = MaSuiteCore.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private Configuration config = new Configuration();
    private String tablePrefix = config.load("converter", "config.yml").getString("proxysuite-prefix");

    private Set<Home> getHomes() {
        Set<Home> homes = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "homes");
            rs = statement.executeQuery();
            PlayerDataFetcher pdf = new PlayerDataFetcher();
            while (rs.next()) {
                Home home = new Home();
                String username = rs.getString("player");
                pdf.load(username);
                Thread.sleep(config.load("converter", "config.yml").getInt("wait-before-next") * 1000);
                if (pdf.getUUID(rs.getString("player")) != null) {
                    home.setId(rs.getInt("id"));
                    home.setName(rs.getString("name"));
                    home.setServer(rs.getString("server"));
                    home.setOwner(UUID.fromString(rs.getString("player")));
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

    public void convertHomes(){
        for (Home home : getHomes()) {
            home.set(home);
        }
        System.out.println("[MaSuite] [Converter] [Homes] Converting done");
    }

    public void convertPortals(){

    }

    private Set<Warp> getWarps() {
        Set<Warp> warps = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "warps");
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

    public void convertWarps(){
        for (Warp warp : getWarps()) {
            warp.create(warp);
        }
        System.out.println("[MaSuite] [Converter] [Warps] Converting done");
    }

    private Set<Spawn> getSpawns() {
        Set<Spawn> spawns = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "spawns");
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
            statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "players");
            rs = statement.executeQuery();
            while (rs.next()) {
                    MaSuitePlayer msp = new MaSuitePlayer();
                    msp.setUUID(UUID.fromString(rs.getString("uuid")));
                    msp.setUsername(rs.getString("name"));
                    msp.setNickname(null);
                    msp.setIpAddress(rs.getString("127.0.0.1"));
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
            msp.insert();
        }
        System.out.println("[MaSuite] [Converter] [MaSuitePlayers] Converting done");
    }
}
