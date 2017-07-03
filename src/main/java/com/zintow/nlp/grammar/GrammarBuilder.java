package com.zintow.nlp.grammar;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class GrammarBuilder {
	public static final String SETTINGS_KEY = "settings";
	public static final String FILTERED_KEY = "filtered";
	public static final String RULES_KEY = "rules";
	public static final String MATCHER_KEY = "matcher";
	public static final String SCORE_KEY = "score";
	public static final String SUCCESSORS_KEY = "successors";

    private static Logger logger = LoggerFactory.getLogger(GrammarNode.class);

	public static GrammarNode buildRoot(Map<String, Object> map) {
        logger.info("Build the the grammar root node");
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put(SUCCESSORS_KEY, map);
        return new GrammarNode(
            null, null, 0.0,
            loadSuccessors(rootMap)
        );
	}

    public static GrammarNode build(String key, Map<String, Object> map){
        logger.info("Build the the grammar node for " + key);
        return new GrammarNode(
            key,
            loadMatcher(map, key),
            loadScore(map),
            loadSuccessors(map)
        );
    }

    private static String loadMatcher(Map<String, Object> map, String key){
        String matcher = key;
        if(map != null && map.containsKey(MATCHER_KEY)){
        	matcher = (String)map.get(MATCHER_KEY);
        }
    	return matcher;
    }

    private static double loadScore(Map<String, Object> map){
    	double score = 0;
        if(map != null && map.containsKey(SCORE_KEY)){
        	score = (Double)map.get(SCORE_KEY);
        }
    	return score;
    }

    @SuppressWarnings("unchecked")
	private static List<GrammarNode> loadSuccessors(Map<String, Object> map){
        List<GrammarNode> successors = null;
        if(map != null && map.containsKey(SUCCESSORS_KEY)){
		    Map<String, Object> sucMap = (Map<String, Object>) map.get(SUCCESSORS_KEY);
        	if(sucMap != null){
        		successors =  new ArrayList<>();
        		for (Entry<String, Object> entry : sucMap.entrySet()) {
        			successors.add(GrammarBuilder.build(entry.getKey(), (Map<String, Object>) entry.getValue()));
				}
        	}

        }
    	return successors;
    }

	public static GrammarTree build(String file){
        logger.info("Build the default the grammar tree");
		return build(GrammarTree.class.getResourceAsStream(file));
    }

    @SuppressWarnings("unchecked")
	public static GrammarTree build(InputStream is){
        Yaml yaml = new Yaml();
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>)yaml.load(new InputStreamReader(is, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to build grammar tree for {}", e.getMessage());
        }
        return new GrammarTree((Map<String, Object>)map.get(RULES_KEY), buildSettings(map.get(SETTINGS_KEY)));
    }

    @SuppressWarnings("unchecked")
	private static GrammarSetting buildSettings(Object object) {
        GrammarSetting setting = new GrammarSetting();
		Map<String, Object> filteredMap = (Map<String, Object> ) object;
        List<String> filtered = (ArrayList<String>) filteredMap.get(FILTERED_KEY);
        setting.setFiltered(new HashSet<>(filtered));
        logger.info("Filter is " + filtered);
		return setting;
	}
    
    private GrammarBuilder(){
        logger.info("Init the Grammar Builder");
    }
}
