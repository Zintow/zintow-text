package com.zintow.nlp.grammar;

import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;

public class Solution {
	private List<Term> tokens;
	private GrammarSetting settings;

    private double score;
    private String str;
    private String explainStr;

	public Solution(){
        score = 0.0;
		tokens = new ArrayList<>();
	}

	private Solution(List<Term> tokens, GrammarSetting settings){
		this.tokens = tokens;
		this.settings = settings;
	}

	public Solution(GrammarSetting settings) {
		this();
		this.settings = settings;
	}

	public void append(Term token, double score) {
		this.tokens.add(token);
        this.score = score;

        StringBuilder buf = new StringBuilder();
        for(Term t : this.tokens){
            if(allowToDisplay(t)){
                buf.append(t.getName());
            }
        }
        str = buf.toString() + ":ph";
        explainStr = str + ": " + tokens.toString() + ": " + score;

	}

	public Solution copy(){
		return new Solution(
			this.tokens.subList(0, this.tokens.size()),
			this.settings
			);

	}

    private boolean allowToDisplay(Term token){
        return settings.getFiltered().contains(token.getNatureStr()) ||
				settings.getFiltered().contains(token.getName());
    }

    public String output(){
        return this.str;
    }

    @Override
	public String toString(){
        return this.explainStr;
    }

	public double getScore() {
		return score;
	}
	
	public void clear() {
		this.tokens.clear();
	}

}
