package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuiteconverter.commands.Convert;
import fi.matiaspaavilainen.masuitecore.Updator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.database.Database;
import fi.matiaspaavilainen.masuitehomes.MaSuiteHomes;
import net.md_5.bungee.api.plugin.Plugin;

public class MaSuiteConverter extends Plugin {

    public static Database db = new Database();
    public static Boolean homes = false;
    public static Boolean warps = false;
    public static Boolean teleports = false;
    public static Boolean chat = false;
    public static Boolean core = false;
    public static Boolean portals = false;
    @Override
    public void onEnable() {
        super.onEnable();

        // Create connection
        db.connect();

        getProxy().getPluginManager().registerCommand(this, new Convert());

        new Configuration().create(this,"converter", "config.yml");
        // EDIT AFTER RESOURCE POST
        // new Updator().checkVersion(this.getDescription(), "");
        setup();
    }


    private void setup(){
        setupHomes();
        setupWarps();
        setupTeleports();
        setupChat();
        setupCore();
        setupPortals();
    }
    private void setupHomes(){
        if(getProxy().getPluginManager().getPlugin("MaSuiteHomes") != null){
            homes = true;
        }
    }

    private void setupWarps(){
        if(getProxy().getPluginManager().getPlugin("MaSuiteWarps") != null){
            warps = true;
        }
    }

    private void setupTeleports(){
        if(getProxy().getPluginManager().getPlugin("MaSuiteTeleports") != null){
            teleports = true;
        }
    }

    private void setupChat(){
        if(getProxy().getPluginManager().getPlugin("MaSuiteChat") != null){
            chat = true;
        }
    }

    private void setupCore(){
        if(getProxy().getPluginManager().getPlugin("MaSuiteCore") != null){
            core = true;
        }
    }

    private void setupPortals(){
        if(getProxy().getPluginManager().getPlugin("MaSuitePortals") != null){
            portals = true;
        }
    }
}
