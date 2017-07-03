package com.zintow.nlp.standardization;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现文本标准化，将口语化或非标准化的描述语言改成标准化描述语言。
 */
public class Standardization {
    private static Logger logger = LoggerFactory.getLogger(Standardization.class);
    private Map<String, String> words = new HashMap<>();

    protected Standardization(String fileName) {
        readFile("/standardization/" + fileName + ".sta", words);
    }

    private void readFile(String file, Map<String, String> words) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(Standardization.class.getResourceAsStream(file), "utf-8");
            Iterable<CSVRecord> records = CSVFormat.TDF.parse(in);
            for (CSVRecord record : records) {
                words.put(record.get(0), record.get(1));
            }
        } catch (IOException e) {
            logger.error("Failed to read file for {}", e.getMessage());
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Failed to close input stream for {}", e.getMessage());
                }
            }
        }
    }

    public String standardize(String original) {
        for (Map.Entry entry : words.entrySet()) {
            String standardWord = entry.getKey().toString();
            String originalWord = entry.getValue().toString();
            original = original.replaceAll(originalWord, standardWord);
        }
        return original;
    }
}
