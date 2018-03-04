package bignerdranch.android.todolist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by mrx on 3/3/18.
 */

public class ToDoContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.bignerdranch.todoprovider/todoitems");
    private MySQLiteOpenHelper myOpenHelper;
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher =new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.bignerdranch.todoprovider", "todoitems", ALLROWS);
        uriMatcher.addURI("com.bignerdranch.todoprovider", "todoitems/#", SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri){
        switch (uriMatcher.match(uri)){
            case ALLROWS: return "vnd.android.cursor.dir/vnd.bignerdranch.todos";
            case SINGLE_ROW: return "vnd.android.cursor.item/vnd.bignerdranch.todos";
            default: throw new IllegalArgumentException("Usuported uri: " + uri);
        }
    }

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATION_DATE = "creation_date";


    @Override
    public boolean onCreate() {
        myOpenHelper = new MySQLiteOpenHelper(getContext(), MySQLiteOpenHelper.DATABASE_NAME, null, MySQLiteOpenHelper.DATABASE_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String groupBy = null;
        String having = null;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteOpenHelper.DATBASE_TABLE);

        switch (uriMatcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
                default:break;
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        String nullColumnHack = null;
        long id = db.insert(MySQLiteOpenHelper.DATBASE_TABLE, nullColumnHack, values);
        if (id > 1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
        else
            return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : " ");
                default:break;
        }
        if (selection == null)
            selection = "1";
        int deleteCount = db.delete(MySQLiteOpenHelper.DATBASE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : " " );
                default:break;
        }
        int updateCount = db.update(MySQLiteOpenHelper.DATBASE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "todoDatabase.db";
        private static final String DATABASE_VERSION = "1";
        private static final String DATBASE_TABLE = "todoItemTable";
        private static final String DATABASE_CREATE = "create table " + DATBASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, "+ KEY_TASK +  " text not null, " + KEY_CREATION_DATE + " long);";
        private static final String DROP_TABLE_IF_IT_EXISTS = "DROP TABLE IF IT EXISTS";
        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, String version){
            super(context, name, factory, Integer.parseInt(version));
        }
        @Override
        public void onCreate(SQLiteDatabase  db){
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w("TaskAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all data");
            db.execSQL(DROP_TABLE_IF_IT_EXISTS + DATBASE_TABLE);
            onCreate(db);
        }
    }
}
