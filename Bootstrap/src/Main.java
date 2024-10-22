import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.json.ExternalFile;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.SplashScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Main {
    private static GameInfos gameInfos = new GameInfos("tutolauncher", new GameVersion("1.16.5", GameType.V1_13_HIGHER_VANILLA), new GameTweak[]{});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "crashes");
    public static File launcherFile = new File(String.valueOf(path), "launchers");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), path);

    public static void main(String[] args) throws Exception {
        crashFile.mkdirs();
        launcherFile.mkdirs();

        showSplashScreen();
        doUpdate();
        launchLauncher();
    }


    public static void showSplashScreen() throws IOException {
        SplashScreen splashScreen = new SplashScreen("TutoLauncher Updater", ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("ressources/logo.png"))));
        splashScreen.displayFor(5000L);
    }

    public static void doUpdate() throws Exception {
        FlowUpdater flowUpdater = new FlowUpdater.FlowUpdaterBuilder().withExternalFiles(ExternalFile.getExternalFilesFromJson("https://github.com/Jachou-yt/TutoLauncher/launcher_update/launcher.json")).build();
        flowUpdater.update(path);
    }

    public static void launchLauncher() throws LaunchException {
        ClasspathConstructor constructor = new ClasspathConstructor();
        constructor.add((List<Path>) new File(launcherFile, "TutoLauncher.jar"));

        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.jachou.tutolauncher.Frame", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p = launcher.launch();
    }


}
