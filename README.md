# JavaFX Visual Novel Engine

![Status](https://img.shields.io/badge/status-work%20in%20progress-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![License](https://img.shields.io/badge/license-MIT-green)

A lightweight Visual Novel Engine built with JavaFX, featuring State Pattern, Command Pattern, and custom Script parsing.

> **Note**:  This project is currently in active development. Features may change and some functionality is still being implemented.

---
## Features

### Implemented
- **Character System**
  - Dynamic positioning (LEFT, CENTER, RIGHT)
  - Character image swapping for expressions
  - Automatic speaker highlighting with opacity
  - Horizontal flip support

- **Dialogue System**
  - Typewriter text effect with customizable speed
  - Speaker name display
  - Skip typing functionality

- **Script Parser**
  - Custom scripting language
  - Command-based system (background, show, hide, change, wait)
  - Easy-to-learn syntax

- **Visual Management**
  - Background image system
  - Character layering (Background → Characters → Dialogue)
  - Automatic image scaling
### In Progress
  - Sound system (BGM & SFX)
  - Transition effects
  - Save/Load system
### Planned
  - Choice system
  - Character animations
  - Settings menu & Menu
  - etc (maybe something interesting but we will have it later)

---

## Getting Started

### Prerequisites
- **Java 21** or higher
- **Maven 3.8+**
- **JavaFX 21**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/meoluoi3/JavaFX-VN-Engine.git
   cd JavaFX-VN-Engine
   ```
2. **Build the project**
   ```bash
   mvn clean install
   ```
3. **Run the application**
   ```bash
   mvn javafx:run
   ```

---

## Script Syntax
Create story scripts in '.txt' files inside `src/main/resources/com/vnengine/dialogue/`
(or use absolute path to the .txt file)

### Basic Commands
#### Background
```
[background image_name. jpg]
```

#### Characters
```
[show CharacterName image. png Position Flipped]
[hide CharacterName]
[change CharacterName new_image.png]
```

**Positions**:  `LEFT`, `CENTER`, `RIGHT`  
**Flipped**: `true` or `false` (optional, default:  false)

#### Dialogue
```
CharacterName:  Dialogue text goes here. 
```

#### Timing
```
[wait 2.0]
```

### Example Script

```
[background Spiral_Atlas_VN_House_Backgrounds/single_bedroom.jpg]

[show Alice referencepose/png_256x288/body2_35.png LEFT false]
Alice: Hello! Welcome to my room. 

[wait 1.0]

[show Bob referencepose/png_256x288/body2_35.png RIGHT true]
Bob: Nice to meet you, Alice!

[change Alice referencepose/png_256x288/body2_40.png]
Alice: I'm so happy you're here! 

[hide Bob]
Alice: See you later!
```

---

## Architecture
This engine uses several design patterns:

- **State Pattern**:  Manages different game states (Menu, Gameplay, etc.)
- **Command Pattern**:  Extensible script command system
- **Singleton Pattern**:  GameDirector, CharacterManager, AssetLoader
- **MVC-inspired**:  Separation of logic, UI, and data (need more development & refactoring work)

---

## Technologies
- **JavaFX 21** - UI framework and rendering
- **SLF4J + Logback** - Logging
- **Maven** - Build tool and dependency management

## Asset Credits

### Backgrounds
- **Spiral Atlas Visual Novel House Backgrounds** by [Spiral Atlas](https://spiralatlas.github.io/credits/)  
  Licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)

### Character Sprites
- **Editable Pixel Art Pose Pack** by Noraneko Games (Raziel Nozac Zerreitug)  
  Free to use with optional attribution

See [CREDITS.md](CREDITS.md) for full details.

---

##  Contributing

Contributions are welcome! Whether you're:
- Reporting bugs
- Suggesting features
- Submitting pull requests
- Improving documentation

Please feel free to open an issue or PR! 

### Development Setup

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## Roadmap

- [x] Character system with positioning
- [x] Dialogue box with typewriter effect
- [x] Script parser and executor
- [x] Background management
- [ ] Sound system (BGM/SFX)
- [ ] Save/Load functionality
- [ ] Choice system for branching stories
- [ ] Transition effects (fade, slide, etc.)
- [ ] CG (event images) support
- [ ] Settings menu (volume, text speed, etc.)
- [ ] Multiple save slots
- [ ] Skip/Auto-read mode

---

## License

This project's **code** is open source under the MIT License.  
**Assets** (backgrounds, characters) have their own licenses - see [CREDITS.md](CREDITS.md).

---

## Contact

**Developer**: [@meoluoi3](https://github.com/meoluoi3)

For questions, suggestions, or bug reports, please [open an issue](https://github.com/meoluoi3/JavaFX-VN-Engine/issues).

---

## Acknowledgements

- [Ren'Py](https://www.renpy.org/) - Inspiration for VN structure
- [Spiral Atlas](https://spiralatlas.github.io/) - Beautiful background art
- Raziel Nozac Zerreitug - Character sprite bases
- JavaFX community for excellent documentation

---

## Screenshots

Coming soon! 
