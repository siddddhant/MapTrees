package com.example.maptrees;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Collaborate extends Activity{

	private final String TAG="com.example.maptrees.TAGS";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	int prevTextViewId=0;
	boolean animation=false;
	int maxTags=5;
	File photoFile;
	String mCurrentPhotoPath;
	static Uri capturedImageUri=null;
	JSONParser jsonParser = new JSONParser();
	ImageView imageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_collaboration);
		Intent i=getIntent();
		Bundle bundle=i.getExtras();
		Session session =Session.getActiveSession();
		if(bundle!=null && bundle.containsKey("success")){
			Boolean sentImage=(Boolean) bundle.get("success");
			if(sentImage==true){
				findViewById(R.id.sendToServer).setVisibility(View.GONE);
				findViewById(R.id.shareOnFacebook).setVisibility(View.VISIBLE);
			}
		}
		else
			findViewById(R.id.shareOnFacebook).setVisibility(View.INVISIBLE);
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        photoFile=null;
	    	try{
	    		photoFile=createImageFile();
	    	} catch(IOException e){
	    		e.printStackTrace();
	    		Log.i(TAG,"IOError");
	    	}
	    	if(photoFile!=null){
	    		capturedImageUri=Uri.fromFile(photoFile);
	    	
	    		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
	    		//takePictureIntent.putExtra("fileLocation",Uri.fromFile(photoFile));
	    		startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    	}
	    	else{
	    		Toast.makeText(this, "photoFile Error", Toast.LENGTH_SHORT).show();
	    	}
	        
	    }
	    maxTags=5;
	    
	    GPSTracker gpsTracker = new GPSTracker(this);
	    if(gpsTracker.canGetLocation()){
	    	String stringLatitude = String.valueOf(gpsTracker.latitude);
	    	String stringLongitude=String.valueOf(gpsTracker.longitude);
	    	TextView latti=(TextView)findViewById(R.id.latitude);
	    	TextView longi=(TextView)findViewById(R.id.longitude);	
	    	longi.setText(stringLongitude);
	    	latti.setText(stringLatitude);
	    }
	    
	    
	    

	}

	

	
	public void shiftLeft(View view)
	{
		/*
		if(animation==false)
		{
			ImageView img_animation=(ImageView) findViewById(R.id.imageView1);
			TranslateAnimation moveLefttoRight = new TranslateAnimation(0, -200, 0, 0);
			moveLefttoRight.setDuration(500);
			moveLefttoRight.setFillAfter(true);
			img_animation.startAnimation(moveLefttoRight);  // start animation
			animation=true;
		}*/
		if(maxTags==0)
			return;
		else if(maxTags==1){
	    	TextView tagtext=(TextView) findViewById(R.id.tag);
	    	tagtext.setVisibility(View.GONE);
	    	Button btntag=(Button) findViewById(R.id.btntag);
	    	btntag.setVisibility(View.GONE);
		}
	    else if(maxTags==2){
	    		Toast.makeText(this, "maximum limit of tags reached", Toast.LENGTH_SHORT);
	    	
	    	
	    }
	    
	    maxTags--;
		LinearLayout linearlayout=(LinearLayout) findViewById(R.id.linearTags);
	    TextView tag=(TextView) findViewById(R.id.tag);
	    final TextView textview=new TextView(this);
	    textview.setText(tag.getText());
	    tag.setText("");
	    int curTextViewId=prevTextViewId+1;
	    textview.setId(curTextViewId);
	    textview.setTextSize(20);
	    final LinearLayout.LayoutParams params = 
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
	    textview.setLayoutParams(params);
	    prevTextViewId++;
	    linearlayout.addView(textview,params);
	    
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG,"Reached on activity result");
	    if (resultCode == RESULT_OK) {
	        if (requestCode == REQUEST_IMAGE_CAPTURE) {

	            //use imageUri here to access the image
	        	/*if(data==null){
	        		Toast.makeText(this, "Error in data returned", Toast.LENGTH_SHORT).show();
	        		return;
	        	}
	        	Log.i(TAG,"Request code is OK");
	            Bundle extras = data.getExtras();
	            for(String s:extras.keySet()){
	            	Log.i(TAG,s);
	            }
	          
	           Bitmap bmp = (Bitmap) extras.get("data");
	           
	            
				*/
	            // here you will get the image as bitmap
	        	Log.i(TAG,mCurrentPhotoPath);
	        	
	        	BitmapFactory.Options options = new BitmapFactory.Options();;
	        	BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
	        	int outHeight = options.outHeight;
	            int outWidth = options.outWidth;
	            int inSampleSize = 1;
	            if (outHeight > 350 || outWidth > 200)
	            {
	                inSampleSize = outWidth > outHeight
	                                   ? outHeight / 350
	                                   : outWidth / 200;
	            }
	            options.inSampleSize = inSampleSize;
	            options.inJustDecodeBounds = false;
	            Bitmap bmp=BitmapFactory.decodeFile(photoFile.getAbsolutePath(),options);
	            imageview=(ImageView) findViewById(R.id.imageView1);
	            imageview.setImageBitmap(bmp);
	            
	            
	        } 
	          else if (resultCode == RESULT_CANCELED) {
	            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
	           } 
	         }


	    }
	
	  
	

	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    Log.i(TAG,"Creted Name");
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    Log.i(TAG,"Created Dir");
	    Log.i(TAG,storageDir.getAbsolutePath());
	    Log.i(TAG,imageFileName);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    Log.i(TAG,"Saved Name");
	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    Log.i(TAG,"Returning Image");
	    galleryAddPic();
	    return image;
	}

		
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}


	public void sendData(View view){
		ImageView imageView=(ImageView)findViewById(R.id.imageView1);
		imageView.buildDrawingCache();
		Bitmap bm = imageView.getDrawingCache();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray();

		String encodedImage = Base64.encodeToString(b , Base64.DEFAULT);
		new CreateNewProduct(encodedImage,"Srivastava","").execute();
		
	}
	
	public void shareImageFacebook(View view){
		Log.i(TAG,"I have reached here");
		Session session=Session.getActiveSession();
		if(session==null){
			Toast.makeText(this, "No user Logged IN",Toast.LENGTH_SHORT);
			return;
		}
		
		Log.i(TAG,"I have reached here");
		Bundle postParams=new Bundle();
	    //postParams.putString("name", "MapTrees");
        //postParams.putString("caption", "Mapping trees to their GPS locations");
        //postParams.putString("description", "An IIMB initiative to map trees to their GPS Locations");
        //postParams.putString("link", "https://developers.facebook.com/android");
        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
		
		Log.i(TAG,"I have reached here");
		Request.Callback callback=new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				// TODO Auto-generated method stub
				if(response==null){
					Log.i(TAG,"Response is null");
				}
				Log.i(TAG,response.toString());
				JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                        
                        
				String postId = null;
				try {
                    postId = graphResponse.getString("id");
                } catch (JSONException e) {
                    Log.i(TAG,
                        "JSON error "+ e.getMessage());
                }
                FacebookRequestError error = response.getError();
                if (error != null) {
                	Toast.makeText(getApplicationContext(), error.getErrorCode(), Toast.LENGTH_LONG).show();
                    
                    }
                else {
                	Toast.makeText(getApplicationContext(), postId, Toast.LENGTH_LONG).show();
                        
                }
			}
		};
		  Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
                  

		  RequestAsyncTask task = new RequestAsyncTask(request);
		  task.execute();			
		}
	



class CreateNewProduct extends AsyncTask<String, String, String> {
	 
    /**
     * Before starting background thread Show Progress Dialog
     * */
	private String name,image,description;
	public CreateNewProduct(String image,String name,String description){
		this.name=name;
		this.image=image;
		this.description=description;
	}
	private static final String url_create_product = "http://10.0.2.2/android_connect/create_product.php";
	 private static final String TAG_SUCCESS = "success";
	 private ProgressDialog pDialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(Collaborate.this);
        pDialog.setMessage("Sending Image..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    /**
     * Creating product
     * */
    protected String doInBackground(String... args) {
    	Log.i(TAG,"In do in background");
        /*String name = "Siddhant";
        String image = "Woah";
        String description = "description";*/

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("image", image));
        params.add(new BasicNameValuePair("description", description));
        Log.i(TAG,"In do in background");
        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                "POST", params);
        Log.i(TAG,"In do in background");
        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // successfully created product
                Intent i = new Intent(getApplicationContext(), Collaborate.class);
                i.putExtra("success", true);
                startActivity(i);

                // closing this screen
                finish();
            } else {
                // failed to create product
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
}
