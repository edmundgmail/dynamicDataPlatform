package com.ddp.jarmanager

import com.ddp.models.CodeSnippet
import com.ddp.utils.Testing

class TestScalaSourceCompiler extends Testing{

  it("Test a scala file can be compiled and ingested"){
      val scalaScript = new CodeSnippet("com.user.MyClass",
        """
          package com.user
          case class Student(name: String, age: Integer)

          class MyClass() {
            def run() {
              "this is a test"
            }
          }
        """.stripMargin)

    //ScalaSourceCompiler.compile(scalaScript)
    //ScalaSourceCompiler.run("com.user.MyClass", "run")
  }
}
