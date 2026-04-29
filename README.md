# Maze Game Application

## Overview
[cite_start]This repository contains a fully functional, multi-layered desktop Maze Game application, developed as the final project for the Advanced Topics in Programming course at Ben-Gurion University of the Negev[cite: 1, 2, 6, 25]. [cite_start]The project demonstrates advanced software engineering concepts, transitioning from algorithmic pseudo-code to a robust Client-Server architecture and culminating in an interactive Graphical User Interface[cite: 10, 11, 13].

---

## Tech Stack & Architecture
* [cite_start]**Language:** Java [cite: 25]
* [cite_start]**Framework:** JavaFX 16 [cite: 13, 87]
* [cite_start]**Build & Logging:** Maven & Log4j2 [cite: 949, 951]
* [cite_start]**Design Patterns:** MVVM (Model-View-ViewModel), Decorator, Strategy, and Adapter [cite: 3, 14, 18, 241, 587, 800]
* [cite_start]**Concurrency:** Java Threads & Thread Pools [cite: 15, 18, 590, 602]

---

## Key Features

### 1. Algorithmic Core (Part A)
* [cite_start]**Maze Generation:** Implements randomized maze generation algorithms (such as Prim's or DFS) capable of building complex 2D and 3D grids up to 1000x1000 cells efficiently[cite: 161, 180, 181, 310, 381].
* [cite_start]**Search Algorithms:** Features Breadth-First Search (BFS), Depth-First Search (DFS), and Best-First Search to solve mazes and find the optimal path to the exit[cite: 229, 235, 236, 237, 291].

### 2. Client-Server & Network (Part B)
* [cite_start]**Concurrent Servers:** Utilizes a Thread Pool to simultaneously handle requests from multiple clients without blocking[cite: 590, 602]. [cite_start]One server focuses on maze generation, while the second handles maze solving[cite: 487, 488, 489].
* [cite_start]**Data Compression:** Employs the Decorator design pattern over Java IO Streams to compress maze data, significantly reducing payload size during network transmission[cite: 490, 495, 514, 533].
* [cite_start]**Solution Caching:** The solving server persists previously calculated solutions to the disk, allowing for instant retrieval and avoiding redundant computations[cite: 491, 493, 605, 609].

### 3. Interactive GUI (Part C)
* [cite_start]**MVVM Architecture:** Ensures a clean separation of concerns between UI logic (View), presentation logic (ViewModel), and business logic (Model)[cite: 3, 800, 801, 842].
* [cite_start]**Event-Driven Gameplay:** Users can navigate the maze using the Numpad (including diagonal support) or by dragging the character with the mouse[cite: 891, 892, 893, 905].
* [cite_start]**User Experience:** Includes dynamic Zoom In/Out (Ctrl + Mouse wheel), background music, and adaptive window resizing[cite: 902, 903, 904, 907].
* [cite_start]**Persistence & Configuration:** Allows players to save and load maze states and manage application settings through a dedicated properties file[cite: 740, 745, 750, 897].

* To play create a new game, choose settings, and move with the numpads. (2 down, 8 up, 4 left, 6 right)
