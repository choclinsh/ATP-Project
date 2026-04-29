# Maze Game Application

## Overview
This repository contains a fully functional, multi-layered desktop Maze Game application, developed as the final project for the **Advanced Topics in Programming** course at **Ben-Gurion University of the Negev**.

The project demonstrates advanced software engineering concepts, transitioning from algorithmic pseudo-code to a robust **Client-Server architecture** and culminating in an interactive **Graphical User Interface**.

---

## Tech Stack & Architecture
* **Language:** Java
* **UI Framework:** JavaFX
* **Build & Logging:** Maven & Log4j2
* **Design Patterns:** MVVM (Model-View-ViewModel), Decorator Pattern, Strategy Pattern
* **Concurrency:** Java Threads & Thread Pools

---

## Key Features

### Algorithmic Core (Part A)
* **Maze Generation:** Features randomized Prim's maze generation algorithm capable of building complex 2D grids (and optionally 3D dimensions) up to 1000x1000 cells efficiently.
* **Search Algorithms:** Implements Breadth-First Search (BFS), Depth-First Search (DFS), and Best-First Search to solve mazes and find the optimal path to the exit.

### Client-Server & Network (Part B)
* **Concurrent Servers:** Utilizes a Thread Pool to simultaneously handle requests from multiple clients without blocking. One server handles maze generation, while another handles maze solving.
* **Data Compression:** Uses the Decorator design pattern over Java IO Streams to compress maze data, significantly reducing the payload size during network transmission and file saving.
* **Solution Caching:** The solving server saves previously calculated maze solutions to the disk, fetching them instantly if the same maze is queried again.

### Interactive GUI (Part C)
* **MVVM Architecture:** Clean separation of concerns between the UI logic (View), presentation logic (ViewModel), and business logic/networking (Model).
* **Event-Driven Gameplay:** Users can navigate the maze using the Numpad (including diagonal movements) or by dragging the character with the mouse.
* **Rich Media:** Includes dynamic zoom in/out functionalities, background music, and custom images for characters and walls.
* **Save/Load State:** Players can save their current maze state to disk and load it later to resume playing.
* **Configuration Management:** Dynamic application settings (e.g., thread pool size, generation algorithms) are handled cleanly via a properties configuration file.
