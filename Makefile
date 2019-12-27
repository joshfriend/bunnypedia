ASSETS := bunnies/src/main/assets
DATABASE_DIR := ${ASSETS}/databases
DATABASE := ${DATABASE_DIR}/cards.sqlite3

DECKS := $(wildcard database/*.json)
IMAGE_DIR := mezzanine
IMAGES = $(wildcard ${IMAGE_DIR}/*.jpg)
THUMBNAIL_DIR := ${ASSETS}/card_thumbnails
THUMBNAILS = $(patsubst ${IMAGE_DIR}/%.jpg,${THUMBNAIL_DIR}/%.jpg,$(IMAGES))

.PHONY: all
all: ${THUMBNAIL_DIR} ${DATABASE} ${THUMBNAILS}

${DATABASE_DIR}:
	mkdir -p $@

${DATABASE}: ${DATABASE_DIR} ${DECKS}
	./database/dbseed.py

.PHONY: clean
clean:
	rm -r ${DATABASE_DIR}

.PHONY: clean-all
clean-all: clean
	rm -r ${THUMBNAIL_DIR}

${THUMBNAIL_DIR}:
	mkdir -p $@

${THUMBNAIL_DIR}/%.jpg: ${IMAGE_DIR}/%.jpg
	convert $^ -resize 200 -quality 70 $@
