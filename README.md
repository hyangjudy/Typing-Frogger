# Typing Frogger

Typing Frogger is a game that combines frogger and typing game together. 

The idea comes from the Frogger ([金山打字通-青蛙过河](https://github.com/hyangjudy/Typing-Frogger/blob/master/media/%E9%87%91%E5%B1%B1%E6%89%93%E5%AD%97%E9%80%9A%E9%9D%92%E8%9B%99%E8%BF%87%E6%B2%B3.jpeg)), which I played often when I was a child.

## Demo

[![Watch the video](https://github.com/hyangjudy/Typing-Frogger/blob/master/media/demo-cover.png)](https://youtu.be/nSPNwvJjMaM)

## How to play

Choose one button (i.e. EASY / MEDIUM / HARD) to start the game. You can pause the game when pressing PAUSE button.

In the game, please type the correct word according to those on the leaves and help the frog to cross the river. The frog are strictly allowed to jump to the leaves in the next row. Once the typed character matches the words, the words will turn red; once dismatches, turn white. 

With game level up, the speed of moving leaves will be faster. The score obtained by each successful jump is proportional to the difficulty and the speed of the leaf, and negatively proportional to the time of typing the words. 

If one player passes all the levels, then he/she wins. The score will be displayed. To restart the game, press the RESTART button.

However, if the frog encounters the bound of the game frame, it will lose one life. If the time is up or the frog loses all its lives, the game will end. 

## Folder Structure

```
/src/frogger
├── .gitignore
├── media/
│   ├── demo.mp4
│   └── 金山打字通青蛙过河.jpeg
├── README.md
└── src/
    └── frogger/
        ├── fonts/
        │   └── font.ttf
        ├── images/
        │   ├── Back.png
        │   ├── Frog.gif
        │   ├── Frogger.png
        │   ├── Frontimg.png
        │   ├── Leaf.gif
        │   ├── River.gif
        │   └── Typing.png
        ├── mvc/
        │   ├── controller/
        │   │   └── Game.java
        │   ├── model/
        │   │   ├── BestScore.java
        │   │   ├── CollisionOp.java
        │   │   ├── CommandCenter.java
        │   │   ├── Frog.java
        │   │   ├── GameOpsList.java
        │   │   ├── Leaf.java
        │   │   ├── Movable.java
        │   │   ├── Sprite.java
        │   │   ├── Word.java
        │   │   └── WordGenerator.java
        │   └── view/
        │       ├── GameFrame.java
        │       └── GamePanel.java
        ├── score/
        │   └── history.ser
        ├── sounds/
        │   ├── Boing.wav
        │   ├── game.wav
        │   ├── Oh-yeah.wav
        │   ├── Sound.java
        │   ├── splash.wav
        │   └── Uh-oh.wav
        └── words/
            ├── wordlist-easy.txt
            ├── wordlist-hard.txt
            └── wordlist-medium.txt
```

| Folder | Discription | 
| --- | --- |
| fonts | contains the font used in the game |
| images | contains the game images |
| mvc | model, view, controller |
| score | contains a `ser` file to save game scores (the system will create a new one if `ser` file cannot be found) |
| sounds | contains the sound effects |
| words | contains the word lists of different difficulty |
