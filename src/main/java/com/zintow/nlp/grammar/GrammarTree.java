package com.zintow.nlp.grammar;

import org.ansj.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GrammarTree {
    private static Logger logger = LoggerFactory.getLogger(GrammarTree.class);

	private GrammarNode root;
	private GrammarSetting settings;

   GrammarTree(Map<String, Object> treeNodes, GrammarSetting settings){
        this.root = GrammarBuilder.buildRoot(treeNodes);
        this.settings = settings;
    }
	
    public List<Solution> search(List<Term> tokens){
        ArrayList<Solution> results = new ArrayList<>();
        Solution sol = new Solution(settings);
        while (!tokens.isEmpty()) {
            ArrayList<Solution> subSol = new ArrayList<>();
            int depth = root.dfs(tokens, 0, sol, subSol);
            results.addAll(subSol);
            tokens = tokens.subList( depth == 0 ? 1 : depth , tokens.size() );
        }
        results.sort((Solution a, Solution b) ->
                (int) (1000 * (b.getScore() - a.getScore())));
    	logger.info("Search {} through tree and get {}", tokens, results);
        return results;
    }

    public String search(String tokens){
        ArrayList<String> results = new ArrayList<>();
        root.dfs(tokens, results);
        results.sort((String a, String b) ->
                (int) (1000 * (Double.parseDouble(b.split("_")[1]) - Double.parseDouble(a.split("_")[1]))));
        return results.isEmpty() ? "" : results.get(0).split("_")[0].trim();
    }

}
