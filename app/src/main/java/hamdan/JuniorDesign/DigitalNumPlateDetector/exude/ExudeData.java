package hamdan.JuniorDesign.DigitalNumPlateDetector.exude;

import android.content.Context;

import java.util.logging.Level;
import java.util.logging.Logger;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.Constants;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeRequest;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeResponse;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.exception.InvalidDataException;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.factory.ExudeFactory;

public class ExudeData {

    private static ExudeData instance = null;
    Logger logger = Logger.getLogger("ExudeData");
    Context ctx;
    private ExudeRequest exudeRequest = new ExudeRequest();

    protected ExudeData(Context context) {
        ctx = context;
    }

    public static ExudeData getInstance(Context context) {
        if (instance == null) {
            instance = new ExudeData(context);
        }
        return instance;
    }

    public String filterStoppings(String data) throws InvalidDataException {
        try {
            if (data.isEmpty()) {
                throw new InvalidDataException(Constants.INVALID_DATA);
            }
            exudeRequest.setData(data);
            ExudeFactory exudeFactory = new ExudeFactory(exudeRequest, ctx);
            ExudeResponse response = exudeFactory.filterStopppingData();
            return response.getResultData();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data", e);
        }
    }

    public String filterStoppingsKeepDuplicates(String data) throws InvalidDataException {
        try {
            if (data.isEmpty()) {
                throw new InvalidDataException(Constants.INVALID_DATA);
            }
            exudeRequest.setData(data);
            exudeRequest.setKeepDuplicate(true);
            ExudeFactory exudeFactory = new ExudeFactory(exudeRequest, ctx);
            ExudeResponse response = exudeFactory.filterStopppingData();
            return response.getResultData();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data", e);
        }
    }

    public String getSwearWords(String data) throws InvalidDataException {
        try {
            if (data.isEmpty()) {
                throw new InvalidDataException(Constants.INVALID_DATA);
            }
            exudeRequest.setData(data);
            exudeRequest.setKeepDuplicate(true);
            ExudeFactory exudeFactory = new ExudeFactory(exudeRequest, ctx);
            ExudeResponse response = exudeFactory.getSwearWords();
            return response.getResultData();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data", e);
        }
    }

}
