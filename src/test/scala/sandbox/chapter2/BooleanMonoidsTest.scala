package sandbox.chapter2

import org.scalatest.flatspec.AnyFlatSpec
import sandbox.chapter2.BooleanMonoids.Monoid

class BooleanMonoidsTest extends AnyFlatSpec {

  private val allBoolTriplets: Seq[(Boolean, Boolean, Boolean)] = for {
    a <- Seq(true, false)
    b <- Seq(true, false)
    c <- Seq(true, false)
  } yield (a, b, c)

  def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
    m.combine(x, m.empty) == x
  }

  def identityLawForAll[A](
    values: Seq[A]
  )(implicit monoid: Monoid[A]): Boolean = {
    values.map(x => identityLaw(x)(monoid)).forall(_ == true)
  }

  def commutativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
    val left: A = m.combine(m.combine(x, y), z)
    val right: A = m.combine(x, m.combine(y, z))
    left == right
  }

  def commutativeLawForAll[A](
    values: Seq[(A, A, A)]
  )(implicit m: Monoid[A]): Boolean = {
    values
      .map { case (x, y, z) => commutativeLaw(x, y, z)(m) }
      .forall(_ == true)
  }

  "identity law" should "hold for Monoid(Boolean, &&)" in {
    // We defined multiple implicits for Monoid[Boolean], so
    // it has to be explicitly imported or the scala compiler
    // will find ambiguous definitions only
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanAnd
    identityLawForAll(Seq(true, false))
  }

  "commutative law" should "hold for Monoid(Boolean, &&)" in {
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanAnd
    commutativeLawForAll(allBoolTriplets)
  }

  "identity law" should "hold for Monoid(Boolean, ||" in {
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanOr
    identityLawForAll(Seq(true, false))
  }

  "commutative law" should "hold for Monoid(Boolean, ||)" in {
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanOr
    commutativeLawForAll(allBoolTriplets)
  }

  "identity law" should "hold for Monoid(Boolean, xor" in {
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanXor
    identityLawForAll(Seq(true, false))
  }

  "commutative law" should "hold for Monoid(Boolean, xor)" in {
    import sandbox.chapter2.BooleanMonoids.Monoid.booleanXor
    commutativeLawForAll(allBoolTriplets)
  }
}
