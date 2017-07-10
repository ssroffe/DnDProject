#!/bin/bash

if [[ `uname` == "CYGWIN_NT-10.0" ]]; then 
    javac -d . -cp ".;jars/*" src/CharacterMgr.java;
    echo "Compiled"
else
    javac -d . -cp ".:jars/*" src/CharacterMgr.java;
    echo "Compiled"
fi

jar cvfm root/main/CharacterMgr.jar manifest.mf dnd/*

cd root/

jar -cvfm ../CharacterMgr.jar boot-manifest.mf .
