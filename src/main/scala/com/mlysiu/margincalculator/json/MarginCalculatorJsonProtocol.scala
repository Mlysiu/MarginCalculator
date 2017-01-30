package com.mlysiu.margincalculator.json

import com.mlysiu.margincalculator.domain.MarginCalculatorDomain
import MarginCalculatorDomain._
import spray.json._

object MarginCalculatorJsonProtocol extends DefaultJsonProtocol {
  val resLEq = "<=([0-9])+".r
  val resL = "<([0-9])+".r
  val resM = ">([0-9])+".r
  val resMEq = ">=([0-9])+".r
  val resE = "([0-9])+".r

  implicit object MonthsBoundsJsonFormat extends RootJsonFormat[MonthsBounds] {
    def read(value: JsValue) = {
      val monthStr = value.asInstanceOf[JsString].value
      monthStr match {
        case resLEq(x) => LessEqualThan(monthStr.substring(2).toInt)
        case resL(x) => LessThan(monthStr.substring(1).toInt)
        case resM(x) => MoreThan(monthStr.substring(1).toInt)
        case resMEq(x) => MoreEqualThan(monthStr.substring(2).toInt)
        case resE(x) => Equal(monthStr.toInt)
      }
    }

    override def write(obj: MonthsBounds): JsValue =
      obj match {
        case x: LessEqualThan => new JsString(s"<=${x.months}")
        case x: LessThan => new JsString(s"<${x.months}")
        case x: MoreThan => new JsString(s">=${x.months}")
        case x: MoreEqualThan => new JsString(s">${x.months}")
        case x: Equal => new JsString(x.months.toString)
      }
  }

  implicit val reverseLTVFormat = jsonFormat2(ReverseLTV)
  implicit val loanAmountFormat = jsonFormat3(LoanAmount)
  implicit val margeFormat = jsonFormat2(Marge)
  implicit val timePeriodFormat = jsonFormat2(TimePeriod)
  implicit val priceTermsFormat = jsonFormat2(PriceTerms)
}


