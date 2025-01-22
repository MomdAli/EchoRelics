package utils

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class FileEncryptionSpec extends AnyWordSpec with Matchers {

  "FileEncryption" should {

    "encrypt and decrypt data correctly" in {
      val originalData = "This is a test string."
      val encryptedData = FileEncryption.encrypt(originalData)
      val decryptedData = FileEncryption.decrypt(encryptedData)

      decryptedData shouldEqual originalData
    }

    "decrypt to the original data for different encrypted outputs of the same input" in {
      val originalData = "This is a test string."
      val encryptedData1 = FileEncryption.encrypt(originalData)
      val encryptedData2 = FileEncryption.encrypt(originalData)

      FileEncryption.decrypt(encryptedData1) shouldEqual originalData
      FileEncryption.decrypt(encryptedData2) shouldEqual originalData
    }

    "handle empty strings correctly" in {
      val originalData = ""
      val encryptedData = FileEncryption.encrypt(originalData)
      val decryptedData = FileEncryption.decrypt(encryptedData)

      decryptedData shouldEqual originalData
    }

    "handle special characters correctly" in {
      val originalData = "!@#$%^&*()_+-=<>?/|\\"
      val encryptedData = FileEncryption.encrypt(originalData)
      val decryptedData = FileEncryption.decrypt(encryptedData)

      decryptedData shouldEqual originalData
    }
  }
}
