#!/bin/sh

# install spring deps
cd spring && mvn -B dependency:go-offline && cd ..

# install react deps
npm install --prefix react
