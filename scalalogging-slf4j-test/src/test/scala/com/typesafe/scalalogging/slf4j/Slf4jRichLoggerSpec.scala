package com.typesafe.scalalogging.slf4j

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoMatchers
import org.slf4j.{ Logger => Underlying, Marker, MarkerFactory }
import java.util.concurrent.atomic.AtomicReference

object Slf4jRichLoggerSpec extends Specification with Mockito with MockitoMatchers {

  private val Message = "String message!"
  private val Throwable = new Exception("Exception")
  private val Marker = MarkerFactory getMarker "Marker"
  private val Params = List("1", "2", new java.util.Date(3))

  private def mockUnderlying(res: AtomicReference[String]): Underlying = {
    val underlying = mock[Underlying]
    underlying.isErrorEnabled returns true

    underlying.error(anyString) answers { msg => res.set(msg.asInstanceOf[String]) }
    underlying.error(anyString, any[Throwable]) answers { msg => res.set(msg.asInstanceOf[String]) }
    underlying.error(anyString, any[AnyRef], any[AnyRef], any[AnyRef]) answers {
      msg => res.set(msg.asInstanceOf[String])
    }
    underlying.error(any[Marker], anyString) answers {
      (msg, _) => res.set(msg.asInstanceOf[Array[AnyRef]](1).asInstanceOf[String])
    }
    underlying.error(any[Marker], anyString, any[Throwable]) answers {
      (msg, _) => res.set(msg.asInstanceOf[Array[AnyRef]](1).asInstanceOf[String])
    }
    underlying.error(any[Marker], anyString, any[AnyRef], any[AnyRef], any[AnyRef]) answers {
      (msg, _) => res.set(msg.asInstanceOf[Array[AnyRef]](1).asInstanceOf[String])
    }

    underlying
  }

  private def testEnclosingMethod(logger: Logger, errorMsg: AtomicReference[String]) {
    val methodName = "testEnclosingMethod(...)"
    logger.error(Message)
    errorMsg.get() must contain(methodName)
    logger.error(Message, "1", "2", new java.util.Date(3))
    errorMsg.get() must contain(methodName)
    logger.error(Message, Throwable)
    errorMsg.get() must contain(methodName)
    logger.error(Marker, Message)
    errorMsg.get() must contain(methodName)
    logger.error(Marker, Message, Params: _*)
    errorMsg.get() must contain(methodName)
    logger.error(Marker, Message, Throwable)
    errorMsg.get() must contain(methodName)
  }

  // Don't change number of lines, otherwise those tests will break
  "addLoggingContext" should {
    "be able to add line number to message" in {
      val errorMsg = new AtomicReference[String]("")
      val underlying = mockUnderlying(errorMsg)
      val logger = new Logger(underlying) {
        override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
          s"$callerLocation $message"
        }
      }
      val lineNum = 64
      logger.error(Message)
      errorMsg.get() must contain(s":${lineNum + 1}")
      logger.error(Message, "1", "2", new java.util.Date(3))
      errorMsg.get() must contain(s":${lineNum + 3}")
      logger.error(Message, Throwable)
      errorMsg.get() must contain(s":${lineNum + 5}")
      logger.error(Marker, Message)
      errorMsg.get() must contain(s":${lineNum + 7}")
      logger.error(Marker, Message, Params: _*)
      errorMsg.get() must contain(s":${lineNum + 9}")
      logger.error(Marker, Message, Throwable)
      errorMsg.get() must contain(s":${lineNum + 11}")
    }

    "be able to add file name to message" in {
      val errorMsg = new AtomicReference[String]("")
      val underlying = mockUnderlying(errorMsg)
      val logger = new Logger(underlying) {
        override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
          s"$callerLocation $message"
        }
      }
      val fileName = "Slf4jRichLoggerSpec.scala:"
      logger.error(Message)
      errorMsg.get() must contain(fileName)
      logger.error(Message, "1", "2", new java.util.Date(3))
      errorMsg.get() must contain(fileName)
      logger.error(Message, Throwable)
      errorMsg.get() must contain(fileName)
      logger.error(Marker, Message)
      errorMsg.get() must contain(fileName)
      logger.error(Marker, Message, Params: _*)
      errorMsg.get() must contain(fileName)
      logger.error(Marker, Message, Throwable)
      errorMsg.get() must contain(fileName)
    }

    "be able to add module name to message" in {
      val errorMsg = new AtomicReference[String]("")
      val underlying = mockUnderlying(errorMsg)
      val logger = new Logger(underlying) {
        override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
          s"$callerPosition $message"
        }
      }
      val objectName = "Slf4jRichLoggerSpec"
      logger.error(Message)
      errorMsg.get() must contain(objectName)
      logger.error(Message, "1", "2", new java.util.Date(3))
      errorMsg.get() must contain(objectName)
      logger.error(Message, Throwable)
      errorMsg.get() must contain(objectName)
      logger.error(Marker, Message)
      errorMsg.get() must contain(objectName)
      logger.error(Marker, Message, Params: _*)
      errorMsg.get() must contain(objectName)
      logger.error(Marker, Message, Throwable)
      errorMsg.get() must contain(objectName)
    }

    "be able to add function name to message" in {
      val errorMsg = new AtomicReference[String]("")
      val underlying = mockUnderlying(errorMsg)
      val logger = new Logger(underlying) {
        override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
          s"$callerPosition $message"
        }
      }
      testEnclosingMethod(logger, errorMsg)
    }
  }
}
