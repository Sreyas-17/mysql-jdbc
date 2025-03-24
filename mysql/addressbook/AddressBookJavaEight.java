package com.bridgelabz.AddressBook;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.google.gson.*;

// Class to manage multiple address books
class AddressBooksSystem {
    static HashMap<String, AddressBook> addressBookList = new HashMap<>();
    static HashMap<String, List<Contact>> cityMap = new HashMap<>();
    static HashMap<String, List<Contact>> stateMap = new HashMap<>();

    // Add a new address book to the system
    public static void addAddressBook(AddressBook addressBook) {
        addressBookList.put(addressBook.getName(), addressBook);
    }

    public static void duplicateCity(){
        Map<String, Long> countByCity=addressBookList.values().stream()
                .flatMap(book->book.contactList.stream())
                .collect(Collectors.groupingBy(
                        Contact::getCity,
                        Collectors.counting()));

        System.out.println("\nDuplicate Cities with more than one contact:");
        countByCity.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .forEach(entry -> System.out.println(entry.getKey() + " | Count: " + entry.getValue()));

    }
    // Update city and state maps based on current contacts
    public static void updateMap(AddressBook addressBook) {
        cityMap.clear();
        stateMap.clear();

        addressBookList.values().stream()
                .flatMap(book -> book.contactList.stream())
                .forEach(contact -> {
                    cityMap.computeIfAbsent(contact.getCity(), k -> new ArrayList<>()).add(contact);
                    stateMap.computeIfAbsent(contact.getState(), k -> new ArrayList<>()).add(contact);
                });

        // Display city-wise and state-wise contact counts
        System.out.println("\nCity-wise contact counts:");
        cityMap.forEach((city, list) ->
                System.out.println("City: " + city + " | Count: " + list.size()));

        System.out.println("\nState-wise contact counts:");
        stateMap.forEach((state, list) ->
                System.out.println("State: " + state + " | Count: " + list.size()));
    }

    // View contacts by city
    public static void viewPersonsByCity(String city) {
        System.out.println("\nContacts in city: " + city);
        List<Contact> persons = cityMap.getOrDefault(city, new ArrayList<>());
        if (persons.isEmpty()) {
            System.out.println("No contacts found in " + city);
        } else {
            persons.forEach(System.out::println);
        }
    }

    // View contacts by state
    public static void viewPersonsByState(String state) {
        System.out.println("\nContacts in state: " + state);
        List<Contact> persons = stateMap.getOrDefault(state, new ArrayList<>());
        if (persons.isEmpty()) {
            System.out.println("No contacts found in " + state);
        } else {
            persons.forEach(System.out::println);
        }
    }

    // Search contacts by city
    public static void searchPersonsByCity(String city) {
        System.out.println("\nSearching for people in city: " + city);
        addressBookList.values().stream()
                .flatMap(book -> book.contactList.stream())
                .filter(contact -> contact.getCity().equalsIgnoreCase(city))
                .forEach(System.out::println);
    }

    // Search contacts by state
    public static void searchPersonsByState(String state) {
        System.out.println("\nSearching for people in state: " + state);
        addressBookList.values().stream()
                .flatMap(book -> book.contactList.stream())
                .filter(contact -> contact.getState().equalsIgnoreCase(state))
                .forEach(System.out::println);
    }
}

// Class to store individual contact details
class Contact {
    private String firstName;
    private String lastName;
    private String city;
    private String address;
    private String state;
    private String phoneNumber;
    private String email;
    private String zip;


    public Contact(String firstName, String lastName, String city, String address, String zip, String state, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.address = address;
        this.zip = zip;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;

    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public String getZip() { return zip; }
    public String getState() { return state; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }

    public void setCity(String city) { this.city = city; }
    public void setAddress(String address) { this.address = address; }
    public void setZip(String zip) { this.zip = zip; }
    public void setState(String state) { this.state = state; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }

    // Equality based on first and last name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contacts = (Contact) o;
        return firstName.equalsIgnoreCase(contacts.firstName) &&
                lastName.equalsIgnoreCase(contacts.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
    }

    // Display contact information
    @Override
    public String toString() {
        return firstName + " " + lastName + " | Phone: " + phoneNumber +
                " | Email: " + email + " | City: " + city + " | State: " + state;
    }
}

// Class address book to store multiple contacts
class AddressBook {
    private String name;
    ArrayList<Contact> contactList = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public AddressBook(String name) { this.name = name; }
    public String getName() { return name; }

    // Add a new contact
    public void addContact(Contact contact) {
        if (contactList.stream().anyMatch(c -> c.equals(contact))) {
            System.out.println("Duplicate contact! " + contact.getFirstName() + " " + contact.getLastName() + " already exists.");
        } else {
            contactList.add(contact);
            System.out.println("Contact added successfully.");
        }
    }

    // Edit contact details by name
    public void editDetails(String firstName, String lastName) {
        for (Contact contact : contactList) {
            if (contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName)) {
                System.out.println("Enter new city: ");
                contact.setCity(scanner.nextLine());
                System.out.println("Enter new address: ");
                contact.setAddress(scanner.nextLine());
                System.out.println("Enter new zip: ");
                contact.setZip(scanner.nextLine());
                System.out.println("Enter new state: ");
                contact.setState(scanner.nextLine());
                System.out.println("Enter new phone number: ");
                contact.setPhoneNumber(scanner.nextLine());
                System.out.println("Enter new email: ");
                contact.setEmail(scanner.nextLine());
                System.out.println("Contact updated successfully.");
                return;
            }
        }
        System.out.println("No contact found with the provided name.");
    }

    // Delete a contact by name
    public void deleteDetails(String firstName, String lastName) {
        boolean removed = contactList.removeIf(contact ->
                contact.getFirstName().equalsIgnoreCase(firstName) &&
                        contact.getLastName().equalsIgnoreCase(lastName));
        if (removed) {
            System.out.println("Contact deleted successfully.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    // Sort and display contacts by name
    public void sortAndDisplayByName() {
        System.out.println("\nContacts sorted by name in " + name + ":");
        contactList.stream()
                .sorted(Comparator.comparing(Contact::getFirstName)
                        .thenComparing(Contact::getLastName))
                .forEach(System.out::println);
    }

    // Sort and display contacts by city
    public void sortAndDisplayByCity() {
        System.out.println("\nContacts sorted by city in " + name + ":");
        contactList.stream()
                .sorted(Comparator.comparing(Contact::getCity))
                .forEach(System.out::println);
    }

    // Sort and display contacts by state
    public void sortAndDisplayByState() {
        System.out.println("\nContacts sorted by state in " + name + ":");
        contactList.stream()
                .sorted(Comparator.comparing(Contact::getState))
                .forEach(System.out::println);
    }

    //Sort and display contacts by zip
    public void sortAndDisplayByZip() {
        System.out.println("\nContacts sorted by zip in " + name + ":");
        contactList.stream()
                .sorted(Comparator.comparing(Contact::getZip))
                .forEach(System.out::println);
    }

    //Adding the details to a file
    public void writeToFile(String filePath){

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {

            for (Contact contact : contactList) {
                bw.write(String.join(",", contact.getFirstName(), contact.getLastName(), contact.getCity(),
                        contact.getZip(),contact.getState(), contact.getAddress(), contact.getPhoneNumber(), contact.getEmail()));
                bw.newLine();
            }
            System.out.println("\nContacts saved to file: " + filePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Reading details from the file
    public void readFromFile(String filePath){
        System.out.println("\nReading from a file");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            contactList.clear();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    Contact contact = new Contact(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                    contactList.add(contact);
                }
            }
            System.out.println("Contacts loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    // Method to write contacts to a CSV file
    public void writeToCSV(String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            writer.writeNext(new String[]{"FirstName ", "LastName", "City", "Address", "Zip", "State", "PhoneNumber", "Email"});

            for (Contact contact : contactList) {
                writer.writeNext(new String[]{
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getCity(),
                        contact.getAddress(),
                        contact.getZip(),
                        contact.getState(),
                        contact.getPhoneNumber(),
                        contact.getEmail()
                });
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println("\nContacts saved to " + filePath);
    }

    // Read contacts from CSV
    public void readFromCSV(String filePath) {
        contactList.clear();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                contactList.add(new Contact(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[7]));
            }
        }
        catch(Exception  e){
            System.out.println(e.getMessage());
        }
        System.out.println("\nContacts loaded from " + filePath + ":");
        contactList.forEach(System.out::println);
    }

    // Method to write contacts to a JSON file
    public void writeToJson(String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(contactList, writer);
            System.out.println("\nContacts saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    // Method to read contacts from a JSON file
    public void readFromJson(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            Type contactListType = new TypeToken<List<Contact>>() {}.getType();
            contactList = gson.fromJson(reader, contactListType);
            System.out.println("\nContacts loaded from " + filePath + ":");
            contactList.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading from JSON file: " + e.getMessage());
        }
    }
}



public class AddressBookJavaEight {
    public static void main(String[] args) {
        System.out.println("WELCOME TO ADDRESS BOOK SYSTEM");
        Scanner scanner = new Scanner(System.in);

        // Creating address book and adding contacts
        AddressBook bookOne = new AddressBook("MyContacts");
        AddressBooksSystem.addAddressBook(bookOne);

        Contact contactOne = new Contact("Sreyas", "Sannuthi", "Chennai",
                "513246-Street36", "517212","TamilNadu", "6678900765", "ssreyas@gmail.com");
        bookOne.addContact(contactOne);

        Contact contactTwo = new Contact("Ravi", "Kishore", "Hyderabad",
                "871577-Street45","123456", "Telangana", "9090909090", "rkishore@gmail.com");
        bookOne.addContact(contactTwo);

        // Updating maps and displaying counts
        AddressBooksSystem.updateMap(bookOne);

        // Viewing and searching contacts
        AddressBooksSystem.viewPersonsByCity("Chennai");
        AddressBooksSystem.searchPersonsByState("TamilNadu");

        // Editing a contact
        System.out.println("\nEnter First Name to edit: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter Last Name to edit: ");
        String lastName = scanner.nextLine();
        bookOne.editDetails(firstName, lastName);

        // Updating maps
        AddressBooksSystem.updateMap(bookOne);

        // Displaying sorted contacts
        bookOne.sortAndDisplayByName();
        bookOne.sortAndDisplayByCity();
        bookOne.sortAndDisplayByState();
        bookOne.sortAndDisplayByZip();

        // Deleting a contact and showing updated list
        bookOne.deleteDetails("Ravi", "Kishore");
        bookOne.sortAndDisplayByName();

        //FileIO Operations
        String filePath="src/com/bridgelabz/AddressBook/fileIO.txt";
        bookOne.writeToFile(filePath);
        bookOne.readFromFile(filePath);

        //CSV Operations
        bookOne.writeToCSV("src/com/bridgelabz/AddressBook/opencsv.csv");
        bookOne.readFromCSV("src/com/bridgelabz/AddressBook/opencsv.csv");

        //JSON Operations
        bookOne.writeToJson("src/com/bridgelabz/AddressBook/GSON.json");
        bookOne.readFromJson("src/com/bridgelabz/AddressBook/GSON.json");

        scanner.close();
    }
}
