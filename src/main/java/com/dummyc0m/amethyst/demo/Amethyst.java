package com.dummyc0m.amethyst.demo;


import com.dummyc0m.amethystcore.config.ACConfig;

/**
 * com.dummyc0m.demo
 * Created by Dummyc0m on 3/13/15.
 */
public class Amethyst {
    private ACConfig configuration;
    private ServerSettings serverSettings;
    private com.dummyc0m.amethyst.Amethyst amethyst;

    public Amethyst(com.dummyc0m.amethyst.Amethyst amethyst) {
        this.load();
    }

    public final void load(){
        this.amethyst = amethyst;
        this.configuration = new ACConfig("settings.json", ServerSettings.class);
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
