# Build

## Local

Requirements: JDK 17+, Android SDK with platform 35 and build-tools 35.x, and Gradle 8.7 (or the checked-in wrapper once generated).

```bash
./gradlew test
./gradlew assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

The sandbox used to bootstrap this repository does not include an Android SDK, so the authoritative build runs in the GitHub Actions Android environment. The workflow installs SDK platform 35/build-tools and then runs validation, tests, and `assembleDebug`.

## GitHub Actions

`.github/workflows/android.yml` runs on pushes to `main` and manual dispatch. It sets up JDK 17, installs Android SDK packages, validates Gradle, runs unit tests, builds the debug APK, and uploads it as the `DreamByteOS-M-debug` artifact.
