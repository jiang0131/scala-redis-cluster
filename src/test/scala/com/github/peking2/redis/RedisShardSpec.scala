package com.github.peking2.redis

import org.scalatest._

class RedisShardSpec extends FlatSpec with Matchers {
  val config = Config(Seq("127.0.0.1"), Seq(6379))
  val rs = RedisShard(config)

  "md5" should "works" in {
    RedisShard.md5("D2CADB5F-410F-4963-AC0C-2A78534BDF1E") shouldBe "dc4920c26bcc478fbdd2e5bfd0dbe551"
    RedisShard.md5("24680_fc5e038d38a57032085441e7fe7010b0_1332792966") shouldBe "b159366cb50f17bab55013837c92d73a"
  }

  "getNode" should "return correct node" in {
    val node = rs.getNode("12345")
    node.host shouldBe "127.0.0.1"
    node.port shouldBe 6379
  }

  "set" should "works" in {
    val res = rs.set("12345", "1")
    res shouldBe true
  }

  "get" should "works" in {
    val res = rs.get("12345")
    res.get shouldBe ("1")
  }

  "expire" should "works" in {
    val res = rs.expire("12345", 600)
    res shouldBe true
  }

  "ttl" should "return ttl in seconds" in {
    val res = rs.ttl("12345")
    res.isRight shouldBe true
    res.right.get should be <= 600 
  }

  "ttlNotSet" should "return Left[true]" in {
    rs.set("abcde", "1")
    val res = rs.ttl("abcde")
    res.isLeft shouldBe true
    res.left.get shouldBe true
  }

  "ttlNoKey" should "return Left[false]" in {
    val res = rs.ttl("6789")
    res.isLeft shouldBe true
    res.left.get shouldBe false
  }

  "expireAt" should "works" in {
    rs.set("abcde", "1")
    val res = rs.expireAt("abcde", 7258032)
    res shouldBe true
    val resTtl = rs.ttl("12345")
    resTtl.isRight shouldBe true
    resTtl.right.get should be >= 0
  }
}
