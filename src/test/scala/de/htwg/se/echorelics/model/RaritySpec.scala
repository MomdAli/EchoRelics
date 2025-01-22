package de.htwg.se.echorelics.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.item.Rarity

class RaritySpec extends AnyWordSpec with Matchers {

    "A Rarity" should {

        "have correct values for Common" in {
            val rarity = Rarity.Common
            rarity.value should be(50)
            rarity.probability should be(20)
        }

        "have correct values for Uncommon" in {
            val rarity = Rarity.Uncommon
            rarity.value should be(250)
            rarity.probability should be(15)
        }

        "have correct values for Rare" in {
            val rarity = Rarity.Rare
            rarity.value should be(400)
            rarity.probability should be(10)
        }

        "have correct values for Epic" in {
            val rarity = Rarity.Epic
            rarity.value should be(500)
            rarity.probability should be(4)
        }

        "have correct values for Legendary" in {
            val rarity = Rarity.Legendary
            rarity.value should be(550)
            rarity.probability should be(1)
        }

        "be retrievable by name" in {
            Rarity.withName("Common") should be(Rarity.Common)
            Rarity.withName("Uncommon") should be(Rarity.Uncommon)
            Rarity.withName("Rare") should be(Rarity.Rare)
            Rarity.withName("Epic") should be(Rarity.Epic)
            Rarity.withName("Legendary") should be(Rarity.Legendary)
        }

        "throw an exception for an invalid name" in {
            an[MatchError] should be thrownBy Rarity.withName("Invalid")
        }
    }
}