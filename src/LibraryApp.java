import java.util.Scanner;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 09/13/25
 * LibraryApp
 * Console application for the Library Management System to allow librarians to manage patrons.
 * Provides the core user interface for the application (UI Layer).
 * Implements basic CRUD functionality for patron management, as well as bulk file import.
 * Users will be presented with a menu:
 * 1. Import Patron File
 * 2. Create new Patron
 * 3. Retrieve Patron
 * 4. Update Existing Patron
 * 5. Remove Existing Patron
 * 6. List All Patrons
 * 7. Exit
 * Subclasses will be called, with results displayed to the user by this class.
 */
public class LibraryApp {
    /**
     * Class attributes:
     *     inputScanner: Scanner - an instance of the Scanner class to capture user inputs
     */
    private static Scanner inputScanner = new Scanner(System.in);

    /**
     * method: main
     * parameters: String[] args - command line arguments (not used)
     * return: void
     * purpose: Launches the Library Management System application. Welcomes users and runs the main menu loop method.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Library Management System!");
        menu();
    }

    /**
     * method: menu
     * parameters: none
     * return: void
     * purpose: The main menu loop method, displaying menu options, handles user input, and calls the appropriate method based on user input.
     */
    public static void menu() {
        while(true) {
            System.out.println("Please select an option:");
            System.out.println("1. Import Patron File");
            System.out.println("2. Create new Patron");
            System.out.println("3. Retrieve Patron");
            System.out.println("4. Update Existing Patron");
            System.out.println("5. Remove Existing Patron");
            System.out.println("6. List All Patrons");
            System.out.println("7. Exit");

            int menuOption = 0;
            try {
                menuOption = Integer.parseInt(inputScanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid option selected. Please enter 1-7");
                continue;
            }
            switch(menuOption) {
                case 1:
                    importPatrons();
                    break;
                case 2:
                    createPatron();
                    break;
                case 3:
                    retrievePatronById();
                    break;
                case 4:
                    updatePatron();
                    break;
                case 5:
                    deletePatron();
                    break;
                case 6:
                    listPatrons();
                    break;
                case 7:
                    exit();
                    break;
                default:
                    System.out.println("Invalid option selected. Please enter 1-7");
                    break;
            }
         }
    }

    /**
     * method: importPatrons
     * parameters: none
     * return: void
     * purpose: Prompts the user to enter a file path for a patron data file and calls the LibraryManagementSystem.importPatrons method to import the file.
     * Writes out the result of the import to the user.
     */
    public static void importPatrons() {
        try {
            String filePath = "";
            while (filePath.trim().isEmpty()) {
                System.out.println("To return to the main menu, enter 'menu'.");
                System.out.println("Please enter the full file path for the patron data file you would like to import: ");
                filePath = inputScanner.nextLine();

                //Return to the main menu if the user wants to exit the current function
                if (filePath.trim().equalsIgnoreCase("menu"))
                {
                    return;
                }
            }
            String status = LibraryManagementSystem.importPatrons(filePath);
            System.out.println(status);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method: createPatron
     * parameters: none
     * return: void
     * purpose: Prompts the user for details to create a Patron and calls the LibraryManagementSystem.createPatron method to create the Patron.
     * Writes out the result of the create action to the user.
     */
    public static void createPatron() {
        try {
            String id = getPatronID("create", false);

            //Return to the main menu if the user wants to exit the current function
            if(id.trim().equalsIgnoreCase("menu"))
                return;

            System.out.println("Please enter the name for the Patron: ");
            String name = inputScanner.nextLine();
            System.out.println("Please enter the address for the Patron: ");
            String address = inputScanner.nextLine();

            String fine = getFineAmount("Please enter the Overdue Fine Amount for the Patron: ");

            if(LibraryManagementSystem.createPatron(id, name, address, fine)) {
                System.out.println("Patron " + id.trim() + " has been created.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * method: retrievePatronById
     * parameters: none
     * return: void
     * purpose: Prompts the user for a Patron ID to retrieve and calls the LibraryManagementSystem.retrievePatronById method to retrieve the Patron.
     * Writes out the patron details to the user if found, or a message indicating no patron was found.
     */
    public static void retrievePatronById() {
        try {
            String id = getPatronID("retrieve", true);

            //Return to the main menu if the user wants to exit the current function
            if(id.trim().equalsIgnoreCase("menu"))
                return;

            Patron patron = LibraryManagementSystem.retrievePatron(id);
            if (patron != null) {
                System.out.println(patron.toString());
            }
            else {
                System.out.println("No Patron found with id " + id.trim());
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method: updatePatron
     * parameters: none
     * return: void
     * purpose: Prompts the user for details of the Patron to update and calls the LibraryManagementSystem.updatePatron method to update the Patron.
     * Writes out the result of the update action to the user.
     */
    public static void updatePatron() {
        try {
            String id = getPatronID("update", true);
            //Return to the main menu if the user wants to exit the current function
            if(id.trim().equalsIgnoreCase("menu"))
                return;

            boolean changeMade = false;

            System.out.println("Please enter the updated name for the Patron (leave blank for no update): ");
            String name = inputScanner.nextLine();
            System.out.println("Please enter the updated address for the Patron (leave blank for no update): ");
            String address = inputScanner.nextLine();

            //Setting fine to a default value to loop on until valid input is received
            String fine = getFineAmount("Please enter the updated Overdue Fine Amount for the Patron (leave blank for no update): ");

            if(!name.trim().isEmpty() || !address.trim().isEmpty() || !fine.trim().isEmpty()) {
                changeMade = true;
            }

            if(!changeMade) {
                System.out.println("No changes made to the Patron.");
                return;
            }

            if(LibraryManagementSystem.updatePatron(id, name, address, fine)) {
                System.out.println("Patron " + id.trim() + " has been updated.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method: deletePatron
     * parameters: none
     * return: void
     * purpose: Prompts the user for a Patron ID to retrieve and calls the LibraryManagementSystem.deletePatron method to remove the Patron from the collection.
     * Writes out the result of the delete action to the user.
     */
    public static void deletePatron() {
        try {
            String id = getPatronID("delete", true);

            //Return to the main menu if the user wants to exit the current function
            if(id.trim().equalsIgnoreCase("menu"))
                return;

            if(LibraryManagementSystem.deletePatron(id)) {
                System.out.println("Patron " + id.trim() + " has been deleted.");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method: listPatrons
     * parameters: none
     * return: void
     * purpose: Calls the LibraryManagementSystem.listPatrons method to get a string list of all patrons in the collection.
     * Writes out the list of patrons in the collection to the user.
     */
    public static void listPatrons() {
        try {
            String list = LibraryManagementSystem.listPatrons();
            System.out.println(list);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method: exit
     * parameters: none
     * return: void
     * purpose: Exits the application
     */
    public static void exit() {
        System.exit(0);
    }

    /**
     * method: getPatronID
     * parameters: String action - the action being performed, for example, retrieve, update, etc.
     *             boolean shouldIdExist - true if the ID should exist in the collection, false if it should not.
     * return: String
     * purpose: Prompts the user to enter the ID of a patron for a specific action, looping until a valid ID is entered.
     *          Checks if the ID should exist or not. Returns the entered ID if all validations pass.
     *          Entering 'menu' will return an id of 'menu' for the calling function to process.
     *          Returns 'menu' to the calling function if ID should exist, but the collection is empty (no IDs exist).
     */
    private static String getPatronID(String action, boolean shouldIdExist) {
        if(shouldIdExist && LibraryManagementSystem.isPatronCollectionEmpty()) {
            System.out.println("No Patrons in the collection. Please add Patrons before performing this action.");
            return "menu";
        }

        String id = "";
        while (id.trim().isEmpty()) {
            System.out.println("To return to the main menu, enter 'menu'.");
            System.out.println("Please enter the id of the Patron you would like to " + action + ": ");
            id = inputScanner.nextLine();

            //Return the id of 'menu' if the user wants to exit the current function
            if (id.trim().equalsIgnoreCase("menu"))
            {
                return id;
            }

            try {
                boolean idExists = LibraryManagementSystem.idExists(id);
                if(idExists != shouldIdExist) {
                    if(shouldIdExist) {
                        System.out.println("Patron ID does not exist. Please enter a valid Patron ID.");
                    } else {
                        System.out.println("Patron ID already exists. Please enter a new Patron ID.");
                    }
                    id = "";
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                id = "";
            }
        }
        return id;
    }

    /**
     * method: getFineAmount
     * parameters: String messageToDisplay - the fine amount entry message to display to the user. Allows for different messages depending on action taken.
     * return: String
     * purpose: Prompts the user to enter the fine amount, looping until a valid amount is entered.
     *          Returns the entered fine amount if all validations pass.
     */
    private static String getFineAmount(String messageToDisplay) {
        //Setting fine to a default value to loop on until valid input is received
        String fine = "input";
        while (fine.equals("input")) {
            System.out.println(messageToDisplay);
            fine = inputScanner.nextLine();

            //Handle blank fine input
            if (fine.trim().isEmpty())
                fine = "";
            //Otherwise, validate the input
            else {
                try {
                    if(LibraryManagementSystem.validateFine(fine))
                        return fine;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    fine = "input";
                }
            }
        }
        return fine;
    }
}
