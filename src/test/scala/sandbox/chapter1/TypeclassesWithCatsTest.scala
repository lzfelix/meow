package sandbox.chapter1

import cats.implicits.catsSyntaxEq
import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter1.TypeclassesWithCats.Cat

class TypeclassesWithCatsTest extends AnyFlatSpec {
  private val garfield: Cat = Cat("Garfield", 23, "orange")
  private val garfieldInDisguise: Cat = Cat("Garfield", 7, "black")

  // These tests use Eqv instead of === because the latter refers to
  // scalactic's ===, not the Cats one
  it should "be able to compare two ints" in {
    assert(12 eqv 12)
  }

  it should "be able to compare two Cats" in {
    assert(garfield eqv garfieldInDisguise)
  }

  it should "be able to compare Option[Cat], where Cat is a custom type," +
    "even though we didn't implement an implicit for Option[Cat]" in {
    // true, because Cats know how to compare Option[A] and we have
    // provided a way to compare A when A = Cat.
    assert(Option(garfield) eqv Option(garfieldInDisguise))
  }

  it should "be able to compare primitive types out of the box" in {
    assert(12 eqv 12)
  }
}
