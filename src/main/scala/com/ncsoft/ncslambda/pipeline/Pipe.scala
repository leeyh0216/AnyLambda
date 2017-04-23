package com.ncsoft.ncslambda.pipeline

import com.ncsoft.ncslambda.application.ApplicationContext

import scala.reflect.runtime.{universe => u}

/**
  * Input, Output으로 정의 된 단방향 Pipe
  * 해당 Pipe를 상속받아 새로운 Pipe를 생성할 수 있다.
  *
  * @tparam InputType Pipe의 Input으로 사용할 Data Type
  * @tparam OutputType Pipe의 Output으로 사용할 Data Type
  */
abstract class Pipe[InputType <:Any : u.TypeTag, OutputType <:Any : u.TypeTag]{

  def cast[A](a : Any ): A = a.asInstanceOf[A]

  /**
    * Pipe의 작업을 실제로 수행하는 클래스
    * Application Context와 Input Type을 가진 Data를 매개변수로 받아 Output Type 결과를 리턴하는 형식이다.
    *
    * @param ctx Application Context
    * @param input Generic에서 정의된 Input Type의 객체
    * @throws java.lang.Exception 해당 함수에서 발생할 수 있는 Exception
    * @return Output Type의 Data
    */
  @throws(classOf[Exception])
  def process(ctx : ApplicationContext, input : InputType) : OutputType

  /**
    * process 함수에서 발생한 Exception을 처리하는 클래스
    *
    * @param exception process() 함수에서 발생한 Exception 객체
    */
  def caughtError(exception : Exception) : Unit

  /**
    * Input Type의 정보를 얻을 수 있는 Tag를 반환한다.
    * @return Input Type Tag
    */
  def getInputTypeTag() = u.typeTag[InputType]

  /**
    * Output Type의 정보를 얻을 수 있는 Tag를 반환한다.
    * @return Output Type Tag
    */
  def getOutputTypeTag() = u.typeTag[OutputType]
}