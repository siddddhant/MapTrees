package geo.mapper.maptrees;

import geo.mapper.databasehandlers.DatabaseHandler;
import geo.mapper.databasehandlers.FacebookShare;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import com.example.maptrees.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;





public class MainFragment extends Fragment {

	private static final String TAG = MainFragment.class.getSimpleName();

	private UiLifecycleHelper uiHelper;
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private DatabaseHandler databaseHandler;
	private final List<String> permissions;

	public MainFragment() {
		permissions = Arrays.asList("user_status");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Log.i("POS","In onCreateView");
		View view = inflater.inflate(R.layout.activity_main, container, false);
	    
		
		profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		// Find the user's name view
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
	
		authButton.setPublishPermissions(Arrays.asList("publish_stream"));
		
		Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	    	Log.i("TAG","Session is ok");
	        makeMeRequest(session);
	        Log.i("TAG","Going got facebook");
	        facebookShareData(session);
	    }
		
		return view;
	}
	
	private void facebookShareData(final Session session){
		try{
			databaseHandler=new DatabaseHandler(getActivity().getApplicationContext());
			List<FacebookShare> fbs= databaseHandler.getAllFacebookShareData();
			int totalLikes;
			
			for(FacebookShare fb:fbs){
				
				RequestBatch batch=new RequestBatch();
				Log.i("postId",fb.getPostId());
				Log.i("Task","Executing task");
				final String postId=fb.getPostId();
				Bundle params = new Bundle();
				params.putString("summary", "true");
				Log.i("Task","Executing task");
				final String likes,shares,comments;
				
				//params.putString("fields", "shares");
				Log.i("Task","Executing task");
				Request request = new Request(session, "/"+postId+"/comments", params, HttpMethod.GET, new Request.Callback() {

				    @Override
				    public void onCompleted(Response response) {
				        try {
				        	Log.i("Task","Executing task");
				            JSONObject res = response.getGraphObject().getInnerJSONObject();
				            Log.i("TAG",res.toString());
				            JSONObject result=res.getJSONObject("summary");
				            Log.i("RESULT",result.getString("total_count"));
				            databaseHandler.updateFacebookPostComments(postId,result.getString("total_count"));
				            
				        } catch (Exception e) {
				        	Log.i("Error","Exception in onCompleted");
				        	e.printStackTrace();

				        }
				    }
				});
				batch.add(request);
				request=new Request(session,"/"+postId+"/likes", params, HttpMethod.GET, new Request.Callback(){

					@Override
					public void onCompleted(Response response) {
						// TODO Auto-generated method stub
						 try {
					        	Log.i("Task","Executing task for likes");
					            JSONObject res = response.getGraphObject().getInnerJSONObject();
					            Log.i("TAG",res.toString());
					            JSONObject result=res.getJSONObject("summary");
					            Log.i("RESULT",result.getString("total_count"));
					            databaseHandler.updateFacebookPostLikes(postId,result.getString("total_count"));
					            
					        } catch (Exception e) {
					        	Log.i("Error","Exception in onCompleted");
					        	e.printStackTrace();

					        }
					    }
						
					
					
				});
				batch.add(request);
				
				/*request=new Request(session,"/"+postId+"/shares", params, HttpMethod.GET, new Request.Callback(){

					@Override
					public void onCompleted(Response response) {
						// TODO Auto-generated method stub
						 try {
					        	Log.i("Task","Executing task for shares");
					        	Log.i("DATA",response.toString());
					        	JSONObject res = response.getGraphObject().getInnerJSONObject();
					            Log.i("TAG",res.toString());
					            
					        } catch (Exception e) {
					        	Log.i("Error","Exception in onCompleted");
					        	e.printStackTrace();

					        }
					    }
						
					
					
				});
				
				batch.add(request);
				*/
				RequestAsyncTask task = new RequestAsyncTask(batch);
				Log.i("Task","Executing task");
				task.execute();
			}
			
			
	        databaseHandler.close();
		}
		catch(NullPointerException e){
			Log.i("ERROR","There is an error!");
			e.printStackTrace();
		}
			

	}
	
	private void makeMeRequest(final Session session) {
	    // Make an API call to get user data and define a 
	    // new callback to handle the response.
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            // If the response is successful
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                    // Set the id for the ProfilePictureView
	                    // view that in turn displays the profile picture.
	                    profilePictureView.setProfileId(user.getId());
	                    Log.i("user id",user.getId());
	                    Log.i("user info",user.getUsername());
	                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	                    SharedPreferences.Editor editor = preferences.edit();
	                    editor.putString("userId", user.getId());
	                    editor.putString("userName", user.getUsername());
	                    editor.commit();
	                    // Set the Textview's text to the user's name.
	                    userNameView.setText("Welcome "+user.getName());	
	                    
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors, will do so later.
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			makeMeRequest(session);
			facebookShareData(session);
			Toast.makeText(getActivity(), session.getAccessToken(), Toast.LENGTH_SHORT).show();
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	
	
}