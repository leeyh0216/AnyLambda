package com.anylambda.pipes

import com.anylambda.application.{ApplicationContext, HttpContext}
import com.anylambda.util.CodeComplier
import org.junit.{After, Assert, Before, Test}

/**
  * Created by leeyh0216 on 17. 4. 22.
  */
class PipeCodeGeneratorTest {

  @Before
  def setUp(): Unit = {

  }

  @After
  def tearDown(): Unit = {

  }


  @Test
  def testPipeCodeGenerator(){
    val p = new CodeComplier()
    val pipe : Pipe[_,_] = p.sourceToClass(
      """
       import com.anylambda.application._
       import com.anylambda.pipes._
       new Pipe[List[String], Int]{

       override def process(ctx: ApplicationContext, input: List[String]): Int = {
         return input.size
       }

       override def caughtError(exception: Exception): Unit = {
         println(exception.getMessage)
       }
     }
      """)

    Assert.assertEquals(2,pipe.process(new ApplicationContext("TestApp"),pipe.cast(List("a","b"))))
  }

  @Test
  def testHttpPipeCodeGenerator(): Unit ={
    val p = new CodeComplier()
    val pipe : Pipe[_,_] = p.sourceToClass(
      """
       import com.anylambda.application._
       import com.anylambda.pipes._
       new HttpPipe{

       override def process(ctx: ApplicationContext, input: Map[String,String]): String = {
         input.mkString(",")
       }

       override def caughtError(exception: Exception): Unit = {
         println(exception.getMessage)
       }
     }
      """)

    Assert.assertEquals(pipe.process(new HttpContext("localhost"),pipe.cast(Map(("HELLO","WORLD"),("nc","soft")))),"HELLO -> WORLD,nc -> soft")
  }
}
