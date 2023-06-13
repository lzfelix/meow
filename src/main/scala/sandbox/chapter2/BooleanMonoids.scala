package sandbox.chapter2

object BooleanMonoids {
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid

    implicit val booleanAnd: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = true
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

    implicit val booleanOr: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = false
      override def combine(x: Boolean, y: Boolean): Boolean = x && y
    }

    implicit val booleanXor: Monoid[Boolean] = new Monoid[Boolean] {
      override def empty: Boolean = false
      override def combine(x: Boolean, y: Boolean): Boolean =
        (x && !y) || (!x && y)
    }
  }
}
