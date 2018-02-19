package bignerdranch.android.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {
    private ArrayAdapter<String> aa;
    private ArrayList<String> todoItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment)fm.findFragmentById(R.id.ToDoListFragment);
        todoItems = new ArrayList<String>();
        int resID= R.layout.todolist_item;
        aa = new ArrayAdapter<String>(this, resID, todoItems);
        toDoListFragment.setListAdapter(aa);


    }

    @Override
    public void onNewItemAdded(String newitem) {
        todoItems.add(newitem);
        aa.notifyDataSetChanged();
    }
}
