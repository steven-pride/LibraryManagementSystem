/**
 * Steven Pride
 * CEN 3024 - Software Development I
 * 09/13/25
 * LibraryApp
 * Models a library patron, storing their ID, name, address, and any overdue fine (Data Model Layer).
 * Provides data validation for ID and overdue fine amount.
 * Provides a toString method to display patron information in a uniform format.
 */
public class Patron {
    /**
     * Class attributes:
     *     id: String - 7-digit patron ID number.
     *     name: String - full name of patron.
     *     address: String - full address of patron.
     *     fine: double - Overdue fine amount of the patron, with a range of 0.00 – 250.00
     */
    private String id;
    private String name;
    private String address;
    private double fine;


    /**
     * method: Patron constructor
     * parameters: String id - the patron ID number
     *             String name - the patron name
     *             String address - the patron address
     *             String fine - the patron overdue fine amount
     * return: A new Patron object.
     * throws: IllegalArgumentException - if the ID or overdue fine amount is invalid
     *         Exception - if an unexpected error occurs during ID or overdue fine amount validation
     * purpose: Initializes a new Patron object with the given ID, name, address, and fine amount. To note: fine is a string input to simplify UI and Business Logic, but is converted to a double by the setter for storage.
     */
    public Patron(String id, String name, String address, String fine) throws Exception {
        setId(id);
        this.name = name;
        this.address = address;
        setFine(fine);
    }

    //Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public double getFine() {
        return fine;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * method: setId
     * parameters: String id - the patron ID number
     * return: void
     * throws: IllegalArgumentException - if the ID is empty or not 7 digits or contains non-numeric characters.
     *         Exception - if an unexpected error occurs during ID validation
     * purpose: Validates the ID is 7 digits and contains only numbers, and sets this.id to the given value.
     */
    public void setId(String id) throws Exception {
        if(validateId(id))
        {
            this.id = id;
        }
    }

    /**
     * method: setFine
     * parameters: String fine - the patron overdue fine amount
     * return: void
     * throws: IllegalArgumentException - if the fine is empty or not a decimal number.
     *         Exception - if an unexpected error occurs during fine validation.
     * purpose: Validates the fine is a decimal number and sets this.fine to the given value converted to double format.
     */
    public void setFine(String fine) throws Exception {
        /**
         * Method attributes:
         *     doubleFine: double - Parsed from String fine, the overdue fine amount of the patron, with a range of 0.00 – 250.00
         */
        double doubleFine = 0.00;

        if(validateFine(fine)) {
            doubleFine = Double.parseDouble(fine);
            this.fine = doubleFine;
        }
    }

    /**
     * method: toString
     * parameters: none
     * return: String - a string representation of the patron's information.
     * purpose: Returns a string representation of the patron's information, formatting the fine to two decimal places.
     * notes: not using StringBuilder here because there is no iteration.
     */
    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nAddress: " + address + "\nFine: $" + String.format("%.2f", fine) + "\n-----------------------------";
    }

     /**
     * method: validateId
     * parameters: String id - the patron ID number
     * return: true if the id is 7 digits and contains only numbers, throws an exception otherwise
     * throws: IllegalArgumentException - if the ID is empty or not 7 digits or contains non-numeric characters.
     *      *         Exception - if an unexpected error occurs during ID validation
     * purpose: Validates the ID is 7 digits and contains only numbers.
     */
    public static boolean validateId(String id) throws Exception {
        if( id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Patron ID cannot be empty.");

        if(id.trim().length() != 7)
            throw new IllegalArgumentException("Patron ID must be 7 digits.");

        try {
            Integer.parseInt(id.trim());
            return true;
        }
        //Catch if the ID is not parsable to an integer, meaning it contains non-numeric characters
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Patron ID must be only numbers.");
        }
        //Catch all other exceptions
        catch(Exception e) {
            throw new Exception("An unexpected error occurred during ID validation: " + e.getMessage());
        }
    }

    /**
     * method: validateFine
     * parameters: String fine - the patron overdue fine amount
     * return: true if the fine is a valid decimal number and in range, throws an exception otherwise
     * throws: IllegalArgumentException - if the fine is empty or not a decimal number.
     *         Exception - if an unexpected error occurs during fine validation.
     * purpose: Validates the fine is a decimal number in the range of 0.00-250.00.
     */
    public static boolean validateFine(String fine) throws Exception {
        /**
         * Method attributes:
         *     doubleFine: double - Parsed from String fine, the overdue fine amount of the patron, with a range of 0.00 – 250.00
         */
        double doubleFine = 0.00;

        if(fine == null || fine.trim().isEmpty())
            throw new IllegalArgumentException("Fine cannot be empty.");

        try {
            doubleFine = Double.parseDouble(fine.trim());
        }
        //Catch if the fine is not parsable to a double, meaning it is not in the format of 0.00
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Patron Overdue fine must be in the format of 0.00, you can only use numbers and one decimal point.");
        }
        //Catch all other exceptions
        catch(Exception e) {
            throw new Exception("An unexpected error occurred during Overdue fine validation: " + e.getMessage());
        }

        //Check if in range
        if (doubleFine < 0.00 || doubleFine > 250.00)
            throw new IllegalArgumentException("Overdue fine must be between 0.00 and 250.00.");

        return true;
    }
}

