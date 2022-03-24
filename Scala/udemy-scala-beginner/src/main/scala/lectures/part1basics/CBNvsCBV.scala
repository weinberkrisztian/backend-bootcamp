package lectures.part1basics

object CBNvsCBV extends App {

  // calculates the parameter before actually going inside the method
  def callByValue(x: Long) = {
    println("Called by value: " + x)
    println("Called by value: " + x)
  }

  // the parameter calculation happens inside when it is needed, if the parameter is not used it won't be calculated
  def callByName(x: => Long) = {
    println("Called by name: " + x)
    println("Called by name: " + x)
  }


  callByValue(System.nanoTime())
  callByName(System.nanoTime())



}
