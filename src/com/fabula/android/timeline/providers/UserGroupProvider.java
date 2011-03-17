package com.fabula.android.timeline.providers;

import java.util.HashMap;

import com.fabula.android.timeline.database.SQLStatements;
import com.fabula.android.timeline.models.Group.GroupColumns;
import com.fabula.android.timeline.models.User.UserColumns;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class UserGroupProvider extends BaseContentProvider {
	
	public static final String AUTHORITY = "com.fabula.android.timeline.providers.usergroupprovider";
	
	private static HashMap<String, String> usersToGroupsProvider;
	
    public static final Uri CONTENT_URI = 
        Uri.parse("content://"+ AUTHORITY + "/userGroup");
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        
		Long rowID = super.getUserDatabase().insert(SQLStatements.USER_GROUP_DATABASE_TABLE_NAME, "", values);
		
		if(rowID > 0){
			Uri userGroupUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(userGroupUri, null);
			return userGroupUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}
	
	@Override
	public Cursor query(Uri uri, String[] columns, String where,
			String[] whereArgs, String sortOrder) {
		
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	      sqlBuilder.setTables(SQLStatements.USER_GROUP_DATABASE_TABLE_NAME);
	      
	      Cursor c = sqlBuilder.query(
		    		 super.getUserDatabase(), 
		 	         columns, 
		 	         where, 
		 	         whereArgs, 
		 	         null, 
		 	         null, 
		 	         sortOrder);

		      c.setNotificationUri(getContext().getContentResolver(), uri);
		      return c;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count = 0;
		count = super.getUserDatabase().delete(SQLStatements.USER_GROUP_DATABASE_TABLE_NAME, where, whereArgs);
		return count;
	}
	
	static {
		usersToGroupsProvider = new HashMap<String, String>();
		usersToGroupsProvider.put(GroupColumns._ID, GroupColumns._ID);
		usersToGroupsProvider.put(UserColumns.USER_NAME, UserColumns.USER_NAME);
	}

}
