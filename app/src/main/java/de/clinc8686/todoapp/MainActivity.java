package de.clinc8686.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int[] toViews = new int[] {R.id.listElementName, R.id.listElementDescription, R.id.listElementDate};
    String[] fromColumns = {DatabaseHelper.KEY_NAME, DatabaseHelper.KEY_DESCRIPTION, DatabaseHelper.KEY_DATE};
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        synctask();
    }

    public void addtask(View view) {
        Intent intent = new Intent(this, todo_form.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            String enddate = data.getStringExtra("enddate");

            insertData(name, description, enddate);
        } else if (resultCode == 2 || resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Cant add Task", Toast.LENGTH_SHORT).show();
        }
    }

    public void synctask() {
        countTasks();
        showTasks();
    }

    private void showTasks() {
        Cursor c = db.getData();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.task_template, c, fromColumns, toViews, 0);
        ListView listview = findViewById(R.id.taskList);
        listview.setAdapter(adapter);
    }

    private void countTasks() {
        TextView counterTasks = findViewById(R.id.counterTasks);

        long countedTasks = db.countTasks();
        if (countedTasks < 0) {
            Toast.makeText(getApplicationContext(), "Cant count tasks from database", Toast.LENGTH_SHORT).show();
        } else {
            counterTasks.setText(db.countTasks() + " active tasks: ");
        }
    }

    public void removeTasks(View view) {
        if (!db.deleteFullData()) {
            Toast.makeText(getApplicationContext(), "Cant delete tasks from database", Toast.LENGTH_SHORT).show();
            return;
        }
        synctask();
    }

    private void insertData(String name, String description, String date) {
        boolean completed = db.addTask(name, description, date);
        if (!completed) {
            Toast.makeText(getApplicationContext(), "Cant add task to database", Toast.LENGTH_SHORT).show();
            return;
        }
        synctask();
    }

    public void changeDate(View view) {
        Cursor c = db.getDataToday();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.task_template, c, fromColumns, toViews, 0);
        ListView listview = findViewById(R.id.taskList);
        listview.setAdapter(adapter);
    }
}