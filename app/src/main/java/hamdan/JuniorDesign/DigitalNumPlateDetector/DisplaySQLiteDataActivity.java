package hamdan.JuniorDesign.DigitalNumPlateDetector;

import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class DisplaySQLiteDataActivity extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ListAdapter listAdapter ;

    //display data into listView
    ListView LISTVIEW;
        //creating rows
    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    ArrayList<String> TIME_Array;

    //7 8 13 14 15 16 17 18 24
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sqlite_data);

        LISTVIEW = findViewById(R.id.listView1);

        ID_Array = new ArrayList<>();

        NAME_Array = new ArrayList<>();

        TIME_Array = new ArrayList<>();

        sqLiteHelper = new SQLiteHelper(this);

    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        //creating query
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_NAME+"", null);

        ID_Array.clear();
        NAME_Array.clear();
        TIME_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                ID_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                NAME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1_Name)));

                TIME_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2_Time)));

            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(DisplaySQLiteDataActivity.this,

                ID_Array,
                NAME_Array,
                TIME_Array
        );

        LISTVIEW.setAdapter(listAdapter);

        cursor.close();
    }
}