package fr.jachou.tutolauncher;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.jachou.tutolauncher.utils.Animation;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Frame extends JFrame {

    private static Frame instance;
    private Panel panel;
    private static File ramFile = new File(String.valueOf(Launcher.getPath()), "ram.txt");
    private static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");
    private static Saver saver = new Saver(saverFile);

    public Frame() throws IOException {
        instance = this;
        this.setTitle("TutoLauncher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(522, 600);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImage("bouton.png"));
        this.setContentPane(panel = new Panel());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        Animation.fadeInFrame(this, 30);

        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Launcher.crashFile.mkdirs();
        if (!ramFile.exists()) {
            ramFile.createNewFile();
        }
        if (!saverFile.exists()) {
            saverFile.createNewFile();
        }

        instance = new Frame();

        launchRPC();
    }

    public static void launchRPC() {
        final DiscordRPC lib = DiscordRPC.INSTANCE;
        final String appID = "1110990670035963994";
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(appID, handlers, true, "");
        DiscordRichPresence discordRichPresence = new DiscordRichPresence();
        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000;
        discordRichPresence.details = "Playing Tutolauncher";
        discordRichPresence.state = "Jachou";

        lib.Discord_UpdatePresence(discordRichPresence);
    }



    public static Image getImage(String fichier) throws IOException {
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(fichier);
        return ImageIO.read(inputStream);
    }

    public static BufferedImage getBufferedImage(String fichier) throws IOException {
        InputStream inputStream = Frame.getInstance().getClass().getClassLoader().getResourceAsStream(fichier);
        return ImageIO.read(inputStream);
    }

    public static Frame getInstance() {
        return instance;
    }

    public Panel getPanel() {
        return this.panel;
    }

    public static File getRamFile() {
        return ramFile;
    }

    public static Saver getSaver() {
        return saver;
    }
}
