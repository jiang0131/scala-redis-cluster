package com.github.peking2.redis

import org.apache.commons.codec.digest.DigestUtils
import scala.concurrent.Await
import scala.concurrent.duration._
import scredis._

case class Node(id:Int, host:String, port:Int, redis:Redis)

class RedisShard private (config: Config) {
  private val nodes = config.hostList.map(i=> Node(i.id, i.host, i.port, Redis(i.host, i.port)))

  def close:Unit = {
    for(i<- nodes) i.redis.quit()
  }

  def getNode(key:String):Node={
    val id = RedisShard.getNodeId(key)
    nodes.toList.filter(_.id >= id) match {
      case head::_ => head
      case _ => nodes(0)
    }
  }

  def get(key: String):Option[String] = {
    val f = getNode(key).redis.get(key)
    Await.result(f, 10 second)
  }

  def set(key: String, value: String):Boolean = {
    val f = getNode(key).redis.set(key, value)
    Await.result(f, 10 second)
  }

  def del(key: String):Long = {
    val f = getNode(key).redis.del(key)
    Await.result(f, 10 second)
  }

  def hGetAll(key: String):Option[Map[String,String]] = {
    val f = getNode(key).redis.hGetAll(key)
    Await.result(f, 10 second)
  }

  def zAdd(key: String, value:String, score:Int):Boolean = {
    val f = getNode(key).redis.zAdd(key, value, score)
    Await.result(f, 10 second)
  }

  def zRange(key: String, start: Long, stop: Long) = {
    val f = getNode(key).redis.zRange(key, start, stop)
    Await.result(f, 10 second).toVector
  }
}

object RedisShard {
  def md5(key:String) = DigestUtils.md5Hex(key)
  def getNodeId(key:String) = Integer.parseInt(md5(key) takeRight 3, 16) % Config.MAX_NODE

  def apply(config: Config):RedisShard = new RedisShard(config)
}
