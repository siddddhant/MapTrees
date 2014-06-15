package geo.mapper.maptrees;

import geo.mapper.databasehandlers.DatabaseHandler;
import geo.mapper.databasehandlers.FacebookShare;
import geo.mapper.maptrees.Collaborate.CreateNewProduct;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.maptrees.R;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private static List<Images> itemDetailsarrayList;
	private ArrayList<Images> arrayList;
	private LayoutInflater l_Inflater;
	private String TAG = "in Adapter";
	Context context;

	public LazyAdapter(Context context, List<Images> images) {
		itemDetailsarrayList = images;
		Log.i("Number", String.valueOf(images.size()));
		this.arrayList = new ArrayList<Images>();
		this.arrayList.addAll(images);
		l_Inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemDetailsarrayList.size();
	}

	@Override
	public Images getItem(int arg0) {
		// TODO Auto-generated method stub
		return itemDetailsarrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = l_Inflater.inflate(R.layout.list_row, null);

		TextView imageName = (TextView) vi.findViewById(R.id.imageName); //
		final TextView imageAddress = (TextView) vi
				.findViewById(R.id.imageAddress); //
		final TextView sent = (TextView) vi.findViewById(R.id.sent);
		final TextView shared = (TextView) vi.findViewById(R.id.shared);
		final Button btn = (Button) vi.findViewById(R.id.buttonShare);
		final Button btn1 = (Button) vi.findViewById(R.id.buttonSend);
		final ImageView thumb_image = (ImageView) vi
				.findViewById(R.id.list_image); // thumb image
		imageName.setText("Image Name: "
				+ itemDetailsarrayList.get(position).get_imageName());
		imageAddress.setText("Image Address: "
				+ itemDetailsarrayList.get(position).get_imageAddress());
		if (itemDetailsarrayList.get(position).isSent()) {
			sent.setText("Sent: True");
			btn1.setVisibility(View.GONE);
		} else {
			sent.setText("Sent: False");
			btn1.setVisibility(View.VISIBLE);
		}
		if (itemDetailsarrayList.get(position).isShared()) {
			shared.setText("Shared on FB: True");
			btn.setVisibility(View.GONE);
		} else {
			shared.setText("Shared on FB: False");
			btn.setVisibility(View.VISIBLE);
		}
		Bitmap bmp = BitmapFactory.decodeFile(itemDetailsarrayList
				.get(position).get_imageAddress());
		thumb_image.setImageBitmap(bmp);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub

				String address = itemDetailsarrayList.get(position)
						.get_imageAddress();
				thumb_image.buildDrawingCache();
				Bitmap bm = thumb_image.getDrawingCache();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
				byte[] data = null;
				data = baos.toByteArray();

				new shareFacebook(data, address).execute();
							

			}
		});

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(v.getContext(),
						itemDetailsarrayList.get(position).get_imageAddress(),
						Toast.LENGTH_SHORT).show();
				thumb_image.buildDrawingCache();
				Bitmap bm = thumb_image.getDrawingCache();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the
																	// bitmap
																	// object
				byte[] b = baos.toByteArray();

				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
				new CreateNewProduct(encodedImage, "Srivastava", "", "", "",
						imageAddress.getText().toString()).execute();
			}
		});

		return vi;
	}

	public void filter(String text) {
		Log.i("Error", text);
		itemDetailsarrayList.clear();
		if (text == "all")
			itemDetailsarrayList.addAll(arrayList);
		else if (text.equals("sent")) {
			Log.i("Error!", "In  sent");
			for (Images image : arrayList) {
				if (image.isSent())
					itemDetailsarrayList.add(image);
			}
		} else if (text.equals("nonsent")) {
			Log.i("Error!", "In not sent");
			for (Images image : arrayList) {
				Log.i("Not sent", String.valueOf(image.isSent()));
				if (image.isSent() == false)
					itemDetailsarrayList.add(image);
			}
		}
		notifyDataSetChanged();

	}

	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		private String name, image, imageAddress;
		private CharSequence tag;
		private String longitude, lattitude;
		JSONParser jsonParser = new JSONParser();

		public CreateNewProduct(String image, String name, CharSequence tag,
				String longitude, String lattitude, String address) {
			this.name = name;
			this.image = image;
			this.tag = tag;
			this.lattitude = lattitude;
			this.longitude = longitude;
			this.imageAddress = address;
		}

		private static final String url_create_product = "http://10.0.2.2/android_connect/create_product.php";
		private static final String TAG_SUCCESS = "success";
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sending Image..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			Log.i("Prod", "In do in background");
			/*
			 * String name = "Siddhant"; String image = "Woah"; String
			 * description = "description";
			 */

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("image", image));
			params.add(new BasicNameValuePair("tag", (String) tag));
			params.add(new BasicNameValuePair("lattitude", String
					.valueOf(lattitude)));
			params.add(new BasicNameValuePair("longitude", String
					.valueOf(longitude)));

			Log.i("prod", "In do in background");
			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check log cat fro response

			// check for success tag

			try {

				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					DatabaseHandler db = new DatabaseHandler(context);
					db.updateImageData(imageAddress);
					// closing this screen

				} else {
					// failed to create product
					Toast.makeText(activity,
							"Failed in saving Data at the server",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Log.i("prod", "There is an error");
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(activity, "Error in Sending Data",
								Toast.LENGTH_SHORT).show();
					}
				});

				e.printStackTrace();
			} catch (NullPointerException e) {

				e.printStackTrace();
				((Activity) context).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(context, "Error in Sending Data",
								Toast.LENGTH_SHORT).show();
					}
				});

				// Toast.makeText(activity, "Error in Sending Data",
				// Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
	
	
	
	class shareFacebook extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;
		private boolean pDialogWorking = true;
		byte[] data;
		private String mCurrentPhotoPath;
		public shareFacebook(byte[] data,String mCurrentPhotoPath){
			this.data=data;
			this.mCurrentPhotoPath=mCurrentPhotoPath;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sharing Image..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i(TAG, "I have reached here");
			Session session = Session.getActiveSession();
			if (session == null) {
				((Activity)context).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText((Activity)context, "No user Logged IN", Toast.LENGTH_SHORT).show();
					}
				});
				
				return null;
			}
			
			Bundle postParams = new Bundle();
			postParams.putString("name", "MapTrees");
			postParams.putString("caption",
					"Mapping trees to their GPS locations");
			postParams.putString("description",
					"An IIMB initiative to map trees to their GPS Locations");
			postParams.putString("link",
					"https://developers.facebook.com/android");
			postParams.putByteArray("picture", data);
			;

			Log.i(TAG, "I have reached here");
			Request.Callback callback = new Request.Callback() {

				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
					if (response == null) {
						((Activity)context).runOnUiThread(new  Runnable() {
							public void run() {
								Toast.makeText(context, "Error in sharing Image! Check internet Connection", Toast.LENGTH_SHORT);
							}
						});
						return;
					}
					Log.i(TAG, response.toString());
					JSONObject graphResponse = null;
					try{
					graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					}
					catch(NullPointerException e){
						e.printStackTrace();
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//TextView errorView=(TextView) findViewById(R.id.errorData);
								//errorView.setText("Error in Sharing Image!");
								//errorView.setVisibility(View.VISIBLE);
								Toast.makeText(context, "Error in Sharing Image", Toast.LENGTH_SHORT).show();
							}
						});
					}
					String postId ="";
					try {
						Log.i(TAG, graphResponse.toString());
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i(TAG, "JSON error " + e.getMessage());
					}
					catch(NullPointerException e){
						e.printStackTrace();
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//TextView errorView=(TextView) findViewById(R.id.errorData);
								//errorView.setText("Error in Sharing Image!");
								//errorView.setVisibility(View.VISIBLE);
								Toast.makeText(context, "Error in Sharing Image", Toast.LENGTH_SHORT).show();
							}
						});
					}
					final FacebookRequestError error = response.getError();
					if (error != null) {
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//TextView errorView=(TextView) findViewById(R.id.errorData);
								//errorView.setText("Error in Sharing Image!");
								//errorView.setVisibility(View.VISIBLE);
								Toast.makeText(context,
										"Couldn't connect to FB", Toast.LENGTH_LONG).show();
							}
						});
						return;

					} else {
						final String temp=postId;
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//TextView errorView=(TextView) findViewById(R.id.errorData);
								//errorView.setVisibility(View.GONE);
							}
						});
						
						DatabaseHandler db = new DatabaseHandler(
								context);
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(context);
						FacebookShare share = new FacebookShare(
								preferences.getString("userId", ""),
								preferences.getString("userName", ""),
								mCurrentPhotoPath, postId);
						db.addFacebookSharedata(share);

						List<FacebookShare> fbs = db.getAllFacebookShareData();
						for (FacebookShare fb : fbs) {
							Log.i("postId", fb.getPostId());
						}

						Log.i("currentPath", mCurrentPhotoPath);
						int result = db
								.updateImageSharedData(mCurrentPhotoPath);
						Log.i("RES", String.valueOf(result));
						db.close();
					}
				}
			};
			// Request request = new Request(session, "me/feed", postParams,
			// HttpMethod.POST, callback);
			Request request = new Request(session, "me/photos", postParams,
					HttpMethod.POST, callback);
			final RequestAsyncTask task = new RequestAsyncTask(request);
			((Activity)context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					task.execute();
				}
			});
			

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done	
			if (pDialogWorking)
				pDialog.dismiss();
		}

	}


}