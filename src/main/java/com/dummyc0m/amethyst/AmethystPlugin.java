package com.dummyc0m.amethyst;

import com.dummyc0m.amethyst.amethyst.Amethyst;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * com.dummyc0m.amethyst
 * Created by Dummyc0m on 3/13/15.
 */
public class AmethystPlugin extends JavaPlugin{
    private static final String AP_VERSION = "0.1-SNAPSHOT";
    private static AmethystPlugin AMETHYSTPLUGIN;
    private Amethyst amethyst;

    @Override
    public void onEnable(){
        AMETHYSTPLUGIN = this;
        this.amethyst = new Amethyst(this);
        this.getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        this.amethyst.unload();
        this.getLogger().info("Disabled");
    }

    public AmethystPlugin getInstance(){
        return AMETHYSTPLUGIN;
    }
}
