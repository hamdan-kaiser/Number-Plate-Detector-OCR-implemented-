package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.handler.ScrawlWords;

public class StoppingParser {

    public static StoppingParser instance = null;
    public static Set<String> finalFilteredSet = new LinkedHashSet<String>();
    public static List<String> finalFilterListWithDuplicates = new ArrayList<String>();
    Context ctx;
    private ScrawlWords scrawlWords;

    protected StoppingParser(Context context) {
        ctx = context;
        scrawlWords = ScrawlWords.getInstance(ctx);
    }

    public static StoppingParser getInstance(Context context) {
        if (instance == null) {
            instance = new StoppingParser(context);
        }
        return instance;
    }

    public static List<String> getFinalFilterListWithDuplicates() {
        return finalFilterListWithDuplicates;
    }

    public void finalData(StringBuilder finalFilteredData, String outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), Constants.UTF_8));
            writer.write(finalFilteredData + " ");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getWords(String data) {
        String[] words = data.split(Constants.SPACE);
        if (words.length == 1) {
            words = data.split(Constants.COMMA);
        }
        return words;
    }

    private String filterStoppings(String data) {
        String[] words = getWords(data);
        StringBuilder filteredWords = new StringBuilder();
        try {
            if (words.length > 0) {
                for (String word : words) {
                    word = ScrawlWords.getOnlyStrings(word);
                    scrawlWords.filterStopingWord(word.trim(), filteredWords);
                }
            } else {
                data = ScrawlWords.getOnlyStrings(data);
                scrawlWords.filterStopingWord(data.trim(), filteredWords);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredWords.toString();
    }

    public String filterStoppingWordsKeepDuplicates(String data) {
        String filteredWords = filterStoppings(data);
        if (!filteredWords.isEmpty()) {
            finalFilterListWithDuplicates.add(filteredWords.toLowerCase());
        }
        return filteredWords;
    }

    public String filterStoppingWords(String data) {
        String filteredWords = filterStoppings(data);
        finalFilteredSet.add(filteredWords.toLowerCase());
        return filteredWords;
    }

    public Set<String> getResultSet() {
        return finalFilteredSet;
    }

    public void reset() {
        finalFilteredSet = new LinkedHashSet<String>();
        finalFilterListWithDuplicates = new ArrayList<String>();
    }

}
