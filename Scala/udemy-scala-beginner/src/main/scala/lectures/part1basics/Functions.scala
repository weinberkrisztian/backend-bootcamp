package lectures.part1basics

import scala.annotation.tailrec

object Functions extends App {

  def aFunction(a: String, b: Int):String = {
    println("Called aFunction")
    a + " " + b
  }

  println(aFunction("hyy", 3))

  def aParameterlessFunction():Int =
    42

  // We can call a parameterless function without the parentheses
  println(aParameterlessFunction())
  println(aParameterlessFunction)

  // WHEN YOU NEED LOOPS USE RECURSIVE
  def aRepeatedFunction(aString: String, aNumber: Int): String = {
    if(aNumber == 1) aString
    else aString + aRepeatedFunction(aString, aNumber -1)
  }
  println(aRepeatedFunction("hy", 4))

  def aFunctionWithOnlySideEffect(): Unit = println("It returns only Unit which is Void!")

  // define functions inside each other
  def aBigFunction(n : Int):Int = {
    def aSmallFunction(n1:Int, n2:Int):Int = n1 + n2
    aSmallFunction(n-1, n-2)
  }

  // Greeting
  def greet(name: String, age: Int) : Unit = println("Hy my name is " + name + " and I am " + age + " years old.")
  greet("Krisztian", 24)

  // Factorial 1*2*3*4...*n
  def factorial(n: Int):Int = {
    if(n == 1) return n
    else n * factorial(n-1)
  }
  println(factorial(5))

  // Fibonacci
  def fibonacci(n: Int):Int = {
      if(n <= 1) n
      else fibonacci(n-1) + fibonacci(n-2)
  }
  println(fibonacci(3))

  def alternativeFactorial(n: Int): BigInt = {
    @tailrec
    def factHelper(x:Int, accumulator: BigInt):BigInt = {
      if (x <= 1) accumulator
      else factHelper(x - 1, x * accumulator) // TAIL RECURSIVE, use a recursive call as the LAST expression
    }
      factHelper(n, 1)
  }

  println(alternativeFactorial(500))

  @tailrec
  def alternativeStringConcat(n:Int, word:String, acc:String):String =
    if(n <= 1) acc
    else alternativeStringConcat(n-1, word, word+acc)

  println(alternativeStringConcat(6, "test", "test"))


  def fibonacciTailRecursiveVersion(n:Int):Int = {
    @tailrec
    def fiboTailRec(i:Int, last: Int, nextToLast: Int) : Int = {
      if(i >= n) last
      else fiboTailRec(i + 1, last + nextToLast, last)
    }

    if(n <= 2) 1
    else fiboTailRec(2, 1, 1)
  }

  println(fibonacciTailRecursiveVersion(6))
}

