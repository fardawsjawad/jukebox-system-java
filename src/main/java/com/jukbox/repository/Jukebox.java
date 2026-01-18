package com.jukbox.repository;

import com.jukbox.model.MediaItem;
import com.jukbox.model.Playlist;
import com.jukbox.model.Podcast;
import com.jukbox.model.Song;
import com.jukbox.utility.MediaLoader;

import java.util.*;

public class Jukebox {

    private List<Song> allSongs;
    private List<Podcast> allPodcasts;
    private Map<Playlist, Set<MediaItem>> playlists;

    public Jukebox(String songsFolderPath, String podcastsFolderPath) {
        this.allSongs = MediaLoader.loadSongsFromFolder(songsFolderPath);
        this.allPodcasts = MediaLoader.loadPodcastsFromFolder(podcastsFolderPath);
        this.playlists = new HashMap<>();
    }

    public List<Song> getAllSongs() {
        return allSongs;
    }

    public void setAllSongs(List<Song> allSongs) {
        this.allSongs = allSongs;
    }

    public List<Podcast> getAllPodcasts() {
        return allPodcasts;
    }

    public void setAllPodcasts(List<Podcast> allPodcasts) {
        this.allPodcasts = allPodcasts;
    }

    public Map<Playlist, Set<MediaItem>> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Map<Playlist, Set<MediaItem>> playlists) {
        this.playlists = playlists;
    }

    public MediaItem getMediaItemById(int mediaItemId) {
        for (Song song : allSongs) {
            if (song.getId() == mediaItemId)
                return song;
        }

        for (Podcast podcast : allPodcasts) {
            if (podcast.getId() == mediaItemId) {
                return podcast;
            }
        }

        return null;
    }

    public List<MediaItem> getMediaItemsById(int mediaItemId) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getId() == mediaItemId)
                mediaItems.add(song);
        }

        for (Podcast podcast : allPodcasts) {
            if (podcast.getId() == mediaItemId) {
                mediaItems.add(podcast);
            }
        }

        return mediaItems;
    }

    public List<MediaItem> getMediaItemsByGenre(String genre) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                mediaItems.add(song);
            }
        }

        for (Podcast podcast : allPodcasts) {
            if (podcast.getGenre().equalsIgnoreCase(genre)) {
                mediaItems.add(podcast);
            }
        }

        return mediaItems;
    }

    public List<MediaItem> getMediaItemsByAlbum(String album) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getAlbum().equalsIgnoreCase(album)) {
                mediaItems.add(song);
            }
        }

        return mediaItems;
    }

    public List<MediaItem> getMediaItemsByArtistOrHost(String name) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Song song : allSongs) {
            if (song.getArtist().equalsIgnoreCase(name)) {
                mediaItems.add(song);
            }
        }

        for (Podcast podcast : allPodcasts) {
            if (podcast.getHost().equalsIgnoreCase(name)) {
                mediaItems.add(podcast);
            }
        }

        return mediaItems;
    }
}
