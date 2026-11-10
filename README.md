<div align="center">

# SetMyHome

### Save named homes and teleport back to them

Create personal locations, manage them with commands, and configure teleport delays and home limits.

</div>

---

## Key Features

- **Named homes** — save multiple locations for each player.
- **Persistent storage** — home data is stored by player UUID in `homes.yml`.
- **Teleport controls** — set a delay, cancel it when a player moves, and play a sound after teleporting.
- **Home limits** — configure a default limit and grant higher limits through rank permissions.
- **Administration** — list or remove homes belonging to a known player.
- **Configurable messages** — edit MiniMessage-formatted messages in `messages.yml`.

---

## Requirements

- Minecraft server with API version `1.19` or newer.
- Java 17 or newer.

The Maven project uses Spigot API `1.19.4-R0.1-SNAPSHOT` and targets Java 17.

---

## Build and Install

Build the plugin with Maven:

```sh
mvn clean package
```

Copy `target/setmyhome-1.0.0.jar` into the server's `plugins` directory and restart the server.

---

## Commands

| Command | Description |
| --- | --- |
| `/sethome <name>` | Save your current location as a home. |
| `/home <name>` | Teleport to one of your homes. |
| `/delhome <name>` | Delete one of your homes. |
| `/listhomes` | List your homes. |
| `/admindelhome <player> <home>` | Delete a home belonging to a known player. |
| `/adminlisthomes <player>` | List homes belonging to a known player. |

Player commands require the matching `homeplugin.sethome`, `homeplugin.home`, `homeplugin.delhome`, or `homeplugin.listhomes` permission. They are enabled by default. The administrative commands require `homeplugin.admindelhome` or `homeplugin.adminlisthomes`, granted to operators by default.

---

## Configuration

The plugin creates `plugins/SetMyHome/config.yml` and `plugins/SetMyHome/messages.yml` on first startup.

| Setting | Default | Description |
| --- | --- | --- |
| `cooldown` | `0` | Delay before a home teleport, in seconds. |
| `max-homes.default` | `15` | Default maximum number of homes per player. |
| `max-homes.<rank>` | — | Optional limit for players with `homeplugin.limit.<rank>`. |
| `cancel-on-move` | `false` | Cancel a pending teleport when the player changes block. |
| `play-sound` | `true` | Play a sound after a successful teleport. |

Edit `messages.yml` to customize player-facing text. Message values support MiniMessage formatting and placeholders such as `{name}`, `{player}`, and `{seconds}`. Home data is saved in `plugins/SetMyHome/homes.yml`.

---

Licensed under GPL-3.0. See [LICENSE](LICENSE). Bundled dependency notices are included in [THIRD_PARTY_NOTICES.md](src/main/resources/THIRD_PARTY_NOTICES.md) and in the plugin JAR.
