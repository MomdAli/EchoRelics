package utils

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import javafx.scene.{Parent, Node => JfxNode}
import javafx.scene.layout.Pane

class NodeFinderSpec extends AnyWordSpec with Matchers {

  "NodeFinder" should {

    "return None if the root is null" in {
      val resultById = NodeFinder.findNodeById(null, "someId")
      val resultByStyle = NodeFinder.findNodeByStyleClass(null, "someClass")
      resultById shouldBe None
      resultByStyle shouldBe None
    }

    "find a node by fx:id in a simple hierarchy" in {
      val root = new Pane()
      root.setId("rootPane")
      val childPane = new Pane()
      childPane.setId("childPane")
      root.getChildren.add(childPane)

      NodeFinder.findNodeById(root, "rootPane") shouldBe Some(root)
      NodeFinder.findNodeById(root, "childPane") shouldBe Some(childPane)
      NodeFinder.findNodeById(root, "missingId") shouldBe None
    }

    "find a node by style class in a simple hierarchy" in {
      val root = new Pane()
      root.getStyleClass.add("root-style")
      val childPane = new Pane()
      childPane.getStyleClass.add("child-style")
      root.getChildren.add(childPane)

      NodeFinder.findNodeByStyleClass(root, "root-style") shouldBe Some(root)
      NodeFinder.findNodeByStyleClass(root, "child-style") shouldBe Some(
        childPane
      )
      NodeFinder.findNodeByStyleClass(root, "missing-class") shouldBe None
    }

    "handle deeper nested parents correctly" in {
      val root = new Pane()
      val level1Pane = new Pane()
      val level2Pane = new Pane()
      level1Pane.setId("level1")
      level2Pane.setId("level2")
      level1Pane.getChildren.add(level2Pane)
      root.getChildren.add(level1Pane)

      NodeFinder.findNodeById(root, "level2") shouldBe Some(level2Pane)
      NodeFinder.findNodeById(root, "level1") shouldBe Some(level1Pane)
    }

    "handle multiple children with the same style class" in {
      val root = new Pane()
      val childA = new Pane()
      childA.getStyleClass.add("common-class")
      val childB = new Pane()
      childB.getStyleClass.add("common-class")
      root.getChildren.addAll(childA, childB)

      // Should return the first node found
      NodeFinder.findNodeByStyleClass(root, "common-class") shouldBe Some(
        childA
      )
    }
  }
}
