# DreamShell

DreamShell is a controlled interpreter inside the app. It is not a Unix shell and does not invoke arbitrary processes.

Supported first commands: `help`, `clear`, `echo`, `date`, `time`, `whoami`, `version`, `about`, `dream`, `theme`, and the `pkg` command family.

Commands such as `python`, `wget`, `curl`, and `git` have reserved command names, but return an explicit not-installed message. This avoids pretending that a userland exists before it is implemented.

Future work can replace individual command handlers with a DreamByte Userland service while retaining the same terminal UI and command contract.
