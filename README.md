# PlayStation Center Management System 🎮

A comprehensive, Java-based Object-Oriented application designed to manage the daily operations of a PlayStation gaming center. The system streamlines user tracking, session allocations, device maintenance, and structural bookings.

## 🚀 Key Features & Project Structure

The system is built around a cohesive set of classes that handle specialized functionalities:

* **Central Execution (`SquadGameSystem`)**: The main entry point of the application containing the central execution logic and user interface.
* **Core Architecture (`SystemEntity`)**: The base abstract class providing core attributes and shared properties for all system entities.
* **User Management (`User`)**: Manages different system users, profiles, and access roles within the center.
* **Session Management (`Gaming`)**: Tracks active gaming sessions, live console usage, and calculates time/cost.
* **Reservation System (`Reservation`)**: Handles scheduling and booking rooms or specific consoles in advance for customers.
* **Inventory Control (`GameItem`)**: Manages inventory items, including available game disks, accessories, and consoles.
* **Maintenance Tracking (`Maintenance`)**: Logs hardware status, tracking consoles that are out-of-service or scheduled for repair.
* **Database Integration (`DBOperations`)**: Controls data persistence, facilitating clean retrieval and storage operations smoothly.
* **Custom Error Handling (`SystemDatabaseException`)**: A custom exception class designed to handle runtime anomalies and database errors securely.

## 🏗️ System Architecture

This project is built using advanced Object-Oriented Programming (OOP) principles, ensuring modularity, code reusability, and system stability. The architecture utilizes:
* **Inheritance & Polymorphism**: Implemented extensively through the core `SystemEntity` class to manage different types of users and gaming entities efficiently.
* **Encapsulation**: Protecting sensitive data such as pricing, user roles, and database connection details.

## 🛠️ Technologies Used
* **Language**: Java
* **Concepts**: Advanced OOP, Exception Handling, File/Database I/O

## 📋 How to Use
1. Clone this repository to your local machine.
2. Ensure you have a Java IDE (like IntelliJ IDEA, Eclipse, or NetBeans) installed.
3. Import the project files.
4. Run the main class `SquadGameSystem.java` to start the application.
