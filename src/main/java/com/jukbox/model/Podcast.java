package com.jukbox.model;

import java.time.LocalDate;
import java.util.Objects;

public class Podcast implements MediaItem {

    private int id;
    private String title;
    private String host;
    private String genre;
    private String language;
    private LocalDate releaseDate;
    private int durationInMilliSeconds;
    private String filePath;

    public Podcast() {
    }

    public Podcast(int id, String title, String host,
                   String genre, String language, LocalDate releaseDate,
                   int durationInMilliSeconds, String filePath) {

        this.id = id;
        this.title = title;
        this.host = host;
        this.genre = genre;
        this.language = language;
        this.releaseDate = releaseDate;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDurationInMilliSeconds() {
        return durationInMilliSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInMilliSeconds = durationInSeconds;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", host='" + host + '\'' +
                ", genre='" + genre + '\'' +
                ", language='" + language + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + formatDuration(durationInMilliSeconds) + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Podcast podcast = (Podcast) object;
        return Objects.equals(title, podcast.title) && Objects.equals(host, podcast.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, host);
    }

    private String formatDuration(int durationInMillis) {
        int totalSeconds = durationInMillis / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
