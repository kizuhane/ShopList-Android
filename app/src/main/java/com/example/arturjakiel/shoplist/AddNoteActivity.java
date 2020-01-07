package com.example.kizuhane.shoplist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class AddNoteActivity extends AppCompatDialogFragment {


    private EditText editTextTitle;
    private EditText editTextDescription;
    private AddNoteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_note, null);


        builder.setView(view);
        builder.setTitle("Add Item");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //put whats happen
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                //listener for done button calling function applyTexts

                listener.saveNote(title, description);
            }
        });
        editTextTitle = view.findViewById(R.id.edit_add_text_title);
        editTextDescription = view.findViewById(R.id.edit_add_text_description);

        return builder.create();
    }
    //Send data to main activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface AddNoteDialogListener {
        void saveNote(String title, String description);
    }

}
