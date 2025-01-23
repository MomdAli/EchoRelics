package utils

import javafx.scene.{Node, Parent}

object NodeFinder {

  /** Recursively search for a node by its fx:id */
  def findNodeById(root: Parent, id: String): Option[Node] = {
    if (root == null) return None
    if (id.equals(root.getId)) return Some(root)

    root.getChildrenUnmodifiable.toArray
      .collect { case child: Parent => findNodeById(child, id) }
      .flatten
      .headOption
  }

  /** Recursively search for a node by its style class */
  def findNodeByStyleClass(root: Parent, styleClass: String): Option[Node] = {
    if (root == null) return None
    if (root.getStyleClass.contains(styleClass)) return Some(root)

    root.getChildrenUnmodifiable.toArray
      .collect { case child: Parent => findNodeByStyleClass(child, styleClass) }
      .flatten
      .headOption
  }
}
