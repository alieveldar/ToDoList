package bignerdranch.android.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener, LoaderManager.LoaderCallbacks<Cursor> {
    private ToDoItemAdapter aa;
    private ArrayList<ToDoItem> todoItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment)fm.findFragmentById(R.id.ToDoListFragment);
        todoItems = new ArrayList<ToDoItem>();
        int resID= R.layout.todolist_item;
        aa = new ToDoItemAdapter(this, resID, todoItems);
        toDoListFragment.setListAdapter(aa);
        getLoaderManager().initLoader(0, null, this);


    }

    @Override
    public void onResume(){
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        CursorLoader loader = new CursorLoader(this, ToDoContentProvider.CONTENT_URI, null, null, null,null);

        return loader;
    }
    @Override
    public void onNewItemAdded(String newitem) {
        ContentResolver cr = getContentResolver();
        ContentValues  values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, newitem);

        cr.insert(ToDoContentProvider.CONTENT_URI, values);
        getLoaderManager().restartLoader(0, null, this);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);

        todoItems.clear();
        while (cursor.moveToNext()){
            ToDoItem newItem  = new ToDoItem(cursor.getString(keyTaskIndex));
            todoItems.add(newItem);
        }
        aa.notifyDataSetChanged();
    }



    @Override
    public void onLoaderReset(Loader loader) {

    }
}
