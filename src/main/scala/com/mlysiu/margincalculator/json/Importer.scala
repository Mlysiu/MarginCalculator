package com.mlysiu.margincalculator.json

import com.mlysiu.margincalculator
import com.mlysiu.margincalculator.domain.MarginCalculatorDomain
import MarginCalculatorDomain.PriceTerms
import spray.json._
import MarginCalculatorJsonProtocol._
import scala.io.Source
import scala.util.Try
import scalaz.\/
import margincalculator._
import MarginCalculatorDomain._

object Importer {
  def readSchemaJson: \/[Throwable, PriceTerms] =
    for {
      schema <- Try(Source.fromResource("schema.json").mkString)
      priceTerms <- Try(schema.parseJson.convertTo[PriceTerms])
    } yield priceTerms
}

