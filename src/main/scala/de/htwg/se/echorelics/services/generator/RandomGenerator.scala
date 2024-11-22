package services.generator

import utils.{Direction, Position}

object RandomGenerator {
  def next(seed: Long): (Long, Long) = {
    val newSeed = (seed * 0x5deece66dL + 0xbL) & ((1L << 48) - 1)
    (newSeed, newSeed >>> (48 - 31))
  }

  def nextInt(seed: Long, bound: Int): (Long, Int) = {
    val (newSeed, value) = next(seed)
    (newSeed, (value.toInt % bound).abs)
  }

  def nextBoolean(seed: Long): (Long, Boolean) = {
    val (newSeed, value) = nextInt(seed, 2)
    (newSeed, value == 0)
  }

  def randomDirection(seed: Long): (Long, Direction) = {
    val directions = Direction.values
    val (newSeed, index) = nextInt(seed, directions.length)
    (newSeed, directions(index))
  }

  def randomPosition(seed: Long, size: Int): (Long, Position) = {
    val (seed1, x) = nextInt(seed, size)
    val (seed2, y) = nextInt(seed1, size)
    (seed2, Position(x, y))
  }
}
