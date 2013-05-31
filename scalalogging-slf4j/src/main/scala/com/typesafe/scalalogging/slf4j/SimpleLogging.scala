package com.typesafe.scalalogging.slf4j

import org.slf4j.{ Marker, LoggerFactory }
import scala.Predef._
import language.experimental.macros

trait SimpleLogging {
  lazy val underlying = LoggerFactory getLogger getClass.getName

  // Error

  def error(message: String): Unit = macro LoggerMacros.errorMessage

  def error(message: String, params: AnyRef*): Unit = macro LoggerMacros.errorMessageParams

  def error(message: String, t: Throwable): Unit = macro LoggerMacros.errorMessageThrowable

  def error(marker: Marker, message: String): Unit = macro LoggerMacros.errorMarkerMessage

  def error(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.errorMarkerMessageParams

  def error(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.errorMarkerMessageThrowable

  // Warn

  def warn(message: String): Unit = macro LoggerMacros.warnMessage

  def warn(message: String, params: AnyRef*): Unit = macro LoggerMacros.warnMessageParams

  def warn(message: String, t: Throwable): Unit = macro LoggerMacros.warnMessageThrowable

  def warn(marker: Marker, message: String): Unit = macro LoggerMacros.warnMarkerMessage

  def warn(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.warnMarkerMessageParams

  def warn(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.warnMarkerMessageThrowable

  // Info

  def info(message: String): Unit = macro LoggerMacros.infoMessage

  def info(message: String, params: AnyRef*): Unit = macro LoggerMacros.infoMessageParams

  def info(message: String, t: Throwable): Unit = macro LoggerMacros.infoMessageThrowable

  def info(marker: Marker, message: String): Unit = macro LoggerMacros.infoMarkerMessage

  def info(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.infoMarkerMessageParams

  def info(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.infoMarkerMessageThrowable

  // Debug

  def debug(message: String): Unit = macro LoggerMacros.debugMessage

  def debug(message: String, params: AnyRef*): Unit = macro LoggerMacros.debugMessageParams

  def debug(message: String, t: Throwable): Unit = macro LoggerMacros.debugMessageThrowable

  def debug(marker: Marker, message: String): Unit = macro LoggerMacros.debugMarkerMessage

  def debug(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.debugMarkerMessageParams

  def debug(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.debugMarkerMessageThrowable

  // Trace

  def trace(message: String): Unit = macro LoggerMacros.traceMessage

  def trace(message: String, params: AnyRef*): Unit = macro LoggerMacros.traceMessageParams

  def trace(message: String, t: Throwable): Unit = macro LoggerMacros.traceMessageThrowable

  def trace(marker: Marker, message: String): Unit = macro LoggerMacros.traceMarkerMessage

  def trace(marker: Marker, message: String, params: AnyRef*): Unit = macro LoggerMacros.traceMarkerMessageParams

  def trace(marker: Marker, message: String, t: Throwable): Unit = macro LoggerMacros.traceMarkerMessageThrowable
}
