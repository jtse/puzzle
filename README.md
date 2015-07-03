Puzzle
======
Puzzle experiment for toddlers. Written in Java using JOGL. Not meant for public use.

[Download](http://jtse.github.com/puzzle/puzzle.jar)

[Example script file](https://raw.github.com/jtse/puzzle/master/src/main/install/puzzle/demo/demo.txt)

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


Import into Eclipse
-------------------

1. Launch Eclipse.
2. File >> Import >> Maven >> Existing Maven Projects
3. Pick [THIS DIRECTORY]


Dependent libraries
-------------------
* lwjgl
* slick
* Various native libraries for OpenGL and OpenAL

Removed jogg and jvorbis because of ZipException from One-Jar


TODOs
-----
Break up the Puzzle class into smaller components.
