import java.io.*;
import java.util.*;

class Contact {
    String id;
    String name;
    String phone;
    String email;

    public Contact(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + phone + (email != null && !email.isEmpty() ? " | " + email : "");
    }
}

class ContactLinkedList {
    private static class Node {
        Contact data;
        Node next;

        Node(Contact data) {
            this.data = data;
        }
    }

    private Node head;

    public void add(Contact contact) {
        Node newNode = new Node(contact);
        if (head == null) {
            head = newNode;
            return;
        }
        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = newNode;
    }

    public boolean removeById(String id) {
        if (head == null) return false;
        if (head.data.id.equalsIgnoreCase(id)) {
            head = head.next;
            return true;
        }
        Node curr = head;
        while (curr.next != null) {
            if (curr.next.data.id.equalsIgnoreCase(id)) {
                curr.next = curr.next.next;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public List<Contact> getAll() {
        List<Contact> list = new ArrayList<>();
        Node curr = head;
        while (curr != null) {
            list.add(curr.data);
            curr = curr.next;
        }
        return list;
    }
}

class ContactBST {
    private static class BSTNode {
        Contact contact;
        BSTNode left, right;

        BSTNode(Contact contact) {
            this.contact = contact;
        }
    }

    private BSTNode root;

    public void insert(Contact contact) {
        root = insertRec(root, contact);
    }

    private BSTNode insertRec(BSTNode node, Contact contact) {
        if (node == null) return new BSTNode(contact);

        int cmp = contact.name.compareToIgnoreCase(node.contact.name);
        if (cmp < 0) {
            node.left = insertRec(node.left, contact);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, contact);
        } else {
            if (contact.id.compareToIgnoreCase(node.contact.id) < 0) {
                node.left = insertRec(node.left, contact);
            } else {
                node.right = insertRec(node.right, contact);
            }
        }
        return node;
    }

    public List<Contact> searchByName(String name) {
        List<Contact> results = new ArrayList<>();
        searchRec(root, name.trim(), results);
        return results;
    }

    private void searchRec(BSTNode node, String name, List<Contact> results) {
        if (node == null) return;
        int cmp = name.compareToIgnoreCase(node.contact.name);
        if (cmp == 0) {
            results.add(node.contact);
            searchRec(node.left, name, results);
            searchRec(node.right, name, results);
        } else if (cmp < 0) {
            searchRec(node.left, name, results);
        } else {
            searchRec(node.right, name, results);
        }
    }

    public void displayInOrder() {
        if (root == null) {
            System.out.println("Directory is empty.");
            return;
        }
        inOrderRec(root);
    }

    private void inOrderRec(BSTNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.println(node.contact);
            inOrderRec(node.right);
        }
    }

    public void clear() {
        root = null;
    }
}

public class ContactManager {
    private static final String FILE_NAME = "contacts.txt";

    private final ContactLinkedList contactList = new ContactLinkedList();
    private final ContactBST nameBST = new ContactBST();
    private final HashMap<String, Contact> phoneMap = new HashMap<>();
    private int idCounter = 101;

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    private void syncIndexes() {
        nameBST.clear();
        phoneMap.clear();
        for (Contact c : contactList.getAll()) {
            nameBST.insert(c);
            phoneMap.put(c.phone, c);
        }
    }

    public void addContact(String name, String phone, String email) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }
        if (!isValidPhone(phone)) {
            System.out.println("Error: Phone number must contain exactly 10 digits.");
            return;
        }
        if (phoneMap.containsKey(phone)) {
            System.out.println("Error: Duplicate phone number. Contact already exists.");
            return;
        }

        String id = "C" + idCounter++;
        Contact contact = new Contact(id, name.trim(), phone.trim(), email == null ? "" : email.trim());

        contactList.add(contact);
        nameBST.insert(contact);
        phoneMap.put(contact.phone, contact);

        System.out.println("Contact added: " + contact.id + " | " + contact.name + " | " + contact.phone);
    }

    public void displayContacts() {
        List<Contact> all = contactList.getAll();
        if (all.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        for (Contact c : all) {
            System.out.println(c);
        }
    }

    public void browseByNameBST() {
        nameBST.displayInOrder();
    }

    public void searchByPhone(String phone) {
        Contact contact = phoneMap.get(phone);
        if (contact == null) {
            System.out.println("Contact not found.");
        } else {
            System.out.println(contact);
        }
    }

    public void searchByName(String name) {
        List<Contact> matches = nameBST.searchByName(name);
        if (matches.isEmpty()) {
            System.out.println("Contact not found.");
        } else {
            for (Contact c : matches) {
                System.out.println(c);
            }
        }
    }

    public void updateContact(String existingPhone, String newName, String newPhone, String newEmail) {
        Contact contact = phoneMap.get(existingPhone);
        if (contact == null) {
            System.out.println("Contact not found.");
            return;
        }

        if (newPhone != null && !newPhone.trim().isEmpty() && !newPhone.trim().equals(existingPhone)) {
            if (!isValidPhone(newPhone.trim())) {
                System.out.println("Error: Phone number must contain exactly 10 digits.");
                return;
            }
            if (phoneMap.containsKey(newPhone.trim())) {
                System.out.println("Error: Duplicate phone number.");
                return;
            }
            contact.phone = newPhone.trim();
        }

        if (newName != null && !newName.trim().isEmpty()) {
            contact.name = newName.trim();
        }
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            contact.email = newEmail.trim();
        }

        syncIndexes();
        System.out.println("Contact updated: " + contact);
    }

    public void deleteContact(String phone) {
        Contact contact = phoneMap.get(phone);
        if (contact == null) {
            System.out.println("Contact not found.");
            return;
        }
        contactList.removeById(contact.id);
        syncIndexes();
        System.out.println("Contact deleted: " + contact.id + " | " + contact.name);
    }

    public void displaySortedReport(boolean descending) {
        List<Contact> all = contactList.getAll();
        if (all.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }
        List<Contact> sorted = mergeSort(all, descending);
        for (int i = 0; i < sorted.size(); i++) {
            Contact c = sorted.get(i);
            System.out.println((i + 1) + ". " + c.id + " | " + c.name + " | " + c.phone
                    + (c.email.isEmpty() ? "" : " | " + c.email));
        }
    }

    private List<Contact> mergeSort(List<Contact> list, boolean descending) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        List<Contact> left = mergeSort(new ArrayList<>(list.subList(0, mid)), descending);
        List<Contact> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), descending);
        return merge(left, right, descending);
    }

    private List<Contact> merge(List<Contact> left, List<Contact> right, boolean descending) {
        List<Contact> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            int cmp = left.get(i).name.compareToIgnoreCase(right.get(j).name);
            boolean takeLeft = descending ? (cmp >= 0) : (cmp <= 0);
            if (takeLeft) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 100;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String phone = parts[2].trim();
                    String email = parts.length >= 4 ? parts[3].trim() : "";

                    if (isValidPhone(phone) && !phoneMap.containsKey(phone)) {
                        Contact c = new Contact(id, name, phone, email);
                        contactList.add(c);
                        nameBST.insert(c);
                        phoneMap.put(phone, c);

                        if (id.startsWith("C")) {
                            try {
                                int num = Integer.parseInt(id.substring(1));
                                if (num > maxId) maxId = num;
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
            idCounter = maxId + 1;
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Contact c : contactList.getAll()) {
                pw.println(c.id + "|" + c.name + "|" + c.phone + "|" + c.email);
            }
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ContactManager manager = new ContactManager();
        manager.loadFromFile();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n========= CONTACT MANAGER =========");
            System.out.println("1. Add Contact");
            System.out.println("2. Display All Contacts (Linked List)");
            System.out.println("3. Browse Alphabetically (BST)");
            System.out.println("4. Search by Phone (HashMap)");
            System.out.println("5. Search by Name (BST)");
            System.out.println("6. Update Contact");
            System.out.println("7. Delete Contact");
            System.out.println("8. Sorted Report (Ascending)");
            System.out.println("9. Sorted Report (Descending)");
            System.out.println("0. Save & Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Phone (10 digits): ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Email (optional): ");
                    String email = sc.nextLine();
                    manager.addContact(name, phone, email);
                    break;
                case "2":
                    manager.displayContacts();
                    break;
                case "3":
                    manager.browseByNameBST();
                    break;
                case "4":
                    System.out.print("Enter Phone Number: ");
                    manager.searchByPhone(sc.nextLine().trim());
                    break;
                case "5":
                    System.out.print("Enter Name: ");
                    manager.searchByName(sc.nextLine().trim());
                    break;
                case "6":
                    System.out.print("Enter Existing Phone Number: ");
                    String oldPhone = sc.nextLine().trim();
                    System.out.print("Enter New Name (press Enter to skip): ");
                    String newName = sc.nextLine();
                    System.out.print("Enter New Phone (press Enter to skip): ");
                    String newPhone = sc.nextLine();
                    System.out.print("Enter New Email (press Enter to skip): ");
                    String newEmail = sc.nextLine();
                    manager.updateContact(oldPhone, newName, newPhone, newEmail);
                    break;
                case "7":
                    System.out.print("Enter Phone Number to Delete: ");
                    manager.deleteContact(sc.nextLine().trim());
                    break;
                case "8":
                    manager.displaySortedReport(false);
                    break;
                case "9":
                    manager.displaySortedReport(true);
                    break;
                case "0":
                    manager.saveToFile();
                    System.out.println("Contacts saved to " + FILE_NAME + ". Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}