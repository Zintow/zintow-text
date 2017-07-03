package com.zintow.nlp;

import com.zintow.nlp.grammar.GrammarBuilder;
import com.zintow.nlp.grammar.GrammarTree;
import com.zintow.nlp.grammar.Solution;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Extractor{
    private static final Logger logger = LoggerFactory.getLogger(Extractor.class);
    private GrammarTree phraseGrammarTree;

    protected Extractor() {}

    protected Extractor(String domainName) {
        this.phraseGrammarTree = GrammarBuilder.build("/grammar/phrase/" + domainName +"_phrase_grammar.yml");
        updateDic("/dictionary/" + domainName + ".dic");
    }

    public String getToken(String str){
        Result rs = DicAnalysis.parse(str);
        StringBuilder sb = new StringBuilder();
        for (Term t:rs) {
            if (null!=t.getNatureStr() && !"null".equals(t.getNatureStr())){
                sb.append(t.getName()).append(":").append(t.getNatureStr()).append(" ");
            }
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return sb.toString().trim();
        }
    }

    public String getPhrase(String str) {
        if (phraseGrammarTree == null) {
            return "";
        }
        List<Solution> solutions = phraseGrammarTree.search(DicAnalysis.parse(str).getTerms());
        StringBuilder buf = new StringBuilder();
        for (Solution s : solutions) {
            buf.append(s.output()).append(" ");
        }
        return buf.toString().trim();
    }

    private void updateDic(String dicFile){
        InputStreamReader in = null;
        try{
            in = new InputStreamReader(Extractor.class.getResourceAsStream(dicFile), "utf-8");
            Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
            for (CSVRecord record : records) {
                DicLibrary.insert(DicLibrary.DEFAULT,record.get(0), record.get(1), Integer.valueOf(record.get(2)));
            }
        }catch (IOException e){
            logger.error("Update Dictionary Error for {}", e.getMessage());
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Failed to close input stream for {}", e.getMessage());
                }
            }
        }
    }
}
