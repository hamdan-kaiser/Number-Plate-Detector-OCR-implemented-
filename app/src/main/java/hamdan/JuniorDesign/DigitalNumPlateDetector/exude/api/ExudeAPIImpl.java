package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.api;

import android.content.Context;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeRequest;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeResponse;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.exception.InvalidDataException;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping.StoppingParser;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping.TrushDuplicates;

public class ExudeAPIImpl {

    private static ExudeAPIImpl instance = null;
    private Context ctx;

    private TrushDuplicates trushDuplicates;

    private ExudeAPIImpl(Context context) {
        ctx = context;
        trushDuplicates = TrushDuplicates.getInstance(ctx);
    }

    public static ExudeAPIImpl getInstance(Context context) {
        if (instance == null) {
            instance = new ExudeAPIImpl(context);
        }
        return instance;
    }

    public ExudeResponse filterStoppings(ExudeRequest exudeRequest) throws InvalidDataException {
        String tempData = "";
        StringBuilder finalFilteredData = new StringBuilder();
        try {
            String fileData = exudeRequest.getData();
            tempData = trushDuplicates.filterDuplicates(fileData);
            StoppingParser stoppingParser = StoppingParser.getInstance(ctx);
            Set<String> dataSet = trushDuplicates.filterData(tempData);
            Iterator<String> iterable = dataSet.iterator();
            while (iterable.hasNext()) {
                String line = iterable.next();
                stoppingParser.filterStoppingWords(line.replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " "));
            }
            TrushDuplicates.filterDuplicate(stoppingParser.getResultSet());
            Iterator<String> _iterable = TrushDuplicates.getTempSet().iterator();
            while (_iterable.hasNext()) {
                String line = _iterable.next();
                finalFilteredData.append(line.trim()).append(" ");
            }
            stoppingParser.reset();
            trushDuplicates.reset();
            return populateResponse(Constants.STATUS.SUCCESS.name(), finalFilteredData.toString());
        } catch (Exception e) {
            return populateResponse(Constants.STATUS.FAILURE.name(), e.getMessage());
        }
    }

    public ExudeResponse filterStoppingWithDuplicate(ExudeRequest exudeRequest) throws InvalidDataException {
        String tempData = "";
        StringBuilder finalFilteredData = new StringBuilder();
        try {
            tempData = exudeRequest.getData();
            StoppingParser stoppingParser = StoppingParser.getInstance(ctx);
            List<String> dataSet = trushDuplicates.filterDataKeepDuplicate(tempData);
            for (String line : dataSet) {
                stoppingParser.filterStoppingWordsKeepDuplicates(line.replaceAll(Constants.MULTIPLE_SPACE_TAB_NEW_LINE, " "));
            }
            for (String line : TrushDuplicates.getTempList()) {
                finalFilteredData.append(line.trim()).append(" ");
            }
            stoppingParser.reset();
            trushDuplicates.reset();
            return populateResponse(Constants.STATUS.SUCCESS.name(), finalFilteredData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new InvalidDataException("Invalid Data");
    }

    public ExudeResponse getSwearWords(ExudeRequest exudeRequest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ExudeResponse populateResponse(String status, String result) throws InvalidDataException {
        ExudeResponse response = new ExudeResponse();

        switch (Constants.STATUS.valueOf(status)) {
            case SUCCESS:
                response.setStatus(Constants.STATUS.SUCCESS.name().toUpperCase());
                response.setMessage("Sucessfully Processed the data");
                response.setResultData(result);
                return response;
            case FAILURE:
                response.setStatus(Constants.STATUS.FAILURE.name().toUpperCase());
                response.setMessage("Sucessfully Processed the data");
                response.setResultData(result);
                return response;
            default:
                break;

        }
        throw new InvalidDataException("Invalid Data");
    }

}
