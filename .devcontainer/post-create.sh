#!/bin/sh

mvn -B -f backend/pom.xml dependency:go-offline
cd frontend && npm install
