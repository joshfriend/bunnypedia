ASSETS := bunnies/src/main/assets
DATABASE_DIR := ${ASSETS}/databases
DATABASE := ${DATABASE_DIR}/db.sqlite3.gz

DECKS := $(wildcard database/*.json)
IMAGE_DIR := mezzanine
JPG_IMAGES = $(wildcard ${IMAGE_DIR}/*.jpg)
PNG_IMAGES = $(wildcard ${IMAGE_DIR}/*.png)
IMAGES = $(JPG_IMAGES) $(PNG_IMAGES)
THUMBNAIL_DIR := ${ASSETS}/card_thumbnails
THUMBNAILS = $(patsubst ${IMAGE_DIR}/%.jpg,${THUMBNAIL_DIR}/%.webp,$(IMAGES)) $(patsubst ${IMAGE_DIR}/%.png,${THUMBNAIL_DIR}/%.webp,$(IMAGES))

.PHONY: all
all: ${THUMBNAIL_DIR} ${DATABASE} ${THUMBNAILS}

${DATABASE_DIR}:
	mkdir -p $@

${DATABASE}: ${DATABASE_DIR} ${DECKS} ./database/dbseed.py
	./database/dbseed.py

.PHONY: clean
clean:
	rm -r ${DATABASE_DIR}

.PHONY: clean-all
clean-all: clean
	rm -r ${THUMBNAIL_DIR}

${THUMBNAIL_DIR}:
	mkdir -p $@

${THUMBNAIL_DIR}/%.webp: ${IMAGE_DIR}/%.*
	convert $^ -resize 200 -quality 70 $@
