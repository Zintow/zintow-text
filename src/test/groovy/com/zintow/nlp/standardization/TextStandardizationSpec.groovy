package com.zintow.nlp.standardization

import com.zintow.nlp.domain.Domains
import spock.lang.Specification

class TextStandardSpec extends Specification{

    def "test standardization"() {
        when:
        def conditions = new HashMap()
        conditions.put("tags", "测试")
        conditions.put("others", "其他")
        def result = StandardizationFactory.createInstance(conditions).standardize(original)

        then:
        result == standard

        where:
        original || standard
        "价格比较合适,价格便宜,价格还可以,价格非常给力,性价比很高,性价比不错" || "价格低,价格低,价格低,价格低,价格低,价格低"
        "价格真的不合理,价格非常不合适,价格太不给力,价格贵,性价比太低,性价比不高" || "价格高,价格高,价格高,价格高,价格高,价格高"
    }
}