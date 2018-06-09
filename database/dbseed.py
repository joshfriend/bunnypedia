#!/usr/bin/python

import json
import glob
import sqlite3
import os
from os.path import join, dirname, abspath


class Card:
    DDL = ("""
    CREATE TABLE Card (
        id INTEGER PRIMARY KEY NOT NULL, canonicalId INTEGER,
        title TEXT NOT NULL, deck TEXT NOT NULL, type TEXT NOT NULL, rank TEXT,
        zodiacType TEXT, bunnyRequirement TEXT NOT NULL, dice TEXT NOT NULL,
        symbols TEXT NOT NULL, pawn TEXT, weaponLevel TEXT,
        cabbage INTEGER NOT NULL, water INTEGER NOT NULL, psi TEXT,
        specialSeries TEXT
    );
    """,
    )

    INSERT_STMT = """
    INSERT INTO Card (
        id, canonicalId, title, deck, type, rank, zodiacType, bunnyRequirement,
        dice, symbols, pawn, weaponLevel, cabbage, water, psi, specialSeries
    ) VALUES (
        :id, :canonicalId, :title, :deck, :type, :rank, :zodiacType,
        :bunnyRequirement, :dice, :symbols, :pawn, :weaponLevel, :cabbage,
        :water, :psi, :specialSeries
    );
    """

    def __init__(self, **kwargs):
        self.id = kwargs.get('id')
        self.canonicalId = kwargs.get('canonicalId')
        self.title = kwargs.get('title')
        self.deck = kwargs.get('deck')
        self.type = kwargs.get('type')
        self.rank = kwargs.get('rank')
        self.zodiacType = kwargs.get('zodiacType')
        self.bunnyRequirement = kwargs.get('bunnyRequirement', 'NOT_APPLICABLE')
        dice = kwargs.get('dice')
        if dice:
            self.dice = ','.join(dice)
        else:
            self.dice = ''
        symbols = kwargs.get('symbols')
        if symbols:
            self.symbols = ','.join(symbols)
        else:
            self.symbols = ''
        self.pawn = kwargs.get('pawn')
        self.weaponLevel = kwargs.get('weaponLevel')
        self.cabbage = kwargs.get('cabbage', 0)
        self.water = kwargs.get('water', 0)
        self.psi = kwargs.get('psi')
        self.specialSeries = kwargs.get('specialSeries')

    @classmethod
    def create_table(cls, conn):
        for ddl in cls.DDL:
            conn.execute(ddl)

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


class Rule:

    DDL = ("""
    CREATE TABLE Rule (
        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, cardId INTEGER NOT NULL,
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

    @classmethod
    def create_table(cls, conn):
        for ddl in cls.DDL:
            conn.execute(ddl)

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


ROOT_DIR = abspath(join(dirname(__file__), '..'))
SRC_DIR = join(ROOT_DIR, 'bunnies/src/main')
ASSETS_DIR = join(SRC_DIR, 'assets')
JSON_FILES = join(ROOT_DIR, 'database/*.json')
DB_FILE = join(ASSETS_DIR, 'databases/db.sqlite3')

cards_data = []
for deck in glob.glob(JSON_FILES):
    with open(deck, 'rb') as fp:
        data = json.load(fp)
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

for card_json in cards_data:
    selectedCard = Card(**card_json)
    selectedCard.insert(conn)

    for rule_json in card_json.get('rules', []):
        rule = Rule(selectedCard.id, **rule_json)
        rule.insert(conn)
conn.commit()

conn.execute("pragma user_version = 1")
conn.execute("pragma foreign_keys = 1")
conn.commit()
conn.close()
print(DB_FILE)