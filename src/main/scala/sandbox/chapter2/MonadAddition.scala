package sandbox.chapter2

import cats.instances.int._
import cats.instances.tuple._
import cats.syntax.semigroup._
import cats.{Eq, Monoid}
import cats.implicits.catsSyntaxEq

object MonadAddition {
  def add(l: List[Int]): Int = l.reduce(_ |+| _)

  def smartAdd[A](l: List[A])(implicit monoid: Monoid[A]): A = {
    l.foldLeft(monoid.empty)(_ |+| _)
  }

  case class Order(totalCost: Double, quantity: Double)
  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    override def empty: Order = Order(0, 0)

    override def combine(x: Order, y: Order): Order =
      Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
  }

  implicit val orderEq: Eq[Order] = new Eq[Order] {
    override def eqv(x: Order, y: Order): Boolean = {
      x.totalCost == y.totalCost && x.quantity == y.quantity
    }
  }

  def main(args: Array[String]): Unit = {
    val inputData = (1 to 10).map(x => Option(x)).toList
    assert(smartAdd(inputData) === Option(55))

    assert(smartAdd(List(Some(1), None, Some(2), Some(3), None)) === Some(6))

    assert(smartAdd((1 to 10).toList) === 55)

    assert(smartAdd(List.fill[Option[Int]](10)(None)) === None)

    val orders = List(Order(100, 20), Order(200, 50), Order(300, 20))
    val expectedOutput = Order(600, 90)
    assert(smartAdd(orders) === expectedOutput)
  }
}
