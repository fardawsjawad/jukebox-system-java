package com.jukbox.UI;

import com.jukbox.model.MediaItem;
import com.jukbox.model.Playlist;
import com.jukbox.repository.Jukebox;
import com.jukbox.service.PlaylistService;

import java.time.LocalDate;
import java.util.*;

import static java.lang.System.exit;

public class PlaylistsUI {

    private final Scanner scanner;
    private final PlaylistService playlistService;

    public PlaylistsUI(Scanner scanner, Jukebox jukebox) {
        this.scanner = scanner;
        this.playlistService = new PlaylistService(jukebox);
    }

    public void displayPlaylistsMenu() {
        while (true) {
            int userChoice = getUserChoice();

            switch (userChoice) {
                case 1 -> viewAllPlaylists();
                case 2 -> viewAllTracksWithinAPlaylist();
                case 3 -> createPlaylist();
                case 4 -> deletePlaylist();
                case 5 -> addTrackToPlaylist();
                case 6 -> playPlaylistTracks();
                case 7 -> {
                    System.out.println();
                    return;
                }
                case 8 -> exit(0);
            }
        }
    }

    private int getUserChoice() {
        System.out.println("\n-------------Playlists Menu-------------");
        System.out.println("\nPlease select an option:");
        System.out.println("1. View All Playlists");
        System.out.println("2. View All Tracks Within a Playlist");
        System.out.println("3. Create a Playlist");
        System.out.println("4. Delete a Playlist");
        System.out.println("5. Add a Track to a Playlist");
        System.out.println("6. Play Playlist Tracks");
        System.out.println("7. Return to the Previous Menu");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            return Integer.parseInt(userChoiceStr);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }

        return -1;
    }

    private void viewAllPlaylists() {
        Map<Playlist, Set<MediaItem>> playlists = playlistService.getAllPlaylists();

        if (playlists.isEmpty()) {
            System.out.println("You have not created any playlists yet.\n");
            return;
        }

        System.out.println("\n-------------Your Playlists-------------");

        playlists.forEach((playlistName, mediaItems) -> {
            System.out.println(playlistName);
        });
    }

    private void viewAllTracksWithinAPlaylist() {
        System.out.print("\nEnter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);

        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        Set<MediaItem> playlistTracks = playlistService.getAllTracksWithinAPlaylist(playlist);

        if (playlistTracks.isEmpty()) {
            System.out.println("No tracks have been added to the playlist yet. The playlist is empty.\n");
            return;
        }

        System.out.println("\n-------------Tracks Under the '" + playlistName + "' Playlist-------------");
        playlistTracks.forEach(System.out::println);
    }

    private void createPlaylist() {
        int newPlaylistId;

        Map<Playlist, Set<MediaItem>> playlists = playlistService.getAllPlaylists();
        Map<Playlist, Set<MediaItem>> sortedMap = new TreeMap<>(playlists);

        Playlist lastKeyPlaylist = null;
        for (Playlist key : sortedMap.keySet()) {
            lastKeyPlaylist = key;
        }

        if (lastKeyPlaylist != null) {
            newPlaylistId = lastKeyPlaylist.getPlaylistId() + 1;
        } else {
            newPlaylistId = 1;
        }

        Playlist playlist = getPlaylistToCreate(newPlaylistId);

        boolean playlistCreated = playlistService.addPlaylist(playlist);

        if (playlistCreated) {
            System.out.println("Playlist successfully created.\n");
        }
    }

    private Playlist getPlaylistToCreate(int playlistId) {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();
        System.out.print("Playlist Description (Optional): ");
        String playlistDescription = scanner.nextLine();

        return new Playlist(
                playlistId, playlistName, playlistDescription,
                LocalDate.now(), new HashSet<>()
        );
    }

    private void deletePlaylist() {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        boolean playlistDeleted = playlistService.removePlaylist(playlist);

        if (playlistDeleted) {
            System.out.println("Playlist successfully deleted.\n");
        }
    }

    private void addTrackToPlaylist() {
        if (playlistService.getAllPlaylists().isEmpty()) {
            System.out.println("You have not created any playlists yet.\n");
            return;
        }

        System.out.println("Select one of the Following Options: ");
        System.out.println("1. Add a Single Track");
        System.out.println("2. Add All Tracks Under a Genre");
        System.out.println("3. Add All Tracks Under an Album");
        System.out.println("4. Add All Tracks by Artist/Celebrity(Host)");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> addSingleTrack();
                case 2 -> addTracksUnderGenre();
                case 3 -> addTracksUnderAlbum();
                case 4 -> addTracksByArtistOrHost();
                default -> System.out.println("Invalid input.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, enter a valid number.\n");
        }
    }

    private void addSingleTrack() {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        System.out.print("Enter Track ID: ");
        String trackIdStr = scanner.nextLine();

        try {
            int trackId = Integer.parseInt(trackIdStr);

            MediaItem mediaItem = playlistService.getMediaItemById(trackId);
            if (mediaItem == null) {
                System.out.println("No tracks exist with the provided ID. Please enter a valid track ID.\n");
                return;
            }

            boolean trackAdded = playlistService.addTrackToPlaylist(playlist, mediaItem);

            if (trackAdded) {
                System.out.println("Track successfully added to the playlist.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void addTracksUnderGenre() {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        List<MediaItem> mediaItems = playlistService.getMediaItemsByGenre(genre);
        if (mediaItems.isEmpty()) {
            System.out.println("No tracks exist for the specified genre.\n");
            return;
        }

        boolean tracksAdded = false;
        for (MediaItem mediaItem : mediaItems) {
            tracksAdded = playlistService.addTrackToPlaylist(playlist, mediaItem);
        }

        if (tracksAdded) {
            System.out.println("All tracks under the specified genre has been successfully added to the playlist.\n");
        }
    }

    private void addTracksUnderAlbum() {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        System.out.print("Enter Album Name: ");
        String album = scanner.nextLine();

        List<MediaItem> mediaItems = playlistService.getMediaItemsByAlbum(album);
        if (mediaItems.isEmpty()) {
            System.out.println("No tracks exist for the specified album.\n");
            return;
        }

        boolean tracksAdded = false;
        for (MediaItem mediaItem : mediaItems) {
            tracksAdded = playlistService.addTrackToPlaylist(playlist, mediaItem);
        }

        if (tracksAdded) {
            System.out.println("All tracks under the specified album has been successfully added to the playlist.\n");
        }
    }

    private void addTracksByArtistOrHost() {
        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        System.out.print("Enter Artist/Celebrity(Host) Name: ");
        String name = scanner.nextLine();

        List<MediaItem> mediaItems = playlistService.getMediaItemsByArtistOrHost(name);
        if (mediaItems.isEmpty()) {
            System.out.println("No tracks exist for the specified artist/celebrity.\n");
            return;
        }

        boolean tracksAdded = false;
        for (MediaItem mediaItem : mediaItems) {
            tracksAdded = playlistService.addTrackToPlaylist(playlist, mediaItem);
        }

        if (tracksAdded) {
            System.out.println("All tracks of the specified artist/host has been successfully added to the playlist.\n");
        }
    }

    private void playPlaylistTracks() {
        if (playlistService.getAllPlaylists().isEmpty()) {
            System.out.println("You have not created any playlists yet.\n");
            return;
        }

        System.out.print("Enter Playlist Name: ");
        String playlistName = scanner.nextLine();

        Playlist playlist = playlistService.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("No playlists exist with the specified name.\n");
            return;
        }

        System.out.println("\nSelect the Mode you want to Play the Tracks in: ");
        System.out.println("1. Sequentially");
        System.out.println("2. In Shuffle Mode");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> playlistService.playAllPlaylistTracksSequentially(playlist);
                case 2 -> playlistService.playAllPlaylistTracksInShuffleMode(playlist);
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

}

