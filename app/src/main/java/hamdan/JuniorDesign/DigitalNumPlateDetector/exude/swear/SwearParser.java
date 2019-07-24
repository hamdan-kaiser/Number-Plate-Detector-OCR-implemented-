package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.swear;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.handler.ScrawlWords;

public class SwearParser {

    static String swearBySpace = null;
    private static SwearParser instance = null;
    private static Set<String> swearWords = new LinkedHashSet<String>();
    Context ctx;
    Pattern Spacepattern = Pattern.compile("\\s");
    private ScrawlWords scrawlWords;

    protected SwearParser(Context context) {
        ctx = context;
        scrawlWords = ScrawlWords.getInstance(ctx);
    }

    public static SwearParser getInstance(Context context) {
        if (instance == null) {
            instance = new SwearParser(context);
        }
        return instance;
    }

    public String filterSwearWords(String data) {
        String[] words = data.split(Constants.SPACE);
        StringBuilder filteredWords = new StringBuilder();
        try {
            if (words.length > 0) {
                int prevSwearIndex = 0;
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    ValidateSwearBySpace(word.trim(), i, prevSwearIndex);
                    scrawlWords.filterSwearWord(word.trim(), filteredWords);
                }
            } else {
                scrawlWords.filterSwearWord(data.trim(), filteredWords);
            }

            if (swearWords.size() > 0) {
                for (String _data : swearWords) {
                    scrawlWords.filterSwearWord(_data.trim(), filteredWords);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredWords.toString();
    }

    public String getSwearWords(String data) {
        String[] words = data.split(Constants.SPACE);
        StringBuilder filteredWords = new StringBuilder();
        try {
            if (words.length > 0) {
                int prevSwearIndex = 0;
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    ValidateSwearBySpace(word, i, prevSwearIndex);
                    String result = scrawlWords.getSwearWord(word.trim());
                    if (result.trim().length() > 0) {
                        filteredWords.append(result).append(" ");
                    }
                }
            } else if (data.trim().length() > 11) {
                String result = scrawlWords.getSwearWord(data.trim());
                if (result.trim().length() > 1) {
                    filteredWords.append(result).append(" ");
                }
            }

            if (swearWords.size() > 0) {
                for (String _data : swearWords) {
                    if (_data.trim().length() > 1) {
                        filteredWords.append(_data).append(" ");
                    }
                }
            }
            return filteredWords.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void ValidateSwearBySpace(String word, int currentIndex, int prevSwearIndex) {
        try {
            Matcher matcher = Spacepattern.matcher(word);
            boolean spacefound = matcher.find();
            if (word.length() <= 2) {
                if (prevSwearIndex == 0) {
                    prevSwearIndex = currentIndex;
                }
                prevSwearIndex++;
                if (prevSwearIndex - currentIndex == 1 && !spacefound) {
                    if (swearBySpace == null) {
                        swearBySpace = word.trim();
                    } else {
                        swearBySpace = swearBySpace + word.trim();
                    }
                }
            } else if (swearBySpace != null) {
                prevSwearIndex = 0;
                String result = scrawlWords.getSwearWord(swearBySpace.trim());
                if (result != null && result.trim().length() > 0) {
                    swearWords.add(swearBySpace);
                }
                swearBySpace = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void resetSwearWords() {
        swearWords = new LinkedHashSet<String>();
    }
}
