package sandbox.chapter2

import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter2.MonoidAddition.{smartAdd, Order}

class MonoidAdditionTest extends AnyFlatSpec {

  it should "add integers" in {
    assert(smartAdd((1 to 10).toList) === 55)
  }

  it should "add Option[Int] using the same code" in {
    val inputData = (1 to 10).map(x => Option(x)).toList
    assert(smartAdd(inputData) === Option(55))
  }

  it should "add multiple None values as None" in {
    assert(smartAdd(List.fill[Option[Int]](10)(None)) === None)
  }

  it should "add order together using implicit monoid" in {
    val orders = List(Order(100, 20), Order(200, 50), Order(300, 20))
    val expectedOutput = Order(600, 90)
    assert(smartAdd(orders) === expectedOutput)
  }
}
