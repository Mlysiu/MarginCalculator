package com.mlysiu.margincalculator

import com.mlysiu.margincalculator.domain.MarginCalculatorDomain
import MarginCalculatorDomain._
import com.mlysiu.margincalculator.json.SchemaProvider

import scalaz.{-\/, \/, \/-}

class MarginCalculator {
  self: SchemaProvider =>

  def calculate(params: MarginCalcInputParameters): \/[String, Float] =
    for {
      schema <- provideSchema()
      loanMarge <- calculateMargin(params, schema)
    } yield loanMarge


  private def calculateMargin(params: MarginCalcInputParameters, schema: PriceTerms): \/[String, Float] = {
    val MarginCalcInputParameters(months, loanAmount, deductible) = params
    val reverseLtv = (deductible * 100) / loanAmount

    println(s"Calculation margin with LTV = $reverseLtv, loanAmount = $loanAmount and for $months months")
    //TODO: pmlynarz: Logging?
    val timePeriods = schema.timePeriods.filter(_.months.eval(months))
    val marges = (tp: TimePeriod) => tp.marges.filter(mar => evalReverseLTV(mar.reverseLTV, reverseLtv))
    val loans = (m: Marge) => m.loanAmounts.filter(loan => evalLoanAmount(loan, loanAmount))
    for {
    //TODO: pmlynarz: Unify calls below?
      timePeriod <- if (timePeriods.size == 1) \/-(timePeriods.head) else -\/(s"Time periods found (should be 1): $timePeriods")
      marge <- if (marges(timePeriod).size == 1) \/-(marges(timePeriod).head) else -\/(s"Marges found (should be 1): ${marges(timePeriod)}")
      loanMarge <- if (loans(marge).size == 1) \/-(loans(marge).head) else -\/(s"Loans found (should be 1): ${loans(marge)}")
    } yield loanMarge.value
  }

  private def evalReverseLTV(schemaReverseLTV: ReverseLTV, reverseLtv: Int) =
    if (reverseLtv >= schemaReverseLTV.from && reverseLtv < schemaReverseLTV.to)
      true
    else
      false

  private def evalLoanAmount(schemaLoanAmount: LoanAmount, loanAmount: Int) = schemaLoanAmount.to match {
    case Some(to) => if (loanAmount >= schemaLoanAmount.from && loanAmount <= to) true else false
    case None => if (loanAmount >= schemaLoanAmount.from) true else false
  }

}
