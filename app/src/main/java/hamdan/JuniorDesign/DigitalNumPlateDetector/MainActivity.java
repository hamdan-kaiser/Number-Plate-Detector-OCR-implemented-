package hamdan.JuniorDesign.DigitalNumPlateDetector;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import hamdan.JuniorDesign.DigitalNumPlateDetector.photoedit.PhotoEditingActivity;

import static hamdan.JuniorDesign.DigitalNumPlateDetector.utils.AppUtils.showToast;

/**
 * Number Plate detection is an OCR and Image processing Android Based Application that
 * Captures number plate of a car using camera.
 * Converts it into text using Optical Character Recognition
 * Record the detail time.
 * Store it into memory
 * This application is very much useful to maintain a digital Car Parking Management system
 *
 * We have designed our application for Testing Google Vision API into our project
 *
 * Our Team Members
 *
 * Monim Kaiser Prince
 * Hamdan Kaiser
 *
 * */

public class MainActivity extends AppCompatActivity {

    public static Bitmap bitmap = null;
    public static boolean initiatePOS;
    public static final String USER_PREF = "USER_PREF" ;


    SQLiteDatabase sqLiteDatabaseObj;//relational database
    String NameHolder,SQLiteDataBaseQueryHolder;
    Boolean EditTextEmptyHold;
    String TAG = "MainActivity";
    //To where the data captured from image to be display
    EditText recognizeResult;
    TextView showTime;//shows the picture taken time
    ImageView showImage;//display captured image
    Button openCamera, openGallery, runPOS,runOcr; //required options
    SharedPreferences sp; // for saving and retrieving data
    private String Timeget;

    public static boolean GetPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check whether all permissions are granted or not
        checkPermission();

        showImage = findViewById(R.id.showImages);
        recognizeResult = findViewById(R.id.showResult);
        openCamera = findViewById(R.id.openCamera);
        openGallery = findViewById(R.id.openGallery);
        runOcr = findViewById(R.id.runOcr);
        runPOS = findViewById(R.id.showPOS);
        showTime = findViewById(R.id.time);
        if (bitmap != null) {
            showImage.setImageBitmap(bitmap);
        }
        //request gallery
        openGallery.setOnClickListener(v ->
                openGallery()
        );

        //request camera
        openCamera.setOnClickListener(v ->
                openCamera()
        );
        //handle database
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);


        //Run OCR operation
        runOcr.setOnClickListener(v -> {
            try {
                if (bitmap == null) {
                    showToast(MainActivity.this, "No text found for OCR!", false);
                    return;
                }

                recognizeResult.setText(null);
                showTime.setText(null);

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (textRecognizer.isOperational()) {
                    //AI starts from here....
                    Log.d(TAG, "processImage: started");

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {
                        TextBlock textBlock = items.valueAt(i);
                        stringBuilder.append(textBlock.getValue());
                        stringBuilder.append("\n");
                    }
                    //picture taken time
                    Date currentTime  = Calendar.getInstance().getTime();
                    recognizeResult.setText(stringBuilder.toString());

                    showTime.setText(currentTime.toString());


                    SQLiteDataBaseBuild();

                    SQLiteTableBuild();

                    CheckEditTextStatus();

                    InsertDataIntoSQLiteDatabase();

                    EmptyEditTextAfterDataInsert();





                    /*String name = recognizeResult.getText().toString();
                    SharedPreferences.Editor editor  = sp.edit();
                    editor.putString(KEY_NAME,name);
                    editor.apply();
*/

                } else {
                    Log.d(TAG, "processImage: not operational");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error init data OCR. Message: " + e.getMessage());
            }
        });

        //display the databse from another intent
        runPOS.setOnClickListener(v -> {
              Intent intent = new Intent(getApplicationContext(),DisplaySQLiteDataActivity.class);
              startActivity(intent);
        });


    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        pickPhoto.setType("image/*");
        startActivityForResult(pickPhoto, 2);
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    public void SQLiteDataBaseBuild(){
        //creating database
        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){


        //creating table into the database
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "+SQLiteHelper.TABLE_NAME+"(" +SQLiteHelper.Table_Column_ID +
                " PRIMARY KEY AUTOINCREMENT NOT NULL,"+SQLiteHelper.Table_Column_1_Name+" " +
                " VARCHAR,"+SQLiteHelper.Table_Column_2_Time+" VARCHAR);");

    }

    public void CheckEditTextStatus(){

        NameHolder = recognizeResult.getText().toString() ;
        Timeget = showTime.getText().toString() ;


        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(Timeget)){

            EditTextEmptyHold = false ;

        }
        else {

            EditTextEmptyHold = true ;
        }
    }

    public void InsertDataIntoSQLiteDatabase(){


        //only if the required fields in the database gets non-empty fields, it inserts data successfully
        if(EditTextEmptyHold)
        {

            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name, time) VALUES('"+NameHolder+"','"+Timeget+"');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            Toast.makeText(MainActivity.this,"Data Inserted Successfully", Toast.LENGTH_LONG).show();

        }
        else {

            Toast.makeText(MainActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }


    public void EmptyEditTextAfterDataInsert(){

        //clears the field when stored data completely
        recognizeResult.getText().clear();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {

                    try {
                        InputStream stream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                        bitmap = BitmapFactory.decodeStream(stream);
                        if (stream != null) {
                            stream.close();
                        }

                        bitmap = bitmap.getWidth() > bitmap.getHeight()
                                ? rotateBitmap(bitmap, 90) : bitmap;
                        showImage.setImageResource(0);
                        showImage.setImageBitmap(bitmap);
                        saveImage(bitmap);

                    } catch (Exception e) {

                    }
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {

                    Uri contentURI = data.getData(); // approaching to contentProvider
                    if (contentURI == null) break;
                    ContentResolver resolver = this.getContentResolver();
                    String typeOfMedia = Objects.requireNonNull(resolver.getType(contentURI)).toUpperCase().split("/")[0];

                    if (typeOfMedia == null || !typeOfMedia.contains("IMAGE") && !typeOfMedia.contains("VIDEO"))
                        //if pictures are not in the format
                        break;

                    //For Image
                    Log.e(TAG, "Got an Media! type of media ->" + typeOfMedia);

                    String[] projections = {MediaStore.Images.Media.DATA};
                    Cursor cursor = resolver.query(contentURI, null, null, null, null);
                    if (cursor == null) break;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projections[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Log.e(TAG, "File = " + filePath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        showImage.setImageResource(0);
                        //showImage.setImageBitmap(bitmap);
                        startActivity(new Intent(MainActivity.this, PhotoEditingActivity.class));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private void saveImage(Bitmap bitmap) {
        File direct = new File(Environment.getExternalStorageDirectory().toString() + "/Quebo/Images");
        if (!direct.exists()) {
            direct.mkdirs();
            Log.e("ProblemOnDirectory", "//////////////////////////////////");
        }

        FileOutputStream stream = null;
        try {
            File file = new File(direct,
                    "Quebo" + Calendar.getInstance().get(Calendar.YEAR)
                            + Calendar.getInstance().get(Calendar.MONTH)
                            + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                            + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg");

            stream = new FileOutputStream(file);
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                stream.write(bytes.toByteArray());
            }
            MediaScannerConnection.scanFile(this, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void checkPermission() {
        int permitAll = 1;
        String[] Permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!GetPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, permitAll);
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("1", "Granted");
                    } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("2", "Granted");
                    }
                }
            }
        }
    }
}
