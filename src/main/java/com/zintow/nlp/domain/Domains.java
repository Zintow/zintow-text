package com.zintow.nlp.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据conditions判断文本所属的领域。
 */
public class Domains {
    private static final Logger logger = LoggerFactory.getLogger(Domains.class);
    private static final String file = "domain_rules.yml";
    private Map<String, Map> map = new HashMap<>();

    public Domains() {
        readYMlFile(file);
    }

    public ArrayList<Domain> guessDomains(double threshold, Map<String, String> conditions) {
        ArrayList<Domain> guessList = new ArrayList<>();
        HashMap<String, Double> guessMap = new HashMap<>();
        for (Map.Entry domainEntry : map.entrySet()) {
            String domain = (String) domainEntry.getKey();
            double score = 0;
            Map<String, Object> domainRules = (Map<String, Object>) domainEntry.getValue();
            for (Map.Entry conditionEntry : conditions.entrySet()) {
                String conKey = (String) conditionEntry.getKey();
                String conValue = (String) conditionEntry.getValue();
                if (domainRules.containsKey(conKey)) {
                    score += getScore(domainRules, conKey, conValue);
                }
            }
            guessMap.put(domain, score);
        }
        for (Map.Entry entry : guessMap.entrySet()) {
            if ((double) entry.getValue() <= threshold) {
                continue;
            }
            Domain domain = new Domain();
            domain.setName((String) entry.getKey());
            domain.setScore((double) entry.getValue());
            guessList.add(domain);
        }
        Collections.sort(guessList, (Domain o1, Domain o2) -> o1.getScore() - o2.getScore() > 0 ? -1 : 1);
        logger.info(guessList.toString());
        return guessList;
    }

    public ArrayList<Domain> guessDomains(Map<String, String> conditions) {
        return guessDomains(0.0, conditions);
    }

    private double getScore(Map<String, Object> domainRules, String conKey, String conValue) {
        double score = 0;
        Map<String, Double> rules = (Map<String, Double>) domainRules.get(conKey);
        if (rules.containsKey(conValue)) {
            //精确匹配
            score += rules.get(conValue);
        } else {
            //模糊匹配
            double maxScore = 0.0;
            for (Map.Entry rulesEntry : rules.entrySet()) {
                String rulesKey = (String) rulesEntry.getKey();
                double rulesScore = (double) rulesEntry.getValue();
                if (conValue.contains(rulesKey) && rulesScore > maxScore) {
                    maxScore = rulesScore;
                }
            }
            score += maxScore;
        }
        return score;
    }

    protected void readYMlFile(String file) {
        Yaml yaml = new Yaml();
        try {
            Map<String, Object> ymlMap = (Map<String, Object>)yaml.load(
                    new InputStreamReader(Domains.class.getResourceAsStream("/domain/" + file), "utf-8"));
            for (Map.Entry entry : ymlMap.entrySet()) {
                String domain = (String) entry.getKey();
                Map<String, Object> domainRules = (Map<String, Object>) entry.getValue();
                for (Map.Entry kv : domainRules.entrySet()) {
                    String key = (String) kv.getKey();
                    ArrayList<Map<String, Double>> value = (ArrayList<Map<String, Double>>) kv.getValue();
                    Map<String, Double> rules = new HashMap<>();
                    assembleMap(value, rules);
                    domainRules.put(key, rules);
                }
                map.put(domain, domainRules);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Failed to read yml file for {}", e.getMessage());
        }
    }

    private void assembleMap(ArrayList<Map<String, Double>> array, Map<String, Double> map) {
        if (array != null) {
            for (Map<String, Double> m : array) {
                for (Map.Entry e : m.entrySet()) {
                    map.put((String) e.getKey(), (double) e.getValue());
                }
            }
        }
    }
}
