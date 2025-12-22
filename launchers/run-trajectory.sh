#!/bin/bash

APP_CLASS="org.jscience.apps.physics.trajectory.InterplanetaryTrajectoryApp"

echo "Starting Interplanetary Trajectory Planner..."
mvn -f ../pom.xml exec:java -pl jscience-killer-apps -Dexec.mainClass="$APP_CLASS"
