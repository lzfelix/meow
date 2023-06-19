# Scala with Cats Code

Sandbox project for the exercises in the book [Scala with Cats][book].
Based on the [cats-seed.g8][cats-seed] template by [Underscore][underscore].

Copyright Anonymous Aardvark. Licensed [CC0 1.0][license].

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)][gitter]



# Chapter 1. Introduction / Type classes

Type classes are a Functional Programming (FP) construct (do not confuse with a regular `class`) allowing the
description of an expected behavior over a type we don't own. In Scala, they are created through a type-parametrized
`trait Example[A]` and by using `implicit`s to define the expected behavior for each sub-type of `A`.

Differently from OOP, type classes **do not** require any inheritance, nor even access to the code we want to set a
specific behavior. It requires, however, the language supports extension methods (`implicit`s in Scala).
[More details here](https://docs.scala-lang.org/scala3/book/ca-type-classes.html).

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

1. [BooleanMonoids](./src/main/scala/sandbox/chapter2/BooleanMonoids.scala): Implements boolean operations as monoids
2. [MonoidAddition](./src/main/scala/sandbox/chapter2/MonoidAddition.scala): Implements addition over list as a monoid

# Chapter 3. Functors

## Higher Kinded types

Are represented by `T[_]` and are basically parametrized (1+) types, for instance, `List[Int]`. In this, case we say
`List` is a type constructor (because it takes a parameter), whereas `List[Boolean]` is a type.

## Functors definition
Functor is a type `F[A]` with a map operation of type `(A => B) => F[B]`. It is as if we had a type `A` wrapped by a
type `F`, but when we do `F[A].map(A => B)`, the result is still wrapped in `F` again, hence `F[B]`. Functors obey two
laws:
- Identity: `fa.map(a -> a) = fa`, aka. calling map with identity does nothing
- Composition: `fa.map(g(f(_)) == fa.map(f).map(g) == (f o g)(fa)`

Scala `Function1[I, O]` can be seen as a functor if we fix the input parameter `I` and allow the output
parameter `O` to vary. Namely, let `f: Function1*[O] = Function[I, O]`, because `I` is fixed. Then, we can see `Function1*[O]`
as a functor that can receive a function (to be used in the map) of type `g: O => U` and return a `Function*[U]`. Under
the hood, what we have is `Function1*[U] = Function1*[O].map(O => U) = Function1[I, O].map(O => U) = f(g(x))`.

Scala Function1 are functors: `(func1 map func2)(x)`, thus allowing lazy operation chaining. However, Cats allows
creating functors for any single-parameter kind.

1. [FunctorExamples](./src/main/scala/sandbox/chapter3/FunctorExamples.scala): Basic functor examples with function
chaining
2. [BranchingWithFunctor](./src/main/scala/sandbox/chapter3/BranchingWithFunctor.scala): Implements function chaining as
functors
3. [PrintableContramap](./src/main/scala/sandbox/chapter3/PrintableContramap.scala): Shows how to define a new
`Functor[B]` in terms of an already-existing `Functor[A]` though contra-maps.

[cats-seed]: https://github.com/underscoreio/cats-seed.g8
[underscore]: https://underscore.io
[book]: https://www.scalawithcats.com/dist/scala-with-cats.pdf
[license]: https://creativecommons.org/publicdomain/zero/1.0/
[gitter]: https://gitter.im/underscoreio/scala?utm_source=essential-scala-readme&utm_medium=badge&utm_campaign=essential-scala
