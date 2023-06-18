package sandbox.chapter3

import cats.Functor // Allow importing List functors
import cats.instances.function._ // for function Functors
import cats.syntax.functor._ // for map operator

object FunctorExamples {
  // Let's us have an f: Int => Int
  val double: Int => Int = (x: Int) => x * 2

  // Given f: A => B, Functor[T].lift(f) will turn it into
  // f' = T[A] => T[B] and the wrap/unwrapping is done
  // automatically
  val doubleOption: Option[Int] => Option[Int] =
    Functor[Option].lift(double)

  def main(args: Array[String]): Unit = {
    // Example 1: Functor Lift
    val optionValues: Seq[Option[Int]] = List(1, 2, 3, 4)
      .map(x => if (x % 2 == 0) Some(x) else None)
      .map(doubleOption)

    println(optionValues) // List(None, Some(4), None, Some(8))

    // Example 2: Function composition
    val increment = (a: Int) => a + 1
    val doubleIt = (a: Int) => a * 2
    val str = (a: Int) => s"$a!"

    val result = increment.map(doubleIt).map(str)(123)
    println(result) // 248!

    // Example 3: abstract maps
    // Notice we can't supply a parametrized type F[Int] to Scala, this will cause a compilation error.
    // Hence, we say doMath expects a 1-Kind, and right afterward say the type-parameter must be an Int.
    // Now this will work with any container holding Ints
    def doMath[F[_]](start: F[Int])(implicit f: Functor[F]): F[Int] =
      start.map(n => (n + 1) * 2)

    import cats.instances.list._
    import cats.instances.option._
    println(doMath(Option(20)))
    println(doMath(List(1, 2, 3)))
  }
}
