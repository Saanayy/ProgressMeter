package com.bytebucket1111.progressmeter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ProjectInfoDialog extends DialogFragment {

    TextView tvTitle, tvDescription, tvGeolocation, tvStartDate, tvDuration;
    private ShowProjectInfoListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.project_info_dialog, null);
        tvTitle = view.findViewById(R.id.project_info_dialog_title);
        tvDescription = view.findViewById(R.id.project_info_dialog_description);
        tvGeolocation = view.findViewById(R.id.project_info_dialog_geolocation);
        tvStartDate = view.findViewById(R.id.project_info_dialog_startdate);
        tvDuration = view.findViewById(R.id.project_info_dialog_duration);

        listener.showProjectDetails(tvTitle, tvDescription, tvGeolocation, tvStartDate, tvDuration);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ShowProjectInfoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }

    }

    public interface ShowProjectInfoListener {
        void showProjectDetails(TextView tvTitle,TextView tvDescription,TextView tvGeolocation,TextView tvStartDate,TextView tvDuration);
    }
}
