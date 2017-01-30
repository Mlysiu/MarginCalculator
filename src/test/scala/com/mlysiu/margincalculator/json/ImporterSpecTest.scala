package com.mlysiu.margincalculator.json

import com.mlysiu.margincalculator.domain.MarginCalculatorDomain
import MarginCalculatorDomain._
import com.mlysiu.margincalculator.MarginCalculator
import org.scalatest.{Matchers, WordSpec}

import scalaz.{-\/, \/, \/-}


class ImporterSpecTest extends WordSpec with Matchers {
    "find the correct loan marge for schema loan" in {
      trait MockSchemaProvider extends SchemaProvider {
        override def provideSchema(): \/[String, PriceTerms] = \/-(
          PriceTerms("TEST_BANK", List(
            TimePeriod(
              LessEqualThan(12),
              List(
                Marge(ReverseLTV(0, 100), List(
                  LoanAmount(0, None, 2f)
                ))
              )
            )
          ))
        )
      }

      val calc = new MarginCalculator with MockSchemaProvider
      val res = calc.calculate(MarginCalcInputParameters(10, 50000, 10000))

      res match {
        case \/-(loanMarge) => loanMarge should be (2f)
        case -\/(msg) => fail(msg)
      }
    }

  "find the correct loan marge when there are more than 1 merge defined in schema" in {
    trait MockSchemaProvider extends SchemaProvider {
      override def provideSchema(): \/[String, PriceTerms] = \/-(
        PriceTerms("TEST_BANK", List(
          TimePeriod(
            LessEqualThan(12),
            List(
              Marge(ReverseLTV(0, 50), List(
                LoanAmount(0, None, 4f)
              )),
              Marge(ReverseLTV(50, 100), List(
                LoanAmount(0, None, 1.2f)
              ))
            )
          )
        ))
      )
    }

    val calc = new MarginCalculator with MockSchemaProvider
    val res = calc.calculate(MarginCalcInputParameters(10, 50000, 40000))

    res match {
      case \/-(loanMarge) => loanMarge should be (1.2f)
      case -\/(msg) => fail(msg)
    }
  }

  "find the correct loan marge when there are more than 1 time periods defined in schema" in {
    trait MockSchemaProvider extends SchemaProvider {
      override def provideSchema(): \/[String, PriceTerms] = \/-(
        PriceTerms("TEST_BANK", List(
          TimePeriod(
            LessEqualThan(12),
            List(
              Marge(ReverseLTV(0, 50), List(
                LoanAmount(0, None, 4f)
              )),
              Marge(ReverseLTV(50, 100), List(
                LoanAmount(0, None, 3f)
              ))
            )
          ),
          TimePeriod(
            MoreThan(12),
            List(
              Marge(ReverseLTV(0, 50), List(
                LoanAmount(0, None, 2f)
              )),
              Marge(ReverseLTV(50, 100), List(
                LoanAmount(0, None, 1.9f)
              ))
            )
          )
        ))
      )
    }

    val calc = new MarginCalculator with MockSchemaProvider
    val res = calc.calculate(MarginCalcInputParameters(20, 50000, 40000))

    res match {
      case \/-(loanMarge) => loanMarge should be (1.9f)
      case -\/(msg) => fail(msg)
    }
  }

  //TODO: pmlynarz: More tests
}
