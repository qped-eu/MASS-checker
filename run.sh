#!/bin/bash

mvn --batch-mode --quiet generate-sources exec:java@CheckerRunner
