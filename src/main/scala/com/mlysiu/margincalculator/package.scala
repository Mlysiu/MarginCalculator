package com.mlysiu

import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}
import scalaz.{-\/, \/-, \/}

package object margincalculator {

  /**
    * implicitly convert Try to Disjunction
    */
  implicit def convertTryToDisjunction[T](tryExpression: Try[T]): \/[Throwable, T] =
    tryExpression match {
      case Success(value) => \/-(value)
      case Failure(ex) => -\/(ex)
    }
}
