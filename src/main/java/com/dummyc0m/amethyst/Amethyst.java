package com.dummyc0m.amethyst;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * com.dummyc0m.demo
 * Created by Dummyc0m on 3/13/15.
 */
public class Amethyst extends JavaPlugin {
    private static Amethyst AMETHYST;

    public static Amethyst getInstance() {
        return AMETHYST;
    }

    @Override
    public void onEnable(){
        AMETHYST = this;
        this.getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabled");
    }
}
