import io.gatling.core.Predef.*
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class EventSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val postRequest: ChainBuilder = exec(
    http("Post Event")
      .post("/api/events")
      .body(StringBody(
        """{
          "data": {
            "name": "John Doe",
            "age": 30,
            "address": {
              "street": "123 Main St",
              "city": "Anytown",
              "country": {
                "name": "CountryName",
                "code": "CN"
              }
            },
            "contact": {
              "email": "john.doe@example.com",
              "phone": "123-456-7890"
            },
            "createdAt": "2023-10-01T12:00:00Z"
          }
        }"""
      )).asJson
      .check(status.is(201))
      .check(jsonPath("$.id").saveAs("eventId"))
  )

  val getRequest: ChainBuilder = exec(
    http("Get Event")
      .get("/api/events/#{eventId}")
      .check(status.is(200))
  )

  val scenarioBuilder: ScenarioBuilder = scenario("Post and Get Scenario")
    .exec(postRequest)
    .pause(1)
    .exec(getRequest)

  setUp(scenarioBuilder.inject(atOnceUsers(10)))
    .protocols(httpProtocolBuilder)
    .assertions(global.successfulRequests.percent.is(100))
  
}