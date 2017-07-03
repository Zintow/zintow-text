package com.zintow.nlp.domain

import spock.lang.Specification

class DomainsTestSpec extends Specification{

    def "test domain"() {
        when:
        def conditions = new HashMap()
        conditions.put("tags", tags)
        conditions.put("others", others)
        def guess = new Domains().guessDomains(conditions)
        println(guess)

        then:
        guess[0].getName() == real

        where:
        tags | others || real
        "测试程序" | "系统" || "test"
        "测试程序" | "其他" || "test"
        "测验程序" | "其他" || "test"
        "测验程序" | "系统" || "test"
        "开源程序" | "系统" || "test"
    }

    def "test domain null"() {
        when:
        def conditions = new HashMap()
        conditions.put("tags", tags)
        conditions.put("others", others)
        def guess = new Domains().guessDomains(conditions)

        then:
        guess[0] == null

        where:
        tags | others
        "java程序" | "其他"
    }
}
