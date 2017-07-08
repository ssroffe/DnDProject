#!/bin/bash

if [[ `uname` == "CYGWIN_NT-10.0" ]]; then 
    javac -d . -cp ".;jars/*" src/CharacterMgr.java;
else
    javac -d . -cp ".:jars/*" src/CharacterMgr.java;
fi

jar cvfm root/main/CharacterMgr.jar manifest.mf dnd/*

cd root/

jar -cvfm ../CharacterMgr.jar boot-manifest.mf .
