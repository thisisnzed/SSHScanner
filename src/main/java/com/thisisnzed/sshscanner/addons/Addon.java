package com.thisisnzed.sshscanner.addons;

import com.thisisnzed.sshscanner.arguments.Configuration;
import com.thisisnzed.sshscanner.utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Addon {

    private final ArrayList<String> list;

    public Addon(final Configuration configuration, final String addonName, final char needed) {
        this.list = new ArrayList<>();
        try {
            final File file = configuration.get(addonName).equals("") ? this.getResourceAsFile(addonName + ".txt") : new File((String) configuration.get(addonName));
            if (!file.exists()) file.createNewFile();
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String nextLine = scanner.nextLine();
                if (nextLine.contains(String.valueOf(needed)) || needed == ' ')
                    this.list.add(nextLine);
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
        final int size = this.list.size();
        if (size == 0) {
            Logger.log(String.format("(%s) The file must have at least one valid line", addonName));
            System.exit(-1);
            return;
        }
        Logger.log(String.format("(%s) Loaded %d lines!", addonName, size));
    }

    private File getResourceAsFile(final String resourcePath) {
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null)
                return null;

            File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1)
                    fileOutputStream.write(buffer, 0, bytesRead);
            }
            return tempFile;
        } catch (final IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getList() {
        return this.list;
    }

}
