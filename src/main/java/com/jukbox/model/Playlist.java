package com.jukbox.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Playlist implements Comparable<Playlist> {

    private int playlistId;
    private String name;
    private String description;
    private LocalDate createdOn;
    private Set<MediaItem> mediaItems;

    public Playlist() {
    }

    public Playlist(int playlistId, String name, String description,
                    LocalDate createdOn, Set<MediaItem> mediaItems) {

        this.playlistId = playlistId;
        this.name = name;
        this.description = description;
        this.createdOn = createdOn;
        this.mediaItems = mediaItems;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public Set<MediaItem> getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(Set<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Playlist playlist = (Playlist) object;
        return name.equalsIgnoreCase(playlist.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public int compareTo(Playlist o) {
        return Integer.compare(this.playlistId, o.playlistId);
    }
}
