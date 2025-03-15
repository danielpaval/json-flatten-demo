package io.paval.demo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class SaveFindAndReportEventSimulation extends AbstractEventSimulation {

  val scenarioBuilder: ScenarioBuilder = scenario("Save, Find & Report Scenario")
    .exec(saveAndFlushRequest)
    .exec(findRequest)
    .exec(reportRequest)

  setUp(
    scenarioBuilder.inject(
      constantUsersPerSec(20).during(60)
    )
  )
    .protocols(httpProtocolBuilder)
    .assertions(global.successfulRequests.percent.is(100))

}