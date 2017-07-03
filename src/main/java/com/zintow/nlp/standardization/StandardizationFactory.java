package com.zintow.nlp.standardization;

import com.zintow.nlp.domain.Domain;
import com.zintow.nlp.domain.Domains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StandardizationFactory {
    private static Map<String, Standardization> instances = new HashMap<>();
    private static Domains domains = new Domains();

    public static Standardization createInstance(Map<String, String> conditions) {
        ArrayList<Domain> domainArray = domains.guessDomains(conditions);
        return createInstance(domainArray.size() == 0 ? null : domainArray.get(0));
    }

    public static Standardization createInstance(Domain domain) {
        if (domain == null) {
            return null;
        }
        String domainName = domain.getName();
        if (instances.containsKey(domainName)) {
            return instances.get(domainName);
        } else {
            Standardization standardization = new Standardization(domainName);
            instances.put(domainName, standardization);
            return standardization;
        }
    }

    private StandardizationFactory() {}
}
