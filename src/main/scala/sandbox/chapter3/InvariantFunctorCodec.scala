package sandbox.chapter3

object InvariantFunctorCodec {

  trait Codec[A] { self =>
    def encode(value: A): String
    def decode(value: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))
      override def decode(value: String): B = dec(self.decode(value))
    }
  }

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

  // defining a String => String codec that works as identity
  // note that A = String, but B will only be defined when imap is implemented
  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(value: String): String = value
    override def decode(value: String): String = value
  }

  // A: String, B: Int/Bool/Double through the iamp
  implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)
  implicit val boolCodec: Codec[Boolean] =
    stringCodec.imap(_.toBoolean, _.toString)
  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap(_.toDouble, _.toString)

  // Implementing a String Codec for a custom 1-Kind
  final case class Box[A](value: A)
  implicit def boxCodec[A](implicit innerCodec: Codec[A]): Codec[Box[A]] =
    stringCodec.imap(
      strValue => Box(innerCodec.decode(strValue)),
      boxValue => innerCodec.encode(boxValue.value)
    )
}
