#!/usr/bin/env python

import json
import glob
import gzip
import sqlite3
import os
from os.path import join, dirname, abspath

class _Table:
    @classmethod
    def create_table(cls, conn):
        for ddl in cls.DDL:
            conn.execute(ddl)


class Card(_Table):
    DDL = ("""
    CREATE TABLE Card (
        pk INTEGER PRIMARY KEY NOT NULL, id TEXT NOT NULL, canonicalId TEXT,
        title TEXT NOT NULL, deck TEXT NOT NULL, type TEXT NOT NULL, rank TEXT,
        zodiacSign TEXT, zodiacAnimal TEXT, bunnyRequirement TEXT , dice TEXT,
        symbols TEXT, pawn TEXT, weaponLevel TEXT,
        cabbage INTEGER NOT NULL, radish INTEGER NOT NULL,
        water INTEGER NOT NULL, milk INTEGER NOT NULL, psi TEXT,
        specialSeries TEXT,
        FOREIGN KEY(canonicalId) REFERENCES Card(id)
    );
    """,
    """
    CREATE UNIQUE INDEX index_Card_id ON Card(id);
    """,
    """
    CREATE INDEX index_Card_canonicalId ON Card(canonicalId);
    """,
    )

    INSERT_STMT = """
    INSERT INTO Card (
        pk, id, canonicalId, title, deck, type, rank, zodiacSign, zodiacAnimal, bunnyRequirement,
        dice, symbols, pawn, weaponLevel, cabbage, radish, water, milk, psi, specialSeries
    ) VALUES (
        :pk, :id, :canonicalId, :title, :deck, :type, :rank, :zodiacSign,
        :zodiacAnimal, :bunnyRequirement, :dice, :symbols, :pawn, :weaponLevel,
        :cabbage, :radish, :water, :milk, :psi, :specialSeries
    );
    """

    def __init__(self, pk, **kwargs):
        self.pk = pk
        self.id = kwargs.get('id')
        self.canonicalId = kwargs.get('canonicalId')
        self.title = kwargs.get('title')
        self.deck = kwargs.get('deck')
        self.type = kwargs.get('type')
        self.rank = kwargs.get('rank')
        self.zodiacSign = kwargs.get('zodiacSign')
        self.zodiacAnimal = kwargs.get('zodiacAnimal')
        self.bunnyRequirement = kwargs.get('bunnyRequirement')
        dice = kwargs.get('dice', [])
        self.dice = ','.join(dice) or None
        symbols = kwargs.get('symbols', [])
        self.symbols = ','.join(symbols) or None
        self.pawn = kwargs.get('pawn')
        self.weaponLevel = kwargs.get('weaponLevel')
        self.cabbage = kwargs.get('cabbage', 0)
        self.radish = kwargs.get('radish', 0)
        self.water = kwargs.get('water', 0)
        self.milk = kwargs.get('milk', 0)
        self.psi = kwargs.get('psi')
        self.specialSeries = kwargs.get('specialSeries')

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


class Rule(_Table):

    DDL = ("""
    CREATE TABLE Rule (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cardId TEXT NOT NULL,
        title TEXT NOT NULL, text TEXT NOT NULL,
        FOREIGN KEY(cardId) REFERENCES Card(id) ON DELETE CASCADE
    );
    """,
    """
    CREATE INDEX index_Rule_cardId ON Rule(cardId)
    """,
    )

    INSERT_STMT = """
    INSERT INTO Rule (
        cardId, title, text
    ) VALUES (
        :cardId, :title, :text
    );
    """

    def __init__(self, cardId, **kwargs):
        self.cardId = cardId
        self.title = kwargs['title']
        self.text = kwargs['text']

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


class CardFts(_Table):
    DDL = (
    """
    CREATE VIRTUAL TABLE IF NOT EXISTS CardFts USING FTS4(id, title, content=`Card`)
    """,
    """
    INSERT INTO CardFts(CardFts) VALUES ('rebuild')
    """
    )


ROOT_DIR = abspath(join(dirname(__file__), '..'))
SRC_DIR = join(ROOT_DIR, 'bunnies/src/main')
ASSETS_DIR = join(SRC_DIR, 'assets')
JSON_FILES = join(ROOT_DIR, 'database/*.json')
DB_FILE = join(ROOT_DIR, 'database/db.sqlite3')
DB_GZ_FOLDER = join(ASSETS_DIR, 'databases')
DB_GZ_FILE = join(DB_GZ_FOLDER, 'db.sqlite3.gz')

cards_data = []
decks = sorted(list(glob.glob(JSON_FILES)))
for deck in decks:
    with open(deck, 'rb') as fp:
        try:
            data = json.load(fp)
        except Exception as e:
            raise Exception("Unable to load %s" % deck, e)
    cards_data.extend(data)

try:
    os.makedirs(dirname(DB_FILE))
except:
    pass
try:
    os.remove(DB_FILE)
except:
    pass

conn = sqlite3.connect(DB_FILE)
Card.create_table(conn)
Rule.create_table(conn)
conn.commit()

for pk, card_json in enumerate(cards_data):
    try:
        selectedCard = Card(pk + 1, **card_json)
        selectedCard.insert(conn)
    except Exception as e:
        print("%s: %s" % (e.__class__.__name__, e.message))
        print("Card json: %s" % card_json)
        raise e

    for rule_json in card_json.get('rules', []):
        rule = Rule(selectedCard.id, **rule_json)
        rule.insert(conn)
conn.commit()

CardFts.create_table(conn)
conn.commit()

conn.execute("pragma user_version = 1")
conn.execute("pragma foreign_keys = 1")
conn.commit()
conn.close()

try:
    os.makedirs(DB_GZ_FOLDER)
except:
    pass
with open(DB_FILE, 'rb') as orig_file:
    with gzip.open(DB_GZ_FILE, 'wb') as zipped_file:
        zipped_file.writelines(orig_file)
print(DB_GZ_FILE)
