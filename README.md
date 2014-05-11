# Stage-Scroll
### Version 0.6-Final
#### Developed for EEGJ - Team Riri, New Media Team Project @ Rochester Institute of Technology, 2014

---

## About
This collection of classes were developed for use in Team Riri - Project EEGJ at
the Rochester Institute of Technology, Spring, 2014. Written for the Processing
Java Library, this code servers as a Guitar Hero or Rockband-like game component
for the larger project, EEGJ, which is a generative audio-visual experience created
through the use of Electroencephalography technology.

---

## Working Features
* spawning stage objects:
   ```
   ScrollingStage myStage = new ScrollingStage(xLoc, yLoc, width, height);
   ```
* spawning a note:
   ```
  myStage.spawnNote(n); // where n indicates a column, 1-5
   ```
* manually incrementing note
   ```
  myStage.incrementNote(n); // where n indicates number of rows to increment the note
   ```
* setting the active hitzone
  ```
  myStage.activeHitzone(n); // where n indicates a column, 1-5
  ```
* auto note termination / score-tracking
* focus/relaxed vignette fading depending on active hitzone


## Features in need of some work
* better data / memory management
* refined assets -- more frames for looping animations, smaller dimensions to eat less RAM

---

