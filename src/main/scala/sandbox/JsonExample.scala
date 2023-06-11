package sandbox

object JsonExample {

  sealed trait Json

  /* Case classes to represent each JSON type
   * Note: final case class is a Scala best-practice since it doesn't allow
   * case classes to be inherited.
   */
  final case class JsString(get: String) extends Json
  final case class JsDecimal(get: Double) extends Json
  final case class JsInteger(get: Int) extends Json
  final case class JsObject(get: Map[String, Json]) extends Json

  // All `null`s should point to the same instance
  final object JsNull extends Json {
    override def toString: String = "null"
  }

  /** Trait to serialize Scala objects into JSON objects
    * It basically takes a Scala type and transforms it into a Json type
    */
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  // Some implicit serializers. They are defined here to be used elsewhere
  object JsonWriteInstances {
    implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
      override def write(value: String): Json = JsString(value)
    }

    implicit val decimalWriter: JsonWriter[Double] = new JsonWriter[Double] {
      override def write(value: Double): Json = JsDecimal(value)
    }

    implicit val intWriter: JsonWriter[Int] = new JsonWriter[Int] {
      override def write(value: Int): Json = JsInteger(value)
    }

    /* For Option[T] we need to define a encoder for the case None (since
     * None <: Option[T]) and fallback to another implicit encoder for the
     * type T. If we don't do this, then we'll need to define one encoder
     * for every combination Option[T], ie, Option[String], Option[Int], ...
     *
     * Since the optionEncoder needs other encoder as parameter, it must be
     * a def (not a val, as done before)
     *
     * If we do `Json.toJson(Option("abc"))`, the code will fall into the
     * `Some(concreteValue)` and use the `stringWriter` encoder.
     * `Json.toJson(None)`, on the other hand, returns `JsNull()`.
     *
     * Notice if we do `Json.toJon(Some(RandomClass))`, this returns a compilation
     * error, as there's no implicit encoder for `RandomClass`, and implicits
     * are resolved during compile time (not run time)
     */
    implicit def optionWriter[A](
      implicit writer: JsonWriter[A]
    ): JsonWriter[Option[A]] = new JsonWriter[Option[A]] {
      override def write(value: Option[A]): Json = value match {
        case Some(concreteValue) => writer.write(concreteValue)
        case None                => JsNull
      }
    }

    /* We can use the same concept to build the serializer for Option[A] to also
     * build a serializer of type Map[String, V]. Note, differently from JSON, all
     * keys in tye map will have type V because scala collections are homogeneous.
     * Maybe that could be changed by using Either type?
     */
    implicit def objectWriter[V](
      implicit writer: JsonWriter[V]
    ): JsonWriter[Map[String, V]] = new JsonWriter[Map[String, V]] {
      override def write(value: Map[String, V]): Json =
        JsObject(value.map({
          case (key, value) => key -> writer.write((value))
        }))
    }
  }

  /*& We need to create the method ot serialize a Scala instance as a Json
   * instance in a object, to it can be accessed without instantiating the
   * trait, for instance.
   */
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }

  /** Then, we can define some custom types that can be serialized as well, as
    * long as we provide a custom serializer as well. Notice the serializer goes
    * into a companion object, to it can be accessed regardless any case class
    * instance*/
  final case class Person(name: String, age: Double, email: Option[String])
  object Person {
    // Using local imports just to make it explicit where they are used
    import sandbox.JsonExample.JsonWriteInstances.{optionWriter, stringWriter}

    implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
      override def write(value: Person): Json = {
        JsObject(
          Map(
            "name" -> JsString(value.name),
            "age" -> JsDecimal(value.age),
            "email" -> Json.toJson(value.email)
          )
        )
      }
    }
  }

  /* Another useful capability of implicits, is adding extension methods to
   * code we don't have access (eg: binary code). In this recipe we add the
   * `.toJson` method to every `Json` object provided we can find a implicit
   * `JsonWriter[A]` defined elsewhere.
   */
  object Extensions {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
    }
  }

  def main(args: Array[String]): Unit = {
    // Using local imports just to make it explicit where they are used
    import sandbox.JsonExample.Extensions.JsonWriterOps
    import sandbox.JsonExample.JsonWriteInstances.{
      objectWriter,
      decimalWriter,
      intWriter
    }

    val validEmail: Option[String] = Option("smith.john@mailprovider.com")

    println(Person("John Smith", 41.7, validEmail).toJson)
    println(Person("John Smith", 41.7, None).toJson)
    // Result:
    // JsObject(Map(name -> JsString(John Smith), age -> JsNumber(41.7), email -> JsString(smith.john@mailprovider.com)))
    // JsObject(Map(name -> JsString(John Smith), age -> JsNumber(41.7), email -> null))

    println(Map("ab" -> 456d, "ef" -> 123d).toJson)
    // JsObject(Map(ab -> JsDecimal(456.0), ef -> JsDecimal(123.0)))

    // For instance, we don't have access to the Int class, but due the
    // extension method, `.toJson` can now be used over it :)
    println(10.toJson)
  }
}
