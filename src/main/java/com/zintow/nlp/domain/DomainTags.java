package com.zintow.nlp.domain;

/**
 * 根据text包含的关键词判断领域标签。
 */
public class DomainTags extends Domains {
    private static final String file = "domain_tag_rules.yml";
    public DomainTags() {
        readYMlFile(file);
    }
}
