package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.database.Database;
import fi.matiaspaavilainen.masuitehomes.Home;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Set<Home> getHomes() {
        Set<Home> homes = new HashSet<>();
        ResultSet rs = null;
        try {
            connection = MaSuiteCore.db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM bungeehomes LIMIT 100");
            rs = statement.executeQuery();
            PlayerDataFetcher pdf = new PlayerDataFetcher();
            while (rs.next()) {
                Home home = new Home();
                String username = rs.getString("player");
                pdf.load(username);
                Thread.sleep(config.load("converter", "config.yml").getInt("wait-before-next") * 1000);
                if (pdf.getUUID(rs.getString("player")) != null){
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
                }else{
                    System.out.println("[MaSuite] [MaSuiteConverter] [Skipped] Error while converting " + username + "'s home." );
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
        for(Home home : getHomes()){
            home.set(home);
        }
    }
}
