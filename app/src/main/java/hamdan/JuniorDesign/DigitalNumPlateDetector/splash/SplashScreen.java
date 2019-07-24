package hamdan.JuniorDesign.DigitalNumPlateDetector.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import hamdan.JuniorDesign.DigitalNumPlateDetector.MainActivity;
import hamdan.JuniorDesign.DigitalNumPlateDetector.R;


public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean firsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences("Mypref", MODE_PRIVATE);
        firsTime = sharedPreferences.getBoolean("firsTime", true);



        if (firsTime) {
            Thread splashThread = new Thread() {

                @Override
                public void run() {
                    super.run();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firsTime = false;
                    editor.putBoolean("firstTime", firsTime);
                    editor.apply();

                    try {
                        sleep(3000);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            splashThread.start();
        }

        else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}