package com.bytebucket1111.progressmeter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class AddProjectDialog extends AppCompatDialogFragment {

    private TextInputEditText inputEditTextTitle,inputEditTextDesc,inputEditTextGeolocation,inputEditTextStartDate, inputEditTextDuration;
    private AddProjectListener listener;
    private int mYear, mMonth, mDay;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_project_dialog, null);
        inputEditTextTitle = view.findViewById(R.id.create_project_title);
        inputEditTextDesc = view.findViewById(R.id.create_project_description);
        inputEditTextGeolocation = view.findViewById(R.id.create_project_geolocation);
        inputEditTextStartDate = view.findViewById(R.id.create_project_start_date);
        inputEditTextDuration = view.findViewById(R.id.create_project_duration);

        inputEditTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                inputEditTextStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        builder.setView(view)
                .setTitle("Add Project Details")
                .setIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = inputEditTextTitle.getText().toString();
                        String description = inputEditTextDesc.getText().toString();
                        String geolocation = inputEditTextGeolocation.getText().toString();
                        String startDate = inputEditTextStartDate.getText().toString();
                        String duration = inputEditTextDuration.getText().toString();
                        listener.addProjectToFirebase(title, description,geolocation,startDate,duration);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddProjectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface AddProjectListener {
        void addProjectToFirebase(String title, String desc, String geolocation, String startDate, String duration);
    }


}
