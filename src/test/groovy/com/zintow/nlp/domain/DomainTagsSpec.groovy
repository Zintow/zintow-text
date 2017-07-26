package com.zintow.nlp.domain

import spock.lang.Specification

class DomainTagsTestSpec extends Specification{

    def "test domain tag"() {
        when:
        def conditions = new HashMap()
        conditions.put("text", text)
        def guess = new DomainTags().guessDomains(conditions)
        println(guess)

        then:
        guess[0].getName() == real

        where:
        text || real
        "川菜" || "美食"
        "食物" || "美食"
        "骨科" || "医学"
        "癌症" || "医学"
        "武器" || "军事"
        "战争" || "军事"
        "法律" || "法律"
        "律师" || "法律"
        "足球" || "体育"
        "篮球" || "体育"
    }
}
