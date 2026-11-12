# Java TCP Tic-Tac-Toe

A Java implementation of the classic two-player **Tic-Tac-Toe (X/O)** game using **TCP socket communication** and a **client-server architecture**.

This project was developed as a programming exercise to explore Java networking, socket-based communication, and the coordination of a shared game over a network.

## Project overview

The application uses a client-server model: a server accepts network connections, while clients communicate over TCP to play Tic-Tac-Toe. The project provides a practical example of connecting application-level game logic with network I/O.

## Technologies and concepts

- **Java** — application and game logic
- **TCP/IP sockets** — network communication
- **Client-server architecture** — separate server and client responsibilities
- **Object-oriented programming** — organizing application code
- **Turn-based game state** — representing an X/O game between two players

## Architecture

```text
                TCP/IP
  Player X <------------> Server <------------> Player O
   Client                  |                     Client
                     Game communication
```

Clients connect to the server and exchange the information needed to play the game. The precise message format and state-handling implementation can be found in the source code.

## Getting started

### Prerequisites

- Java Development Kit (JDK)
- A terminal or a Java IDE

### Clone the repository

```bash
git clone https://github.com/mstranjancevic/java-tcp-tic-tac-toe.git
cd java-tcp-tic-tac-toe
```

### Run the project

1. Open the project in your Java IDE or locate the Java source files containing the `main` methods.
2. Compile and start the **server** application first.
3. Start the **client** application for each player.
4. Ensure that both clients use the same host and port configured by the server. For local testing, the host is typically `localhost`.
5. Follow the application's prompts or interface to play.

> **Note:** Exact build and launch commands depend on the repository's Java package structure and entry-point class names. These should be confirmed from the source before adding copy-paste commands here.

## What I practiced

- Writing Java applications that communicate over a network
- Working with TCP connections and exchanging application messages
- Understanding how clients and a server interact
- Connecting network communication with turn-based application logic
- Structuring and debugging a multi-process programming project

## About

Academic Java networking project by **Marko Stranjančević**, Computer Engineering student at the Faculty of Computing (RAF), Belgrade.

[GitHub profile](https://github.com/mstranjancevic)
