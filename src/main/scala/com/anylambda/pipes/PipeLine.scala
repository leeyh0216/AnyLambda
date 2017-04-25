package com.anylambda.pipes

import com.anylambda.application.ApplicationContext
import com.anylambda.util.Logging

import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.{universe => u}
/**
  * Pipe Class들을 연결하여 연산을 수행 후 결과를 돌려주는 Pipeline Class
  * @author leeyh0216
  */
class PipeLine(appCtx : ApplicationContext) extends Logging{
  val ctx = appCtx
  val pipeList = new ListBuffer[Pipe[_,_]]()

  /**
    * pipeline의 맨 뒤에 pipe를 추가한다.
    * @param pipe Pipe를 상속한 클래스
    */
  def addPipe(pipe : Pipe[_,_]) = {
    logger.info("ADD Pipe : ({} => {})",pipe.getInputTypeTag().toString() : Any, pipe.getOutputTypeTag().toString() : Any)
    pipeList.append(pipe)
  }

  private def cast[A](a : Any ): A = a.asInstanceOf[A]

  def process[InputType : u.TypeTag](data : InputType) : (_ , _) = {
    val pipeIterator = pipeList.toList.iterator

    //입력 데이터와 입력 데이터 타입을 임시 변수로 저장한다.
    var lastInputData : Any = data
    var lastInputType : u.TypeTag[_] = u.typeTag[InputType]

    logger.info("Input Data Type : {}",lastInputType : Any)

    //파이프를 끝까지 순회하며 이전 Output Type과 현 Pipe의 Input Type이 일치한다면
    //lastInputData와 lastInputType을 변경한다.
    while(pipeIterator.hasNext){
      //현재 파이프를 가져오고, 해당 파이프의 Input Type 확인
      val currentPipe = pipeIterator.next()
      val acceptableType = currentPipe.getInputTypeTag()
      logger.info("Current Pipe Acceptable Type : {}", acceptableType : Any)

      //데이터 연산
      lastInputData =
        if(lastInputType.tpe =:= acceptableType.tpe)
          currentPipe.process(ctx, cast(lastInputData))
        else
          lastInputData
      //타입 재지정
      lastInputType =
        if(lastInputType.tpe =:= acceptableType.tpe) {
          logger.info("Match   !!! ")
          currentPipe.getOutputTypeTag()
        }
        else
          lastInputType

      logger.info("Next Pipe's Input Type must {}",lastInputType : Any)
    }
    (lastInputType,lastInputData)
  }
}
