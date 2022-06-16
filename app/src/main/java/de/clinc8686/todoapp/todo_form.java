package de.clinc8686.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

public class todo_form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);
    }

    public void addTaskToDatabase(View view) {
        String taskName = ((EditText) findViewById(R.id.FormTaskName)).getText().toString();
        String taskDescription = ((EditText) findViewById(R.id.FormTaskDescription)).getText().toString();
        if (taskName.length() > 0 && taskDescription.length() > 0) {
            DatePicker taskDate = findViewById(R.id.FormDate);
            String date;
            if((taskDate.getMonth()+1) < 10) {
                date = taskDate.getDayOfMonth() + "-" + "0" + (taskDate.getMonth()+1) + "-" + taskDate.getYear();
            } else {
                date = taskDate.getDayOfMonth() + "-" + (taskDate.getMonth()+1) + "-" + taskDate.getYear();
            }

            Intent intent = new Intent();
            intent.putExtra("name", taskName);
            intent.putExtra("description", taskDescription);
            intent.putExtra("enddate", date);
            setResult(1, intent);
            finish();
        } else {
            failed();
        }
    }

    public void failed() {
        Intent intent = new Intent();
        setResult(2);
        finish();
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(2);
        finish();
    }
}