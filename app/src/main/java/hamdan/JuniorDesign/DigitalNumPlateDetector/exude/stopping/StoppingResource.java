package hamdan.JuniorDesign.DigitalNumPlateDetector.exude.stopping;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

import hamdan.JuniorDesign.DigitalNumPlateDetector.exude.commonclass.BaseResource;

public class StoppingResource implements BaseResource {

    private static Properties properties = null;

    private void loadResource(Context context) {
        try {
            if (properties == null) {
                properties = new Properties();

                InputStream input = context.getAssets().open("stopping_en.properties");

                if (input != null) {
                    properties.load(input);
                } else {
                    System.err.println("Stopping word resource file not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProperties(String property, Context context) {
        if (properties == null) {
            loadResource(context);
        }
        return properties.getProperty(property);
    }

}
