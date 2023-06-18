package sandbox.chapter3

import cats.Functor
import cats.syntax.functor._ // to enable the .map operator

object BranchingWithFunctor {
  sealed trait Tree[+A]
  final case class Branch[A](l: Tree[A], r: Tree[A]) extends Tree[A]
  final case class Leaf[A](value: A) extends Tree[A]
  final case class Empty[A]() extends Tree[A]

  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
    def leaf[A](v: A): Tree[A] = Leaf(v)
    def empty: Empty[Nothing] = Empty()

    def toString[A](t: Tree[A]): String = {
      def printLeaf(value: Option[A], depth: Int): String = {
        val padding: String = "\t" * depth
        val formattedValue: String = value.map(_.toString).getOrElse("-")
        s"$padding$formattedValue\n"
      }

      def printBranch(depth: Int): String = {
        s"${"\t" * depth}*\n"
      }

      def _print(t: Tree[A], depth: Int = 0): String = {
        t match {
          case Empty() => printLeaf(None, depth)
          case Leaf(v) => printLeaf(Option(v), depth)
          case Branch(l: Tree[A], r: Tree[A]) =>
            printBranch(depth) +
              _print(l, depth + 1) +
              _print(r, depth + 1)
        }
      }

      _print(t)
    }
  }

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Empty()     => Empty()
      case Leaf(value) => Leaf(f(value))
      case Branch(oldLeft: Tree[A], oldRight: Tree[A]) =>
        Branch(map(oldLeft)(f), map(oldRight)(f))
    }
  }

  def main(args: Array[String]): Unit = {
    println(Tree.leaf(100).map(_ * 2))
    println(
      Tree.toString(
        Tree
          .branch(
            Tree.branch(Tree.leaf(10), Tree.branch(Tree.empty, Tree.leaf(70))),
            Tree.branch(Tree.leaf(20), Tree.branch(Tree.leaf(30), Tree.empty))
          )
          .map(_ * 2)
      )
    )
  }
}
