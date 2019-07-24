package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping;

import java.util.HashMap;
import java.util.Map;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;

public class Codes {

    Map<Character, Integer> alphabetAsciiMap = new HashMap<Character, Integer>();

    public void populateAsciiMap() {
        for (int i = 0; i < Constants.ENGLISH_ALPHABETS.length(); i++) {
            alphabetAsciiMap.put(Constants.ENGLISH_ALPHABETS.charAt(i), (int) Constants.ENGLISH_ALPHABETS.charAt(i));
        }
    }
}
