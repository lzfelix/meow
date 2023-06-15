package sandbox.chapter1

import cats.{Eq, Show}
import cats.syntax.show._

object PrintableWithCats {
  final case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val catEq: Eq[Cat] =
      Eq.instance((left, right) => left.name == right.name)
  }

  def main(args: Array[String]): Unit = {
    val garfield: Cat = Cat("Garfield", 23, "orange")

    // The following two lines of code basically wrap around everything we did in the `PrintLibrary` exercise.
    implicit val showCat: Show[Cat] =
      Show.show(c => s"${c.name} is a ${c.age} year-old ${c.color} cat.")

    println(garfield.show)
  }
}
