#!/bin/bash

if [[ `uname` == "CYGWIN_NT-10.0" ]]; then 
    javac -d . -cp ".;jars/*" src/CharacterMgr.java;
    echo "javac -d . -cp .;jars/* src/CharacterMgr.java"
else
    javac -d . -cp ".:jars/*" src/CharacterMgr.java;
    echo "javac -d . -cp .:jars/* src/CharacterMgr.java"
fi

jar cfm root/main/CharacterMgr.jar manifest.mf dnd/*

cd root/

jar -cfm ../CharacterMgr.jar boot-manifest.mf .
