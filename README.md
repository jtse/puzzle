Puzzle
======
Puzzle experiment for toddlers. Written in Java using OpenGL. Not meant for public use


Developer Notes
===============
Build from source
-----------------
Make sure Maven is install on your system. Then open up a command line:

1. `cd [THIS_DIRECTORY]`
2. `mvn package`

This will create a target/puzzle-${VERSION}.one-jar.jar containing all the dependencies.

Dependent libraries
-------------------
* lwjgl
* slick
* Various native libraries for OpenGL and OpenAL

Removed jogg and jvorbis because of ZipException from One-Jar