package com.darijanv.baza_podataka_vjezba;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etIme, etKolegij, etOcjena;
    private Button btnDodaj, btnUredi, btnObrisi;
    private ListView lvStudenti;
    private SimpleCursorAdapter adapter;
    private StudentsDatabaseHelper database;
    private long selectedID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new StudentsDatabaseHelper(this);

        initWidgets();
        initList();
        setupListeners();
    }

    private void initWidgets(){
        etIme = findViewById(R.id.etIme);
        etKolegij = findViewById(R.id.etKolegij);
        etOcjena = findViewById(R.id.etOcjena);
        btnDodaj = findViewById(R.id.btnDodaj);
        btnUredi = findViewById(R.id.btnUredi);
        btnObrisi = findViewById(R.id.btnObrisi);
        lvStudenti = findViewById(R.id.lvstudenti);
    }

    private void initList(){
        String[] columns = {StudentsDatabaseHelper.COLUMN_NAME, StudentsDatabaseHelper.COLUMN_COURSE, StudentsDatabaseHelper.COLUMN_GRADE};
        int[] viewIds = {R.id.tvIme, R.id.tvKolegij, R.id.tvOcjena};
        adapter = new SimpleCursorAdapter(this, R.layout.list_view, null, columns, viewIds, 0);
        lvStudenti.setAdapter(adapter);
        refreshList();
    }

    private void refreshList(){
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + StudentsDatabaseHelper.TABLE_NAME, null);
        adapter.swapCursor(cursor);
    }

    private void setupListeners() {
        btnDodaj.setOnClickListener(v -> insertStudent(etIme.getText().toString(), etKolegij.getText().toString(), etOcjena.getText().toString()));
        btnUredi.setOnClickListener(v -> updateStudent(etIme.getText().toString(), etKolegij.getText().toString(), etOcjena.getText().toString()));
        btnObrisi.setOnClickListener(v -> deleteStudent());
        lvStudenti.setOnItemClickListener((parent, view, position, id) -> {
            selectedID = id;
            fillForm();
        });
    }

    private void insertStudent(String ime, String kolegij, String ocjena){
        ContentValues values = new ContentValues();
        values.put(StudentsDatabaseHelper.COLUMN_NAME, ime);
        values.put(StudentsDatabaseHelper.COLUMN_COURSE, kolegij);
        values.put(StudentsDatabaseHelper.COLUMN_GRADE, ocjena);
        database.insert(values);
        refreshList();
        clearForm();
    }

    private void updateStudent(String ime, String kolegij, String ocjena){
        ContentValues values = new ContentValues();
        values.put(StudentsDatabaseHelper.COLUMN_NAME, ime);
        values.put(StudentsDatabaseHelper.COLUMN_COURSE, kolegij);
        values.put(StudentsDatabaseHelper.COLUMN_GRADE, ocjena);
        database.update(String.valueOf(selectedID), values);
        refreshList();
        clearForm();
    }

    private void deleteStudent(){
        database.delete(String.valueOf(selectedID));
        refreshList();
        clearForm();
    }

    private void fillForm(){
        Cursor cursor = database.query(String.valueOf(selectedID), null, null, null, null);
        if(cursor.moveToFirst()){
            etIme.setText(cursor.getString(cursor.getColumnIndexOrThrow(StudentsDatabaseHelper.COLUMN_NAME)));
            etKolegij.setText(cursor.getString(cursor.getColumnIndexOrThrow(StudentsDatabaseHelper.COLUMN_COURSE)));
            etOcjena.setText(cursor.getString(cursor.getColumnIndexOrThrow(StudentsDatabaseHelper.COLUMN_GRADE)));
        }
    }

    private void clearForm(){
        etIme.setText("");
        etKolegij.setText("");
        etOcjena.setText("");
        selectedID = -1;
    }
}
