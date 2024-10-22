package fr.jachou.tutolauncher;

import com.azuriom.azauth.AuthClient;
import com.azuriom.azauth.exception.AuthException;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.download.json.MCP;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.Saver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static fr.jachou.tutolauncher.Frame.getSaver;

public class Launcher {
    private static GameInfos gameInfos = new GameInfos("tutolauncher", new GameVersion("1.16.5", GameType.V1_13_HIGHER_VANILLA), new GameTweak[]{});
    private static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "crashes");
    private static CrashReporter reporter = new CrashReporter(String.valueOf(crashFile), path);
    private static AuthInfos authInfos;


    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result = null;
        if (refresh_token != null && !refresh_token.isEmpty()) {
            result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        } else {
            result = microsoftAuthenticator.loginWithWebview();
            Frame.getSaver().set("refresh_token", result.getRefreshToken());
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        }
    }

    /*public static void auth(String username, String password) throws AuthException {
        AuthClient authenticator = new AuthClient("https://youtube.frouzie.fr");

        authInfos = authenticator.login(username, password, () -> {
            String code = null;

            while (code == null || code.isEmpty()) {
                // The parent component for the dialog. You should replace the code
                // below with an instance of your launcher frame/panel/etc
                Container parentComponent = Frame.getInstance().getPanel();
                parentComponent.setVisible(true);

                code = JOptionPane.showInputDialog(parentComponent, "Enter your 2FA code", "2FA", JOptionPane.PLAIN_MESSAGE);
            }

            return code;
        }, AuthInfos.class);
    }*/

    public static void crack() {
        authInfos = new AuthInfos("Jachou", "5655121548", "dezybfzuyfbezuyfbu");
    }

    public static void update() throws Exception {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.16.5").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();


        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).build();
        updater.update(path);
    }

    public static void launch() throws Exception {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(gameInfos, GameFolder.FLOW_UPDATER, authInfos);
        ExternalLauncher launcher = new ExternalLauncher(profile);
        launcher.launch();
    }

    public static CrashReporter getReporter() {
        return reporter;
    }

    public static Path getPath() {
        return path;
    }

    public static AuthInfos getAuthInfos() {
        return authInfos;
    }
}
