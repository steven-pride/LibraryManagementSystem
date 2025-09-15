import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 09/13/25
 * LibraryApp
 * Provides the core business logic for the Library Management System (Business Logic Layer).
 * Provides methods for importing patron data from a file, creating new patrons,
 * retrieving patrons, updating patrons, deleting patrons, and listing all patrons.
 * Outside credit: StringBuilder was a recommended refactoring by the IDE, I accepted it because it felt more elegant than string = string + newString in the for loops.
 *      I have used StringBuilder in C# but hadn't used it in Java before. I wanted to give credit to the IDE for full transparency.
 */
public class LibraryManagementSystem {
    /**
     * Class attributes:
     *     patronCollection: HashMap<Patron> - this is the collection of Patron objects stored in the memory of the application.
     */
    private static Map<String, Patron> patronCollection = new HashMap<>();

    /**
     * method: importPatrons
     * parameters: String filePath - path to the patron data file
     * return: String - count of successes and any exception messages.
     * throws: IOException - if the file cannot be read for any reason.
     * purpose: Reads the file from the parameter, parses each line, and validates the number of columns.
     * Calls the LibraryManagementSystem.createPatrons method to create a new patron in the collection.
     * Expecting a file format of ID-Name-Address-Fine.
     * Returns a count of successful entries and any exception messages, concatenated in a string.
     */
    public static String importPatrons(String filePath) throws Exception {
        /**
         * Method attributes:
         *     successfulEntryCount: int - The count of successful entries
         *     exceptionHashMap: HashMap<String, Exception> - The key value pair of failed entries and their exceptions
         *     importLine: String - The current line being read from the file
         */
        int successfulEntryCount = 0;
        HashMap<String, Exception> exceptionHashMap = new HashMap<>();
        String importLine = null;

        //Check if the file exists and is readable.
        File patronFile = new File(filePath);
        if(!patronFile.exists() || !patronFile.canRead())
            throw new IOException("Error reading file. Check that the file exists and is readable.");

        //Read each line of the file and attempt to create a new patron.
        try (Scanner fileReader = new Scanner(patronFile);) {
            while(fileReader.hasNextLine()) {
                importLine = fileReader.nextLine();
                String[] splitLine = importLine.split("-");

                try {
                    //Validate the number of columns in the line, expecting 4 columns.
                    if (splitLine.length != 4) {
                        throw new Exception("Invalid data format. Expected 4 columns, found " + splitLine.length + ".");
                    }

                    if (createPatron(splitLine[0].trim(), splitLine[1].trim(), splitLine[2].trim(), splitLine[3].trim()))
                        successfulEntryCount++;
                }
                catch(Exception e) {
                    exceptionHashMap.put(importLine, e);
                }
            }
        }
        catch(Exception e) {
            exceptionHashMap.put(importLine, e);
        }

        //Create a concatenated string of the results of the import.
        StringBuilder statusMessage = new StringBuilder();
        statusMessage.append("Successfully imported ").append(successfulEntryCount).append(" patrons.");

        if(!exceptionHashMap.isEmpty())
            statusMessage.append("\nThe following lines failed");

        for (Map.Entry<String, Exception> entry : exceptionHashMap.entrySet()) {
            statusMessage.append("\nLine: ").append(entry.getKey()).append(" Exception: ").append(entry.getValue().getMessage()).append("\n-----------------------------");
        }
        return statusMessage.toString();
    }

    /**
     * method: createPatron
     * parameters: String id - the patron ID number
     *             String name - the patron name
     *             String address - the patron address
     *             String fine - the patron overdue fine amount
     * return: boolean - true if the patron was created successfully.
     * throws: IllegalArgumentException - if the patron ID already exists in the collection
     * purpose: Creates a new Patron object with the given ID, name, address, and fine amount if the ID does not already exist in the collection.
     * Adds the new Patron to the collection.
     */
    public static boolean createPatron(String id, String name, String address, String fine) throws Exception {
        try{
            if(retrievePatronById(id) != null)
                throw new IllegalArgumentException("Patron with id " + id + " already exists.");
            else {
                Patron patron = new Patron(id, name, address, fine);
                patronCollection.put(id, patron);
                return true;
            }
        } catch(Exception e){
            throw new IllegalArgumentException("Unable to create patron.\n" + e.getMessage());
        }


    }

    /**
     * method: retrievePatron
     * parameters: String id - the patron ID number
     * return: Patron - the Patron object with the given ID
     * throws: IllegalArgumentException - if the patron ID does not exist in the collection
     * purpose: Retrieves a Patron object from the collection based on the given ID.
     */
    public static Patron retrievePatron(String id) {
        Patron patron = retrievePatronById(id);
        if(patron != null)
            return patron;
        else
            throw new IllegalArgumentException("No Patron found with id " + id.trim());
    }

    /**
     * method: updatePatron
     * parameters: String id - the patron ID number
     *             String name - the patron name
     *             String address - the patron address
     *             String fine - the patron overdue fine amount
     * return: boolean - true if the patron was updated successfully.
     * throws: IllegalArgumentException - if the patron ID does not exist in the collection
     * purpose: Update a Patron object with the name, address, and fine amount if the patron ID exists in the collection.
     * Blank values are ignored for the update.
     */
    public static boolean updatePatron(String id, String name, String address, String fine) throws Exception {
            Patron patron = retrievePatronById(id);
            if(patron == null)
                throw new IllegalArgumentException("No Patron found with id " + id.trim());
            if(name != null && !name.trim().isEmpty())
                patron.setName(name);
            if(address != null && !address.trim().isEmpty())
                patron.setAddress(address);
            if(fine != null && !fine.trim().isEmpty())
                patron.setFine(fine);
            patronCollection.put(id, patron);
            return true;
    }

    /**
     * method: updatePatron
     * parameters: String id - the patron ID number
     * return: boolean - true if the patron was deleted successfully.
     * throws: IllegalArgumentException - if the patron ID does not exist in the collection
     * purpose: Delete a Patron object if the patron ID exists in the collection.
     */
    public static boolean deletePatron(String id) {
        if(retrievePatronById(id) != null) {
            patronCollection.remove(id);
            return true;
        }
        else
            throw new IllegalArgumentException("No Patron found with id " + id.trim());
    }

    /**
     * method: listPatrons
     * parameters: none
     * return: String - a string representation of all patrons in the collection.
     * purpose: Iterates over the patron collection and returns a concatenated string representation of each patron, using Patron.toString.
     */
    public static String listPatrons() {
        if(isPatronCollectionEmpty())
            return "No patrons found.";
        else {
            StringBuilder statusMessage = new StringBuilder();
            for (Patron patron : patronCollection.values()) {
                statusMessage.append(patron.toString()).append("\n");
            }

            return statusMessage.toString();
        }
    }

    /**
     * method: validateFine
     * parameters: String fine - the patron overdue fine amount
     * return: boolean - true if the fine is in a valid format, throws an exception otherwise
     * purpose: Validates the patron overdue fine amount is a decimal number and in the range of 0.00 to 250.00.
     */
    public static boolean validateFine(String fine) throws Exception {
        return Patron.validateFine(fine);
    }

    /**
     * method: isPatronCollectionEmpty
     * parameters: none
     * return: boolean - true if the patron collection is empty, false otherwise
     * purpose: Checks if the patron collection is empty.
     */
    public static boolean isPatronCollectionEmpty() {
        return patronCollection.isEmpty();
    }

    /**
     * method: idExists
     * parameters: String id - the patron ID number
     * return: boolean - true if the patron with that id exists in the collection, false if it doesn't exist, and throws an exception if the id format is invalid.
     * purpose: Validates the id format and checks if the patron with the given ID exists in the collection.
     */
    public static boolean idExists(String id) throws Exception {
        if(validateId(id) && retrievePatronById(id) != null)
            return true;
        else
            return false;
    }

    /**
     * method: retrievePatronById
     * parameters: String id - the patron ID number
     * return: Patron - the Patron object with the given ID
     * purpose: Retrieves a Patron object from the collection based on the given ID or null if the ID does not exist in the collection.
     * outside credit: IDE recommended refactoring to use getOrDefault method, previously I used patronCollection.containsKey(id) to check for the ID, then patronCollection.get(id) to retrieve the Patron object.
     */
    private static Patron retrievePatronById(String id) {
        return patronCollection.getOrDefault(id, null);
    }

    /**
     * method: validateId
     * parameters: String id - the patron ID number
     * return: boolean - true if the ID is in a valid format, throws an exception otherwise
     * purpose: Validates the patron ID number format.
     */
    private static boolean validateId(String id) throws Exception {
        return Patron.validateId(id);
    }
}
