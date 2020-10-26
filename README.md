
# This project is deprecated. To be honest it was stale for a while. please consider using https://github.com/mvndaemon/mvnd instead which is a far better implementation.

## mvnd
Daemon backend for simple Maven runs without JVM Startup penalty

[![CircleCI](https://circleci.com/gh/Mercateo/mvnd/tree/master.svg?style=svg)](https://circleci.com/gh/Mercateo/mvnd/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4244d23fe9794184a4b4d44256194be2)](https://www.codacy.com/app/uwe/mvnd?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mercateo/mvnd&amp;utm_campaign=Badge_Grade)

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

### Clients

For Linux-amd64, there is a packaged go client you can use. If the project
is buildt using graal, then the client from module mvnd-client is preferred.

For Mac, try to build maven-client with graal
(https://www.graalvm.org/downloads/)

### Please consider contributing either go or graal builds for exotic OSes.
