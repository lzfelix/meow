package sandbox.chapter1

import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter1.JsonExample._
import sandbox.chapter1.JsonExample.Extensions._
import sandbox.chapter1.JsonExample.JsonWriteInstances._

class JsonExampleTest extends AnyFlatSpec {

  private val validEmail: Option[String] = Option("smith.john@mailprovider.com")

  it should "create a valid person with a non-empty email" in {
    val result: Json = Person("John Smith", 41.7, validEmail).toJson
    val expected: Json = JsObject(
      Map(
        "name" -> JsString("John Smith"),
        "age" -> JsDecimal(41.7),
        "email" -> JsString(validEmail.get)
      )
    )

    assert(result == expected)
  }

  it should "create a valid person with an empty email" in {
    val result: Json = Person("John Smith", 41.7, None).toJson
    val expected: Json = JsObject(
      Map(
        "name" -> JsString("John Smith"),
        "age" -> JsDecimal(41.7),
        "email" -> JsNull
      )
    )

    assert(result == expected)
  }

  it should "create a valid map object with string keys and double values" in {
    val result: Json = Map("ab" -> 34d, "cd" -> 99d).toJson
    val expected = JsObject(Map("ab" -> JsDecimal(34), "cd" -> JsDecimal(99)))

    assert(result == expected)
  }

  it should "create an extension method .toJson for integers" in {
    assert((-10).toJson == JsInteger(-10))
  }

  it should "create an extension method .toJson Some/None" in {
    assert(Option(10).toJson == JsInteger(10))
  }

  it should "create an extension method .toJson to Some/None = None" in {
    val x: Option[String] = None
    assert(x.toJson == JsNull)
  }
}
