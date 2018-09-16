zodiacs = [
    "Snake",
    "Horse",
    "Goat",
    "Monkey",
    "Rooster",
    "Dog",
    "Pig",
    "Rat",
    "Ox",
    "Tiger",
    "Rabbit",
    "Dragon",
]

start_year = 1905  # snake

for i, zodiac in enumerate(zodiacs):
    print "{}:".format(zodiac),
    for occurrence in range(0, 16):
        year = start_year + (i + occurrence * 12)
        print "{},".format(year),
    print
