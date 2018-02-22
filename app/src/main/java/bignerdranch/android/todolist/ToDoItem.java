package bignerdranch.android.todolist;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by modus on 2/20/18.
 */

public class ToDoItem {
    String task;
    Date created;

    public String getTask(){
        return task;
    }

    public Date getCreated() {
        return created;
    }

    public ToDoItem(String _task) {
        this(_task, new Date(java.lang.System.currentTimeMillis()));
    }
    public ToDoItem(String _task, Date _created) {
        task = _task;
        created = _created;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");
        String dateString = sdf.format(created);
        return "(" + dateString + ")" + task;
    }
}
