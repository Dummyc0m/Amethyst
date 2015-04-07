package com.dummyc0m.amethyst.amethyst;


import com.dummyc0m.amethyst.AmethystPlugin;
import com.dummyc0m.amethystutil.config.Configuration;

/**
 * com.dummyc0m.amethyst
 * Created by Dummyc0m on 3/13/15.
 */
public class Amethyst {
    private Configuration configuration;
    private ServerSettings serverSettings;
    private AmethystPlugin amethystPlugin;

    public Amethyst(AmethystPlugin amethystPlugin){
        this.load();
    }

    public final void load(){
        this.amethystPlugin = amethystPlugin;
        this.configuration = new Configuration("settings.json", ServerSettings.class);
        this.serverSettings = (ServerSettings) configuration.getSettings();
    }

    public void unload(){
        this.configuration.save();
    }

    private void loadConfigs(){

    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }
}
