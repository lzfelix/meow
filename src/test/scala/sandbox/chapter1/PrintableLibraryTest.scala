package sandbox.chapter1

import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter1.PrintableLibrary.{Cat, Printable}
import sandbox.chapter1.PrintableLibrary.PrintableSyntax._
import sandbox.chapter1.PrintableLibrary.PrintableInstances._

class PrintableLibraryTest extends AnyFlatSpec {
  it should "format integers" in {
    assert(10.format == "Print value for integer: 10")
  }

  // can't use str.format because String has a method called format
  it should "format strings" in {
    val text = "some random string"
    assert(Printable.format(text) == s"Print value for string: $text")
  }

  it should "format a custom object" in {
    val tommyTheCat: Cat = Cat("Tommy", 6, "gray")
    val expected: String = s"Tommy is a 6 year-old gray cat"

    assert(tommyTheCat.format == expected)
  }
}
