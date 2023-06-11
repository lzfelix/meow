package sandbox.chapter1

import cats.{Eq, Show}
import cats.implicits.catsSyntaxEq
import cats.syntax.show._

object PrintableWithCats {
  final case class Cat(name: String, age: Int, color: String)

  def main(args: Array[String]): Unit = {
    val garfield: Cat = Cat("Garfield", 23, "orange")

    // The following two lines of code basically wrap around everything we did in the `PrintLibrary` exercise.
    implicit val showCat: Show[Cat] =
      Show.show(c => s"${c.name} is a ${c.age} year-old ${c.color} cat.")

    println(garfield.show)
    // Using the implicit to add the method .show to Cat, even though we didn't modify its implementation

    // Defining a compile-safe way to compile cats
    implicit val catEq: Eq[Cat] =
      Eq.instance((left, right) => left.name == right.name)

    val garfieldInDisguise: Cat = Cat("Garfield", 7, "black")
    println(garfield === garfieldInDisguise)
    // true, because if names match, then the cats are the same

    // Notice we can do some neat stuff now that we have a type-class Eq for Cat:
    println(Option(garfield) === Option(garfieldInDisguise))
    // true, because Cats know how to compare Option[A] and we have provided a way to compare A when A = Cat.

    // Note we have primitive times comparison out-of-the box
    println(12 === 12)
  }
}
