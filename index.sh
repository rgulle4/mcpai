#!/bin/bash
OPEN_COMMAND=""
if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Linux
    OPEN_COMMAND="xdg-open"
elif [ "$(uname)" == "Darwin" ]; then
    # Mac OS X or MacOS or whatever
    OPEN_COMMAND="open"
elif [ "$(expr substr $(uname -s) 1 10)" == "MINGW32_NT" ]; then
    # Cygwin in Windows
    OPEN_COMMAND="cygstart"
fi

$OPEN_COMMAND "http://localhost:8888" & \
python -m SimpleHTTPServer 8888
