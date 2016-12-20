package GeoNet.Main;

import GeoNet.Graphs.UndirectedGraph;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * A class that contains the primary methods for handling user input and interaction
 */
public class GeoNet
{
    private Scanner scan = new Scanner(System.in);
    private VenturaCountyMap countyMap;
    private UndirectedGraph<Profile> friendGraph;
    private Profile newUser;
    private ArrayList<Profile> existingUserProfiles;
    private final String[][] EXISTING_USERS = new String[][]{{"James Bond", "8236674027", "Fillmore"},
            {"Indiana Jones", "7089259985", "Port Hueneme"},
            {"Dorothy Gale", "0086785722", "Simi Valley"},
            {"Willy Wonka", "0122764433", "Ojai"},
            {"Mary Poppins", "1852789862", "Santa Paula"},
            {"Forrest Gump", "4118904539", "Thousand Oaks"},
            {"Princess Leia", "3172870993", "Moorpark"},
            {"Michelle Obama", "2537512861", "Ventura"},
            {"Snow White", "1594061212", "Santa Paula"},
            {"Jack Sparrow", "7752275544", "Oxnard"}};


    public GeoNet()
    {
        System.out.println("***************Welcome to GeoNet!**************\n");
        this.countyMap = new VenturaCountyMap();
        this.friendGraph = new UndirectedGraph<Profile>();
        this.existingUserProfiles = new ArrayList<>();
        for (int i = 0; i < EXISTING_USERS.length; i++) {
            Profile newUser = new Profile(this.EXISTING_USERS[i][0], this.EXISTING_USERS[i][1],
                    this.EXISTING_USERS[i][2].toLowerCase());
            this.friendGraph.addVertex(newUser);
            this.existingUserProfiles.add(newUser);
        }
        this.friendGraph.addEdge(this.existingUserProfiles.get(0), this.existingUserProfiles.get(1)); //bond + jones
        this.friendGraph.addEdge(this.existingUserProfiles.get(0), this.existingUserProfiles.get(4)); //bond + poppins
        this.friendGraph.addEdge(this.existingUserProfiles.get(0), this.existingUserProfiles.get(6)); //bond + leia

        this.friendGraph.addEdge(this.existingUserProfiles.get(1), this.existingUserProfiles.get(2)); //jones + dorothy
        this.friendGraph.addEdge(this.existingUserProfiles.get(1), this.existingUserProfiles.get(0)); //jones + bond
        this.friendGraph.addEdge(this.existingUserProfiles.get(1), this.existingUserProfiles.get(6)); //jones + leia
        this.friendGraph.addEdge(this.existingUserProfiles.get(1), this.existingUserProfiles.get(7)); //jones + michelle

        this.friendGraph.addEdge(this.existingUserProfiles.get(2), this.existingUserProfiles.get(8)); //dorothy + snow
        this.friendGraph.addEdge(this.existingUserProfiles.get(2), this.existingUserProfiles.get(1)); //dorothy + jones
        this.friendGraph.addEdge(this.existingUserProfiles.get(2), this.existingUserProfiles.get(5)); //dorothy + forrest
        this.friendGraph.addEdge(this.existingUserProfiles.get(2), this.existingUserProfiles.get(3)); //dorothy + wonka

        this.friendGraph.addEdge(this.existingUserProfiles.get(3), this.existingUserProfiles.get(2)); //wonka + dorothy
        this.friendGraph.addEdge(this.existingUserProfiles.get(3), this.existingUserProfiles.get(9)); //wonka + sparrow

        this.friendGraph.addEdge(this.existingUserProfiles.get(4), this.existingUserProfiles.get(0)); //poppins + bond
        this.friendGraph.addEdge(this.existingUserProfiles.get(4), this.existingUserProfiles.get(7)); //poppins + michelle

        this.friendGraph.addEdge(this.existingUserProfiles.get(9), this.existingUserProfiles.get(3)); //sparrow + wonka

        this.friendGraph.addEdge(this.existingUserProfiles.get(6), this.existingUserProfiles.get(0)); //leia + bond
        this.friendGraph.addEdge(this.existingUserProfiles.get(6), this.existingUserProfiles.get(1)); //leia + jones

        this.friendGraph.addEdge(this.existingUserProfiles.get(7), this.existingUserProfiles.get(4)); //michelle + poppins
        this.friendGraph.addEdge(this.existingUserProfiles.get(7), this.existingUserProfiles.get(1)); //michelle + jones
        this.friendGraph.addEdge(this.existingUserProfiles.get(7), this.existingUserProfiles.get(5)); //michelle + forrest

        this.friendGraph.addEdge(this.existingUserProfiles.get(8), this.existingUserProfiles.get(2)); //snow + dorothy

        this.friendGraph.addEdge(this.existingUserProfiles.get(5), this.existingUserProfiles.get(2)); //forrest + dorothy
        this.friendGraph.addEdge(this.existingUserProfiles.get(5), this.existingUserProfiles.get(7)); //forrest + michelle

    }

    /**
     *
     * @param newProfile profile of new user
     * @return true if user was successfully added to GeoNet
     */
    boolean addUser(Profile newProfile)
    {
        boolean userAddedSuccessfully = false;
        newUser = newProfile;
        if (this.friendGraph.addVertex(newProfile)) {
            userAddedSuccessfully = true;
            this.existingUserProfiles.add(newProfile);
            String name = newProfile.getName();
            int firstName = name.indexOf(" ");
            System.out.println("Profile creation complete.\nWelcome to Facenovel, " + name.substring(0, firstName) + "!\n");
            promptUser();
        }
        return userAddedSuccessfully;
    }

    /**
     * Get user commands
     */
    void promptUser()
    {
        int incorrectAttempts = 0;
        boolean validInput = false;
        int userInput = 0;
        do {
            System.out.println("***  MAIN MENU  ***");
            System.out.println("Here are the actions you can perform:");
            System.out.println("1) Search : find friends! (either by name or by status)");
            System.out.println("2) Directions : get directions from A to B!");
            System.out.println("3) Phone Chain : Compute an emergency phone chain such that everyone" +
                    " on the network is contacted by only one other person.");
            System.out.println("4) Logout : log out of your account and exit Facenovel");
            System.out.println("5) View Friends : view friends of a certain user");
            System.out.println("What would you like to do? (Enter a digit from 1-5)");
            try {
                userInput = scan.nextInt();
                validInput = true;
                scan.nextLine();
                switch (userInput) {
                    case 1: //search
                        incorrectAttempts = 0; //reset incorrectAttempts
                        int userSearchType = 0;
                        Boolean validSearch;
                        do {
                            validSearch = true;
                            System.out.println("*** Search Menu ***");
                            System.out.println("1) name : search by name");
                            System.out.println("2) location : search by location");
                            System.out.println("3) back : return to previous menu");
                            System.out.println("4) exit : logout and exit Facenovel");
                            try {
                                userSearchType = scan.nextInt();
                            } catch (InputMismatchException ime) {
                            }
                            scan.nextLine();
                            if (userSearchType == 1) {
                                searchByName();
                                validSearch = false;
                            } else if (userSearchType == 2) {
                                searchByLocation();
                                validInput = false;
                            } else if (userSearchType == 3) {
                                System.out.println(">>>Returning to previous menu.\n");
                                validInput = false;
                            } else if (userSearchType == 4) {
                                validSearch = true;
                                userInput = 4;
                                System.out.println("Thanks for using Facenovel. Goodbye!");
                            } else {
                                System.out.println("Invalid selection. Please enter 1 for search by name," +
                                        " 2 for search by location, or 3 to return to the previous menu.");
                                validSearch = false;
                            }

                        } while (!validSearch);
                        break;
                    case 2: //directions
                        validInput = false;
                        getDirections();
                        break;
                    case 3:
                        validInput = false;
                        computeEmergencyPhoneChain();
                        break;
                    case 4:
                        System.out.println("Thanks for using Facenovel. Goodbye!");
                        break;
                    case 5:
                        System.out.println("Enter the name of the person whose friends you'd like to see.");
                        String person = scan.nextLine();
                        seeFriends(person);
                        validInput = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please enter 1, 2, or 3.");
                        incorrectAttempts++;
                        validInput = false;
                        break;

                }

            } catch (InputMismatchException ime) {
                scan.nextLine();
                incorrectAttempts++;
                if (incorrectAttempts >= 5) {
                    System.out.println("Too many incorrect attempts. Logging out. Goodbye.");
                    System.exit(0);
                }
                System.out.println("Invalid input. Try again.\n");
            }
        } while (!validInput && userInput != 4);
    }

    /**
     *
     * @param profile a user profile
     * @return true if the given profile is connected to the existing graph
     */
    public boolean isConnected(Profile profile)
    {
        System.out.print("The friendGraph is ");
        System.out.println(friendGraph.isConnected(profile) ? "connected." : "disconnected.");
        System.out.println("The number of edges in the friendGraph is " + friendGraph.getNumberOfEdges());
        System.out.println("The number of vertices in the friendGraph is " + friendGraph.getNumberOfVertices());
        return friendGraph.isConnected(profile);
    }

    /**
     *
     * @param begin vertex from which the edge starts
     * @param end vertex to which the edge extends
     * @return true if edge was successfully added
     */
    public boolean addEdge(Profile begin, Profile end)
    {
        return this.friendGraph.addEdge(begin, end);
    }

    /**
     * print all friends of a given user
     * @param name name of the user
     */
    void seeFriends(String name)
    {
        System.out.println("The friends of " + name + " are:");
        for (int i = 0; i < this.existingUserProfiles.size(); i++) {
            if (name.equalsIgnoreCase(this.existingUserProfiles.get(i).getName())) {
                Profile temp = this.existingUserProfiles.get(i);
                for (Profile prof : this.existingUserProfiles) {
                    if (this.friendGraph.hasEdge(prof, temp))
                        System.out.println(prof.getName());
                }
            }
        }
        System.out.println();
    }

    /**
     * search for a user by name
     */
    private void searchByName()
    {
        System.out.println("\nYou have selected Search by Name!");
        boolean validSearch = true;
        do {
            System.out.println("Please enter the first and last name (seperated by a space) of the user who you " +
                    "would like to send a friend request to.");
            System.out.println("For a list of the existing user directory, type \"directory\". You can also enter " +
                    "\"back\" to return to the previous menu or \"exit\" to log out of Facenovel.");
            String input = scan.nextLine().toLowerCase();
            switch (input) {
                case "back":
                    validSearch = true;
                    break;
                case "directory":
                    System.out.println("Here is a list of all the user profiles accessible to you:");
                    for (int i = 0; i < this.EXISTING_USERS.length; i++) {
                        System.out.println(this.EXISTING_USERS[i][0]);
                    }
                    break;
                case "exit":
                    System.out.println("Thank you for using Facenovel. Goodbye.");
                    System.exit(0);
                default:
                    boolean friendFound = false;
                    for (int i = 0; i < this.existingUserProfiles.size() && !friendFound; i++) {
                        if (input.equalsIgnoreCase(this.existingUserProfiles.get(i).getName())) {
                            friendFound = true;
                            System.out.println("Sending a friend request to " + this.existingUserProfiles.get(i).getName());
                            System.out.println("Waiting for response....");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            System.out.println(this.existingUserProfiles.get(i).getName() + " has accepted your request!!");
                            this.friendGraph.addEdge(this.newUser, this.existingUserProfiles.get(i));
                            System.out.println("Enter a number. You may perform additional searches, return to the previous menu," +
                                    " or exit Facenovel completely.\n");
                        }
                    }
                    if (!friendFound) {
                        System.out.println("Invalid search term. Please try again.");
                        validSearch = false;
                    }
                    break;
            }
        } while (!validSearch);
    }

    /**
     * search for a user by their location of residence
     */
    private void searchByLocation()
    {
        System.out.println("Enter the city of Ventura County where you would like to find friends.");
        boolean validCity;
        String city;
        do {
            city = scan.nextLine().toLowerCase();
            validCity = false;
            for (int i = 0; i < VenturaCountyMap.CITIES_OF_VENTURA_COUNTY.length; i++) {
                if (city.equalsIgnoreCase(VenturaCountyMap.CITIES_OF_VENTURA_COUNTY[i]))
                    validCity = true;
            }
            if (!validCity)
                System.out.println("Invalid city. Please try again.");
        } while (!validCity);
        System.out.println("The following people reside in " + city + ".");

        for (int i = 0; i < this.EXISTING_USERS.length; i++) {
            if (city.equalsIgnoreCase(this.EXISTING_USERS[i][2])) {
                System.out.println(this.EXISTING_USERS[i][0]);
            }
        }

    }

    /**
     * print list of users in an order that minimizes the distance between any two consecutive users
     */
    public void computeEmergencyPhoneChain()
    {
        if (this.friendGraph.isConnected(newUser)) {
            Queue<Profile> phoneChain = this.friendGraph.getDepthFirstTraversal(newUser);
            while (!phoneChain.isEmpty()) {
                Profile temp = phoneChain.poll();
                System.out.print(temp.getName());
                if (!phoneChain.isEmpty())
                    System.out.print("-->");
            }
            System.out.println();
        } else {
            System.out.println("No emergency phone chain is currently possible. Not all users are connected.");
        }
        System.out.println();
    }

    /**
     * find and print the shortest path between certain users
     */
    public void getDirections()
    {
        System.out.println("*** Directions Menu ***");
        System.out.println("Here you can find the shortest path from point A to point B.");
        String inputLocation;
        boolean locationFound = false;
        boolean back = false;
        String startLocation = "";
        boolean startIsPerson = false;
        String profileStart = "";
        do {
            System.out.println("Please enter your desired STARTING location. Enter either a specific city name " +
                    " or the name of the person who's town you'd like to start in. Or enter \"back\" to return" +
                    " to the previous menu.");
            inputLocation = scan.nextLine().toLowerCase();
            if (!inputLocation.equalsIgnoreCase("back")) {
                for (int i = 0; i < this.existingUserProfiles.size() && !locationFound; i++) {
                    if (inputLocation.equalsIgnoreCase(this.existingUserProfiles.get(i).getName())) {
                        locationFound = true;
                        startIsPerson = true;
                        startLocation = this.existingUserProfiles.get(i).getCityOfResidence().toLowerCase();
                        profileStart = inputLocation + "'s hometown of " + startLocation;
                    }
                }
                if (!locationFound) {
                    //if location was not a user, search through cities
                    for (int i = 0; i < VenturaCountyMap.CITIES_OF_VENTURA_COUNTY.length; i++) {
                        if (inputLocation.equalsIgnoreCase(VenturaCountyMap.CITIES_OF_VENTURA_COUNTY[i])) {
                            locationFound = true;
                            startLocation = VenturaCountyMap.CITIES_OF_VENTURA_COUNTY[i].toLowerCase();
                        }
                    }
                }
                if (!locationFound) {
                    System.out.println("Invalid location.");
                }
            } else
                back = true;

        } while (!locationFound);

        String destination = "";
        locationFound = false;
        boolean endIsPerson = false;
        String profileEnd = "";
        if (!back) {
            do {
                System.out.println("Please enter your desired DESTINATION. Enter either a specific city name " +
                        " or the name of the person who's town you'd like to start in. Or enter \"back\" to return" +
                        " to the previous menu.");

                inputLocation = scan.nextLine();
                if (!inputLocation.equalsIgnoreCase("back")) {
                    for (int i = 0; i < this.existingUserProfiles.size() && !locationFound; i++) {
                        if (inputLocation.equalsIgnoreCase(this.existingUserProfiles.get(i).getName())) {
                            locationFound = true;
                            endIsPerson = true;
                            destination = this.existingUserProfiles.get(i).getCityOfResidence().toLowerCase();
                            profileEnd = inputLocation + "'s hometown of " +
                                    destination + "";
                        }
                    }
                    if (!locationFound) {
                        //if location was not a user, search through cities
                        for (int i = 0; i < VenturaCountyMap.CITIES_OF_VENTURA_COUNTY.length; i++) {
                            if (inputLocation.equalsIgnoreCase(VenturaCountyMap.CITIES_OF_VENTURA_COUNTY[i])) {
                                locationFound = true;
                                destination = VenturaCountyMap.CITIES_OF_VENTURA_COUNTY[i].toLowerCase();
                            }
                        }
                    }
                    if (!locationFound) {
                        System.out.println("Invalid destination.");
                    }
                } else { //user entered "back"
                    locationFound = true;
                    back = true;
                }

            } while (!locationFound);
        }

        if (!back) {
            Stack<String> stack = new Stack<>();
            double distance = this.countyMap.getCheapestPath(destination, startLocation, stack);
            if (startIsPerson)
                System.out.print("The distance from " + profileStart + " to ");
            else
                System.out.print("The distance from " + startLocation + " to ");
            if (endIsPerson)
                System.out.println(profileEnd + " is " + distance + " miles");
            else
                System.out.println(destination + " is " + distance + " miles");
            if (this.countyMap.hasEdge(startLocation, destination))
                System.out.println(startLocation + " and " + destination + " are adjacent cities" +
                        " so unfortunately you cannot pick up anyone along the way.");
            else {
                System.out.print("From " + stack.pop() + ", travel to ");
                while (!stack.isEmpty()) {
                    String city = stack.pop();
                    if (!stack.isEmpty()) {
                        System.out.print(city + ", where you can pick up: ");
                        for (int i = 0; i < this.EXISTING_USERS.length; i++) {
                            if (city.equalsIgnoreCase(this.EXISTING_USERS[i][2]))
                                System.out.print(this.EXISTING_USERS[i][0] + "   ");
                        }
                        System.out.print("\nthen to ");
                    } else //stack is empty
                        System.out.println(" and finally arrive at your destination: " + destination + ".\n");
                }
            }
        }


    }

    /**
     *
     * @param name name of user
     * @return true if there already exists a user with the given name
     */
    public boolean nameTaken(String name)
    {
        boolean nameTaken = false;
        for (int i = 0; i < this.EXISTING_USERS.length && !nameTaken; i++) {
            if (name.equalsIgnoreCase(this.EXISTING_USERS[i][0]))
                nameTaken = true;
        }
        return nameTaken;
    }

}