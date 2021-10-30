package com.fueledbycaffeine.bunnypedia.database.model

// These appear in the order they are numbered in the game.
// The ordinal value of this enum + 1 is the "zodiac animal number".
enum class ZodiacAnimal(val yearIndex: Int) {
  SNAKE(9),
  HORSE(10),
  GOAT(11),
  MONKEY(0),
  ROOSTER(1),
  DOG(2),
  PIG(3),
  RAT(4),
  OX(5),
  TIGER(6),
  RABBIT(7),
  DRAGON(8),
  ;
}
