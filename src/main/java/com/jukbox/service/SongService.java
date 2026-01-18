package com.jukbox.service;

import com.jukbox.model.Song;
import com.jukbox.repository.Jukebox;
import com.jukbox.utility.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SongService {

    private final Jukebox jukebox;
    private List<Song> userSongList;

    public SongService(Jukebox jukebox) {
        this.jukebox = jukebox;
        this.userSongList = new ArrayList<>();
    }

    public List<Song> getUserSongList() {
        return userSongList;
    }

    public void setUserSongList(List<Song> userSongList) {
        this.userSongList = userSongList;
    }

    public List<Song> getAllSongs() {
        return jukebox.getAllSongs();
    }

    public Optional<Song> getSongFromJukebox(int songId) {
        return getAllSongs().stream()
                .filter(s -> s.getId() == songId)
                .findFirst();
    }

    public boolean addSongToUserList(Song song) {
        if (userSongList.contains(song)) {
            System.out.println("The song is already added.\n");
            return false;
        }

        userSongList.add(song);
        return true;
    }

    public boolean removeSongFromUserList(Song song) {
        if (userSongList.contains(song)) {
            userSongList.remove(song);
            return true;
        }

        System.out.println("The song does not exist in the list of songs.\n");
        return false;
    }

    public List<Song> searchByArtistAmongAllSongs(String artist) {
        return getAllSongs().stream()
                .filter(s -> s.getArtist().equalsIgnoreCase(artist))
                .sorted((s1, s2) -> s1.getTitle().compareTo(s2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByTitleAmongAllSongs(String title) {
        return getAllSongs().stream()
                .filter(s -> s.getTitle().equalsIgnoreCase(title))
                .sorted((s1, s2) -> s1.getTitle().compareTo(s2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByGenreAmongAllSongs(String genre) {
        return getAllSongs().stream()
                .filter(s -> s.getGenre().equalsIgnoreCase(genre))
                .sorted((s1, s2) -> s1.getGenre().compareTo(s2.getGenre()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByAlbumAmongAllSongs(String album) {
        return getAllSongs().stream()
                .filter(s -> s.getAlbum().equalsIgnoreCase(album))
                .sorted((s1, s2) -> s1.getAlbum().compareTo(s2.getAlbum()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByArtistAmongUserSongList(String artist) {
        return userSongList.stream()
                .filter(s -> s.getArtist().equalsIgnoreCase(artist))
                .sorted((s1, s2) -> s1.getArtist().compareTo(s2.getArtist()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByTitleAmongUserSongList(String title) {
        return userSongList.stream()
                .filter(s -> s.getTitle().equalsIgnoreCase(title))
                .sorted((s1, s2) -> s1.getTitle().compareTo(s2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByGenreAmongUserSongList(String genre) {
        return userSongList.stream()
                .filter(s -> s.getGenre().equalsIgnoreCase(genre))
                .sorted((s1, s2) -> s1.getGenre().compareTo(s2.getGenre()))
                .collect(Collectors.toList());
    }

    public List<Song> searchByAlbumAmongUserSongList(String album) {
        return userSongList.stream()
                .filter(s -> s.getAlbum().equalsIgnoreCase(album))
                .sorted((s1, s2) -> s1.getAlbum().compareTo(s2.getAlbum()))
                .collect(Collectors.toList());
    }

    public void playAllJukeboxSongsSequentially() {
        String mediaMessage = "";

        if (getAllSongs().isEmpty()) {
            System.out.println("No songs available in the jukebox.\n");
            return;
        }

        System.out.println("Playing songs sequentially....\n");

        for (Song song : getAllSongs()) {
            if (mediaMessage.equalsIgnoreCase("stop")){
                System.out.println("Stopping all songs playback.\n");
                break;
            }

            System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());

            try {
                mediaMessage = MediaPlayer.playMultipleTracks(song);
            } catch (Exception e) {
                System.out.println("Could not play song. Moving to the next song.\n");
                e.getMessage();
            }
        }

        if (!mediaMessage.equalsIgnoreCase("stop")) {
            System.out.println("All songs have now finished playing.\n");
        }
    }

    public void playAllUserListSongs() {
        String mediaMessage = "";

        if (userSongList.isEmpty()) {
            System.out.println("Your song list is empty.\n");
            return;
        }

        System.out.println("Playing your songs sequentially....");

        for (Song song : userSongList) {
            if (mediaMessage.equalsIgnoreCase("stop")){
                System.out.println("\nStopping all songs playback.\n");
                break;
            }

            System.out.println("\nNow playing: " + song.getTitle() + " by " + song.getArtist());

            try {
                mediaMessage = MediaPlayer.playMultipleTracks(song);
            } catch (Exception e) {
                System.out.println("Could not play song. Moving to the next song.\n");
                e.getMessage();
            }
        }

        if (!mediaMessage.equalsIgnoreCase("stop")) {
            System.out.println("\nAll songs have now finished playing.\n");
        }
    }

    public void playIndividualSong(Song song) {
        System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());

        try {
            MediaPlayer.playIndividualTrack(song);
        } catch (Exception e) {
            System.out.println("Could not play song.\n");
            e.getMessage();
        }

        System.out.println("Song finished playing.\n");
    }

}