#!/bin/bash
echo $1
ls
cd target/classes
java -classpath ./target/classes com.razdolbai.client.Client $1

