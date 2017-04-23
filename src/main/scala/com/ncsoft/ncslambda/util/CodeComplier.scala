package com.ncsoft.ncslambda.util


import com.ncsoft.ncslambda.pipes.Pipe

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import scala.reflect.runtime.{universe => u}
/**
  * Runtime에 Plain Text 형태의 Source Code를 Class로 변환하는 클래스
  *
  */
class CodeComplier() extends Logging{
  val toolbox = currentMirror.mkToolBox()

  def sourceToClass[OutputType : u.TypeTag](code : String) : OutputType = {
     val codeTree = toolbox.parse(code)
     toolbox.compile(codeTree)().asInstanceOf[OutputType]
  }
}
