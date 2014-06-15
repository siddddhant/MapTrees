package geo.mapper.databasehandlers;

import geo.mapper.maptrees.Images;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "imageManager";

	// table names
	private static final String TABLE_IMAGES = "IMDATA";
	private static final String TABLE_FACEBOOK_SHARES = "FACEBOOK_SHARES";

	// Images Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_IMAGE_NAME = "image_name";
	private static final String KEY_IMAGE_ADDRESS = "image_address";
	private static final String KEY_SENT = "sent";
	private static final String KEY_SHARED = "shared";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_TAG = "tag";

	// Share Table columns
	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_POST_ID = "post_id";
	private static final String KEY_UNIQUE_ID = "id";
	private static final String KEY_LIKES = "likes";
	private static final String KEY_RESHARES = "reshares";
	private static final String KEY_COMMENTS = "comments";
	private static final String KEY_CREATED_AT = "created_at";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_IMAGE_NAME + " TEXT,"
				+ KEY_IMAGE_ADDRESS + " TEXT," + KEY_SENT + " INTEGER,"
				+ KEY_SHARED + " INTEGER," + KEY_LATITUDE + " TEXT,"
				+ KEY_LONGITUDE + " TEXT," + KEY_TAG + " TEXT" + ")";

		String CREATE_FACEBOOK_SHARES_TABLE = "CREATE TABLE "
				+ TABLE_FACEBOOK_SHARES + "(" + KEY_UNIQUE_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"
				+ KEY_IMAGE_ADDRESS + " TEXT," + KEY_POST_ID + " TEXT,"
				+ KEY_LIKES + " INTEGER," + KEY_USER_ID + " TEXT,"
				+ KEY_RESHARES + " INTEGER," + KEY_COMMENTS + " INTEGER,"
				+ KEY_CREATED_AT + " DATETIME" + ")";

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

	public void addFacebookSharedata(FacebookShare share) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		values.put(KEY_USER_NAME, share.getUserName());
		values.put(KEY_IMAGE_ADDRESS, share.getImageAddress());
		values.put(KEY_LIKES, share.getLikes());
		values.put(KEY_POST_ID, share.getPostId());
		values.put(KEY_USER_ID, share.getUserId());
		values.put(KEY_RESHARES, share.getReShares());
		values.put(KEY_COMMENTS, share.getComments());
		values.put(KEY_CREATED_AT, dateFormat.format(date));

		db.insert(TABLE_FACEBOOK_SHARES, null, values);
		db.close(); // Closing database connection
	}

	public FacebookShare getFacebookShareData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT  * FROM " + TABLE_FACEBOOK_SHARES + " WHERE "
				+ KEY_UNIQUE_ID + " = " + id;
		Log.i("query", query);
		Cursor c = db.rawQuery(query, null);
		if (c != null)
			c.moveToFirst();
		FacebookShare fb = new FacebookShare();
		fb.setComments(c.getInt(c.getColumnIndex(KEY_COMMENTS)));
		fb.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
		fb.setId(c.getInt(c.getColumnIndex(KEY_UNIQUE_ID)));
		fb.setImageAddress(c.getString(c.getColumnIndex(KEY_IMAGE_ADDRESS)));
		fb.setLikes(c.getInt(c.getColumnIndex(KEY_LIKES)));
		fb.setPostId(c.getString(c.getColumnIndex(KEY_POST_ID)));
		fb.setReShares(c.getInt(c.getColumnIndex(KEY_RESHARES)));
		fb.setUserId(c.getString(c.getColumnIndex(KEY_USER_ID)));
		fb.setUserName(c.getString(c.getColumnIndex(KEY_USER_NAME)));

		return fb;
	}

	public List<FacebookShare> getAllFacebookShareData() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<FacebookShare> shares = new ArrayList<FacebookShare>();
		String query = "SELECT  * FROM " + TABLE_FACEBOOK_SHARES;
		Log.i("query", query);
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst()) {
			do {
				FacebookShare fb = new FacebookShare();
				fb.setComments(c.getInt(c.getColumnIndex(KEY_COMMENTS)));
				fb.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
				fb.setId(c.getInt(c.getColumnIndex(KEY_UNIQUE_ID)));
				fb.setImageAddress(c.getString(c
						.getColumnIndex(KEY_IMAGE_ADDRESS)));
				fb.setLikes(c.getInt(c.getColumnIndex(KEY_LIKES)));
				fb.setPostId(c.getString(c.getColumnIndex(KEY_POST_ID)));
				fb.setReShares(c.getInt(c.getColumnIndex(KEY_RESHARES)));
				fb.setUserId(c.getString(c.getColumnIndex(KEY_USER_ID)));
				fb.setUserName(c.getString(c.getColumnIndex(KEY_USER_NAME)));

				// adding to todo list
				shares.add(fb);
			} while (c.moveToNext());
		}

		return shares;

	}

	public int updateFacebookShare(FacebookShare share) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LIKES, share.getImageAddress());
		values.put(KEY_RESHARES, share.getImageAddress());
		values.put(KEY_COMMENTS, share.getImageAddress());
		return db.update(TABLE_FACEBOOK_SHARES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(share.getId()) });

	}

	public void addImageData(Images image) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMAGE_NAME, image.get_imageName()); // Contact Name
		values.put(KEY_IMAGE_ADDRESS, image.get_imageAddress()); // Contact
																	// Phone
																	// Number
		values.put(KEY_LATITUDE, image.getLatitude());
		values.put(KEY_LONGITUDE, image.getLongitude());
		values.put(KEY_TAG, image.getTag());
		if (image.isSent())
			values.put(KEY_SENT, "1");
		else
			values.put(KEY_SENT, "0");

		if (image.isShared())
			values.put(KEY_SHARED, "1");
		else
			values.put(KEY_SHARED, "0");
		// Inserting Row
		db.insert(TABLE_IMAGES, null, values);
		db.close(); // Closing database connection

	}

	public boolean toBool(int val) {
		if (val == 0)
			return false;
		else
			return true;
	}

	public Images getImageData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
				KEY_IMAGE_NAME, KEY_IMAGE_ADDRESS, KEY_SENT, KEY_SHARED },
				KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Images image = new Images(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2),
				toBool(Integer.parseInt(cursor.getString(3))),
				toBool(Integer.parseInt(cursor.getString(4))),
				cursor.getString(5), cursor.getString(6), cursor.getString(7));
		Log.i("Database", String.valueOf(Integer.parseInt(cursor.getString(4))));
		return image;

	}

	public Images getImageDataFromAddress(String add) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
				KEY_IMAGE_NAME, KEY_IMAGE_ADDRESS, KEY_SENT, KEY_SHARED },
				KEY_ID + "=?", new String[] { String.valueOf(add) }, null,
				null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		Images image = new Images(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2),
				toBool(Integer.parseInt(cursor.getString(3))),
				toBool(Integer.parseInt(cursor.getString(4))),
				cursor.getString(5), cursor.getString(6), cursor.getString(7));
		Log.i("Database", String.valueOf(Integer.parseInt(cursor.getString(4))));
		return image;

	}

	public int getCountSentImages() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID },
				KEY_SENT + "=?", new String[] { String.valueOf(1) }, null,
				null, null, null);
		if (cursor != null) {
			return cursor.getCount();

		} else
			return 1;

	}

	public List<Images> getAllImageData() {
		List<Images> imageList = new ArrayList<Images>();
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
				image.setShared(toBool(Integer.parseInt(cursor.getString(4))));
				image.setLatitude(cursor.getString(5));
				image.setLongitude(cursor.getString(6));
				image.setTag(cursor.getString(7));
				// Adding contact to list
				imageList.add(image);
			} while (cursor.moveToNext());

		}

		return imageList;

	}

	public int getCountImagesData() {
		String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	public int getCountFacebookShares() {
		String countQuery = "SELECT  * FROM " + TABLE_FACEBOOK_SHARES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;

	}

	public int updateFacebookPostData(String id, String likes, String comments,
			String shares) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LIKES, likes);
		values.put(KEY_COMMENTS, comments);
		values.put(KEY_RESHARES, shares);
		return db.update(TABLE_FACEBOOK_SHARES, values, KEY_POST_ID + " = ?",
				new String[] { id });

	}

	public int updateFacebookPostLikes(String id, String likes) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LIKES, likes);

		return db.update(TABLE_FACEBOOK_SHARES, values, KEY_POST_ID + " = ?",
				new String[] { id });

	}

	public int updateFacebookPostComments(String id, String comments) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_COMMENTS, comments);

		return db.update(TABLE_FACEBOOK_SHARES, values, KEY_POST_ID + " = ?",
				new String[] { id });

	}

	public int updateFacebookPostShares(String id, String shares) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_RESHARES, shares);
		return db.update(TABLE_FACEBOOK_SHARES, values, KEY_POST_ID + " = ?",
				new String[] { id });

	}

	public int updateImageData(String imageAddress) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SENT, "1");
		return db.update(TABLE_IMAGES, values, KEY_IMAGE_ADDRESS + " = ?",
				new String[] { imageAddress });
	}

	public int updateImageSharedData(String imageAddress) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SHARED, "1");
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
