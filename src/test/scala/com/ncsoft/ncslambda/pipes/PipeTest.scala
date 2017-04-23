package com.ncsoft.ncslambda.pipes

import com.ncsoft.ncslambda.application.ApplicationContext
import org.junit.{After, Assert, Before, Test}

import scala.reflect.runtime.{universe => u}

class PipeTest {

  val appCtx = new ApplicationContext("TestApp")

  @Before
  def setUp() = {

  }

  @After
  def tearDown() = {

  }

  @Test
  def testPipeProcess() {
    val pipe = new Pipe[List[String], Int]{

      override def process(ctx: ApplicationContext, input: List[String]): Int = {
        return input.size
      }

      override def caughtError(exception: Exception): Unit = {
        println(exception.getMessage)
      }
    }

    val result = pipe.process(appCtx,List("hello","world","my","name","is","lee","yong","hwan"))
    Assert.assertEquals(8,result)
  }

  @Test
  def testPipeException(): Unit = {
    val pipe = new Pipe[List[String],Int]{

      override def process(ctx: ApplicationContext, input: List[String]): Int = {
        return input.size
      }

      override def caughtError(exception: Exception): Unit = {
        println(exception.getMessage)
      }
    }

    try{
      pipe.process(appCtx,null)
    }
    catch{
      case e : NullPointerException =>{

      }
      case _ =>{
        throw new Exception
      }
    }
  }
}