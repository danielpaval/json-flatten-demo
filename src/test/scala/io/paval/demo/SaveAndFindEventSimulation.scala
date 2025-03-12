package io.paval.demo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class SaveAndFindEventSimulation extends AbstractEventSimulation {

  val scenarioBuilder: ScenarioBuilder = scenario("Save & Find Scenario")
    .exec(saveAndFlushRequest)
    .exec(findRequest)

  setUp(
    scenarioBuilder.inject(
      rampUsers(10).during(10),
      constantUsersPerSec(10).during(10).randomized
    )
  )
    .protocols(httpProtocolBuilder)
    .assertions(global.successfulRequests.percent.is(100))

}