Puzzle
======
Puzzle experiment for toddlers. Written in Java using OpenGL. Not meant for public use


Developer Notes
===============
Build from source
-----------------
Make sure ant is install on your system. (If you're on Mac OS X, you're good to go.) Then open up a command line:

1. `cd [THIS_DIRECTORY]`
2. `ant jar`

This will create a puzzle.jar containing all the dependencies in target/

Dependent libraries
-------------------
* lwjgl
* slick
* Various native libraries for OpenGL and OpenAL

Removed jogg and jvorbis because of ZipException from One-Jar