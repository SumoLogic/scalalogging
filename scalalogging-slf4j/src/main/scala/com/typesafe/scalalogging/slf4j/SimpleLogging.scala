package com.typesafe.scalalogging.slf4j

import org.slf4j.LoggerFactory
import scala.Predef._
import language.experimental.macros

trait SimpleLogging {
  lazy val underlying = LoggerFactory getLogger getClass.getName

  def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = message

  // Error

  def error(message: String): Unit = macro LoggerMacros.errorMessage

  def error(message: String, params: AnyRef*): Unit = macro LoggerMacros.errorMessageParams

  def error(message: String, t: Throwable): Unit = macro LoggerMacros.errorMessageThrowable

  // Warn

  def warn(message: String): Unit = macro LoggerMacros.warnMessage

  def warn(message: String, params: AnyRef*): Unit = macro LoggerMacros.warnMessageParams

  def warn(message: String, t: Throwable): Unit = macro LoggerMacros.warnMessageThrowable

  // Info

  def info(message: String): Unit = macro LoggerMacros.infoMessage

  def info(message: String, params: AnyRef*): Unit = macro LoggerMacros.infoMessageParams

  def info(message: String, t: Throwable): Unit = macro LoggerMacros.infoMessageThrowable

  // Debug

  def debug(message: String): Unit = macro LoggerMacros.debugMessage

  def debug(message: String, params: AnyRef*): Unit = macro LoggerMacros.debugMessageParams

  def debug(message: String, t: Throwable): Unit = macro LoggerMacros.debugMessageThrowable

  // Trace

  def trace(message: String): Unit = macro LoggerMacros.traceMessage

  def trace(message: String, params: AnyRef*): Unit = macro LoggerMacros.traceMessageParams

  def trace(message: String, t: Throwable): Unit = macro LoggerMacros.traceMessageThrowable
}

trait SimpleRichLogging extends SimpleLogging {
  override def addLoggingContext(message: String, callerPosition: String, callerLocation: String): String = {
    s"[pos=$callerPosition $callerLocation] $message"
  }
}
