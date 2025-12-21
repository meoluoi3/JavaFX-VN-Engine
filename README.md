# JavaFX Visual Novel Engine

![Status](https://img.shields.io/badge/status-work%20in%20progress-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![License](https://img.shields.io/badge/license-MIT-green)

A lightweight Visual Novel Engine built with JavaFX, featuring State Pattern, Command Pattern, and custom Script parsing. 

> **Note**:  This project is currently in active development.  Features may change and some functionality is still being implemented.

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

- **Audio System**
  - Background music (BGM) with looping and crossfade support
  - Ambient sound effects with independent volume control
  - Voice line playback
  - Fade-in/fade-out effects
  - Multi-channel audio (BGM, Ambient, Voice)
  - Pause/Resume/Stop all controls

- **Script Parser**
  - Custom scripting language
  - Command-based system (background, show, hide, change, wait, music, ambient, voice)
  - Easy-to-learn syntax

- **Visual Management**
  - Background image system
  - Character layering (Background → Characters → Dialogue)
  - Automatic image scaling

### In Progress
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
Create story scripts in '. txt' files inside `src/main/resources/com/vnengine/dialogue/`
(or use absolute path to the . txt file)

### Basic Commands

#### Background
```
[background image_name.jpg]
```

#### Characters
```
[show CharacterName image.png Position Flipped]
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

#### Audio

**Background Music (BGM):**
```
[music filename.mp3]                    # Play once
[music filename.mp3 loop]               # Loop forever
[music filename.mp3 loop 2000]          # Loop with 2-second fade-in
[bgm filename.mp3 loop]                 # Alternative:  use 'bgm' instead of 'music'
```

**Stop Music:**
```
[stopMusic]                             # Stop immediately
[stopMusic 1500]                        # Stop with 1.5-second fade-out
[stopBgm 2000]                          # Alternative: use 'stopBgm'
```

**Crossfade Music:**
```
[crossfade next_track.mp3 3000]         # Smooth transition over 3 seconds
```

**Ambient Sounds:**
```
[ambient filename.mp3]                  # Play once
[ambient filename. mp3 loop]             # Loop forever
```

**Stop Ambient:**
```
[stopAmbient]                           # Stop immediately
[stopAmbient 1000]                      # Stop with 1-second fade-out
```

**Voice Lines:**
```
[voice filename.mp3]                    # Play voice line
```

**Control All Audio:**
```
[pauseAll]                              # Pause all channels (BGM + Ambient + Voice)
[resumeAll]                             # Resume all paused channels
[stopAll]                               # Stop all audio immediately
```

### Example Script

```
[background Spiral_Atlas_VN_House_Backgrounds/single_bedroom.jpg]

# Start peaceful morning music with fade-in
[music bgm/I'll_be_Here.mp3 loop 2000]

[show Alice referencepose/png_256x288/body2_35.png LEFT false]
Alice: Hello! Welcome to my room.

[wait 1.0]

[show Bob referencepose/png_256x288/body2_35.png RIGHT true]
Bob: Nice to meet you, Alice!

[change Alice referencepose/png_256x288/body2_40.png]
Alice: I'm so happy you're here!

# Add ambient sound
[ambient ambient/rain.mp3 loop]

Alice: Listen to that peaceful rain... 

[wait 2.0]

# Crossfade to different music
[crossfade bgm/Strange_Melody.mp3 2500]

Alice: The mood is changing...

[wait 2.0]

# Stop ambient with fade
[stopAmbient 1500]

[hide Bob]
Alice: See you later! 

# Fade out music
[stopMusic 2000]
```

---

## Architecture
This engine uses several design patterns:

- **State Pattern**: Manages different game states (Menu, Gameplay, etc.)
- **Command Pattern**:  Extensible script command system
- **Singleton Pattern**: GameDirector, CharacterManager, AssetLoader, AudioManager
- **MVC-inspired**: Separation of logic, UI, and data (need more development & refactoring work)

---

## Technologies
- **JavaFX 21** - UI framework and rendering
- **JavaFX Media** - Audio playback system
- **SLF4J + Logback** - Logging
- **Maven** - Build tool and dependency management

## Asset Credits

### Audio

**Visual Novel Audio Pack**
- **Composer & Sound Designer**: Tim Reichert ([website](https://tim-reichert.com))
- **Source**: [Visual Novel Audio Pack](https://fulminisictus.itch.io/visual-novel-audio-pack)
- **Licenses**:
  - UI SFX & Transitions:  [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/) (Commercial use allowed)
  - Music & Ambient: [CC BY-NC 4.0](https://creativecommons.org/licenses/by-nc/4.0/) (Non-commercial only)

**Collaborators**:
- Vocalist & Lyricist (I'll be Here): Eliana Z. ([@TheStorysinger](https://twitter.com/TheStorysinger))
- Vocalist (The Power in my Hands): Terence Kern ([@tjthepanda](http://sometag.com/account/tjthepanda/5480923258/))
- Vocalist (Strange Melody): Yukari ([@killuanya](https://twitter.com/killuanya))

> **Note for Commercial Use:** The demo audio files are for demonstration and testing purposes.  UI SFX may be used commercially with attribution, but music and ambient tracks are non-commercial only. For commercial projects, replace these files or contact [Tim Reichert](https://tim-reichert.com) for licensing. 

### Backgrounds
- **Spiral Atlas Visual Novel House Backgrounds** by [Spiral Atlas](https://spiralatlas.github.io/credits/)  
  Licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)

### Character Sprites
- **Editable Pixel Art Pose Pack** by Noraneko Games (Raziel Nozac Zerreitug)  
  Free to use with optional attribution

See [CREDITS.md](CREDITS.md) for full details.

---

## Contributing

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
- [x] Sound system (BGM/SFX/Ambient)
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
**Assets** (backgrounds, characters, audio) have their own licenses - see [CREDITS.md](CREDITS.md).

---

## Contact

**Developer**: [@meoluoi3](https://github.com/meoluoi3)

For questions, suggestions, or bug reports, please [open an issue](https://github.com/meoluoi3/JavaFX-VN-Engine/issues).

---

## Acknowledgements

- [Ren'Py](https://www.renpy.org/) - Inspiration for VN structure
- [Spiral Atlas](https://spiralatlas.github.io/) - Beautiful background art
- Raziel Nozac Zerreitug - Character sprite bases
- Tim Reichert & Collaborators - High-quality audio pack
- JavaFX community for excellent documentation

---

## Screenshots

Coming soon! 