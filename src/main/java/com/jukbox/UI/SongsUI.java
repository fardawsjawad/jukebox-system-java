package com.jukbox.UI;

import com.jukbox.model.Song;
import com.jukbox.repository.Jukebox;
import com.jukbox.service.SongService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.System.exit;

public class SongsUI {

    private final Scanner scanner;
    private final SongService songService;

    public SongsUI(Scanner scanner, Jukebox jukebox) {
        this.scanner = scanner;
        this.songService = new SongService(jukebox);
    }

    public void displaySongsMenu() {
        while (true) {
            int userChoice = getUserChoice();

            switch (userChoice) {
                case 1 -> viewAllSongs();
                case 2 -> addSongToUserList();
                case 3 -> removeSongFromUserList();
                case 4 -> playAllJukeboxSongs();
                case 5 -> playAllUserSongs();
                case 6 -> playIndividualSong();
                case 7 -> search();
                case 8 -> {
                    System.out.println();
                    return;
                }
                case 9 -> {
                    System.out.println("Exiting the program, bye....");
                    exit(0);
                }
            }
        }
    }

    private int getUserChoice() {
        System.out.println("\n-------------Songs Menu-------------");
        System.out.println("\nPlease select an option:");
        System.out.println("1. View All Songs");
        System.out.println("2. Add a Song to your Song List");
        System.out.println("3. Remove a Song from your Song List");
        System.out.println("4. Play All Songs Available in the Jukebox");
        System.out.println("5. Play All Songs in your Song List");
        System.out.println("6. Play an Individual Song");
        System.out.println("7. Search Songs");
        System.out.println("8. Return to the Previous Menu");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
        String userChoice = scanner.nextLine();

        try {
            return Integer.parseInt(userChoice);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }

        return -1;
    }

    private void viewAllSongs() {
        System.out.println("\n1. View All Songs Available in the Jukebox");
        System.out.println("2. View All Songs in your Song List");
        System.out.print("Enter your choice: ");
        String userChoice = scanner.nextLine();

        try {
            int choice = Integer.parseInt(userChoice);

            switch (choice) {
                case 1 -> viewAllJukeboxSongs();
                case 2 -> viewAllUserSongs();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void viewAllJukeboxSongs() {
        List<Song> songs = songService.getAllSongs();

        if (songs.isEmpty()) {
            System.out.println("There are no songs available in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Available Songs in the Jukebox-------------");
        songs.forEach(System.out::println);
    }

    private void viewAllUserSongs() {
        List<Song> songs = songService.getUserSongList();

        if (songs.isEmpty()) {
            System.out.println("There are no songs available in your list.\n");
            return;
        }

        System.out.println("\n-------------Songs in your Song List-------------");
        songs.forEach(System.out::println);
    }

    private void addSongToUserList() {
        System.out.print("\nEnter Song ID to Add: ");
        String songIdStr = scanner.nextLine();

        try {
            int songId = Integer.parseInt(songIdStr);

            Optional<Song> optionalSong = songService.getSongFromJukebox(songId);

            if (optionalSong.isEmpty()) {
                System.out.println("The song with the specified ID does not exist.\n");
                return;
            }

            Song song = optionalSong.get();

            boolean songAdded = songService.addSongToUserList(song);

            if (songAdded) {
                System.out.println("Song successfully added to your list.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void removeSongFromUserList() {
        System.out.print("\nEnter song ID: ");
        String songIdStr = scanner.nextLine();

        try {
            int songId = Integer.parseInt(songIdStr);

            Optional<Song> optionalSong = songService.getSongFromJukebox(songId);
            if (optionalSong.isEmpty()) {
                System.out.println("The song with the specified ID does not exist.\n");
                return;
            }

            Song song = optionalSong.get();
            boolean songRemoved = songService.removeSongFromUserList(song);

            if (songRemoved) {
                System.out.println("Song successfully removed from your list.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void playAllJukeboxSongs() {
        songService.playAllJukeboxSongsSequentially();
    }

    private void playAllUserSongs() {
        songService.playAllUserListSongs();
    }

    private void playIndividualSong() {
        System.out.print("\nEnter song ID: ");
        String songIdStr = scanner.nextLine();

        try {
            int songId = Integer.parseInt(songIdStr);

            Optional<Song> optionalSong = songService.getSongFromJukebox(songId);
            if (optionalSong.isEmpty()) {
                System.out.println("Song with the specified ID does not exist.\n");
                return;
            }

            Song song = optionalSong.get();
            songService.playIndividualSong(song);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void search() {
        System.out.println("\n-------------Search Songs-------------");
        System.out.println("1. Search Among All Songs Available in the Jukebox");
        System.out.println("2. Search in your Song List");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchAmongAllJukeboxSongs();
                case 2 -> searchAmongUserSongs();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, enter a valid number.\n");
        }
    }

    private void searchAmongAllJukeboxSongs() {
        System.out.println("\n-------------Search for Songs in the Jukebox-------------");
        System.out.println("1. Search by Artist");
        System.out.println("2. Search by Genre");
        System.out.println("3. Search by Album");
        System.out.println("4. Search by Title");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchByArtistAmongAllSongs();
                case 2 -> searchByGenreAmongAllSongs();
                case 3 -> searchByAlbumAmongAllSongs();
                case 4 -> searchByTitleAmongAllSongs();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void searchAmongUserSongs() {
        System.out.println("\n-------------Search for Songs in your List-------------");
        System.out.println("1. Search by Artist");
        System.out.println("2. Search by Genre");
        System.out.println("3. Search by Album");
        System.out.println("4. Search By Title");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchByArtistAmongUserSongs();
                case 2 -> searchByGenreAmongUserSongs();
                case 3 -> searchByAlbumAmongUserSongs();
                case 4 -> searchByTitleAmongUserSongs();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void searchByArtistAmongAllSongs() {
        System.out.print("Enter Artist Name: ");
        String artist = scanner.nextLine();

        List<Song> songs = songService.searchByArtistAmongAllSongs(artist);

        if (songs.isEmpty()) {
            System.out.println("There are no songs available for '" + artist + "' artist in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Songs by " + artist + "-------------");
        songs.forEach(System.out::println);
    }

    private void searchByGenreAmongAllSongs() {
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        List<Song> songs = songService.searchByGenreAmongAllSongs(genre);

        if (songs.isEmpty()) {
            System.out.println("There are no songs available under the '" + genre + "' genre in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Songs under the '" + genre + "' Genre-------------");
        songs.forEach(System.out::println);
    }

    private void searchByAlbumAmongAllSongs() {
        System.out.print("Enter Album Name: ");
        String album = scanner.nextLine();

        List<Song> songs = songService.searchByAlbumAmongAllSongs(album);

        if (songs.isEmpty()) {
            System.out.println("There are no albums named '" + album + "' in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Songs in the '" + album + "' Album-------------");
        songs.forEach(System.out::println);
    }

    private void searchByTitleAmongAllSongs() {
        System.out.print("Enter Song Title: ");
        String title = scanner.nextLine();

        List<Song> songs = songService.searchByTitleAmongAllSongs(title);

        if (songs.isEmpty()) {
            System.out.println("There are no songs by the '" + title + "' title in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Songs with the '" + title + "' Title-------------");
        songs.forEach(System.out::println);
    }

    private void searchByArtistAmongUserSongs() {
        System.out.print("Enter Artist Name: ");
        String artist = scanner.nextLine();

        List<Song> songs = songService.searchByArtistAmongUserSongList(artist);

        if (songs.isEmpty()) {
            System.out.println("There are no songs available for '" + artist + "' artist in your song list.\n");
            return;
        }

        System.out.println("\n-------------Songs by " + artist + " in Your Song List-------------");
        songs.forEach(System.out::println);
    }

    private void searchByGenreAmongUserSongs() {
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        List<Song> songs = songService.searchByGenreAmongUserSongList(genre);

        if (songs.isEmpty()) {
            System.out.println("There are no songs available under the '" + genre + "' genre in your song list.\n");
            return;
        }

        System.out.println("\n-------------Songs under the '" + genre + "' Genre in your Song List-------------");
        songs.forEach(System.out::println);
    }

    private void searchByAlbumAmongUserSongs() {
        System.out.print("Enter Album Name: ");
        String album = scanner.nextLine();

        List<Song> songs = songService.searchByAlbumAmongUserSongList(album);

        if (songs.isEmpty()) {
            System.out.println("There are no albums named '" + album + "' in your song list.\n");
            return;
        }

        System.out.println("\n-------------Songs in the '" + album + "' Album in your Song List-------------");
        songs.forEach(System.out::println);
    }

    private void searchByTitleAmongUserSongs() {
        System.out.print("Enter Song Title: ");
        String title = scanner.nextLine();

        List<Song> songs = songService.searchByTitleAmongUserSongList(title);

        if (songs.isEmpty()) {
            System.out.println("There are no songs by the '" + title + "' title in your song list..\n");
            return;
        }

        System.out.println("\n-------------Songs with the '" + title + "' Title in your Song List-------------");
        songs.forEach(System.out::println);
    }
}
