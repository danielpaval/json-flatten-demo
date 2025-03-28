package io.paval.demo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.paval.demo.util.EventGenerator

class AbstractEventSimulation extends Simulation {

  val httpProtocolBuilder: HttpProtocolBuilder = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val objectMapper: ObjectMapper = new ObjectMapper()
    .registerModule(new JavaTimeModule)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL);

  def createSaveRequest(flush: Boolean): ChainBuilder = exec(
    http(if (flush) "Save & Flush Event" else "Save Event")
      .post("/api/events")
      .queryParam("flush", flush)
      .body(StringBody(objectMapper.writeValueAsString(EventGenerator.generateOrder()))).asJson
      .check(status.is(201))
      .check(header("X-Event-Id").saveAs("eventId"))
  )

  val saveRequest: ChainBuilder = exec(createSaveRequest(false))
  val saveAndFlushRequest: ChainBuilder = exec(createSaveRequest(true))

  val findRequest: ChainBuilder = exec(
    http("Find Event")
      .get("/api/events/#{eventId}")
      .check(status.is(200))
  )

  val reportRequest: ChainBuilder = exec(
    http("Get Frequent Customer Report")
      .post("/api/reports")
      .queryParam("type", "FREQUENT_CUSTOMER_REPORT")
      .check(status.is(200))
  )

}