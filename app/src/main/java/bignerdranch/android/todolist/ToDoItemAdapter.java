package bignerdranch.android.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by modus on 2/22/18.
 */

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    int resource;

    public ToDoItemAdapter(@NonNull Context context, int resource, List<ToDoItem> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent){
        LinearLayout todoView;

        ToDoItem item = getItem(position);

        String taskString = item.getTask();
        Date createedDate = item.getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateSttring = sdf.format(createedDate);

        if (convertView == null) {
            todoView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource,todoView,true);

        }else {
            todoView = (LinearLayout) convertView;
        }

        TextView dateView = (TextView)todoView.findViewById(R.id.rowDate);
        TextView taskView = (TextView)todoView.findViewById(R.id.row);

        dateView.setText(dateSttring);
        taskView.setText(taskString);

        return todoView;

    }
}
