#!/bin/bash

# Check if the environment variable DEBUG_MVN is set (to any value).
# If this is the case, run the mvnDebug command (instead of mvn).
# This run the JVM executing the CheckerRunner in debug mode such that a remote debugger can be attached and the checker can be debugged.
# The JVM will wait for a connection on port 8000.
# If no debugger is attached, the program does not terminate.
# See for example:
# IDEA: https://www.jetbrains.com/help/idea/tutorial-remote-debug.html#8d4d52a4
# Eclipse: https://dzone.com/articles/how-debug-remote-java-applicat

if [ -z $DEBUG_MVN ]; then
    MVN_COMMAND=mvn
else
    MVN_COMMAND=mvnDebug
fi

# run Maven with debug output
# batch mode and piping console output through file helps to get readable logs in Quarterfall
$MVN_COMMAND --batch-mode generate-sources exec:java@CheckerRunner > output

cat output

