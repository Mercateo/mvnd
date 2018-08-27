## mvnd
Daemon backend for simple Maven runs without JVM Startup penalty

[![DepShield Badge](https://depshield.sonatype.org/badges/uweschaefer/mvnd/depshield.svg)](https://depshield.github.io)

Install by downloading (or building) the mvnd.jar and running

	java -jar mvnd.jar install /my/path/to/maven

This will create some files in /my/path/to/maven/bin and /my/path/to/maven/lib

Assuming your have the /my/path/to/maven/bin in our PATH already, you can run

    mvnd # in order to start the daemon and
    mvnc # as a replacement for your mvn command. 

For instance, while

    mvn clean 

will fire up a JVM and clean the project in your current path,

    mvnc clean

will do the same, using the daemon (in the already running JVM) to execute that goal.

While this might have a limited impact when used manually, it comes in handy for batched usecases. In particular, we created this for using maven in git precommit hooks.
