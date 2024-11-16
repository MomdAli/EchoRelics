<p align="center">
<a style="text-decoration:none" href="https://momdali.de/Studium/Semester-3/Software-Engineering/Echo-Relics/Echo-Relics-(About)">
    <img src="https://img.shields.io/badge/Echo_Relics-Website-7F6DF2" alt="Website"/></a>
  <a style="text-decoration:none" href="https://coveralls.io/github/MomdAli/EchoRelics?branch=main">
    <img src="https://coveralls.io/repos/github/MomdAli/EchoRelics/badge.svg?branch=main" alt="Test coverage" /></a>
  <a style="text-decoration:none" href="https://app.codacy.com/gh/MomdAli/EchoRelics/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
    <img src="https://app.codacy.com/project/badge/Grade/e684383e6c3e473f9cffa11a78e7855e" alt="Code quality" /></a>
</p>

---
# Echo Relics

**Echo Relics** is a turn-based, grid-based puzzle-adventure game set in a mystical world filled with ancient ruins. Players must explore a 10x10 grid, avoid traps, and collect relics, while dealing with echoes of their past moves that replay in future turns. Strategic movement and the careful use of relics are essential for survival and progression.

## Table of Contents

- [Features](#features)
- [How to Play](#how-to-play)
- [Installation](#installation)
- [Game Mechanics](#game-mechanics)
- [Controls](#controls)
- [License](#license)

---

## Features

- **Turn-based gameplay**: Plan your moves carefully as each turn can affect future ones.
- **Echo mechanic**: Each move leaves behind an echo that repeats the player’s previous actions.
- **Relic system**: Collect powerful relics that grant special abilities to help navigate the ruins.
- **Grid-based exploration**: Navigate a mysterious 5x5 grid filled with ancient tiles.
- **Dynamic environment**: Some tiles may contain relics, while others house echoes or traps.

---

## How to Play

In _Echo Relics_, the player moves across a 10x10 grid, leaving echoes of past movements. The challenge is to navigate the grid while avoiding being caught by your own echoes, which repeat your past movements. Along the way, collect relics to help you manage the grid and your echoes.

- **Objective**: Navigate the grid, avoid echoes, and collect relics.
- **Turn-based strategy**: Plan each move, as past actions will be replayed by your echoes.

---

## Installation

### Prerequisites

- **Java 8** or higher (for running the Scala project)
- **Scala 3.5.1 or higher
- **SBT (Scala Build Tool)**

### Steps to Install

1. Clone the repository:
    
    bash
    
    Copy code
    
    `git clone https://github.com/MomdAli/EchoRelics`
    
2. Navigate to the project directory:
    
    bash
    
    Copy code
    
    `cd echo-relics`
    
3. Run the game using SBT:
    
    bash
    
    Copy code
    
    `sbt run`
    

---

## Game Mechanics

### Grid

The game takes place on a 10x10 grid, where each tile can contain:

- **Player**: Represented as `"P"` in the grid.
- **Relic**: Special items, shown as `"R"`, which grant powers to the player.
- **Echo**: A ghostly version of the player that repeats past moves, represented as `"E"`.
- **Empty Tile**: Represented as empty space `" "`.

### Echo Mechanic

- Every time the player moves, an echo of that move is left behind. After a set number of turns, the echo will replay the player’s past actions.
- Be cautious, as these echoes will mimic your past behavior and may block or even trap you.

### Relics

Relics are powerful tools scattered across the grid that provide the player with special abilities. Use relics wisely to escape tough situations.

### Game Over

The game ends when the player is caught by an echo, moves into a trap, or successfully collects all relics on the grid.

---

## Controls

The game is controlled using **keyboard inputs** for movement:

|Key|Action|
|---|---|
|`W`|Move Up|
|`S`|Move Down|
|`A`|Move Left|
|`D`|Move Right|
|`exit`|Exit the game|

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
