package io.paval.demo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class SaveEventSimulation extends AbstractEventSimulation {

  val scenarioBuilder: ScenarioBuilder = scenario("Save Scenario")
    .exec(saveRequest)

  val flushScenarioBuilder: ScenarioBuilder = scenario("Save & Flush Scenario")
    .exec(saveAndFlushRequest)

  setUp(
    scenarioBuilder.inject(
      rampUsers(10).during(10),
      constantUsersPerSec(10).during(10).randomized
    ).andThen(
      flushScenarioBuilder.inject(
        rampUsers(10).during(10),
        constantUsersPerSec(10).during(10).randomized
      )
    )
  )
    .protocols(httpProtocolBuilder)
    .assertions(global.successfulRequests.percent.is(100))

}