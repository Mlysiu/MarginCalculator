package com.mlysiu.margincalculator.domain

/**
  * Domain type class that stores all domain objects
  */
object MarginCalculatorDomain {

  case class PriceTerms(bankName: String, timePeriods: List[TimePeriod])

  case class TimePeriod(months: MonthsBounds, marges: List[Marge])

  case class Marge(reverseLTV: ReverseLTV, loanAmounts: List[LoanAmount])

  case class ReverseLTV(from: Int, to: Int) {
    require(from >= 0, s"LTV 'from' value must be >= than 0, was $from")
    require(to <= 100, s"LTV 'to' value must be <= than 100, was $from")
  }

  case class LoanAmount(from: Int, to: Option[Int], value: Float)

  sealed trait MonthsBounds {
    val months: Int

    def eval(m: Int): Boolean
  }

  case class LessThan(months: Int) extends MonthsBounds {
    override def eval(m: Int): Boolean = if (m < months) true else false
  }

  case class LessEqualThan(months: Int) extends MonthsBounds {
    override def eval(m: Int): Boolean = if (m <= months) true else false
  }

  case class Equal(months: Int) extends MonthsBounds {
    override def eval(m: Int): Boolean = if (m == months) true else false
  }

  case class MoreThan(months: Int) extends MonthsBounds {
    override def eval(m: Int): Boolean = if (m > months) true else false
  }

  case class MoreEqualThan(months: Int) extends MonthsBounds {
    override def eval(m: Int): Boolean = if (m >= months) true else false
  }

  case class MarginCalcInputParameters(months: Int, loanAmount: Int, deductible: Int)
}
