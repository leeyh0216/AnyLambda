package com.anylambda.exception

/**
  * ToolBox를 이용하여 String 형식의 Code를 compile시 발생하는 Exception을 공통으로 사용하기 위하여 정의한 클래스
  *
  * @param cause Toolbox.compile 시 발생한 Exception
  * @author leeyh0216
  */
class CompileException(cause : Throwable) extends Exception(cause){
  override def getMessage: String = String.format("컴파일 도중 오류가 발생하였습니다 : %s",cause.getMessage)
}
