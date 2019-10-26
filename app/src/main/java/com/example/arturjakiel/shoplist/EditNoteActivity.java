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

public class EditNoteActivity extends AppCompatDialogFragment {

    private EditText editEditTextTitle;
    private EditText editEditTextDescription;
    private EditNoteDialogListener editlistener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //bundle here
        final Bundle nodeArgs = getArguments();
        final int noteID = nodeArgs.getInt(MainActivity.ID_INDICATOR);
        String oldTitle = nodeArgs.getString(MainActivity.TITLE_INDICATOR);
        String oldDescription = nodeArgs.getString(MainActivity.DESCRIPTION_INDICATOR);
//




        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_edit_note, null);

//        editEditTextTitle.setText(nodeArgs.getString("Title"));
//        editEditTextDescription.setText(nodeArgs.getString("Description"));

        builder.setView(view)
                .setTitle("Edit Item")
                .setMessage("Item no:  "+noteID+"\n"+oldTitle+"\n"+oldDescription)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = editEditTextTitle.getText().toString();
                        String description = editEditTextDescription.getText().toString();
                        int id = noteID;

                        editlistener.editNote(id,title,description);

                    }
                });
        editEditTextTitle = view.findViewById(R.id.edit_edit_text_title);
        editEditTextDescription = view.findViewById(R.id.edit_edit_text_description);

        editEditTextTitle.setText(oldTitle);
        editEditTextDescription.setText(oldDescription);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            editlistener= (EditNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }


    }

    public interface EditNoteDialogListener{
        void editNote(int Id,String title, String description);
    }
}
