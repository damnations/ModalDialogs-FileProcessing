package com.example.modaldialogs_fileprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_DIRECTORY_NAME = "ModalDialogsFileProcessingDirectory";
    private static final String FILE_EXTENSION = ".txt";

    private RecyclerView recyclerView;
    private Button addButton;
    private List<File> fileList;
    private ModalDialogsFileProcessingRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Map each UI element to a field variable
        recyclerView = findViewById(R.id.listRecyclerView);
        addButton = findViewById(R.id.addNewFileButton);
        fileList = getFilesList();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModalDialogsFileProcessingRecyclerViewAdapter(this, fileList);
        recyclerView.setAdapter(adapter);

        // Set up addButton click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFile();
            }
        });
    }

    // Method to create a file
    private void createNewFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = timeStamp + FILE_EXTENSION;
        String fileContent = "Here is the content of the file '" + fileName + "'";

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + FILE_DIRECTORY_NAME + "/" + fileName);

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(fileContent);
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity.this, "File Creation Successful!", Toast.LENGTH_SHORT).show();
                    fileList.add(file);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "File Creation Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "File already exists!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get a list of all files
    private List<File> getFilesList() {
        List<File> fileArrayList = new ArrayList<>();
        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + FILE_DIRECTORY_NAME);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Toast.makeText(MainActivity.this, "Directory Creation Unsuccessful!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Directory Creation Successful!", Toast.LENGTH_SHORT).show();
            }
        }

        if (directory.listFiles() != null) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(FILE_EXTENSION)) {
                    fileArrayList.add(file);
                }
            }
        }

        return fileArrayList;
    }

    // Method to show contents of a file
    void showFileContents(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            String fileContents = stringBuilder.toString();

            // Show the contents of the file in an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.file_contents_dialog, null);

            builder.setTitle(file.getName());

            EditText editText = view.findViewById(R.id.editText);
            editText.setText(fileContents);

            builder.setView(view);

            // Write the new contents back to the file
            builder.setPositiveButton("Write", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                        outputStreamWriter.write(editText.getText().toString());
                        outputStreamWriter.close();
                        Toast.makeText(MainActivity.this, "File Saved Successfully!", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error Saving File!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error Opening File!", Toast.LENGTH_SHORT).show();
        }
    }
}