package com.dummyc0m.amethyst.demo;

import java.util.HashMap;

/**
 * com.dummyc0m.demo.demo
 * Created by Dummyc0m on 3/13/15.
 */
public class ServerSettings {
    private String serverType;
    private String motd;

    //Player login
    private boolean loginMessage;
    private String loginMessageStr;
    private boolean loginBroadcast;
    private String loginBroadcastStr;
    private boolean loginTitle;
    private String loginTitleStr;

    //Prevent too many player login at the same time
    private int loginTimeoutMillis;

    //Player permission groups
    private HashMap<String, String[]> permissionGroups;

    //Database
    private String dbType;
    private String dbHostname;
    private int dbPort;
    private String db;
    private String dbUsername;
    private String dbPassword;




}
