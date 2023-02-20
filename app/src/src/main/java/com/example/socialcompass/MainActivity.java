package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    // IF SHARED PREFERENCES DON'T EXIST STAY ON PAGE
    // ELSE IMMEDIATELY LEAVE MAIN ACTIVITY AND LOAD MAP!
    private Pair<Double, Double> destination1;
    private String label1;
    private Pair<Double, Double> destination2;
    private String label2;
    private Pair<Double, Double> destination3;
    private String label3;
    private Pair<Double, Double> previousLocation = Pair.create(0.0, 0.0);
    private int manual_rotation;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.loadProfile();
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

        String manualRotationStr = ((TextView)findViewById(R.id.orientation_input)).getText().toString();
        String manualRotationOpt = Utilities.USE_PHONE_ORIENTATION;
        if (!manualRotationStr.equals("") && !Utilities.validOrientation(manualRotationStr)) {
            Utilities.displayAlert(this, "Orientation needs to be a integer between 0 and 359!");
            return;
        } else {
            manualRotationOpt = manualRotationStr;
        }



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


        if (maybeCoordinates.stream().allMatch(cord -> !cord.isPresent())) {
            Utilities.displayAlert(this, Utilities.INCORRECT_EMPTY);
            return;
        }

        if (maybeCoordinates.stream().anyMatch(cord -> cord.isPresent()
                && !Utilities.validCoordinates(cord.get()))){
            Utilities.displayAlert(this, Utilities.INCORRECT_FORMAT);
            return;
        }


        List<float[]> coordinates = maybeCoordinates.stream()
                .map(cord -> {
                    if (cord.isPresent()) {
                        if (!Utilities.validCoordinates(cord.get())) {
                            Utilities.displayAlert(this, Utilities.INCORRECT_LOCATION);
                        }
                        return cord.get();
                    }
                    return new float[]{-360f, -360f};
                })
                .collect(Collectors.toList());



        // if all checks pass open map
        SharedPreferences preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < coordinates.size(); i++) {
            editor.putFloat("label" + (i + 1) + "Lat", coordinates.get(i)[0]);
            editor.putFloat("label" + (i + 1) + "Long", coordinates.get(i)[1]);
            editor.putString("label" + (i + 1) + "Name", labelNames.get(i));
        }

        editor.putString("manual_rotation", manualRotationOpt);
        editor.apply();


        Intent intent = new Intent(this, ShowMapActivity.class);

        startActivity(intent);

    }

    public void loadProfile(){
        preferences = getSharedPreferences("com.example.socialcompass", MODE_PRIVATE);
        destination1 = Pair.create((double) preferences.getFloat("label1Lat", 0f), (double) preferences.getFloat("label1Long", 0f));
        destination2 = Pair.create((double) preferences.getFloat("label2Lat", 0f), (double) preferences.getFloat("label2Long", 0f));
        destination3 = Pair.create((double) preferences.getFloat("label3Lat", 0f), (double) preferences.getFloat("label3Long", 0f));


        label1 = preferences.getString("label1Name", "Label1");
        label2 = preferences.getString("label2Name", "Label2");
        label3 = preferences.getString("label3Name", "Label3");

        manual_rotation = -1;
        try {
            manual_rotation = Integer.parseInt(preferences.getString("manual_rotation", "-1"));
        } catch (NumberFormatException e) {}

        TextView[] labelNameViews = {
                (TextView)findViewById(R.id.label_1_name),
                (TextView)findViewById(R.id.label_2_name),
                (TextView)findViewById(R.id.label_3_name)
        };



        labelNameViews[0].setText(label1);
        labelNameViews[1].setText(label2);
        labelNameViews[2].setText(label3);



        TextView[] labelCoordinateViews = {
                (TextView)findViewById(R.id.label_1_coordinates),
                (TextView)findViewById(R.id.label_2_coordinates),
                (TextView)findViewById(R.id.label_3_coordinates)
        };


        String cord1AsString = Utilities.getDisplayStr(destination1.first + " " + destination1.second);
        String cord2AsString = Utilities.getDisplayStr(destination2.first + " " + destination2.second);
        String cord3AsString = Utilities.getDisplayStr(destination3.first + " " + destination3.second);
        String orientationString = manual_rotation == -1 ? "" : "" + manual_rotation;

        labelCoordinateViews[0].setText(cord1AsString);
        labelCoordinateViews[1].setText(cord2AsString);
        labelCoordinateViews[2].setText(cord3AsString);

        EditText orientationEditText = (EditText)findViewById(R.id.orientation_input);
        Editable orientationEditable = orientationEditText.getText();
        orientationEditable.replace(0, orientationEditable.length(), orientationString);


    }

}