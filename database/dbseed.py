#!/usr/bin/env python

import re
import os
import json
import glob
import gzip
import sqlite3
import unicodedata
from html.parser import HTMLParser
from os.path import join, dirname, abspath

class HTMLStripper(HTMLParser):
    def __init__(self):
        super().__init__()
        self.reset()
        self.strict = False
        self.convert_charrefs = True
        self.fed = []

    def handle_data(self, d):
        self.fed.append(d)

    def get_data(self):
        return ''.join(self.fed)

class _Table:
    @classmethod
    def create_table(cls, conn):
        for ddl in cls.DDL:
            conn.execute(ddl)


class Card(_Table):
    DDL = ("""
    CREATE TABLE Card (
        id INTEGER PRIMARY KEY NOT NULL, canonicalId INTEGER,
        title TEXT NOT NULL, deck TEXT NOT NULL, type TEXT NOT NULL, rank TEXT,
        zodiacType TEXT, zodiacAnimal TEXT, bunnyRequirement TEXT NOT NULL, dice TEXT NOT NULL,
        symbols TEXT NOT NULL, pawn TEXT, weaponLevel TEXT,
        cabbage INTEGER NOT NULL, radish INTEGER NOT NULL,
        water INTEGER NOT NULL, milk INTEGER NOT NULL, psi TEXT,
        specialSeries TEXT,
        FOREIGN KEY(canonicalId) REFERENCES Card(id)
    );
    """,
    """
    CREATE INDEX index_Card_canonicalId ON Card(canonicalId);
    """,
    )

    INSERT_STMT = """
    INSERT INTO Card (
        id, canonicalId, title, deck, type, rank, zodiacType, zodiacAnimal, bunnyRequirement,
        dice, symbols, pawn, weaponLevel, cabbage, radish, water, milk, psi, specialSeries
    ) VALUES (
        :id, :canonicalId, :title, :deck, :type, :rank, :zodiacType,
        :zodiacAnimal, :bunnyRequirement, :dice, :symbols, :pawn, :weaponLevel,
        :cabbage, :radish, :water, :milk, :psi, :specialSeries
    );
    """

    def __init__(self, **kwargs):
        self.id = int(kwargs.get('id'))
        self.canonicalId = kwargs.get('canonicalId')
        self.title = kwargs.get('title')
        self.deck = kwargs.get('deck')
        self.type = kwargs.get('type')
        self.rank = kwargs.get('rank')
        self.zodiacType = kwargs.get('zodiacType')
        self.zodiacAnimal = kwargs.get('zodiacAnimal')
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
        self.radish = kwargs.get('radish', 0)
        self.water = kwargs.get('water', 0)
        self.milk = kwargs.get('milk', 0)
        self.psi = kwargs.get('psi')
        self.specialSeries = kwargs.get('specialSeries')

    def localize(self, stringsfile):
        # Add text to localized string registry and use the resource ID instead
        self.title = stringsfile.add(self.title)

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


class Rule(_Table):

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

    def localize(self, stringsfile):
        # Add text to localized string registry and use the resource ID instead
        self.title = stringsfile.add(self.title)
        self.text = stringsfile.add(self.text)

    def insert(self, conn):
        conn.execute(self.INSERT_STMT, self.__dict__)


class CardFts(_Table):
    DDL = (
    """
    CREATE VIRTUAL TABLE IF NOT EXISTS CardFts USING FTS4(id, title, content=`Card`)
    """,
    """
    INSERT INTO CardFts(CardFts) VALUES ('rebuild')
    """,
    """
    INSERT INTO CardFts(CardFts) VALUES ('optimize')
    """
    )


ROOT_DIR = abspath(join(dirname(__file__), '..'))
SRC_DIR = join(ROOT_DIR, 'bunnies/src/main')
RES_DIR = join(SRC_DIR, 'res')
VALUES_DIR = join(RES_DIR, 'values')
ASSETS_DIR = join(SRC_DIR, 'assets')
JSON_FILES = join(ROOT_DIR, 'database/*.json')
DB_FILE = join(ROOT_DIR, 'database/db.sqlite3')
DB_GZ_FOLDER = join(ASSETS_DIR, 'databases')
DB_GZ_FILE = join(DB_GZ_FOLDER, 'db.sqlite3.gz')
GENERATED_STRINGS_FILE = join(VALUES_DIR, 'db_strings.xml')

_filename_ascii_strip_re = re.compile(r"[^A-Za-z0-9_]")
_illegal_res_id_re = re.compile(r"^[0-9]")

class StringsResXml:
    _data = {}
    _truncated_ids = {}

    def _make_key(self, text, trunc=50):
        nfkd_text = unicodedata.normalize('NFKD', base_text)
        key = nfkd_text.encode('ascii', 'ignore').decode('ascii').lower()

        for sep in os.path.sep, os.path.altsep:
            if sep:
                key = key.replace(sep, " ")
                key = str(_filename_ascii_strip_re.sub("", "_".join(key.split()))).strip("._")
        if _illegal_res_id_re.match(key):
            # have to obey java identifier rules
            key = f'n{key}'

        if len(key) > trunc:
            key = key[:trunc]

    def add(self, base_text):
        key = self._make_key(base_text)

        self._data[key] = base_text
        return key

    def write(self, fp):
        fp.write(b'<?xml version="1.0" encoding="utf-8"?>\n')
        fp.write(b'<resources>\n')
        for key, text in self._data.items():
            fp.write('  <string name=\"{}\">{}</string>\n'.format(key, text).encode('utf-8'))
        fp.write(b'</resources>\n')

stringsfile = StringsResXml()
cards_data = []
for deck in glob.glob(JSON_FILES):
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

for card_json in cards_data:
    try:
        card = Card(**card_json)
        card.localize(stringsfile)
        card.insert(conn)
    except Exception as e:
        print("%s: %s" % (e.__class__.__name__, e.message))
        print("Card json: %s" % card_json)
        raise e

    for rule_json in card_json.get('rules', []):
        rule = Rule(card.id, **rule_json)
        rule.localize(stringsfile)
        rule.insert(conn)
conn.commit()

CardFts.create_table(conn)
conn.commit()

conn.execute("pragma user_version = 1")
conn.execute("pragma foreign_keys = 1")
conn.commit()
conn.close()

# Export the localaized stringsfile
with open(GENERATED_STRINGS_FILE, 'wb') as fp:
    stringsfile.write(fp)

try:
    os.makedirs(DB_GZ_FOLDER)
except:
    pass
with open(DB_FILE, 'rb') as orig_file:
    with gzip.open(DB_GZ_FILE, 'wb') as zipped_file:
        zipped_file.writelines(orig_file)
print(DB_GZ_FILE)
