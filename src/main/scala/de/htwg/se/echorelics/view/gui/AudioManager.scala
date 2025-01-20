package view.gui

import scalafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.scene.media.MediaPlayer.Status
import scala.collection.mutable

class AudioManager {
  private val audioClips = mutable.Map[String, AudioClip]()
  private val mediaPlayers = mutable.Map[String, MediaPlayer]()

  def loadAudioClip(name: String, resourcePath: String): Unit = {
    val audioClip = new AudioClip(getClass.getResource(resourcePath).toString)
    audioClips(name) = audioClip
  }

  def loadMediaPlayer(name: String, resourcePath: String): Unit = {
    val mediaURL = getClass.getResource(resourcePath)
    require(mediaURL != null, s"Media file not found: $resourcePath")
    val media = new Media(mediaURL.toString)
    val mediaPlayer = new MediaPlayer(media)
    mediaPlayers(name) = mediaPlayer
  }

  def playAudioClip(name: String): Unit = {
    audioClips.get(name).foreach { clip =>
      clip.setVolume(0.015)
      clip.play()
    }
  }

  def playMediaPlayer(name: String): Unit = {
    mediaPlayers.get(name).foreach { mediaPlayer =>
      mediaPlayer.play()
      mediaPlayer.setVolume(0.05)
    }
  }

  def stopMediaPlayer(name: String): Unit = {
    mediaPlayers.get(name).foreach(_.stop())
  }

  def setVolume(name: String, volume: Double): Unit = {
    audioClips.get(name).foreach(_.setVolume(volume))
    mediaPlayers.get(name).foreach(_.volume = volume)
  }

  def getVolume(name: String): Double = {
    audioClips.get(name).map(_.getVolume).getOrElse(0.0)
  }
}
