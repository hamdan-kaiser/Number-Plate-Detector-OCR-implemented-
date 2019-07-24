package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.api;

import android.content.Context;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeRequest;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.ExudeResponse;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.exception.InvalidDataException;
import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.swear.SwearParser;

public class ExudeWebLinkData implements ExudeAPI {

    Logger logger = Logger.getLogger("ExudeWebLinkData");

    @Override
    public ExudeResponse filterStoppings(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        try {
            String linkData = getLinkData(exudeRequest.getData()).toString();
            exudeRequest.setData(linkData);
            return ExudeAPIImpl.getInstance(context).filterStoppings(exudeRequest);
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        throw new InvalidDataException("Invalid Data");
    }

    @Override
    public ExudeResponse filterStoppingKeepDuplicate(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        try {
            String linkData = getLinkData(exudeRequest.getData()).toString();
            exudeRequest.setData(linkData);
            return ExudeAPIImpl.getInstance(context).filterStoppingWithDuplicate(exudeRequest);
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        throw new InvalidDataException("Invalid Data");
    }

    public ExudeResponse getSwearWords(ExudeRequest exudeRequest, Context context) throws InvalidDataException {
        StringBuilder finalFilteredData = new StringBuilder();
        try {
            String linkData = getLinkData(exudeRequest.getData()).toString();
            SwearParser swearParser = SwearParser.getInstance(context);
            finalFilteredData.append(swearParser.getSwearWords(linkData));
            swearParser.resetSwearWords();
            ExudeResponse response = new ExudeResponse();
            response.setResultData(finalFilteredData.toString());
            return response;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new InvalidDataException("Invalid Data");
        }
    }

    public BodyContentHandler getLinkData(String data) throws InvalidDataException {
        try {
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);
            Metadata metadata = new Metadata();
            try {
                URL url = new URL(data);
                URLConnection conn = url.openConnection();
                ParseContext context = new ParseContext();
                parser.parse(conn.getInputStream(), handler, metadata, context);
            } catch (UnknownHostException unknownHostException) {
                logger.log(Level.SEVERE, "UnkonwnHost : " + unknownHostException.getMessage());
            }
            return handler;
        } catch (IOException | TikaException | SAXException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        throw new InvalidDataException("Invalid Data");
    }

}
