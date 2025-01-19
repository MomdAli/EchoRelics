// FileEncryption.scala
package utils

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import java.nio.charset.StandardCharsets

object FileEncryption {

  private val algorithm = "AES"
  private val keyString = "ThisIsASecretKey"
  private val key: Key =
    new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), algorithm)

  def encrypt(data: String): String = {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8))
    Base64.getEncoder.encodeToString(encryptedBytes)
  }

  def decrypt(data: String): String = {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, key)
    val decodedBytes = Base64.getDecoder.decode(data)
    new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8)
  }
}
