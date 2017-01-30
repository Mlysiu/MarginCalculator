package com.mlysiu

import scala.util.{Failure, Success, Try}
import scalaz.{-\/, \/-, \/}

package object margincalculator {

  implicit def convertTryToDisjunction[T](tryExpression: Try[T]): \/[Throwable, T] =
    tryExpression match {
      case Success(value) => \/-(value)
      case Failure(ex) => -\/(ex)
    }
}
