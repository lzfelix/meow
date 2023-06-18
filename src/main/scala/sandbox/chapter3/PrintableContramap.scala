package sandbox.chapter3

object PrintableContramap {
  // Defining a type-class
  trait Printable[A] { self =>
    def format(value: A): String
    def contramap[B](f: B => A): Printable[B] = new Printable[B] {
      override def format(value: B): String = self.format(f(value))
    }
  }

  // defining a function to use the type-class via implicits
  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  // defining some encoders for some types
  implicit val strPrintable: Printable[String] = new Printable[String] {
    override def format(value: String): String = s"'$value'"
  }

  implicit val boolPrintable: Printable[Boolean] = new Printable[Boolean] {
    override def format(value: Boolean): String = if (value) "yes" else "no"
  }

  // A custom class
  final case class Box[A](value: A)

  // defining an encoder for a type Box[_]
  // (B = Box) here
  implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
    p.contramap(bx => bx.value)

  // contra-map is not exclusively used for higher kinded types, we can also
  // define a Printable[Int] in terms of Printable[String], notice here we
  // force de dependency over a Printable[String], not a Printable[A]
  implicit def intPrintable(implicit p: Printable[String]): Printable[Int] =
    p.contramap(x => (x * 100).toString)

  // We can do the same thing for custom types as well
  // Here we make Employee printable through Printable[String]
  final case class Employee(name: String, age: Int)
  implicit def employeePrintable(
    implicit p: Printable[String]
  ): Printable[Employee] =
    p.contramap(e => s"${e.name} is ${e.age} years old.")

  def main(args: Array[String]): Unit = {
    println(format(Box("hello world")))
    println(format(Box(true)))
    println(format(523))
    println(format(Employee("John Smith", 23)))
  }
}
