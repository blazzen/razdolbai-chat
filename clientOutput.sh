#!/bin/bash
ls

cd target/classes
java -classpath ./target/classes com.razdolbai.client.OutputConsole $1

