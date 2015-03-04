#!/bin/bash

# basename it

FILENAME="$1"
BASENAME="${FILENAME##*/}"
OUTDIR="${BASENAME%.*}"

if [ ! -f "$FILENAME" ]; then
    echo "File $FILENAME does not exist"
    exit 1
fi

# if dir exists, fail
if [ -d "$OUTDIR" ]; then
    echo "Directory '$OUTDIR' already exists!"
    exit 1
fi

mkdir "$OUTDIR"
unzip -d "$OUTDIR" "$FILENAME"

pushd "$OUTDIR"

find . -type f \( -iname "*.xml" -or -iname "*.rels" \) -exec python -c "import os, sys, xml.dom.minidom
with open('{}', 'r') as fp:
    s = fp.read()
with open('{}.bak', 'w') as fp:
    fp.write(xml.dom.minidom.parseString(s).toprettyxml().encode('utf-8'))
os.rename('{}.bak', '{}')" \;
