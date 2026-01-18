package com.jukbox.utility;

import com.jukbox.model.Podcast;
import com.jukbox.model.Song;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaLoader {

    private MediaLoader() {  }

    public static List<Song> loadSongsFromFolder(String folderPath) {
        List<Song> songs = new ArrayList<>();

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path.");
            return new ArrayList<>();
        }

        File[] wavFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        });

        if (wavFiles == null || wavFiles.length == 0) {
            System.out.println("No audio files found.");
            return new ArrayList<>();
        }

        String[] genres = {"Pop", "Rock", "Jazz", "Classical", "Hip Hop", "Electronic"};
        String[] artists = {"Taylor Swift", "Drake", "Adele", "BTS", "Ed Sheeran", "Billie Eilish", "The Weeknd", "Bad Bunny"};
        String[] albums = {
                "Folklore", "Scorpion", "25",
                "Map of the Soul: 7", "÷ (Divide)", "Happier Than Ever",
                "After Hours", "YHLQMDLG"
        };

        Random random = new Random();

        int counter = 1;

        for (File file : wavFiles) {
            String fileName = file.getName();
            String title = fileName.replaceAll("\\.(wav)$", "");

            String randomGenre = genres[random.nextInt(genres.length)];
            String randomArtist = artists[random.nextInt(artists.length)];
            String randomAlbum = albums[random.nextInt(albums.length)];

            int durationInMilliSeconds = getWavDurationInMilliSeconds(file);

            Song song = new Song(
                    counter,
                    title,
                    randomArtist,
                    randomAlbum,
                    randomGenre,
                    durationInMilliSeconds,
                    file.getAbsolutePath()
            );

            songs.add(song);

            counter++;
        }

        return songs;
    }

    public static List<Podcast> loadPodcastsFromFolder(String folderPath) {
        List<Podcast> podcasts = new ArrayList<>();

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path.");
            return new ArrayList<>();
        }

        File[] wavFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".wav");
            }
        });

        if (wavFiles == null || wavFiles.length == 0) {
            System.out.println("No audio files found.");
            return new ArrayList<>();
        }

        String[] hosts = {"Dax Shepard", "Michelle Obama", "Oprah Winfrey", "Joe Rogan", "Jason Bateman", "Emma Chamberlain"};
        String[] genres = {"True Crime", "Comedy", "Health & Wellness", "Business & Entrepreneurship", "News & Politics", "Society & Culture"};
        String[] languages = {"English", "Spanish", "German"};

        Random random = new Random();

        int counter = 1;

        for (File file : wavFiles) {
            String fileName = file.getName();
            String title = fileName.replaceAll("\\.(wav)$", "");

            String randomHost = hosts[random.nextInt(hosts.length)];
            String randomGenre = genres[random.nextInt(genres.length)];
            String randomLanguage = languages[random.nextInt(languages.length)];

            int durationInMilliSeconds = getWavDurationInMilliSeconds(file);

            Podcast podcast = new Podcast(
                    counter,
                    title,
                    randomHost,
                    randomGenre,
                    randomLanguage,
                    getRandomDate(LocalDate.of(2010, 1, 1), LocalDate.now()),
                    durationInMilliSeconds,
                    file.getAbsolutePath()
            );

            podcasts.add(podcast);

            counter++;
        }

        return podcasts;
    }

    private static int getWavDurationInMilliSeconds(File file) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {

            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            float frameRate = format.getFrameRate();
            float durationInSeconds = frames / frameRate;
            return (int) (durationInSeconds * 1000);

        } catch (UnsupportedAudioFileException | IOException e) {
            System.err.println("Could not read duration of file: " + file.getName());
            return 0;
        }
    }

    public static LocalDate getRandomDate(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        Random random = new Random();
        long randomDays = random.nextInt((int) days + 1);
        return start.plusDays(randomDays);
    }

}
