package com.jukbox.UI;

import com.jukbox.model.Podcast;
import com.jukbox.repository.Jukebox;
import com.jukbox.service.PodcastService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.System.exit;

public class PodcastsUI {

    private final Scanner scanner;
    private final PodcastService podcastService;

    public PodcastsUI(Scanner scanner, Jukebox jukebox) {
        this.scanner = scanner;
        this.podcastService = new PodcastService(jukebox);
    }

    public void displayPodcastsMenu() {
        while (true) {
            int userChoice = getUserChoice();

            switch (userChoice) {
                case 1 -> viewAllPodcasts();
                case 2 -> addPodcastToUserList();
                case 3 -> removePodcastFromUserList();
                case 4 -> playAllJukeboxPodcasts();
                case 5 -> playAllUserPodcasts();
                case 6 -> playIndividualPodcast();
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
        System.out.println("\n-------------Podcasts Menu-------------");
        System.out.println("\nPlease select an option:");
        System.out.println("1. View All Podcasts");
        System.out.println("2. Add a Podcast to your Podcast List");
        System.out.println("3. Remove a Podcast from your Podcast List");
        System.out.println("4. Play All Podcasts Available in the Jukebox");
        System.out.println("5. Play All Podcasts in your Podcast List");
        System.out.println("6. Play an Individual Podcast");
        System.out.println("7. Search Podcasts");
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

    private void viewAllPodcasts() {
        System.out.println("\n1. View All Podcasts Available in the Jukebox");
        System.out.println("2. View All Podcasts in your Song List");
        System.out.print("Enter your choice: ");
        String userChoice = scanner.nextLine();

        try {
            int choice = Integer.parseInt(userChoice);

            switch (choice) {
                case 1 -> viewAllJukeboxPodcasts();
                case 2 -> viewAllUserPodcasts();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void viewAllJukeboxPodcasts() {
        List<Podcast> podcasts = podcastService.getAllPodcasts();

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts available in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Available Podcasts in the Jukebox-------------");
        podcasts.forEach(System.out::println);
    }

    private void viewAllUserPodcasts() {
        List<Podcast> podcasts = podcastService.getUserPodcastList();

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts available in your list.\n");
            return;
        }

        System.out.println("\n-------------Podcasts in your Podcast List-------------");
        podcasts.forEach(System.out::println);
    }

    private void addPodcastToUserList() {
        System.out.print("\nEnter Podcast ID to Add: ");
        String podcastIdStr = scanner.nextLine();

        try {
            int podcastId = Integer.parseInt(podcastIdStr);

            Optional<Podcast> optionalPodcast = podcastService.getPodcastFromJukebox(podcastId);

            if (optionalPodcast.isEmpty()) {
                System.out.println("The podcast with the specified ID does not exist.\n");
                return;
            }

            Podcast podcast = optionalPodcast.get();

            boolean podcastAdded = podcastService.addPodcastToUserList(podcast);

            if (podcastAdded) {
                System.out.println("Podcast successfully added to your list.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void removePodcastFromUserList() {
        System.out.print("\nEnter podcast ID: ");
        String podcastIdStr = scanner.nextLine();

        try {
            int podcastId = Integer.parseInt(podcastIdStr);

            Optional<Podcast> optionalPodcast = podcastService.getPodcastFromJukebox(podcastId);
            if (optionalPodcast.isEmpty()) {
                System.out.println("The podcast with the specified ID does not exist.\n");
                return;
            }

            Podcast podcast = optionalPodcast.get();
            boolean podcastRemove = podcastService.removePodcastFromUserList(podcast);

            if (podcastRemove) {
                System.out.println("Podcast successfully removed from your list.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void playAllJukeboxPodcasts() {
        podcastService.playAllJukeboxPodcastsSequentially();
    }

    private void playAllUserPodcasts() {
        podcastService.playAllUserPodcastsSequentially();
    }

    private void playIndividualPodcast() {
        System.out.print("\nEnter podcast ID: ");
        String podcastIdStr = scanner.nextLine();

        try {
            int podcastId = Integer.parseInt(podcastIdStr);

            Optional<Podcast> optionalPodcast = podcastService.getPodcastFromJukebox(podcastId);
            if (optionalPodcast.isEmpty()) {
                System.out.println("Podcast with the specified ID does not exist.\n");
                return;
            }

            Podcast podcast = optionalPodcast.get();
            podcastService.playIndividualPodcast(podcast);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void search() {
        System.out.println("\n-------------Search Podcasts-------------");
        System.out.println("1. Search Among All Podcasts Available in the Jukebox");
        System.out.println("2. Search in your Podcast List");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchAmongAllJukeboxPodcasts();
                case 2 -> searchAmongUserPodcasts();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, enter a valid number.\n");
        }
    }

    private void searchAmongAllJukeboxPodcasts() {
        System.out.println("\n-------------Search for Podcasts in the Jukebox-------------");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Celebrity (Host): ");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchByTitleAmongAllPodcasts();
                case 2 -> searchByHostAmongAllPodcasts();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void searchAmongUserPodcasts() {
        System.out.println("\n-------------Search for Podcasts in your List-------------");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Celebrity (Host): ");
        System.out.print("Enter your choice: ");
        String userChoiceStr = scanner.nextLine();

        try {
            int userChoice = Integer.parseInt(userChoiceStr);

            switch (userChoice) {
                case 1 -> searchByTitleAmongUserPodcasts();
                case 2 -> searchByHostAmongUserPodcasts();
                default -> System.out.println("Invalid choice.\n");
            }
        } catch (NumberFormatException numberFormatException) {
            System.out.println("Invalid input, please enter a valid number.\n");
        }
    }

    private void searchByTitleAmongAllPodcasts() {
        System.out.print("Enter Podcast Title: ");
        String title = scanner.nextLine();

        List<Podcast> podcasts = podcastService.searchByTitleAmongAllPodcasts(title);

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts by the '" + title + "' title in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Podcasts with the '" + title + "' Title-------------");
        podcasts.forEach(System.out::println);
    }

    private void searchByHostAmongAllPodcasts() {
        System.out.print("Enter Celebrity (Host) Name: ");
        String host = scanner.nextLine();

        List<Podcast> podcasts = podcastService.searchByHostAmongAllPodcasts(host);

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts available from '" + host + "' in the jukebox.\n");
            return;
        }

        System.out.println("\n-------------Podcasts Hosted by '" + host + "'-------------");
        podcasts.forEach(System.out::println);
    }

    private void searchByTitleAmongUserPodcasts() {
        System.out.print("Enter Podcast Title: ");
        String title = scanner.nextLine();

        List<Podcast> podcasts = podcastService.searchByTitleAmongUserPodcasts(title);

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts by the '" + title + "' title in your Podcast List.\n");
            return;
        }

        System.out.println("\n-------------Podcasts with the '" + title + "' Title in your Podcast List-------------");
        podcasts.forEach(System.out::println);
    }

    private void searchByHostAmongUserPodcasts() {
        System.out.print("Enter Celebrity (Host) Name: ");
        String host = scanner.nextLine();

        List<Podcast> podcasts = podcastService.searchByHostAmongUserPodcasts(host);

        if (podcasts.isEmpty()) {
            System.out.println("There are no podcasts available from '" + host + "' in your Podcast List.\n");
            return;
        }

        System.out.println("\n-------------Podcasts Hosted by '" + host + "' in your Podcast List-------------");
        podcasts.forEach(System.out::println);
    }

}
