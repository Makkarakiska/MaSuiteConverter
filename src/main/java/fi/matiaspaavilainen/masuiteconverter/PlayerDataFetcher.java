package fi.matiaspaavilainen.masuiteconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
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
    void load(String name) {
        if (!players.containsKey(name)) {
            ProxyServer.getInstance().getScheduler().runAsync(new MaSuiteCore(), () -> {
                HttpURLConnection request;
                try {
                    URL url = new URL("https://use.gameapis.net/mc/player/uuid/" + name);
                    request = (HttpURLConnection) url.openConnection();
                    request.setDoOutput(true);
                    request.setRequestMethod("GET");

                    request.connect();
                    JsonParser jp = new JsonParser();
                    JsonElement element = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                    JsonObject obj = element.getAsJsonObject();

                    if (request.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        if (obj.get("error") == null) {
                            players.put(name, UUID.fromString(obj.get("uuid_formatted").getAsString()));
                        }

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
