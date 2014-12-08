package com.peking2.redis

import org.scalatest._

class ConfigSpec extends FlatSpec with Matchers {
  "Config" should "return length" in {
    val config = Config((1 to 8).map(i=>s"redis$i.snc1"), (6379 to 6394))
    config.hostList.length shouldBe 128
  }
}
