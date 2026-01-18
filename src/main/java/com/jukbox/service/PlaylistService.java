package com.jukbox.service;

import com.jukbox.model.MediaItem;
import com.jukbox.model.Playlist;
import com.jukbox.model.Podcast;
import com.jukbox.model.Song;
import com.jukbox.repository.Jukebox;
import com.jukbox.utility.MediaPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class PlaylistService {

    private final Jukebox jukebox;

    public PlaylistService(Jukebox jukebox) {
        this.jukebox = jukebox;
    }

    public MediaItem getMediaItemById(int mediaItemId) {
        return jukebox.getMediaItemById(mediaItemId);
    }

    public List<MediaItem> getMediaItemsByGenre(String genre) {
        return jukebox.getMediaItemsByGenre(genre);
    }

    public List<MediaItem> getMediaItemsByAlbum(String album) {
        return jukebox.getMediaItemsByAlbum(album);
    }

    public List<MediaItem> getMediaItemsByArtistOrHost(String name) {
        return jukebox.getMediaItemsByArtistOrHost(name);
    }

    public Map<Playlist, Set<MediaItem>> getAllPlaylists() {
        return jukebox.getPlaylists();
    }

    public Playlist getPlaylist(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = jukebox.getPlaylists();

        return playlists.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(playlist))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(null);
    }

    public Playlist getPlaylistByName(String playlistName) {
        Map<Playlist, Set<MediaItem>> playlists = jukebox.getPlaylists();

        for (Map.Entry<Playlist, Set<MediaItem>> entry : playlists.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(playlistName)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public boolean addPlaylist(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = jukebox.getPlaylists();

        if (playlists.containsKey(playlist)) {
            System.out.println("Playlist with the same name is already created.\n");
            return false;
        }

        playlists.put(playlist, new HashSet<>());
        return true;
    }

    public boolean removePlaylist(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("The specified playlist does not exist.\n");
            return false;
        }

        playlists.remove(playlist);
        return true;
    }

    public Set<MediaItem> getAllTracksWithinAPlaylist(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("The specified playlist does not exist.\n");
            return new HashSet<>();
        }

        for (Map.Entry<Playlist, Set<MediaItem>> entry : playlists.entrySet()) {
            if (entry.getKey().equals(playlist)) {
                return entry.getValue();
            }
        }

        return new HashSet<>();
    }

    public boolean addTrackToPlaylist(Playlist playlist, MediaItem mediaItem) {
        Map<Playlist, Set<MediaItem>> playlists = jukebox.getPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("No playlists exists with the specified name.\n");
            return false;
        }

        Set<MediaItem> mediaItems = playlists.get(playlist);

        if (mediaItems.contains(mediaItem)) {
            System.out.println("The track is already added to the playlist.\n");
            return false;
        }

        mediaItems.add(mediaItem);
        return true;
    }

    public boolean removeTrackFromPlaylist(Playlist playlist, MediaItem mediaItem) {
        Map<Playlist, Set<MediaItem>> playlists = jukebox.getPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("No playlists exist with the specified name.\n");
            return false;
        }

        Set<MediaItem> mediaItems = playlists.get(playlist);

        if (!mediaItems.contains(mediaItem)) {
            System.out.println("The specified track does not exist in the playlist.\n");
            return false;
        }

        mediaItems.remove(mediaItem);
        return true;
    }

    public Map<Playlist, Set<MediaItem>> searchForPlaylist(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        return playlists.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(playlist))
                .findFirst()
                .map(entry -> {
                    Map<Playlist, Set<MediaItem>> result = new HashMap<>();
                    result.put(entry.getKey(), entry.getValue());
                    return result;
                })
                .orElseGet(HashMap::new);
    }

    public MediaItem searchForTrackWithinAPlaylist(Playlist playlist, MediaItem mediaItem) {
        if (mediaItem == null || playlist == null) {
            System.out.println("Media Item and playlist cannot be null.\n");
            return null;
        }

        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("The specified playlist does not exist.\n");
            return null;
        }

        return playlists.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(playlist))
                .findFirst()
                .map(entry -> entry.getValue())
                .orElseGet(HashSet::new)
                .stream()
                .filter(item -> item.getId() == mediaItem.getId())
                .findFirst()
                .orElse(null);
    }

    public void playAllPlaylistTracksSequentially(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("The specified playlist does not exist.\n");
            return;
        }

        Set<MediaItem> mediaItems = playlists.get(playlist);
        if (mediaItems.isEmpty()) {
            System.out.println("No tracks available in the " + playlist.getName() + " playlist.\n");
            return;
        }

        System.out.println("Playing all tracks in the '" + playlist.getName() + "' playlist sequentially...\n");

        List<MediaItem> sortedMediaItems = mediaItems
                .stream()
                .sorted((item1, item2) -> Integer.compare(item1.getId(), item2.getId()))
                .collect(Collectors.toList());

        String mediaResponse = "";
        for (MediaItem mediaItem : sortedMediaItems) {
            if (mediaResponse.equalsIgnoreCase("stop")) {
                System.out.println("Stopping all tracks playback.\n");
                break;
            }

            if (mediaItem instanceof Song) {
                Song song = (Song) mediaItem;
                System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
            } else if (mediaItem instanceof Podcast) {
                Podcast podcast = (Podcast) mediaItem;
                System.out.println("Now playing: " + podcast.getTitle() + " by " + podcast.getHost());
            }

            try {
                mediaResponse = MediaPlayer.playMultipleTracks(mediaItem);
            } catch (Exception e) {
                System.out.println("Could not play track. Moving to the next track.\n");
                e.getMessage();
            }
        }

        if (!mediaResponse.equalsIgnoreCase("stop")) {
            System.out.println("All tracks have now finished playing.\n");
        }
    }

    public void playAllPlaylistTracksInShuffleMode(Playlist playlist) {
        Map<Playlist, Set<MediaItem>> playlists = getAllPlaylists();

        if (!playlists.containsKey(playlist)) {
            System.out.println("The specified playlist does not exist.\n");
            return;
        }

        Set<MediaItem> mediaItems = playlists.get(playlist);
        if (mediaItems.isEmpty()) {
            System.out.println("No tracks available in the " + playlist.getName() + " playlist.\n");
            return;
        }

        System.out.println("Playing all tracks in the '" + playlist.getName() + "' playlist in shuffle mode...\n");

        List<MediaItem> mediaItemsList = new ArrayList<>(mediaItems);
        int[] randomIndex = generateRandomMediaItemIndexArray(mediaItems.size(), 0, mediaItemsList.size());

        String mediaResponse = "";
        for (int i = 0; i < mediaItemsList.size(); i++) {
            if (mediaResponse.equalsIgnoreCase("stop")) {
                System.out.println("Stopping all tracks playback.\n");
                break;
            }

            MediaItem mediaItem = mediaItemsList.get(randomIndex[i]);

            if (mediaItem instanceof Song) {
                Song song = (Song) mediaItem;
                System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
            } else if (mediaItem instanceof Podcast) {
                Podcast podcast = (Podcast) mediaItem;
                System.out.println("Now playing: " + podcast.getTitle() + " by " + podcast.getHost());
            }

            try {
                mediaResponse = MediaPlayer.playMultipleTracks(mediaItem);
            } catch (Exception e) {
                System.out.println("Could not play track. Moving to the next track.\n");
                e.getMessage();
            }
        }

        if (!mediaResponse.equalsIgnoreCase("stop")) {
            System.out.println("All tracks have now finished playing.\n");
        }
    }

    public int[] generateRandomMediaItemIndexArray(int size, int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max must be >= min");
        }

        List<Integer> numberList = new ArrayList<>();

        for (int i = min; i < max; i++) {
            numberList.add(i);
        }

        Collections.shuffle(numberList);

        int[] randomArray = new int[size];
        for (int i = 0; i < size; i++) {
            randomArray[i] = numberList.get(i);
        }

        return randomArray;
    }

}
