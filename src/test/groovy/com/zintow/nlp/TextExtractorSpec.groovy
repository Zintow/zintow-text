package com.zintow.nlp;

import spock.lang.*

class TextExtractSpec extends Specification{

	def "test token"() {
		def conditions = new HashMap()
		conditions.put("tags", "测试")
		conditions.put("others", "其他")

		expect:
		ExtractorFactory.createInstance(conditions).getToken(text) == token

		where:
		text       || token
		"颜值真高"	||	"颜值:n 真:d 高:a"
		"高冷的人"	||	"高冷:a 的:uj 人:n"
	}

	def "test phrase"() {
		def conditions = new HashMap()
		conditions.put("tags", "测试")
		conditions.put("others", "其他")

		expect:
		ExtractorFactory.createInstance(conditions).getPhrase(text) == token

		where:
		text       || token
		"价格高"		||	"价格高:ph"
		"价格很低"	||	"价格低:ph"
	}
}
