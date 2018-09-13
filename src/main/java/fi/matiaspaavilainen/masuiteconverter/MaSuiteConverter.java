package fi.matiaspaavilainen.masuiteconverter;

import fi.matiaspaavilainen.masuitecore.Updator;
import net.md_5.bungee.api.plugin.Plugin;

public class MaSuiteConverter extends Plugin {

    @Override
    public void onEnable() {
        super.onEnable();

        // EDIT AFTER RESOURCE POST
        // new Updator().checkVersion(this.getDescription(), "");
    }
}
