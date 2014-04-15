package com.example.databaseHandlers;

import java.util.ArrayList;
import java.util.List;

import com.example.maptrees.Images;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "imageManager";
 
    //  table names
    private static final String TABLE_IMAGES = "IMAGES_DATABASE";
    private static final String TABLE_FACEBOOK_SHARES="FACEBOOK_SHARES";
 
    // Images Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE_NAME = "image_name";
    private static final String KEY_IMAGE_ADDRESS = "image_address";
    private static final String KEY_SENT="sent";

    // Share Table columns
    private static final String KEY_USER_ID="user_id";
    private static final String KEY_USER_NAME="user_name";
    private static final String KEY_POST_ID="post_id";
    private static final String KEY_UNIQUE_ID="id";
    private static final String KEY_LIKES="likes";
    private static final String KEY_RESHARES="reshares";
    private static final String KEY_COMMENTS="comments";
    private static final String KEY_CREATED_AT="created_at";
    
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGE_NAME + " TEXT,"
                + KEY_IMAGE_ADDRESS + " TEXT," + KEY_SENT+" INTEGER"+")";
		
		String CREATE_FACEBOOK_SHARES_TABLE="CREATE TABLE "
	            + TABLE_FACEBOOK_SHARES + "(" + KEY_UNIQUE_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME
	            + " TEXT," + KEY_POST_ID + " TEXT," + KEY_LIKES + " INTEGER,"+ KEY_USER_ID + " TEXT,"+ KEY_LIKES +
	            " INTEGER,"+ KEY_RESHARES + " INTEGER,"+ KEY_COMMENTS + " INTEGER,"
	            + KEY_CREATED_AT   + " DATETIME" + ")";
		
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_FACEBOOK_SHARES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACEBOOK_SHARES); 
        // Create tables again
        onCreate(db);
		
	}
	
	public void addImageData(Images image){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(KEY_IMAGE_NAME, image.get_imageName()); // Contact Name
	    values.put(KEY_IMAGE_ADDRESS, image.get_imageAddress()); // Contact Phone Number
	    if(image.isSent()){
	    	values.put(KEY_SENT, "1");
	    }
	    else
	    	values.put(KEY_SENT, "0");
	    // Inserting Row
	    db.insert(TABLE_IMAGES, null, values);
	    db.close(); // Closing database connection
		
	}
	public boolean toBool(int val){
		if(val==0)
			return false;
		else
			return true;
	}
	
	public Images getImageData(int id){
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
		            KEY_IMAGE_NAME, KEY_IMAGE_ADDRESS,KEY_SENT }, KEY_ID + "=?",
		            new String[] { String.valueOf(id) }, null, null, null, null);
		 if(cursor!=null){
			 cursor.moveToFirst();
		 }
		 Images image=new Images(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),toBool(Integer.parseInt(cursor.getString(3))));
		 return image;
		 
	}
	public List<Images> getAllImageData(){
		List<Images> imageList=new ArrayList<Images>();
		String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	            Images image = new Images();
	            image.set_id(Integer.parseInt(cursor.getString(0)));
	            image.set_imageName(cursor.getString(1));
	            image.set_imageAddress(cursor.getString(2));
	            image.setSent(toBool(Integer.parseInt(cursor.getString(3))));
	            // Adding contact to list
	            imageList.add(image);
	        } while (cursor.moveToNext());
	        
	    }
	 
		return imageList;
		
	}
	
	public int getCountImagesData(){
		String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
	
	public int updateImageData(String imageAddress){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SENT, "1");
		return db.update(TABLE_IMAGES, values, KEY_IMAGE_ADDRESS + " = ?",
	            new String[] { imageAddress });
	}
	public void deleteImageData(Images image) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_IMAGES, KEY_ID + " = ?",
	            new String[] { String.valueOf(image.get_id()) });
	    db.close();
	}

}
