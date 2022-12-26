package com.thisisnzed.sshscanner;

import com.jcraft.jsch.JSch;
import com.thisisnzed.sshscanner.addons.impl.Combo;
import com.thisisnzed.sshscanner.addons.impl.PortList;
import com.thisisnzed.sshscanner.addons.impl.Range;
import com.thisisnzed.sshscanner.arguments.ArgumentParser;
import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.session.Session;
import com.thisisnzed.sshscanner.utils.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SSHScanner {

    private final Configuration configuration;
    private final ArgumentParser argumentParser;
    private final Session session;
    private final Random random;
    private final ConcurrentHashMap<String, Boolean> hosts;
    private final Combo combo;
    private PortList portList;
    private ConcurrentLinkedQueue<String> ipList;
    private Range range;

    public SSHScanner(final String[] args) {
        this.configuration = new Configuration();
        this.argumentParser = new ArgumentParser(this.configuration, args);
        this.configuration.set("combo", "");
        this.configuration.set("range", "");
        this.configuration.set("threads", 1);
        this.configuration.set("host", "");
        this.configuration.set("port", 22);
        this.configuration.set("portlist", "");
        this.configuration.set("timeout", 6000);
        this.configuration.set("webhook", "");
        this.configuration.set("verbose", true);

        this.argumentParser.loadArguments();

        this.random = new Random();
        this.combo = new Combo(this.configuration, "combo", ':');
        this.hosts = new ConcurrentHashMap<>();
        if (!this.configuration.get("range").equals("")) {
            this.ipList = new ConcurrentLinkedQueue<>();
            this.range = new Range(this.configuration, "range", '.');
            this.performAdder();
        }
        if (!this.configuration.get("host").equals("") && !this.configuration.get("portlist").equals(""))
            this.portList = new PortList(this.configuration, "portlist", ' ');
        this.session = new Session(this.combo, this.configuration, new JSch(), new java.util.Properties(), this.portList);
    }

    public void start() {
        Logger.log("Starting scan...");
        IntStream.range(0, Integer.parseInt(String.valueOf(this.configuration.get("threads")))).forEach(i -> new Thread(() -> {
            while (true) {
                String host;
                if (!this.configuration.get("host").equals("")) host = (String) this.configuration.get("host");
                else if (this.range != null && this.ipList != null) {
                    final Optional<String> optional = this.ipList.stream().findAny();
                    if (optional.isPresent()) {
                        host = optional.get();
                        this.ipList.remove(host);
                    } else {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else
                    host = (this.random.nextInt(254) + 1) + "." + (this.random.nextInt(254) + 1) + "." + (this.random.nextInt(254) + 1) + "." + (this.random.nextInt(254) + 1);
                if (!this.hosts.containsKey(host))
                    this.combo.getList().stream().filter(combo -> this.hosts.getOrDefault(host, true)).forEach(combo -> this.hosts.put(host, this.session.connect(host, combo.split(":")[0], combo.split(":")[1])));
            }
        }).start());
    }

    private void performAdder() {
        Logger.log("Loading & preparing all IPs from ranges, please wait...");
        final long start = System.currentTimeMillis();
        this.range.getList().stream().map(ipRange -> ipRange.split("\\.", -1)).forEach(parts -> this.getNumbers(parts[0]).forEach(part0 -> this.getNumbers(parts[1]).forEach(part1 -> this.getNumbers(parts[2]).forEach(part2 -> this.getNumbers(parts[3]).forEach(part3 -> this.ipList.add(part0 + "." + part1 + "." + part2 + "." + part3))))));
        Logger.log(String.format("(ip) Loaded %s IPs! (in %ds)", this.ipList.size(), (System.currentTimeMillis() - start) / 1000));
    }

    private ArrayList<String> getNumbers(final String range) {
        final ArrayList<String> numbers = new ArrayList<>();
        if (range.contains("-"))
            for (int k = Integer.parseInt(range.substring(0, range.indexOf("-"))); k <= Integer.parseInt(range.substring(range.indexOf("-") + 1)); k++)
                numbers.add(Integer.toString(k));
        else
            numbers.add(range);
        return numbers;
    }
}