# Scala with Cats Code

Sandbox project for the exercises in the book [Scala with Cats][book].
Based on the [cats-seed.g8][cats-seed] template by [Underscore][underscore].

~~Copyright Anonymous Aardvark.~~ Licensed [CC0 1.0][license].

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)][gitter]


# Chapter 1. Introduction / Type classes

Type classes are a Functional Programming (FP) construct (do not confuse with a regular `class`) allowing the
description of an expected behavior over a type we don't own. In Scala, they are created through a type-parametrized
`trait Example[A]` and by using `implicit`s to define the expected behavior for each sub-type of `A`.

Differently from OOP, type classes **do not** require any inheritance, nor even access to the code we want to set a
specific behavior. It requires, however, the language supports extension methods (`implicit`s in Scala).
[More details here](https://docs.scala-lang.org/scala3/book/ca-type-classes.html).

## Chapter exercises

1. [JsonExample](src/main/scala/sandbox/chapter1/JsonExample.scala): Basic example on scala `implicits`
2. [PrintableLibrary](src/main/scala/sandbox/chapter1/PrintableLibrary.scala): First exercise on type classes
3. [TypeclassesWithCats](src/main/scala/sandbox/chapter1/PrintableLibrary.scala): How to use/declare typeclasses on Cats


# Chapter 2. Monoids and Semigroups

## Semigroups
A semigroup is a set (aka. type) equipped with a binary operation, such that:
- The operation must always have type `(A, A) -> A`
  - Integer addition is a suffices, because we can't add two integers and get a non-integer;
  - Integer division doesn't, because there are at least two integers `a, b` such that `a / b` is not an integer
- The binary operation must be associative
  - Integer addition is associative because `a + (b + c) = (a + b) + c`
  - Integer subtraction is not associative because `a - (b - c) != (a - b) - c`
  - String concatenation is associative because `a ++ (b ++ c) == (a ++ b) ++ c`
  - Recall **association** means grouping, **commutation** means swapping:
    - Association: `(a ++ b) ++ c = a ++ (b ++ c)`
    - Commutation: `(a ++ b) ++ c != (a ++ c) ++ b`
    - String concatenation, for instance, is associative, but not commutative.

## Monoids
A monoid is a semigroup containing an identity (empty) element on the set `A`:
- Under integer addition, `0` is the empty (identity) element because for any integer `a`, `a + 0 = a`
- Under integer multiplication, `1` is empty element following the same rationale

## Chapter exercises

1. [BooleanMonoids](./src/main/scala/sandbox/chapter2/BooleanMonoids.scala): Implements boolean operations as monoids
2. [MonoidAddition](./src/main/scala/sandbox/chapter2/MonoidAddition.scala): Implements addition over list as a monoid

# Chapter 3. Functors

## Higher Kinded types

Are represented by `T[_]` and are basically parametrized (1+) types, for instance, `List[Int]`. In this, case we say
`List` is a type constructor (because it takes a parameter), whereas `List[Boolean]` is a type.

## (Covariant) Functors and the map method

Functor is a type `F[A]` with a method `.map(A => B): F[B]`. It is as if we had a type `A` wrapped by a
type `F`, but when we do `F[A].map(A => B)`, the result is still wrapped in `F` again, hence `F[B]`. Functors obey two
laws:
- Identity: `fa.map(a -> a) = fa`, aka. calling map with identity does nothing
- Composition: `fa.map(g(f(_)) == fa.map(f).map(g) == (f o g)(fa)`

Scala `Function1[I, O]` can be seen as a functor if we fix the input parameter `I` and allow the output
parameter `O` to vary. Namely, let `F*[O] = Function1[I, O]`, because `I` is fixed. Then, we can see `F*[O]`
as a functor that can receive a function (to be used in the map) of type `g: O => U` and return a `F*[U]`. Under
the hood, what we have is `F*[U] = F*[O].map(O => U) = F[I, O].map(O => U) = f(g(x))`.

Scala `Function1` are functors: `(func1 map func2)(x)`, thus allowing lazy operation chaining. Besides, Cats allows
creating functors for any single-parameter kind.

## Contravariant Functors and the contramap method

Formalities aside, the underlying idea of functors consists in chaining operations. The `F*[O]` idea from previous
section revolves around the fact it represents a function `f: I => O` with a fixed input type `I`. Since it's a functor,
we can chain (map) it with another function `g: O => U` using `F*[O].map(g: O => U)` and get back an `F*[U]` which, in
the end of the day corresponds to a function chaining `(f o g)(x) = f(g(x)): I => U = F*[U]`.

If instead of appending a function we want to prepend one, `F*[O]` must provide a `.contramap(? => I)` method and, if it
does, then it will be called Contravariant Functor. Under  this arrangement, we can have another function `e: H => I`
and get a `F'[O] = F*[O].contramap(H => I)`. Notice `.contramap` yields a new type `F'[O] = F[J, O]` instead
`F*[O] = F[I, O]`. In a nutshell, contravariant functors allow, through contra-map, function composition by the left,
instead of the usual right: `(e o f)(x) = e(f(x)): J => O = F'[O]`.

Explanation is hard because Cats' functor type is a 1-Kind type, for less-formal and 2-kind type
[this Stack Overflow top answer](https://stackoverflow.com/questions/15457015/explain-contramap) may be way easier to
understand.

## Invariant Functors and the imap method

Invariant functors provide a method `F[A].imap(a: A => B, b: B => A): F[B]` where `a` and `b` are conversion functions
between domains. By specifying `a` and `b` it's possible to construct a new functor type `F[B]`.

## A bridge between Functors and Type naming

- **Covariant type**: means `B` is a subtype of `A`, hence we can always up-cast/coerce/replace `B` into `A`. By
  analogy, if `F` is a (covariant) functor, whenever we have a `F[B]` and a conversion `B => A`, it's possible to obtain
  an `F[A]`.
- **Contravariant type**: is the opposite, meaning `B` is a supertype of `B`. By analogy, a contravariant functor `F[A]`
  allows providing a mapping `Z => A` and yields a new functor `F[Z]`.
- **Invariant functor**: combines the case where we can map from `F[A] => F[B]` via a function `A => B` and vice-versa.

## Chapter exercises

1. [FunctorExamples](./src/main/scala/sandbox/chapter3/FunctorExamples.scala): Basic functor examples with function 
   chaining
2. [BranchingWithFunctor](./src/main/scala/sandbox/chapter3/BranchingWithFunctor.scala): Implements function chaining as 
   functors
3. [PrintableContramap](./src/main/scala/sandbox/chapter3/PrintableContramap.scala): Shows how to define a new
   `Functor[B]` in terms of an already-existing `Functor[A]` though contra-maps for the already-seen `Printable[A]` type.
4. [InvariantFunctorCodec](./src/main/scala/sandbox/chapter3/InvariantFunctorCodec.scala): Shows how bidirectional
   functors and imap works.

[cats-seed]: https://github.com/underscoreio/cats-seed.g8
[underscore]: https://underscore.io
[book]: https://www.scalawithcats.com/dist/scala-with-cats.pdf
[license]: https://creativecommons.org/publicdomain/zero/1.0/
[gitter]: https://gitter.im/underscoreio/scala?utm_source=essential-scala-readme&utm_medium=badge&utm_campaign=essential-scala
