package sandbox.chapter3

import cats.{Contravariant, Show}
import cats.syntax.contravariant._ // for contramap

object CatsFunctors {
  val showString: Show[String] = Show[String]
  val symbolToString: Symbol => String = (s: Symbol) => s"'${s.name}"

  // Thanks to Contravariant, we can create a Show[Symbol] by means of Show[String] and
  // a mapping function Symbol => String
  val showSymbol: Show[Symbol] =
    Contravariant[Show].contramap(showString)(symbolToString)

  def main(args: Array[String]): Unit = {
    val someSymbol: Symbol = Symbol("someSymbolGoesHere")

    println(showSymbol.show(someSymbol))
    // > 'someSymbolGoesHere

    // Another possibility is by using the .contramap extension method
    println(showString.contramap(symbolToString).show(someSymbol))
  }
}
