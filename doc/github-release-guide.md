# GitHub Release Guide

This guide covers how Zorp The Solar Alien is packaged and published on GitHub Releases for Windows, macOS and Linux. Each download bundles its own Java runtime, so players do not need to install Java.

Steps 1–5 are one-time setup and are already done in this repository. Steps 6–8 are repeated for every new version.

---

## Step 1: Add a launcher class

A JavaFX `Application` subclass cannot be the entry point of a jar that bundles JavaFX on the classpath. Starting it that way fails with `JavaFX runtime components are missing`. A plain class that calls the application's `main` avoids this.

`src/main/java/zorp_the_solar_alien/ZorpTheSolarAlienLauncher.java`:

```java
package zorp_the_solar_alien;

public class ZorpTheSolarAlienLauncher {
    public static void main(String[] args) {
        ZorpTheSolarAlienApp.main(args);
    }
}
```

---

## Step 2: Build a runnable jar with Maven

`pom.xml` needs three changes.

1. Fix the jar name inside `<build>`:

   ```xml
   <finalName>zorp-the-solar-alien</finalName>
   ```

2. Add JavaFX native libraries for every platform. Repeat the block below for each module (`javafx-base`, `javafx-graphics`, `javafx-controls`, `javafx-media`) and each classifier (`win`, `mac`, `linux`):

   ```xml
   <dependency>
       <groupId>org.openjfx</groupId>
       <artifactId>javafx-graphics</artifactId>
       <version>${javafx.version}</version>
       <classifier>win</classifier>
   </dependency>
   ```

   `javafx.version` is defined under `<properties>`:

   ```xml
   <javafx.version>22</javafx.version>
   ```

   `mac-aarch64` is not included. Its native libraries have the same file names as the Intel `mac` ones and would overwrite them inside the jar.

3. Add the shade plugin so all dependencies go into one jar with the launcher as its main class:

   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-shade-plugin</artifactId>
       <version>3.5.1</version>
       <executions>
           <execution>
               <phase>package</phase>
               <goals>
                   <goal>shade</goal>
               </goals>
               <configuration>
                   <shadedArtifactAttached>false</shadedArtifactAttached>
                   <createDependencyReducedPom>false</createDependencyReducedPom>
                   <filters>
                       <filter>
                           <artifact>*:*</artifact>
                           <excludes>
                               <exclude>module-info.class</exclude>
                               <exclude>META-INF/*.SF</exclude>
                               <exclude>META-INF/*.DSA</exclude>
                               <exclude>META-INF/*.RSA</exclude>
                           </excludes>
                       </filter>
                   </filters>
                   <transformers>
                       <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                           <mainClass>zorp_the_solar_alien.ZorpTheSolarAlienLauncher</mainClass>
                       </transformer>
                   </transformers>
               </configuration>
           </execution>
       </executions>
   </plugin>
   ```

Verify locally:

```bash
mvn clean package
java -jar target/zorp-the-solar-alien.jar
```

The warning `Unsupported JavaFX configuration: classes were loaded from 'unnamed module'` is expected and does not affect the game.

---

## Step 3: Store save data in the home directory

A packaged app may be launched from a read-only folder, so the save file cannot sit next to the game. `ScoreManager` writes it to the user's home directory instead.

```java
private static final File SAVE_DIRECTORY = new File(System.getProperty("user.home"), ".zorp-the-solar-alien");
private static final File SAVE_FILE = new File(SAVE_DIRECTORY, "save-game.txt");
```

`saveToFile()` calls `SAVE_DIRECTORY.mkdirs()` and writes with `new PrintWriter(SAVE_FILE)`. `loadFromFile()` reads with `new Scanner(SAVE_FILE)`.

---

## Step 4: Add the release workflow

`.github/workflows/release.yml` runs whenever a tag starting with `v` is pushed. It builds the game on a Windows, an Intel macOS and a Linux runner. On each runner, `jpackage` wraps the jar and a Java runtime into a native app, and the result is zipped and uploaded to the release for that tag.

```yaml
name: Release

on:
  push:
    tags: ["v*"]

permissions:
  contents: write

jobs:
  build:
    strategy:
      matrix:
        include:
          - os: windows-latest
            platform: Windows
          - os: macos-15-intel
            platform: macOS
          - os: ubuntu-latest
            platform: Linux
    runs-on: ${{ matrix.os }}
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21

      - name: Build jar
        run: mvn -B clean package

      - name: Build app with bundled Java
        shell: bash
        run: |
          mkdir dist-input
          cp target/zorp-the-solar-alien.jar dist-input/
          jpackage --type app-image \
            --input dist-input \
            --main-jar zorp-the-solar-alien.jar \
            --main-class zorp_the_solar_alien.ZorpTheSolarAlienLauncher \
            --name ZorpTheSolarAlien \
            --dest dist

      - name: Zip app
        shell: bash
        run: |
          cd dist
          if [ "${{ matrix.platform }}" = "Windows" ]; then
            7z a ../ZorpTheSolarAlien-Windows.zip ZorpTheSolarAlien
          elif [ "${{ matrix.platform }}" = "macOS" ]; then
            ditto -c -k --keepParent ZorpTheSolarAlien.app ../ZorpTheSolarAlien-macOS.zip
          else
            zip -r ../ZorpTheSolarAlien-Linux.zip ZorpTheSolarAlien
          fi

      - name: Upload to release
        uses: softprops/action-gh-release@v2
        with:
          files: ZorpTheSolarAlien-*.zip
```

`jpackage` can only build for the operating system it runs on, which is why each platform gets its own runner. The macOS build uses an Intel runner to match the Intel `mac` JavaFX libraries from Step 2. It runs on Apple Silicon through Rosetta.

`permissions: contents: write` lets the workflow create the release and upload files to it.

---

## Step 5: Commit and push the setup

```bash
git add pom.xml src .github README.md
git commit -m "Add cross-platform release build"
git push
```

Pushing to `main` does not start a release. Only a version tag does.

---

## Step 6: Publish a new version

Make sure all changes for the version are committed and pushed to `main`, then create and push a tag:

```bash
git tag v1.1
git push origin v1.1
```

Version tags follow `vMAJOR.MINOR.PATCH`, for example `v1.0`, `v1.1` or `v2.0.1`. Each tag must be unique.

---

## Step 7: Watch the build

1. Open https://github.com/abdursujon/zorp-the-solar-alien/actions.
2. Select the **Release** run for the new tag.
3. Wait for all three jobs (Windows, macOS, Linux) to show a green tick. This takes about 5–10 minutes. Windows is usually the slowest.

If a job fails, open it, expand the red step and read the error. Fix the problem, then follow Step 9 to rebuild the same version.

---

## Step 8: Check the release

1. Open https://github.com/abdursujon/zorp-the-solar-alien/releases/latest.
2. Confirm it lists all three downloads:
   - `ZorpTheSolarAlien-Windows.zip`
   - `ZorpTheSolarAlien-macOS.zip`
   - `ZorpTheSolarAlien-Linux.zip`
3. Optionally click **Edit** on the release to add release notes describing what changed.
4. Test at least one download. On Linux:

   ```bash
   unzip ZorpTheSolarAlien-Linux.zip
   ./ZorpTheSolarAlien/bin/ZorpTheSolarAlien
   ```

---

## Step 9: Rebuild a failed or broken release

To reuse the same version number after a fix, delete the release and tag, then tag again.

1. On GitHub, open the release, click **Delete** and confirm.
2. Delete the tag locally and on GitHub:

   ```bash
   git tag -d v1.1
   git push origin --delete v1.1
   ```

3. Commit and push the fix, then tag again:

   ```bash
   git tag v1.1
   git push origin v1.1
   ```

---

## Notes for players

The Windows and macOS apps are not code-signed, so the operating system warns before the first launch:

- **Windows:** SmartScreen shows "Windows protected your PC". Click **More info**, then **Run anyway**.
- **macOS:** the app is blocked because the developer cannot be verified. Open **System Settings**, go to **Privacy & Security**, and click **Open Anyway**.

Removing these warnings requires a Windows code-signing certificate and an Apple Developer account for signing and notarisation.
