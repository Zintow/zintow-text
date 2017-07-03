package com.zintow.nlp

import com.zintow.nlp.grammar.GrammarBuilder
import com.zintow.nlp.grammar.GrammarTree
import org.ansj.domain.Term
import spock.lang.Shared
import spock.lang.Specification

class GrammarBuilderSpec extends Specification{

	def "test Tree Builder"() {
		when:
		GrammarBuilder.build("/grammar/phrase/test_phrase_grammar.yml");

		then:
		notThrown(Exception)
	}
}

class GrammarLogicSpec extends Specification{
    @Shared
    GrammarTree tree


	private List<Term> populateTerms(String str){
		assert str != null;
		ArrayList<Term> tmp = new ArrayList<>();
		for(String token : str.split(",")){
			String[] ts = token.split("/");
			tmp.add(new Term(ts[0], 0, ts[1], 0));
		}
		return tmp;
	}

    def setupSpec(){
        tree = GrammarBuilder.build("/grammar/phrase/test_phrase_grammar.yml");
    }

	def "test Integratoin"() {
		when:
    		def solutions = tree.search(populateTerms(text))

		then:
		    solutions != null

		where:
    		text                    || token
    		"结局/n,很/d,圆满/a"      || "结局圆满:ph"
	}
}
