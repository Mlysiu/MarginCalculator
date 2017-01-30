package com.mlysiu.margincalculator.json

import com.mlysiu.margincalculator.domain.MarginCalculatorDomain
import MarginCalculatorDomain.PriceTerms

import scalaz.\/

trait SchemaProvider {
  def provideSchema(): \/[String, PriceTerms]
}

trait JsonSchemaProvider extends SchemaProvider {
  override def provideSchema(): \/[String, PriceTerms] =
    Importer.readSchemaJson.leftMap(_.getMessage)
}
