package com.peking2.redis

case class Host(id:Int, host:String, port:Int)

class Config private (hosts: Seq[String], ports: Seq[Int]) {
    private val step = Config.MAX_NODE/(hosts.length*ports.length)

    private val servers = for(i<-hosts; j<-ports) yield (i,j)
    private val ids = 0 until Config.MAX_NODE by step

    val hostList = servers.zip(ids).map(_ match {
      case ((host, port), id) => Host(id, host, port)
    })
}

object Config {
  private[redis] val MAX_NODE = 1024

  def apply(hosts: Seq[String], ports: Seq[Int]): Config= new Config(hosts, ports)
}