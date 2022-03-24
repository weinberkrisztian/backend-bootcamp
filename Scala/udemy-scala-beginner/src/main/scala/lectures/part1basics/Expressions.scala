package lectures.part1basics


object Expressions extends App {

  // Instruction (DO) like in Java or other imperative language (do dis print that set value to that
  // VS
  // Expression (VALUES)


  // IF expression
  val aCondition = true
  val aConditionedValue = if(aCondition) 5 else 3
  println(aConditionedValue)

  // In Scala everything is an expression
  // Side effects are expressions returning Unit
  // side effects: prints, whiles, reassigning

  var aVariable = 3
  val aWeirdValue = (aVariable = 5) // Unit == Void
  println(aWeirdValue)

  // CODE BLOCK

  // it is an expression
  // the value is the last expression inside it
  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if(z < 2) "hello" else "goodbye"
  }
}
