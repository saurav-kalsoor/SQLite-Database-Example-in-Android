package com.example.sqliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText mEditName,mEditSurname,mEditMarks,mEditID,mEditSearch;
    private Button mAddDataBtn,mViewDataBtn,mUpdateDataBtn,mDeleteBtn,mSearchBtn;

    private static final int GALLERY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        mEditMarks = findViewById(R.id.editText_Marks);
        mEditName = findViewById(R.id.editText_Name);
        mEditSurname = findViewById(R.id.editText_Surname);
        mEditID = findViewById(R.id.editText_ID);
        mAddDataBtn = findViewById(R.id.button_AddData);
        mViewDataBtn = findViewById(R.id.button_ViewData);
        mUpdateDataBtn = findViewById(R.id.button_UpdateData);
        mDeleteBtn = findViewById(R.id.button_DeleteData);
        mEditSearch = findViewById(R.id.editText_Search);
        mSearchBtn = findViewById(R.id.button_Search);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData();
            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteID();
            }
        });

        mUpdateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        mViewDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAll();
            }
        });

        mAddDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    private void searchData() {
        String name = mEditSearch.getText().toString();
        Cursor res = myDb.searchRow(name);
        if(res == null){
            Toast.makeText(MainActivity.this,"No Data Found",Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(MainActivity.this,"ID = " + res.getString(0) + "\nName = " +res.getString(1) + "\nSurname = "+res.getString(2)+"\nMarks = "+res.getString(3),Toast.LENGTH_LONG).show();
    }


    private void deleteID() {
        String id = mEditID.getText().toString().trim();

        if(TextUtils.isEmpty(id)){
            Toast.makeText(MainActivity.this,"Enter ID number",Toast.LENGTH_SHORT).show();
            return;
        }

        int deletedId = myDb.deleteData(id);

        if(deletedId>0){
            Toast.makeText(MainActivity.this,"Id Deleted",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,"Error! Id not Deleted",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        String name = mEditName.getText().toString().trim();
        String surname = mEditSurname.getText().toString().trim();
        String marks = mEditMarks.getText().toString().trim();
        String id = mEditID.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(marks) || TextUtils.isEmpty(id)){
            Toast.makeText(MainActivity.this,"Fields Are Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = myDb.updateData(id,name,surname,marks);

        if(isUpdated){
            Toast.makeText(MainActivity.this,"Data Updated",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void viewAll() {
        Cursor res = myDb.getAllData();

        if(res.getCount() == 0){
            showMessage("Error","No Data Found");
            return;
        }

        StringBuffer buffer = new StringBuffer();

        while(res.moveToNext()){
            buffer.append("ID : "+res.getString(0)+"\n");
            buffer.append("Name : "+res.getString(1)+"\n");
            buffer.append("Surname : "+res.getString(2)+"\n");
            buffer.append("Marks : "+res.getString(3)+"\n\n");
        }

        showMessage("Data",buffer.toString());

    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void addData() {
        String name = mEditName.getText().toString().trim();
        String surname = mEditSurname.getText().toString().trim();
        String marks = mEditMarks.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(marks)){
            Toast.makeText(MainActivity.this,"Fields are Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = myDb.insertData(name,surname,marks);

        if(isInserted){
            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this,"Error! Data not Inserted",Toast.LENGTH_SHORT).show();
        }
    }


}
