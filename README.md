# Contact Manager with Fast Search

 **Java** as part of the Data Structures and Algorithms (DSA) coursework at **Rungta International Skills University** (School of Computer Science and Engineering).

---

## Project Overview

- **Problem Statement No.:** 73
- **Project Title:** Contact Manager with Fast Search
- **Language:** Java (JDK 8+)
- **Core Concepts:** Binary Search Tree (BST), Hashing (`HashMap`), Sorting (Merge Sort), Linked List, File Handling

This application maintains a persistent contact directory storing **Contact ID**, **Name**, **10-digit Phone Number**, and **Email Address**. It combines multiple data structures to balance sequential storage, alphabetical browsing, and constant-time phone lookups.

---

## System Architecture & Data Structures

| Data Structure / Technique | Role in Project | Time Complexity |
| :--- | :--- | :--- |
| **Singly Linked List** (`ContactLinkedList`) | Primary sequential storage for all contact records (`C101`, `C102`, ...). | Insertion: $O(n)$<br>Traversal: $O(n)$ |
| **Binary Search Tree** (`ContactBST`) | Indexes contacts alphabetically by name for ordered in-order browsing and fast name search. | Search / Insert: $O(\log n)$ avg |
| **HashMap** (`phoneMap`) | Provides instant phone number lookup and prevents duplicate 10-digit numbers. | Lookup / Insert: $O(1)$ avg |
| **Merge Sort** | Generates sorted directory reports (Ascending & Descending by name) without altering insertion order. | Sorting: $O(n \log n)$ |
| **File Handling** (`contacts.txt`) | Loads existing contacts automatically at startup and saves all changes upon exit. | Load / Save: $O(n)$ |

---

## Key Features

- **Add Contact:** Auto-generates unique Contact IDs (`C101`, `C102`, ...) and validates strictly 10-digit unique phone numbers.
- **Display All Contacts:** Lists all records stored in the Linked List in insertion order.
- **Alphabetical Browsing (BST):** Performs an In-Order Traversal of the Binary Search Tree to display contacts in sorted name order.
- **Fast Search:**
  - **By Phone Number:** $O(1)$ average lookup using `HashMap`.
  - **By Name:** Case-insensitive search using the `BST`.
- **Update & Delete Contact:** Modifies or removes records while automatically re-synchronizing the BST and HashMap indexes.
- **Sorted Directory Reports:** Uses Merge Sort to display contacts in either **Ascending (A–Z)** or **Descending (Z–A)** order.
- **Persistent Storage:** Automatically reads from and writes to `contacts.txt`.

---

## Project Structure

```text
Contact-Manager-Fast-Search/
├── ContactManager.java   # Complete Java source code (Linked List, BST, HashMap, Merge Sort, File I/O)
├── contacts.txt          # Persistent data file (auto-generated on exit)
└── README.md             # Project documentation
