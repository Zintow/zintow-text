package com.zintow.nlp.grammar;

import org.ansj.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrammarNode {
    private static Logger logger = LoggerFactory.getLogger(GrammarNode.class);
    private static Set<String> filterSet = new HashSet<String>();
    private String key;
    private String matcher;
    private Double score;
    private List<GrammarNode> successors;
    private Pattern p;
    private Matcher m;

    static {
        filterSet.add("n");
        filterSet.add("nr");
    }

    public GrammarNode(
    String key,
    String matcher,
    Double score,
    List<GrammarNode> successors) {
        super();
        this.key = key;
        this.matcher = matcher;
        this.successors = successors;
        this.score = score;
    }

    public String getKey() {
        return key;
    }
    public String getMatcher() {
        return matcher;
    }
    public List<GrammarNode> getSuccessors() {
        return successors;
    }
    public Double getScore() {
        return score;
    }

    protected int dfs(List<Term> tokens, int index, Solution solution, List<Solution> results) {
        assert tokens != null;
        int depth = 0;
        if (successors != null && index < tokens.size()) {
            Term token = tokens.get(index);
            boolean exist = false;
            for (GrammarNode node : successors) {
                if (node.isMatch(token)) {
                	exist = true;
                    depth++;
                	logger.debug("Match {} for {}", token,  node);
                    solution.append(token, node.score);
                    addSolution(solution, results, node);
                    depth += node.dfs(tokens, index + 1, solution.copy(), results);
                }
            }
            if (!exist) {
                solution.clear();
            }
        }
        return depth;
    }

    protected void dfs(String tokens, List results) {
        assert tokens != null;
        if (successors != null) {
            for (GrammarNode node : successors) {
                if (node.isMatch(tokens)) {
                    logger.debug("Match {} for {}", tokens,  node);
                    tokens = tokens.replaceAll(node.matcher, "");
                    results.add(tokens.replace("_","")+"_"+node.score);
                    node.dfs(tokens, results);
                }
            }
        }
    }

    private void addSolution(Solution solution, List<Solution> results, GrammarNode node) {
        if(node.isGrammarGap()){
            results.add(solution);
            solution.clear();
        }
    }

	private boolean isGrammarGap() {
		return this.score > 0;
	}

	private boolean isMatch(Object token) {
        logger.debug("Test {} to {}", token, this.matcher);
        p = Pattern.compile(this.matcher);
        m = p.matcher(token.toString());
        boolean show = true;
        String[] tokenArr = token.toString().split("/");
        if (tokenArr.length == 2 && filterSet.contains(tokenArr[1]) && tokenArr[0].length() == 1) {
           show = false;
        }
		return m.find() && show;
	}
}
