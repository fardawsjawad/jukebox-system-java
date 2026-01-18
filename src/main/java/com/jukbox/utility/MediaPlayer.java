package com.jukbox.utility;

import com.jukbox.model.MediaItem;
import com.jukbox.model.Podcast;
import com.jukbox.model.Song;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MediaPlayer {

    private MediaPlayer() {  }

    public static String playMultipleTracks(MediaItem mediaItem) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = null;

        if (mediaItem instanceof Song) {
            Song song = (Song) mediaItem;
            audioStream = AudioSystem.getAudioInputStream(new File(song.getFilePath()).getAbsoluteFile());
        } else if (mediaItem instanceof Podcast) {
            Podcast podcast = (Podcast) mediaItem;
            audioStream = AudioSystem.getAudioInputStream(new File(podcast.getFilePath()).getAbsoluteFile());
        }

        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();

        Scanner scanner = new Scanner(System.in);
        String command = "";
        System.out.println("\nCommands: pause | resume | stop | reset | skip");

        while (true) {
            if (!clip.isRunning() && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                clip.close();
                return "next";
            }

            // Only prompt when song is still playing or paused
            if (System.in.available() > 0) {  // Check if user typed something
                command = scanner.nextLine().toLowerCase().trim();

                switch (command) {
                    case "pause" -> clip.stop();
                    case "resume" -> clip.start();
                    case "stop" -> {
                        clip.stop();
                        clip.close();
                        return "stop";
                    }
                    case "reset" -> clip.setMicrosecondPosition(0);
                    case "skip" -> {
                        clip.stop();
                        clip.close();
                        return "skip";
                    }
                    default -> System.out.println("Invalid command.");
                }
            }

            // Small sleep to avoid tight loop
            try {
                Thread.sleep(100);  // 100 ms polling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void playIndividualTrack(MediaItem mediaItem) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = null;

        if (mediaItem instanceof Song) {
            Song song = (Song) mediaItem;
            audioStream = AudioSystem.getAudioInputStream(new File(song.getFilePath()).getAbsoluteFile());
        } if (mediaItem instanceof Podcast) {
            Podcast podcast = (Podcast) mediaItem;
            audioStream = AudioSystem.getAudioInputStream(new File(podcast.getFilePath()).getAbsoluteFile());
        }

        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();

        Scanner scanner = new Scanner(System.in);
        String command = "";
        System.out.println("\nCommands: pause | resume | stop | reset");

        while (true) {
            if (!clip.isRunning() && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                clip.stop();
                clip.close();
                return;
            }

            if (System.in.available() > 0) {
                command = scanner.nextLine().toLowerCase().trim();

                switch (command) {
                    case "pause" -> clip.stop();
                    case "resume" -> clip.start();
                    case "stop" -> {
                        clip.stop();
                        clip.close();
                        return;
                    }
                    case "reset" -> clip.setMicrosecondPosition(0);
                    default -> System.out.println("Invalid command.");
                }
            }

            try {
                Thread.sleep(100);  // 100 ms polling
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
