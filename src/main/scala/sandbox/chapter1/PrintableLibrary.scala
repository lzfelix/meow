package sandbox.chapter1

import PrintableLibrary.PrintableInstances.{printableInt, printableString}
import PrintableLibrary.PrintableSyntax.PrintableOps

object PrintableLibrary {

  trait Printable[A] {
    def format(arg: A): String
  }

  object PrintableInstances {
    implicit val printableString: Printable[String] = new Printable[String] {
      override def format(arg: String): String = s"Print value for string: $arg"
    }

    implicit val printableInt: Printable[Int] = new Printable[Int] {
      override def format(arg: Int): String = s"Print value for integer: $arg"
    }
  }

  object Printable {
    def format[A](value: A)(implicit printable: Printable[A]): String =
      printable.format(value)

    def print[A](value: A)(implicit printable: Printable[A]): Unit =
      println(format(value))
  }

  final case class Cat(name: String, age: Int, color: String)
  object Cat {
    implicit val printableCat: Printable[Cat] = new Printable[Cat] {
      override def format(c: Cat): String =
        s"${c.name} is a ${c.age} year-old ${c.color} cat"
    }
  }

  object PrintableSyntax {
    implicit class PrintableOps[A](value: A) {
      def format(implicit printable: Printable[A]): String =
        printable.format(value)

      def print(implicit p: Printable[A]): Unit =
        println(format(p))
    }
  }

  def main(args: Array[String]): Unit = {
    val tommyTheCat: Cat = Cat("Tommy", 6, "gray")

    Printable.print(21)
    Printable.print("bananas")
    Printable.print(tommyTheCat)

    tommyTheCat.print
  }
}
