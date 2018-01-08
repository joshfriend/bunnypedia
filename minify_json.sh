#!/bin/bash

for f in ./bunnies/src/main/res/raw/*.json; do
    filename=$(basename "$f")
    directory=$(dirname "$f")
    name="${filename%.*}"
    echo "cat $f | jq -c . | gzip > ${directory}/${name}_min.gz"
    cat $f | jq -c . | gzip > "${directory}/${name}_min.gz"
done
