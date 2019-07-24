package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.api;

import android.content.Context;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeRequest;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeResponse;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.exception.InvalidDataException;

public interface ExudeAPI {
    ExudeResponse filterStoppings(ExudeRequest exudeRequest, Context context) throws InvalidDataException;

    ExudeResponse filterStoppingKeepDuplicate(ExudeRequest exudeRequest, Context context) throws InvalidDataException;

    ExudeResponse getSwearWords(ExudeRequest exudeRequest, Context context) throws InvalidDataException;
}
