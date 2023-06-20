package sandbox.chapter3

import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter3.InvariantFunctorCodec.{decode, encode, Box}

class InvariantFunctorCodecTest extends AnyFlatSpec {
  // Note: all these tests can be simplified through a helper function.
  // For pedagogical reasons, however, they will be kept in a long form

  it should "encode/decode String <=> String @ identity Codec" in {
    assert(
      encode("123") == "123" &&
        decode[String]("123") == "123"
    )
  }

  it should "encode/decode a String <=> Int" in {
    // We must pass a type hint for the decoder because it transforms a String into a type B
    // that can be anything. This is not necessary for the encoder, because it takes a type A
    // and returns a string, however, type A can be inferred from the function parameter. In
    // this case, A = Int.
    assert(
      encode(123) == "123" &&
        decode[Int]("123") == 123
    )
  }

  it should "encode/decode a String <=> Boolean" in {
    assert(
      encode(true) == "true" &&
        !decode[Boolean]("false")
    )
  }

  it should "encode/decode a String <=> Double" in {
    assert(
      encode(12.3) == "12.3" &&
        decode[Double]("12.3") == 12.3
    )
  }

  it should "encode/decode Box[Int] <=> String" in {
    val integerBox = Box(999)
    assert(
      encode(integerBox) == "999" &&
        decode[Box[Int]]("999") == integerBox
    )
  }

  // imagine all the other possible unit tests with many types here
}
