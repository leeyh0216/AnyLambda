package com.anylambda.util

import java.io.{File, FileNotFoundException}
import java.net.{URI, URL, URLClassLoader}

import com.anylambda.exception.CompileException
import org.apache.xbean.classloader.JarFileClassLoader

import scala.reflect.runtime.{ universe => u}
import scala.tools.reflect.ToolBox
import scala.util.control.NonFatal
/**
  * Runtime에 Plain Text 형태의 Source Code를 Class로 변환하는 클래스
  *
  */
class CodeComplier(clsLoader : ClassLoader) extends Logging{
  val toolbox = u.runtimeMirror(clsLoader).mkToolBox()

  /**
    * 매개변수로 전달된 code(Scala)를 컴파일하여 OutputType으로 캐스팅하여 반환하는 함수.
    * code의 마지막 줄은 반드시 new를 이용한 OutputType 객체 생성 구문이어야 합니다.
    *
    * @param code Scala Code(마지막 줄은 OutputType 객체 생성 구문이어야 함)
    * @tparam OutputType code 컴파일 후 반환받을 타입
    * @throws com.anylambda.exception.CompileException 정상적으로 code 컴파일이 이루어지지 않았을 경우 발생
    * @return code Compile 후 OutputType으로 변환된 객체
    */
  @throws(classOf[CompileException])
  def sourceToClass[OutputType : u.TypeTag](code : String) : OutputType = {
    try {
      val codeTree = toolbox.parse(code)
      toolbox.compile(codeTree)().asInstanceOf[OutputType]
    }
    catch{
      case NonFatal(t)  => throw new CompileException(t)
    }
  }
  @throws(classOf[FileNotFoundException])
  def addExternalJarToClassLoader(jarDir : String, classLoader : ClassLoader) = {
    val jarFile = new File(jarDir)
    if(!jarFile.exists())
      throw new FileNotFoundException(String.format("JarFile을 찾을 수 없습니다 : %s", jarDir))

    val jarURI = new URL("file://" + jarFile.getAbsolutePath + "")
    var jarURI2 = new URL("file://"+new File("src/test/resources/okio-1.0.0.jar").getAbsolutePath)
    val jarURLS = new Array[URL](2)
    jarURLS(0) = jarURI
    jarURLS(1) = jarURI2

    new JarFileClassLoader("",jarURLS,classLoader)
  }
}
