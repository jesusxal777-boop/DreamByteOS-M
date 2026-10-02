# DreamByte Package Manager

The prototype exposes these commands:

- `pkg update`
- `pkg search <term>`
- `pkg info <package>`
- `pkg install <package>`
- `pkg remove <package>`
- `pkg list`

The configured repository base is:

`https://raw.githubusercontent.com/jesusxal777-boop/DreamByte-Package-Repository/main/`

0.1 does not simulate installations. `pkg list` reports no installed packages and `pkg install` explains why installation is not yet available. The next implementation should define a signed index, package format, storage permissions, verification policy, and rollback behavior before installing real packages such as Python, wget, curl, git, unzip, zip, or openssl.
