#!/bin/sh

set -eo pipefail

# Make card database
./database/dbseed.py

# Create card thumbnails
mogrify -verbose -resize 200 -quality 70 -path bunnies/src/main/assets/card_thumbnails mezzanine/*.jpg
