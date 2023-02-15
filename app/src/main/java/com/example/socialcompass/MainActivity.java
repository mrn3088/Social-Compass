package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDAITLY LEAVE MAIN ACTIVITY AND LOAD MAP!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Display Radians of phone orientation
    }



    public void onSubmitLabelsClicked(View view) {
        // if no labels have been added display error
        TextView[] labelNameViews = {
                (TextView)findViewById(R.id.label_1_name),
                (TextView)findViewById(R.id.label_2_name),
                (TextView)findViewById(R.id.label_3_name)
        };

        TextView[] labelCoordinateViews = {
                (TextView)findViewById(R.id.label_1_coordinates),
                (TextView)findViewById(R.id.label_2_coordinates),
                (TextView)findViewById(R.id.label_3_coordinates)
        };

        String manual_rotation = ((TextView)findViewById(R.id.orientation_input)).getText().toString();

        List<String> labelNames = Arrays.stream(labelNameViews)
                .map(TextView::getText)
                .map(CharSequence::toString)
                .collect(Collectors.toList());

        List<String> labelCords = Arrays.stream(labelCoordinateViews)
                .map(TextView::getText)
                .map(CharSequence::toString)
                .collect(Collectors.toList());


        // label names cannot be empty
        if (!Utilities.namedLabels(labelNames)) {
            Utilities.displayAlert(this, "you must enter at least one label before proceeding!");
            return;
        }

        // label names cannot exceed 20 characters
        if (!Utilities.validLabelLengths(labelNames)) {
            Utilities.displayAlert(this, "your label strings must be less then 12 characters!");
            return;
        }

        List<Optional<float[]>> maybeCoordinates = labelCords.stream()
                .map(Utilities::parseCoordinate)
                .collect(Collectors.toList());


        if (maybeCoordinates.stream().anyMatch(cord -> !cord.isPresent())) {
            Utilities.displayAlert(this, "your coordinates are not entered in correct format!");
            return;
        }

        //noinspection OptionalGetWithoutIsPresent already checked
        List<float[]> coordinates = maybeCoordinates.stream()
                .map(Optional::get)
                .collect(Collectors.toList());

        if (!Utilities.validCoordinates(coordinates)) {
            Utilities.displayAlert(this, "your location does not exist!");
            return;
        }

        // if all checks pass open map
        SharedPreferences preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < coordinates.size(); i++) {
            editor.putFloat("label" + (i + 1) + "Lat", coordinates.get(i)[0]);
            editor.putFloat("label" + (i + 1) + "Long", coordinates.get(i)[1]);
            editor.putString("label" + (i + 1) + "Name", labelNames.get(i));
        }
        editor.apply();

        Intent intent = new Intent(this, ShowMapActivity.class);
        intent.putExtra("manual_rotation", manual_rotation);
        startActivity(intent);

    }

}