package com.jukbox.model;

import java.util.Objects;

public class Song implements MediaItem {

    private int id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int durationInMilliSeconds;
    private String filePath;

    public Song() {
    }

    public Song(int id, String title, String artist, String album,
                String genre, int durationInMilliSeconds, String filePath) {

        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.durationInMilliSeconds = durationInMilliSeconds;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationInMilliSeconds() {
        return durationInMilliSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInMilliSeconds = durationInSeconds;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public String toString() {
        return "Song{" +
                "songId=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + formatDuration(durationInMilliSeconds) +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Song song = (Song) object;
        return Objects.equals(title, song.title) && Objects.equals(artist, song.artist) && Objects.equals(album, song.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, album);
    }

    private String formatDuration(int durationInMillis) {
        int totalSeconds = durationInMillis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
