package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping;

import android.content.Context;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;

public class TrushDuplicates {

    public static Set<String> tempSet = new LinkedHashSet<String>();
    public static List<String> tempList = new ArrayList<String>();
    public static Set<String> _tempSet = new HashSet<String>();
    public static TrushDuplicates instance = null;
    Set<String> filteredSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
    List<String> resultList = new ArrayList<String>();
    Context ctx;

    protected TrushDuplicates(Context context) {
        ctx = context;
    }

    public static TrushDuplicates getInstance(Context context) {
        if (instance == null) {
            instance = new TrushDuplicates(context);
        }
        return instance;
    }

    public static BodyContentHandler getData(String data) {
        try {
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);
            Metadata metadata = new Metadata();
            boolean isURL = Pattern.matches(Constants.URL_REGULAR_EXPRESSION, data);
            if (isURL) {
                try {
                    URL url = new URL(data);
                    URLConnection conn = url.openConnection();
                    ParseContext context = new ParseContext();
                    parser.parse(conn.getInputStream(), handler, metadata, context);
                } catch (UnknownHostException unknownHostException) {
                    System.err.println("UnkonwnHost : " + unknownHostException.getMessage());
                }
            } else {
                FileInputStream inputstream = new FileInputStream(new File(data));
                ParseContext context = new ParseContext();
                parser.parse(inputstream, handler, metadata, context);
            }
            return handler;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void filterDuplicate(Set<String> dataSet) {
        _tempSet = new HashSet<String>();
        Iterator<String> _iterable = dataSet.iterator();
        while (_iterable.hasNext()) {
            String line = _iterable.next();
            String[] dataarr = line.split(Constants.SPACE);
            String _data = null;
            if (dataarr.length > 0) {
                for (int i = 0; i < dataarr.length; i++) {
                    String tempStr = dataarr[i];
                    String[] tempArr = tempStr.split(Constants.SPACE);
                    if (tempArr.length > 0) {
                        for (int j = 0; j < tempArr.length; j++) {
                            _data = tempArr[j].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                            if (_data != null && _data.trim().length() > 0) {
                                _tempSet.add(_data);
                            }
                        }
                    } else {
                        _data = dataarr[i].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                        if (_data != null && _data.trim().length() > 0) {
                            _tempSet.add(_data);
                        }
                    }
                }
            } else {
                _tempSet.add(line);
            }
        }
    }

    public static Set<String> getTempSet() {
        return _tempSet;
    }

    public static List<String> getTempList() {
        return tempList;
    }

    public Set<String> filter(String file) {
        try {
            File input = new File(file);
            // List<String> data = Files.readAllLines(input.toPath());
            // filteredSet.addAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredSet;
    }

    private String[] separateWords(String data) {
        String[] dataarr = data.split(Constants.SPACE);
        if (dataarr == null) {
            dataarr = data.split(Constants.COMMA);
        }
        return dataarr;
    }

    public List<String> filterDataKeepDuplicate(String data) {
        try {
            String _data = null;
            StoppingParser stoppingParser = StoppingParser.getInstance(ctx);
            String[] dataarr = separateWords(data);
            for (int i = 0; i < dataarr.length; i++) {
                String tempStr = dataarr[i];
                String[] tempArr = tempStr.split(Constants.SPACE);
                if (tempArr.length > 0) {
                    for (int j = 0; j < tempArr.length; j++) {
                        _data = tempArr[j].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                        _data = stoppingParser.filterStoppingWordsKeepDuplicates(_data);
                        if (_data != null && _data.trim().length() > 0) {
                            tempList.add(_data);
                        }
                    }
                } else {
                    _data = dataarr[i].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                    _data = stoppingParser.filterStoppingWordsKeepDuplicates(_data);
                    if (_data != null && _data.trim().length() > 0) {
                        tempList.add(_data);
                    }
                }
            }
            if (tempList.size() > 0) {
                resultList.addAll(tempList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public Set<String> filterData(String data) {
        try {
            String _data = null;
            StoppingParser stoppingParser = StoppingParser.getInstance(ctx);
            String[] dataarr = data.split(Constants.SPACE);
            if (dataarr == null) {
                dataarr = data.split(Constants.COMMA);
            }
            for (int i = 0; i < dataarr.length; i++) {
                String tempStr = dataarr[i];
                String[] tempArr = tempStr.split(Constants.SPACE);
                if (tempArr.length > 0) {
                    for (int j = 0; j < tempArr.length; j++) {
                        _data = tempArr[j].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                        stoppingParser.filterStoppingWords(_data);
                        if (_data != null && _data.trim().length() > 0) {
                            tempSet.add(_data);
                        }
                    }
                } else {
                    _data = dataarr[i].replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " ").toLowerCase();
                    stoppingParser.filterStoppingWords(_data);
                    if (_data != null && _data.trim().length() > 0) {
                        tempSet.add(_data);
                    }
                }
            }
            if (tempSet.size() > 0) {
                Iterator<String> iterable = tempSet.iterator();
                while (iterable.hasNext()) {
                    String line = iterable.next();
                }
                filteredSet.addAll(tempSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredSet;
    }

    public void filterDuplicates(String inputFilePath, String outFilePath) throws IOException {
        BodyContentHandler contentHandler = getData(inputFilePath);
        Set<String> lines = new LinkedHashSet<String>();
        if (contentHandler != null) {
            String line = contentHandler.toString();
            String delims = " ";
            line = line.trim();
            StringTokenizer str = new StringTokenizer(line, delims);
            while (str.hasMoreElements()) {
                line = (String) str.nextElement();
                if (line.trim().length() > 0) {
                    lines.add(line.replace("\"", ""));
                }
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFilePath), Constants.UTF_8));
            for (String unique : lines) {
                if (unique.trim().length() > 0) {
                    writer.write(unique + " ");
                }
            }
            writer.close();
        }
    }

    public String filterDuplicates(String data) {
        Set<String> lines = new LinkedHashSet<String>();
        StringBuilder stringBuilder = new StringBuilder();
        if (data != null) {
            String line = data;
            String delims = " ";
            line = line.trim();
            StringTokenizer str = new StringTokenizer(line, delims);
            while (str.hasMoreElements()) {
                line = (String) str.nextElement();
                if (line.trim().length() > 0) {
                    lines.add(line);
                }
            }
            for (String unique : lines) {
                if (unique.trim().length() > 0) {
                    stringBuilder.append(unique + " ");
                }
            }
            return stringBuilder.toString();
        }
        return "";
    }

    public String filterDuplicatesInText(String inputData) {
        Set<String> filterwords = new LinkedHashSet<String>();
        StringBuilder stringBuilder = new StringBuilder();
        if (inputData != null && inputData.trim().length() > 0) {
            String[] words = inputData.split("\\ ");
            if (words != null && words.length > 0) {
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (word.trim().length() > 0) {
                        filterwords.add(word.replace("\"", ""));
                    }
                }
                for (String unique : filterwords) {
                    if (unique.trim().length() > 0) {
                        stringBuilder.append(unique + " ");
                    }
                }
                return stringBuilder.toString();
            } else {
                return inputData;
            }
        }
        return null;
    }

    public void reset() {
        tempSet = new LinkedHashSet<String>();
        _tempSet = new HashSet<String>();
        tempList = new ArrayList<String>();
    }

}
