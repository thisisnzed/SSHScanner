package com.thisisnzed.sshscanner.combo;

import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.launch.Launch;
import com.thisisnzed.sshscanner.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Combo {

    private final ArrayList<String[]> combo;

    public Combo(final Configuration configuration) {
        this.combo = new ArrayList<>();
        try {
            final File file = new File((String)configuration.get("combo"));
            if(!file.exists()) file.createNewFile();
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine())
                this.combo.add(scanner.nextLine().split(":"));
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
        Logger.log("Loaded " + this.combo.size() + " combinations !");
    }

    public ArrayList<String[]> getCombo() {
        return this.combo;
    }
}
