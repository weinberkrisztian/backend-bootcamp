package lectures.part1basics

object DefaultParameters extends App{

  def defaultGreeting(firstName: String = "Krisztian", lastname: String, age: Int) = {
    println("My name is " + firstName + " " + lastname + " and I am " + age + " years old!")
  }

  defaultGreeting(age =  24, lastname =  "Weinber")

  val name = "Krisztian"
  val age = 24
  val greeting = s"My name is $name and I am ${age + 1} years old!"
  println(greeting)

  val speed = 11.4f
  val burgerEater= f"$name can eat $speed%2.3f burgers per minute!"
  println(burgerEater)

  println(raw"Raw interpreters keep the \n in raw strings")
  val escaped = "Raw interpreters does not keep the \n if the string is not raw"
  println(raw"$escaped")
}
