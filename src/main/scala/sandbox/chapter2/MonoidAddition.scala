package sandbox.chapter2

import cats.syntax.semigroup._
import cats.{Eq, Monoid}

object MonoidAddition {
  def smartAdd[A](l: List[A])(implicit monoid: Monoid[A]): A = {
    l.foldLeft(monoid.empty)(_ |+| _)
  }

  case class Order(totalCost: Double, quantity: Double)
  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order =
      Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  }

  // Implementing Eq for unit tests
  implicit val orderEq: Eq[Order] = Eq.instance(
    (x, y) => x.totalCost == y.totalCost && x.quantity == y.quantity
  )
}
