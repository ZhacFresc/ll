package com.example.ll;

        import android.content.ContentValues;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;

        import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bReg, bVhod, bClear;
    EditText etName, etPassword;
    LinearLayout layout1, layout2, layout3;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
        layout2 = (LinearLayout) findViewById(R.id.LinearLayout2);
        layout3 = (LinearLayout) findViewById(R.id.LinearLayout3);

        bReg = (Button) findViewById(R.id.btnRegist);
        bReg.setOnClickListener(this);

        bClear = (Button) findViewById(R.id.btnClear);
        bClear.setOnClickListener(this);

        bVhod = (Button) findViewById(R.id.btnVhod);
        bVhod.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.editTextTextPersonName);
        etPassword = (EditText) findViewById(R.id.editTextTextPassword);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();
        String password = etPassword.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {

            case R.id.btnRegist:
                if (name.length() > 2 && password.length() > 5) {
                    contentValues.put(DBHelper.KEY_NAME, name);
                    contentValues.put(DBHelper.KEY_PASSWORD, password);
                    if (password.equals("123456"))
                        contentValues.put(DBHelper.KEY_STATUS, "admin");
                    else
                        contentValues.put(DBHelper.KEY_STATUS, "client");

                    database.insert(DBHelper.TABLE, null, contentValues);
                    break;
                }
                else{
                    etName.setText("");
                    etPassword.setText("");

                    etName.setHint("Имя должно иметь хотяб 3 символа");
                    etPassword.setHint("Пароль должно иметь 6 символов");
            }
            case R.id.btnVhod:
                Cursor cursor = database.query(DBHelper.TABLE, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int statusIndex = cursor.getColumnIndex(DBHelper.KEY_STATUS);
                    int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do {
                        Log.d("Log", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", status = " + cursor.getString(statusIndex) +
                                ", password = " + cursor.getString(passwordIndex));
                        if (name.equals(cursor.getString(nameIndex))&&password.equals(cursor.getString(passwordIndex)))
                            if (cursor.getString(statusIndex).equals("client")) {
                                layout1.setVisibility(View.GONE);
                                layout2.setVisibility(View.VISIBLE);
                            }
                            else {
                                layout1.setVisibility(View.GONE);
                                layout3.setVisibility(View.VISIBLE);
                            }
                    } while (cursor.moveToNext());
                } else
                    Log.d("Log","0 rows");

                cursor.close();
                break;
            case R.id.btnClear:
                database.delete(DBHelper.TABLE, null, null);
                break;

        }
        dbHelper.close();
    }
}