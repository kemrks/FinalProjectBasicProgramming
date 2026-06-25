# Simple Tic-Tac-Toe Game with Java Swing, Login, and Statistics

## Student Information
| Field       | Value           |
|-------------|-----------------|
| Name        | *Kemal Arka Satya*   |
| Student ID  | *5026251124*    |
| Class       | ES234211        |

---

## Project Description
This is a simple Tic-Tac-Toe game built using **Java Swing GUI**.  
The player (X) plays against the computer (O) on a 3×3 board.  
The application includes a login system, game statistics tracking, and a Top 5 leaderboard — all connected to a relational database.

---

## Features
- ✅ Login using username and password stored in the database
- ✅ Play Tic-Tac-Toe with a graphical board (Java Swing)
- ✅ Computer makes smart moves (tries to win, blocks player)
- ✅ Detects win, lose, and draw
- ✅ Records wins, losses, draws, and score in the database
- ✅ View personal statistics
- ✅ View Top 5 scorers in a table (JTable)
- ✅ All data is retrieved from the database — nothing hardcoded

---

## Scoring System
| Result | Points |
|--------|--------|
| Win    | +10    |
| Draw   | +3     |
| Lose   | +0     |

---

## Database
**Database system used:** MySQL *(change to PostgreSQL or SQL Server if needed)*  
**Table name:** `players`  
**Number of tables:** 1 (as required)

### Table Schema
| Column   | Type         | Description              |
|----------|--------------|--------------------------|
| id       | INT (PK, AI) | Unique row ID            |
| username | VARCHAR(50)  | Player login name        |
| password | VARCHAR(100) | Player password          |
| wins     | INT          | Number of wins           |
| losses   | INT          | Number of losses         |
| draws    | INT          | Number of draws          |
| score    | INT          | Total score              |

---

## How to Run

### Step 1 — Create the Database
1. Open MySQL Workbench (or your database tool)
2. Run the file `database/schema.sql`
3. This creates the `game_project` database, the `players` table, and 5 sample users

### Step 2 — Configure Database Connection
1. Open `src/DatabaseManager.java`
2. Change these three lines to match your setup:
```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/game_project";
private static final String DB_USER     = "root";
private static final String DB_PASSWORD = "";
```

### Step 3 — Add the JDBC Driver
- For MySQL: download `mysql-connector-j-x.x.x.jar` and add it to your project's classpath
- In IntelliJ IDEA: File → Project Structure → Libraries → Add JAR
- In Eclipse: Right-click project → Build Path → Add External JAR

### Step 4 — Run the Program
- Run `Main.java`
- The Login Window will appear
- Login with: `student1` / `12345`

---

## Class Explanation

| Class             | Responsibility                                      |
|-------------------|-----------------------------------------------------|
| `Main`            | Entry point — opens the Login Window                |
| `DatabaseManager` | Provides the JDBC database connection               |
| `Player`          | Stores one player's data (id, username, stats)      |
| `PlayerService`   | Handles login, statistics update, top 5 query       |
| `GameLogic`       | Board state, move validation, win/draw detection    |
| `LoginFrame`      | GUI: username + password input window               |
| `MainMenuFrame`   | GUI: main menu with 4 navigation buttons            |
| `GameFrame`       | GUI: 3×3 Tic-Tac-Toe board with 9 cell buttons     |
| `StatisticsFrame` | GUI: shows personal stats of the logged-in player   |
| `TopScorersFrame` | GUI: shows Top 5 players in a JTable                |

---

## Screenshots
*(Add screenshots of Login Window, Game Window, Statistics, and Top 5 Scorers here)*

---

## Repository Structure
```
student-swing-game-project/
├── src/
│   ├── Main.java
│   ├── DatabaseManager.java
│   ├── Player.java
│   ├── PlayerService.java
│   ├── GameLogic.java
│   ├── LoginFrame.java
│   ├── MainMenuFrame.java
│   ├── GameFrame.java
│   ├── StatisticsFrame.java
│   └── TopScorersFrame.java
├── database/
│   └── schema.sql
├── screenshots/
│   └── (add your screenshots here)
└── README.md
```

---

## GitHub Link
*[(paste your GitHub repository link here)](https://github.com/kemrks/FinalProjectBasicProgramming/blob/main/Tictactoe%20FP/README.md)*

## YouTube Video Link
*(paste your YouTube demonstration video link here)*
