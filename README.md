# Jelik Language

Jelik is an JVM language similar to Kotlin and Go. Its main concept is simplicity and safety. 
At this moment compiler is in its early stage of development.

Feel free to explore the codebase. Best place to start is to write a unit test. 
For example check the `org.jelik.compiler.integration.FunctionIntegrationTest` class. 

You can also generate tests by using `org.jelik.utils.TestGenerator`. To do this just create a file with `*.jlk` extension in `src/testData` directory. 
After that run `TestGenerator` main function.

## Features

* lambdas
* type inference
* no checked exceptions
* null safety
* smart casts
* extension functions
* `Python` like lists
* conditions without parenthesis (just like in `Go`)

## Building

Project is based on `gradle` so to build a distribution just run the `build` gradle task. 
This will produce a `jelik-lang-new-1.0.0.zip` (and tar) distribution. 
After unpacking it, the compiler executable will be in `bin` folder.

## Example code

Create a `main.jlk` file:

```
fun main(args []String) {
    println("Hello world")
}
```

And compile it:

```
jlkc main.jlk
```

In the location of compilation you will see a `build` folder which contains the compiled file (`main.class`).

To execute it, change directory to `build` and run it with:

```
java main
```

## Syntax

Conditions:

```
fun foo(bar Boolean) -> Int {
    if bar {
        ret 1
    } else {
        ret 2
    }
}
```

Arrays:

```
fun createArray(a Int, b Int, c Int) -> []Int {
    val result = [a, b, c]
    ret result
}
```

Lambdas:

```
fun expr(a Int) -> Int {
    val l = lam b -> { ret b }
    ret a + l(a)
}
```



