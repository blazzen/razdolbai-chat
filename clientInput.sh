#!/bin/bash
echo $1
ls
cd target/classes
java -classpath C:/Users/JulieCat/Desktop/Razdolbai/razdolbai-chat/target/classes com.razdolbai.client.Client $1

