package csie.mpp.goal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;


public class goal extends Activity {
		
	public static final String APP_ID = "117123651735429";
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
    Handler mHandler;
	
	ImageButton btn_sos;
	TextView goal_name, goal_max, goal_current, goal_level, goal_start;
	public String ngoal;
	String goal, max, current, level, start;
    public String myID;
 
	private Button ActionBar_delete_button;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.goal);

        //findView
        btn_sos=(ImageButton)findViewById(R.id.button_sos);	//sos
        goal_name=(TextView)findViewById(R.id.detail_goal_name);//Penguin
        goal_max = (TextView)findViewById(R.id.detail_goal_max);
        goal_current = (TextView)findViewById(R.id.detail_goal_current);
        goal_start = (TextView)findViewById(R.id.detail_goal_start);
        ActionBar_delete_button = (Button)findViewById(R.id.deleteGoal);
        
                
        showResults(savedInstanceState);////Penguin

        //指定AcitonBar 的那顆垃圾桶按下去後要幹嘛
        ActionBar_delete_button.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 實作刪除的部份
    		    LayoutInflater inflater = LayoutInflater.from(goal.this.getBaseContext());  
    	        
    		    final View textEntryView = inflater.inflate(R.layout.deletesuredialog, null);  	
    	        final AlertDialog.Builder builder = new AlertDialog.Builder(goal.this);  
    	        builder.setCancelable(false);   
    	        builder.setTitle("刪除目標");  
    	        builder.setView(textEntryView); 
    	        
    	        //**** OK clicked ****///
    	        builder.setPositiveButton("確定",  
    	            new DialogInterface.OnClickListener() {  
    	                 public void onClick(DialogInterface dialog, int whichButton) { 
    	                	Intent i = new Intent();
    	                 	Bundle b = new Bundle();
    	                 	b.putString("Msg",goal);
    	                 	i.putExtras(b);
    	                 	setResult(RESULT_OK,i);
    	                 	Log.e("ddddd", "dddddddddd");
    	                 	finish();
    	                 }
    	                });  

    	        builder.setNegativeButton("取消",  
    	                new DialogInterface.OnClickListener() {  
    	                    public void onClick(DialogInterface dialog, int whichButton) {  
    	                        
    	                    }  
    	                });  
    		
    	        AlertDialog	alertDialog=builder.create();
    	        alertDialog.show();

    	}
          	}
          );
				

        
        //設定按下SOS按鍵後的事件
        btn_sos.setOnClickListener(new ImageButton.OnClickListener()
        {      	
        	public void onClick(View v)
        	{       		
        		Log.d("goal","SOS clicked");
                mFacebook = new Facebook(APP_ID);
               	mAsyncRunner = new AsyncFacebookRunner(mFacebook);      	
               	mHandler = new Handler();        
                SessionStore.restore(mFacebook, goal.this);
                                
                if( !mFacebook.isSessionValid())
      	        {      			
         			mFacebook.authorize(goal.this, new String[] {"user_photos","publish_stream","read_stream"}, new DialogListener() {
      						@Override
      						public void onComplete(Bundle values) {
      							// TODO Auto-generated method stub
      							Log.e("token",mFacebook.TOKEN);
      							SessionStore.save(mFacebook, getApplicationContext());
      						}

      						@Override
      						public void onFacebookError(FacebookError e) {
      							// TODO Auto-generated method stub
      							Log.e("FBError","");
      						}

      						@Override
      						public void onError(DialogError e) {
      							// TODO Auto-generated method stub
      							Log.e("Error","");
      						}

      						@Override
      						public void onCancel() {
      							// TODO Auto-generated method stub
      							
      						}});
      	        
      	       }
                
      	        mAsyncRunner.request("me", new IDRequestListener());
                Log.e("token2",Boolean.toString(mFacebook.isSessionValid()));
       	    	
                LayoutInflater inflaterPost = LayoutInflater.from(goal.this.getBaseContext());  		        	        
   		        final View PostTextEntryView = inflaterPost.inflate(R.layout.postdialog, null);  			//textEntryView: dialog layout		        	        
   	            final AlertDialog.Builder PostBuilder = new AlertDialog.Builder(goal.this);  
   	            final EditText edtInput = (EditText)PostTextEntryView.findViewById(R.id.sos_post);
   	            TextView textpost = (TextView)PostTextEntryView.findViewById(R.id.textViewPostSure);
   	            EditText sostextpost = (EditText)PostTextEntryView.findViewById(R.id.sos_post);
   	            textpost.setText("撐不下去了嗎? 快發求救到Facebook上請朋友們幫你集氣!!");
   	            sostextpost.setText("我再差一點點就要達成目標了，請大家幫我加油!!");
   	            PostBuilder.setCancelable(false);
   	            PostBuilder.setTitle("SOS");  
   	            PostBuilder.setView(PostTextEntryView); 
   	        
   	        //**** OK clicked ****///
   	        PostBuilder.setPositiveButton("發文",  
   	            new DialogInterface.OnClickListener() {  
   	                 public void onClick(DialogInterface dialog, int whichButton) { 
   	                	  	        	        
   	                	Log.d("sos", "postclick");	  	            		
   	                	Bundle Postb = new Bundle();
   	                	Postb.putString("method", "POST");
   	                	Log.d("sos_bundle_mothod", "ok"); 
   	                	Postb.putString("message", edtInput.getText().toString());
   	                	Log.d("sos_bundle_msg", edtInput.getText().toString());
   	                  	 
   	                  	 if(!edtInput.getText().equals(""))
   	                  		 mAsyncRunner.request("me/feed",Postb,PostRequest);

          			        Log.d("sos_post", "ok"); 	                    	
   	                 } });  

   	        PostBuilder.setNegativeButton("取消",  
   	                new DialogInterface.OnClickListener() {  
   	                    public void onClick(DialogInterface dialog, int whichButton) {  
   	                    	Log.d("sos", "cancel");
   	                    }  
   	                });  
   		
   	        AlertDialog	PostAlertDialog = PostBuilder.create();
   	        PostAlertDialog.show();
   	        
      	     }        		       	       	
        });
	}
	public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
          
        	 Log.e("Login Succeed: " , "test");
                  }

        public void onAuthFail(String error) {
            Log.e("Login Failed: " , error);
        }
        }
	//Penguin
	public void showResults(Bundle savedInstanceState){
		Bundle bundle = this.getIntent().getExtras();
		goal = bundle.getString("KEY_Goal");
	    max = bundle.getString("KEY_Goal_Max");
	    current = bundle.getString("KEY_Goal_Current");
        level = bundle.getString("KEY_Goal_Level");
        start= bundle.getString("KEY_Goal_Start");
		Log.d("goal", goal);
		
		goal_name.setText(goal);
	    goal_max.setText(max);
	    goal_current.setText(current);
	    goal_start.setText(start);

	}
	
    //PO文
    public BaseRequestListener PostRequest= new BaseRequestListener()
	{
		@Override
		public void onComplete(String response, Object state) {
	        goal.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	//TODO: put toast
	            	Toast toast = Toast.makeText(goal.this, "求救發文成功!!", Toast.LENGTH_SHORT);
	        		toast.show();
	            	Log.d("Post_toast","ok");
	            }	});	
		}
	};
     
    //登入Facebook後取得自己的id
      public class IDRequestListener extends BaseRequestListener {
        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: executed in background thread
                Log.d("Facebook-getID", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                myID = json.getString("id");
                Log.e("id", "Response: " + myID);                         

            } catch (JSONException e) {
                Log.w("Facebook-ID", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-ID", "Facebook Error: " + e.getMessage());
            }
        }
    }
      
      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          mFacebook.authorizeCallback(requestCode, resultCode, data);          
      }
	
}