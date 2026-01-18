package com.jukbox.UI;

import com.jukbox.model.MediaItem;
import com.jukbox.model.Podcast;
import com.jukbox.model.Song;
import com.jukbox.repository.Jukebox;
import com.jukbox.utility.MediaPlayer;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class BaseUI {

    private final Scanner scanner;
    private final Jukebox jukebox;
    private final SongsUI songsUI;
    private final PodcastsUI podcastsUI;
    private final PlaylistsUI playlistsUI;

    public BaseUI () {
        this.scanner = new Scanner(System.in);
        this.jukebox = new Jukebox("/Users/f/Desktop/IntelliJ/Jukebox/Songs", "/Users/f/Desktop/IntelliJ/Jukebox/Podcasts");
        this.songsUI = new SongsUI(scanner, jukebox);
        this.podcastsUI = new PodcastsUI(scanner, jukebox);
        this.playlistsUI = new PlaylistsUI(scanner, jukebox);
    }

    public void displayMenu() {
        System.out.println("===========================");
        System.out.println("\tWelcome to Jukebox");
        System.out.println("===========================");

        while (true) {
            int userChoice = getUserChoice();

            switch (userChoice) {
                case 1 -> songsUI.displaySongsMenu();
                case 2 -> podcastsUI.displayPodcastsMenu();
                case 3 -> playlistsUI.displayPlaylistsMenu();
                case 4 -> search();
                case 5 -> playTrack();
                case 6 -> {
                    System.out.println("Exiting the program, bye...");
                    exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private int getUserChoice() {
        System.out.println("Please select an option: ");
        System.out.println("1. Songs");
        System.out.println("2. Podcasts");
        System.out.println("3. Playlists");
        System.out.println("4. Search for Tracks");
        System.out.println("5. Play a Track");
        System.out.println("6. Exit");
        System.out.println("===========================");
        System.out.print("Enter your choice: ");
        String userChoice = scanner.nextLine();

        try {
            return Integer.parseInt(userChoice);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }

        return -1;
    }

    private void search() {
        System.out.print("\nEnter Track ID: ");
        String mediaItemIdStr = scanner.nextLine();

        try {
            int mediaItemId = Integer.parseInt(mediaItemIdStr);

            List<MediaItem> mediaItems = jukebox.getMediaItemsById(mediaItemId);
            if (mediaItems.isEmpty()) {
                System.out.println("No tracks exist for the specified ID.\n");
                return;
            }

            for (MediaItem mediaItem : mediaItems) {
                if (mediaItem instanceof Song) {
                    System.out.println("- Songs: ");
                    System.out.println(mediaItem);
                } else if (mediaItem instanceof Podcast) {
                    System.out.println();
                    System.out.println("- Podcasts: ");
                    System.out.println(mediaItem);
                }
            }

            System.out.println();
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void playTrack() {
        System.out.print("\nEnter Track ID: ");
        String trackIdStr = scanner.nextLine();

        try {
            int trackId = Integer.parseInt(trackIdStr);

            MediaItem mediaItem = jukebox.getMediaItemById(trackId);
            if (mediaItem == null) {
                System.out.println("No tracks exist for the specified ID.\n");
                return;
            }

            MediaPlayer.playIndividualTrack(mediaItem);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        } catch (Exception e) {
            System.out.println("Could not play track due to some error.\n");
        }
    }

}
