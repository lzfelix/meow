# Scala with Cats Code

Sandbox project for the exercises in the book [Scala with Cats][book].
Based on the [cats-seed.g8][cats-seed] template by [Underscore][underscore].

Copyright Anonymous Aardvark. Licensed [CC0 1.0][license].

[![Gitter](https://badges.gitter.im/Join%20Chat.svg)][gitter]

## Table of Contents

### Type classes

> Type classes are a Functional Programming (FP) construct (do not confuse with a regular `class`) allowing the
> description of an expected behavior over a type we don't own. In Scala, they are created through a type-parametrized
> `trait Example[A]` and by using `implicit`s to define the expected behavior for each sub-type of `A`.
> 
> Differently from OOP, type classes **do not** require any inheritance, nor even access to the code we want to set a
> specific behavior. It requires, however, the language supports extension methods (`implicit`s in Scala).
> [More details here](https://docs.scala-lang.org/scala3/book/ca-type-classes.html).

1. [JsonExample](src/main/scala/sandbox/chapter1/JsonExample.scala): Basic example on scala `implicits`
2. [PrintableLibrary](src/main/scala/sandbox/chapter1/PrintableLibrary.scala): First exercise on type classes
