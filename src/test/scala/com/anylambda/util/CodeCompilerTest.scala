package com.anylambda.util

import java.io.File
import java.lang.reflect.Method
import java.net.{URI, URL, URLClassLoader}

import com.anylambda.application.ApplicationContext
import com.anylambda.exception.CompileException
import com.anylambda.pipes.Pipe
import org.junit.{After, Assert, Before, Test}
import sun.misc.Launcher

import scala.util.control.NonFatal

/**
  * Created by leeyh0216 on 17. 4. 25.
  */
class CodeCompilerTest extends Logging{

  val codeCompiler = new CodeComplier(Thread.currentThread().getContextClassLoader)

  @throws(classOf[Exception])
  @Before
  def setUp() = {

  }

  @throws(classOf[Exception])
  @After
  def tearDown() = {

  }

  @Test
  def testCompileCode() = {
    val ctx = new ApplicationContext("TestApplication")

    val testPipe : Pipe[Int,Int] = codeCompiler.sourceToClass(
      """
        | import com.anylambda.pipes._
        | import com.anylambda.application._
        | new Pipe[Int,Int] {
        |      override def process(ctx: ApplicationContext, input: Int): Int = input+1
        |
        |      override def caughtError(exception: Exception): Unit = println("Exception Caused")
        |    }
      """.stripMargin)

    val ret = testPipe.process(ctx,1)
    Assert.assertEquals(2,ret)
  }

  @Test(expected = classOf[ClassCastException])
  def testClassCastException() = {
    val testPipe : Pipe[_,_] = codeCompiler.sourceToClass(
      """
        |class A{
        |}
      """.stripMargin)
  }

  @Test(expected = classOf[CompileException])
  def testMissImportCompileException() = {
    val testPipe : Pipe[_,_] = codeCompiler.sourceToClass(
      """
        | //import com.anylambda.pipes._
        | import com.anylambda.application._
        | new Pipe[Int,Int] {
        |      override def process(ctx: ApplicationContext, input: Int): Int = input+1
        |
        |      override def caughtError(exception: Exception): Unit = println("Exception Caused")
        |    }
      """.stripMargin)
  }

  @Test(expected = classOf[CompileException])
  def testInvalidSyntaxCompileException() = {
    val testPipe : Pipe[String,Int] = codeCompiler.sourceToClass(
      """
        | //import com.anylambda.pipes._
        | import com.anylambda.application._
        | new Pipe[String,Int] {
        |      //There is no method input.len
        |      override def process(ctx: ApplicationContext, input: String): Int = input.len + 1
        |
        |      override def caughtError(exception: Exception): Unit = println("Exception Caused")
        |    }
      """.stripMargin)

  }

  @Test
  def testLoadExternalJar() = {
    val jarURI = new URI("file://"+new File("src/test/resources/okhttp-3.7.0.jar").getAbsolutePath).toURL
    var jarURI2 = new URI("file://"+new File("src/test/resources/okio-1.0.0.jar").getAbsolutePath).toURL
    val clsLoader = new DynamicClassLoader(Seq(jarURI,jarURI2),Thread.currentThread().getContextClassLoader)
    Thread.currentThread().setContextClassLoader(clsLoader)

    val cc = new CodeComplier(clsLoader)

    val client : Any= cc.sourceToClass(
      """
        |import okhttp3.OkHttpClient
        |val okhttp = new OkHttpClient
        |okhttp
      """.stripMargin)

  }

}
