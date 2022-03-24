package lectures.part1basics

object ValuesVariablesTypes extends App {

  val x: Int = 42
  println(x)
  // VALS ARE IMMUTABLE

  // COMPILER can infer types like: val x = 42

  val aString: String = "testString"
  val inferredString = "inferredTestString"
  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val anInt: Int = 1
  val aShor: Short = 423
  val aLong: Long = 4234444444L
  val aFloat: Float = 4.3F
  val aDouble: Double = 4.3

  // VARIABLE
  var aVariable: Int = 4
  println(aVariable)
  aVariable = 5
  println(aVariable)
  // variables can be re-assigned, they are used as side effects
}

