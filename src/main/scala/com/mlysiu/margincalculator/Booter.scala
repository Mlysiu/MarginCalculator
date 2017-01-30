package com.mlysiu.margincalculator

import com.mlysiu.margincalculator.domain.MarginCalculatorDomain.MarginCalcInputParameters
import com.mlysiu.margincalculator.json.JsonSchemaProvider

import scala.language.implicitConversions
import scala.util.Try
import scalaz.{-\/, \/, \/-}

object Booter {
  def main(args: Array[String]) {
    println(s"Launching application with ${args.toList.toString} input parameters")
    val calc = new MarginCalculator with JsonSchemaProvider
    sanityCheck(args).flatMap(calc.calculate) match {
      case \/-(loanMargin) =>
        println(s"Loan Margin = $loanMargin")
      case -\/(errMsg) =>
        println(errMsg)
        System.exit(1)
    }
  }

  private def sanityCheck(args: Array[String]): \/[String, (MarginCalcInputParameters)] =
    for {
      _ <- Try(require(args.length == 3, "Not Enough Input Parameters")).leftMap(_.getMessage)
      months <- Try(args(0).toInt).leftMap(_ => "months has to be a number")
      loanAmount <- Try(args(1).toInt).leftMap(_ => "loanAmount has to be a number")
      deductible <- Try(args(2).toInt).leftMap(_ => "deductible has to be a number")
    } yield MarginCalcInputParameters(months, loanAmount, deductible)

}


