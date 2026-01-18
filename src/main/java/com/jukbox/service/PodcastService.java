package com.jukbox.service;

import com.jukbox.model.Podcast;
import com.jukbox.repository.Jukebox;
import com.jukbox.utility.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PodcastService {

    private final Jukebox jukebox;
    private List<Podcast> userPodcastList;

    public PodcastService(Jukebox jukebox) {
        this.jukebox = jukebox;
        this.userPodcastList = new ArrayList<>();
    }

    public List<Podcast> getUserPodcastList() {
        return userPodcastList;
    }

    public void setUserPodcastList(List<Podcast> userPodcastList) {
        this.userPodcastList = userPodcastList;
    }

    public List<Podcast> getAllPodcasts() {
        return jukebox.getAllPodcasts();
    }

    public Optional<Podcast> getPodcastFromJukebox(int podcastId) {
        return getAllPodcasts().stream()
                .filter(p -> p.getId() == podcastId)
                .findFirst();
    }

    public boolean addPodcastToUserList(Podcast podcast) {
        if (userPodcastList.contains(podcast)) {
            System.out.println("Podcast is already added.\n");
            return false;
        }

        userPodcastList.add(podcast);
        return true;
    }

    public boolean removePodcastFromUserList(Podcast podcast) {
        if (userPodcastList.contains(podcast)) {
            userPodcastList.remove(podcast);
            return true;
        }

        System.out.println("The podcast does not exist in the list of podcasts.\n");
        return false;
    }

    public List<Podcast> searchByTitleAmongAllPodcasts(String title) {
        return getAllPodcasts().stream()
                .filter(p -> p.getTitle().equalsIgnoreCase(title))
                .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Podcast> searchByHostAmongAllPodcasts(String host) {
        return getAllPodcasts().stream()
                .filter(p -> p.getHost().equalsIgnoreCase(host))
                .sorted((p1, p2) -> p1.getHost().compareTo(p2.getHost()))
                .collect(Collectors.toList());
    }

    public List<Podcast> searchByTitleAmongUserPodcasts(String title) {
        return userPodcastList.stream()
                .filter(p -> p.getTitle().equalsIgnoreCase(title))
                .sorted((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()))
                .collect(Collectors.toList());
    }

    public List<Podcast> searchByHostAmongUserPodcasts(String host) {
        return userPodcastList.stream()
                .filter(p -> p.getHost().equalsIgnoreCase(host))
                .sorted((p1, p2) -> p1.getHost().compareTo(p2.getHost()))
                .collect(Collectors.toList());
    }

    public void playAllJukeboxPodcastsSequentially() {
        String mediaMessage = "";

        if (getAllPodcasts().isEmpty()) {
            System.out.println("No podcasts available in the jukebox.\n");
            return;
        }

        System.out.println("Playing podcasts sequentially....\n");

        for (Podcast podcast : getAllPodcasts()) {
            if (mediaMessage.equalsIgnoreCase("stop")){
                System.out.println("Stopping all podcasts playback.\n");
                break;
            }

            System.out.println("Now playing: " + podcast.getTitle() + " by " + podcast.getHost());

            try {
                mediaMessage = MediaPlayer.playMultipleTracks(podcast);
            } catch (Exception e) {
                System.out.println("Could not play podcast. Moving to the next podcast.\n");
                e.getMessage();
            }
        }

        if (!mediaMessage.equalsIgnoreCase("stop")) {
            System.out.println("All podcasts have now finished playing.\n");
        }
    }

    public void playAllUserPodcastsSequentially() {
        String mediaMessage = "";

        if (userPodcastList.isEmpty()) {
            System.out.println("Your podcast list is empty.\n");
            return;
        }

        System.out.println("Playing your podcasts sequentially....");

        for (Podcast podcast : userPodcastList) {
            if (mediaMessage.equalsIgnoreCase("stop")){
                System.out.println("\nStopping all podcasts playback.\n");
                break;
            }

            System.out.println("\nNow playing: " + podcast.getTitle() + " by " + podcast.getHost());

            try {
                mediaMessage = MediaPlayer.playMultipleTracks(podcast);
            } catch (Exception e) {
                System.out.println("Could not play podcast. Moving to the next song.\n");
                e.getMessage();
            }
        }

        if (!mediaMessage.equalsIgnoreCase("stop")) {
            System.out.println("\nAll songs have now finished playing.\n");
        }
    }

    public void playIndividualPodcast(Podcast podcast) {
        System.out.println("Now playing: " + podcast.getTitle() + " by " + podcast.getHost());

        try {
            MediaPlayer.playIndividualTrack(podcast);
        } catch (Exception e) {
            System.out.println("Could not play podcast.\n");
            e.getMessage();
        }

        System.out.println("Podcast finished playing");
    }
}
