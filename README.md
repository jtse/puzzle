Puzzle
======
Puzzle experiment for toddlers. Written in Java using OpenGL. Not meant for public use


Developer Notes
===============
Compile from source
-------------------
Make sure Maven is installed. Then open up a command prompt:

1. `cd [THIS_DIRECTORY]`
2. `mvn package`

This will create a target/puzzle-${VERSION}.one-jar.jar containing all the dependencies.


Run from source
---------------
Again, make sure Maven is installed and open up a command prompt:

1. `cd [THIS DIRETORY]`
2. `mvn compile exec:exec`


Dependent libraries
-------------------
* lwjgl
* slick
* Various native libraries for OpenGL and OpenAL

Removed jogg and jvorbis because of ZipException from One-Jar
