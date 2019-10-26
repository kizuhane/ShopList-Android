package com.example.kizuhane.shoplist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddNoteActivity.AddNoteDialogListener , EditNoteActivity.EditNoteDialogListener{
    public static final String EXTRA_ID = "com.example.kizuhane.shoplist.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.kizuhane.shoplist.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION  = "com.example.kizuhane.shoplist.EXTRA_DESCRIPTION";
    public static final String EXTRA_COMPLETE = "com.example.kizuhane.shoplist.EXTRA_COMPLETE";
    public static final int ADD_NOTE_REQUEST = 1;

    // Name value
    public static final String ID_INDICATOR = "com.example.kizuhane.shoplist.ID_INDICATOR";
    public static final String TITLE_INDICATOR = "com.example.kizuhane.shoplist.TITLE_INDICATOR";
    public static final String DESCRIPTION_INDICATOR = "com.example.kizuhane.shoplist.DESCRIPTION_INDICATOR";
    public static final String COMPLETE_INDICATOR = "com.example.kizuhane.shoplist.COMPLETE_INDICATOR";



    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteActivity AddNoteDialog = new AddNoteActivity();
                AddNoteDialog.show(getSupportFragmentManager(), "Add Note dialog");
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //update RecyclerView
                adapter.setNotes(notes);
            }
        });

        // Delete note by swiping
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //edit
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {

                //to send arguments to dialog
                Bundle nodeArgs = new Bundle();
                nodeArgs.putInt(ID_INDICATOR,note.getId());
                nodeArgs.putString(TITLE_INDICATOR,note.getTitle());
                nodeArgs.putString(DESCRIPTION_INDICATOR,note.getDescription());
                //call Dialog
                EditNoteActivity editNoteActivity = new EditNoteActivity();
                editNoteActivity.setArguments(nodeArgs);//push arguments to dialog
                editNoteActivity.show(getSupportFragmentManager(),"Edit Note dialog");
            }
        });
        //check box
        adapter.setOnCheckBoxListener(new NoteAdapter.OnCheckBoxListener() {
            @Override
            public void onCheckBoxClick(Note note) {
                int noteCompletedStatus ;
                // mark as completed
                if(note.getId() == -1) {
                    Toast.makeText(MainActivity.this, "Note cant be mark as completed", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check what value data base have of this note Complete value
                if(note.getComplete()==1){
                    noteCompletedStatus = 0;
                }else {
                    noteCompletedStatus = 1;
                }
                //send ob click opposite value to data base
                Note UpNote = new Note(note.getTitle(),note.getDescription(),noteCompletedStatus);
                UpNote.setId(note.getId());
                noteViewModel.update(UpNote);
            }
        });
    }

    // ADD NOTE LOGIC
    @Override
    public void saveNote(String title, String description) {
        //here put whats whats send
        if (title.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Item Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Note note = new Note(title, description, 0);
        noteViewModel.insert(note);
        Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
    }
    // EDIT NOTE LOGIC
    @Override
    public void editNote(int id,String title, String description) {

        if (title.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Item Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(id == -1) {
            Toast.makeText(this, "Note " + id + " can't be updated", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(title, description, 0);
        note.setId(id);
        noteViewModel.update(note);
        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
    }

    //main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All item deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete_done_notes:
                noteViewModel.deleteAllCompleteNotes();
                Toast.makeText(this, "All completed item deleted", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
