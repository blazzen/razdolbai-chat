#!/bin/bash
ls

cd target/classes
java -classpath ./target/classes com.razdolbai.server.ServerLauncher $1

