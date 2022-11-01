package com.thisisnzed.sshscanner.launch;

import com.thisisnzed.sshscanner.SSHScanner;

public class Launch {

    public static void main(final String[] args) {
        new SSHScanner(args).start();
    }
}
