package io.paval.demo

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class SaveEventSimulation extends AbstractEventSimulation {

  val saveScenarioBuilder: ScenarioBuilder = scenario("Save Scenario")
    .exec(saveRequest)

  val saveAndFlushScenarioBuilder: ScenarioBuilder = scenario("Save + Flush Scenario")
    .exec(saveAndFlushRequest)

  setUp(
    /*saveScenarioBuilder.inject(
      //rampUsers(100).during(10),
      constantUsersPerSec(20).during(60)
    )*/
    saveAndFlushScenarioBuilder.inject(
      //rampUsers(100).during(10),
      constantUsersPerSec(20).during(60)
    )
  )
    .protocols(httpProtocolBuilder)
    .assertions(global.successfulRequests.percent.is(100))

}