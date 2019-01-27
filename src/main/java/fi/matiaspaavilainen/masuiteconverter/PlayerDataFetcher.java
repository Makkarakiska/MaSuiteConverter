package fi.matiaspaavilainen.masuiteconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fi.matiaspaavilainen.masuitecore.bungee.MaSuiteCore;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataFetcher {

    private HashMap<String, UUID> players = new HashMap<>();

    public void load(String name) {
        if (!players.containsKey(name)) {
            ProxyServer.getInstance().getScheduler().runAsync(new MaSuiteCore(), () -> {
                HttpURLConnection request;
                try {
                    URL url = new URL("https://api.ashcon.app/mojang/v1/user/" + name);
                    request = (HttpURLConnection) url.openConnection();
                    request.setDoOutput(true);
                    request.setRequestMethod("GET");
                    request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                    request.connect();

                    if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        JsonParser jp = new JsonParser();
                        JsonElement element = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                        JsonObject obj = element.getAsJsonObject();
                        players.put(name, UUID.fromString(obj.get("uuid").getAsString()));
                    } else {
                        System.out.println("[MaSuite] [MaSuiteConverter] [Skipped] Can't find " + name + "'s player data.");
                    }
                } catch (IOException e) {
                    System.out.println(name);
                    e.printStackTrace();
                }
            });
        }
    }

    public UUID getUUID(String name) {
        return players.get(name);
    }
}
