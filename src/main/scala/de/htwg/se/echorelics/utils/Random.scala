package utils

case class Random(seed: Int) {

  /** Generates a random integer within the specified bound.
    *
    * @param bound
    *   the upper bound (exclusive) for the generated random integer
    * @return
    *   a tuple containing the generated random integer and the new Random
    *   instance
    */
  def nextInt(bound: Int = Int.MaxValue): (Int, Random) = {
    val newSeed = (seed * 0x4deece64563dL + 0xbL) & ((1L << 48) - 1)
    val value = (newSeed >>> (48 - 31)).toInt % bound
    (value.abs, Random(newSeed.toInt))
  }

  /** Generates a random boolean value.
    *
    * @return
    *   A tuple containing a random boolean value and a new instance of Random.
    */
  def nextBoolean: (Boolean, Random) = {
    val (value, newRng) = nextInt(2)
    (value == 0, newRng)
  }

  /** Generates a random direction.
    *
    * @return
    *   A tuple containing a randomly selected Direction and a Random instance.
    */
  def randomDirection: (Direction, Random) = {
    val directions = Direction.values
    val (index, newRng) = nextInt(directions.length)
    (directions(index), newRng)
  }

  /** Generates a random position within the given size.
    *
    * @param size
    *   The size of the area within which the position should be generated.
    * @return
    *   A tuple containing the generated Position and the updated Random
    *   instance.
    */
  def randomPosition(size: Int): (Position, Random) = {
    val (x, rng1) = nextInt(size)
    val (y, rng2) = rng1.nextInt(size)
    (Position(x, y), rng2)
  }

  /** Shuffles a list of elements.
    *
    * @param list
    *   The list to be shuffled.
    * @tparam A
    *   The type of elements in the list.
    * @return
    *   A tuple containing the shuffled list and the updated Random instance.
    */
  def shuffle[A](list: List[A]): (List[A], Random) = {
    def shuffleRec(
        remaining: List[A],
        acc: List[A],
        rng: Random
    ): (List[A], Random) = {
      if (remaining.isEmpty) {
        (acc, rng)
      } else {
        val (index, newRng) = rng.nextInt(remaining.length)
        val elem = remaining(index)
        val newRemaining = remaining.patch(index, Nil, 1)
        shuffleRec(newRemaining, elem :: acc, newRng)
      }
    }
    shuffleRec(list, Nil, this)
  }
}
