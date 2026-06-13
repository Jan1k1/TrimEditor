# Building from source

## Prerequisites

- **JDK 21** (the build targets Java 21 bytecode).
- No local Gradle install needed — use the bundled wrapper.

## Build

```bash
git clone https://github.com/Jan1k1/TrimEditor.git
cd TrimEditor
./gradlew build
```

The shaded plugin jar is written to `build/libs/TrimEditor-<version>.jar`.
`build` runs the test suite first, so a green build is also a passing test run.

## Common tasks

| Task | What it does |
|------|--------------|
| `./gradlew test` | Run the unit tests (JUnit 5 + MockBukkit) |
| `./gradlew shadowJar` | Produce the minimized plugin jar |
| `./gradlew build` | Test, then build the jar |
| `./gradlew runServer` | Boot a local Paper 1.21 test server with the plugin and Vault |

`runServer` downloads a Paper server and the plugin's runtime dependencies on
first use, then launches it with TrimEditor installed — handy for manual testing.

## Tech stack

| | |
|---|---|
| Language | Kotlin 2.4.0 |
| Build | Gradle (wrapper) + Shadow + run-paper |
| API | Paper API 1.21 |
| Tests | JUnit 5, MockBukkit |

## Project layout

```
src/main/kotlin/org/jan1k/plugin/trimeditor/
  api/        # public Bukkit service for other plugins
  command/    # /te command + tab completion
  config/     # config model, loader, migration
  cost/       # requirements + economy (Vault) services
  apply/      # apply flow
  gui/        # screens, buttons, click guard, listener
  item/       # armor trim editing + item fingerprint
  lang/       # language loading (MiniMessage)
  session/    # per-player edit sessions
  trim/       # trim catalog + options
  platform/   # Folia-aware scheduler
```
