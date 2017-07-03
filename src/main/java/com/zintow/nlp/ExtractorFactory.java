package com.zintow.nlp;

import com.zintow.nlp.domain.Domain;
import com.zintow.nlp.domain.Domains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExtractorFactory {
    private static Map<String, Extractor> instances = new HashMap<>();
    private static Domains domains = new Domains();

    public static Extractor createInstance(Map<String, String> conditions) {
        ArrayList<Domain> domainArray = domains.guessDomains(conditions);
        return createInstance(domainArray.size() == 0 ? null : domainArray.get(0));
    }

    public static Extractor createInstance(Domain domain) {
        if (domain == null) {
            if (instances.containsKey(null)) {
                return instances.get(null);
            } else {
                Extractor textExtractor = new Extractor();
                instances.put(null, textExtractor);
                return textExtractor;
            }
        }
        String domainName = domain.getName();
        if (instances.containsKey(domainName)) {
            return instances.get(domainName);
        } else {
            Extractor textExtractor = new Extractor(domainName);
            instances.put(domainName, textExtractor);
            return textExtractor;
        }
    }
}
