package csie.mpp.goal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import csie.mpp.goal.goal.IDRequestListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TestFineActivity extends Activity {
    /** Called when the activity is first created. */   
	//**** view Container ****//
    Button Btadd;
    Button ActionBarBtn_add;		//action bar, add new goal button
    TextView ItemTitle;
    Runnable mRunable;
    Handler mHandler;  
    
    public static final String APP_ID = "117123651735429";
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;   
    public String myID;
    public boolean fake = false;
    
    public static final String PREF="GoalList";
    ArrayList<HashMap<String, Object>> listItem1;
    SimpleAdapter listItemAdapter1;
    public static  HashMap<String, Object> select = new HashMap<String, Object>();	//array list: select
    private static final int EDIT=1;	//for startActivityForResult
    static ListView list;
    
    //**** On Create ****//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);	//for self define title bar?
        setContentView(R.layout.main);//set content view: main.xml
        
        mFacebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);      	     
        SessionStore.restore(mFacebook, TestFineActivity.this);
        
        //插入Demo要用的假資料
        if(!fake)
        {
        	FakeData();
        	fake = true;
        } 
        mHandler = new Handler();
        mRunable = new Runnable(){
        //檢查與上次打勾的時間相差幾天，超過一天就將check狀態更新為未打勾
        @Override
        public void run() {
            	
            	//抓現在的日期存到變數now
            	Date date = new Date();
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            	String now = df.format(date);
            	Log.e("TryNOW", now);
            	
            	//分別抓出目前日期的年，月，日
            	Integer nowyear, nowmonth, nowday;
    		    nowyear = Integer.parseInt(now.substring(0, 4));
    		    nowmonth = Integer.parseInt(now.substring(6, 7));
    		    nowday = Integer.parseInt(now.substring(9,10));
            	
            	//讀出Preferenceh存到timermap
            	SharedPreferences settingstime= getSharedPreferences(PREF,0);
    			Map<String,?>timermap = settingstime.getAll();
    			
    			//讀取每一筆Item資料
    			for(String dataKey1 : timermap.keySet())   
    			{   
    				String timek1 = dataKey1.substring(0,9);
    				String timek2 = dataKey1.substring(9);
 
    				Log.d("TRYtimek1",timek1 ); 
    				Log.d("TRYtimek2",timek2 ); 
    				
    				//讀出每一筆資料
    				if(timek1.equals("ItemTitle"))
    				{
    					Log.d("TRYItemTitle", timek1.toString());
    				    String goalcurrent ;
    				    String goallevel ;
    				    String goalcheck = timermap.get("ItemCheck"+timek2).toString();
    				    String goallast = timermap.get("ItemLast"+timek2).toString();
    				   
    				    
    				    //存入上次打勾的時間(年,月,日)
    				    Integer lastyear, lastmonth, lastday;		        		    
    	    		    lastyear = Integer.parseInt(goallast.substring(0, 4));
    	    		    lastmonth = Integer.parseInt(goallast.substring(6, 7));
    	    		    lastday = Integer.parseInt(goallast.substring(9, 10));
    	    		    Log.d("TRYlastday", lastday.toString());
    	    			
    					//計算目前時間與上次打勾相差的日期存入subday
    					 Date lastcheck = new Date(lastyear,lastmonth,lastday);
    				     Date nowdate = new Date(nowyear, nowmonth, nowday);	
    				     Long n = nowdate.getTime() - lastcheck.getTime();
    				     int subday=(int)(n/(1000*60*60*24)); //今天與上次打勾相差的日數
    				     Log.d("TRYsubday", Integer.toString(subday));
    				     
    				     //如果目前日期與上次打勾只差一天，設定可以再重新打勾
    				     if(subday == 1)
    				     {    				    	 
    				    	 goalcheck = Integer.toString(0);    				    	 
    				    	 Editor editor = settingstime.edit();
    				    	 editor.putInt("ItemCheck"+timermap.get("ItemTitle"+timek2).toString(), 0);
                  	         editor.commit();				    	      				    	     
                  	         updateStatus();
                    	      
    				    	 Log.d("TRYsubday=1", "ok");
    				     }
    				     
    				     //如果目前日期與上次打勾差了兩天以上，設為可重新打勾，並將current與level重設為零
    				     else if(subday > 1)
    				     {
    				    	 goalcheck = Integer.toString(0);
    				    	 goalcurrent = Integer.toString(0);
    				    	 goallevel = Integer.toString(0);
    				    	 
    				    	 Editor editor = settingstime.edit();
    				    	 editor.putInt("ItemCheck"+timermap.get("ItemTitle"+timek2).toString(), 0);
    				    	 editor.putInt("ItemCurrent"+timermap.get("ItemTitle"+timek2).toString(), 0);
    				    	 editor.putInt("ItemLevel"+timermap.get("ItemTitle"+timek2).toString(), 0);
    				    	 editor.commit();
    				    	 
    				    	 updateStatus();
    				    	 Log.d("TRYsubday>1", "ok");
    				     }  	   				     
    				}
    			}
                //每十分鐘執行一次  	
            	mHandler.postDelayed(mRunable, 600000);
              } 
            };
            
        Thread t = new Thread(new Runnable(){
        @Override
        public void run() {
                	mHandler.postDelayed(mRunable, 1000);
                  } 
                });
            
        t.run();
       
        //**** find view ****//
        ActionBarBtn_add=(Button)this.findViewById(R.id.addGoal);		//find view: action bar button
        list = (ListView) findViewById(R.id.list01); 	
        
        //******* Item on Click Listener ******//
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
	      		  Log.d("onItemClick_goal","item clicked (arg2):"+arg2+" (arg3):"+arg3);
	      		  
	      		  //ListView list = (ListView)arg1;
	      		  //* get item Position *//
	      		  HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(arg2);
	      		  HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(arg2);
	      		  TestFineActivity.select = itemAtPosition;
	              Log.d("onItemClick_select","ok");
	      		  
	              Intent intent = new Intent();
	      		  intent.setClass(TestFineActivity.this,goal.class);
	      		  Bundle bundle =new Bundle();
	      		  bundle.putString("KEY_Goal", map2.get("ItemTitle").toString());
	      		  bundle.putString("KEY_Goal_Max", map2.get("ItemMax").toString());
	      		  bundle.putString("KEY_Goal_Current", map2.get("ItemCurrent").toString());
	      		  bundle.putString("KEY_Goal_Level", map2.get("ItemLevel").toString());	 
	      		  intent.putExtras(bundle);
	      		  Log.d("onItemClick_Intent", "ok");
	 
	      		  startActivityForResult(intent,2);	
			}
        	}
        );      
        
        //******* List Item Click ********//        
        ListView.OnHierarchyChangeListener listitemclick = new ListView.OnHierarchyChangeListener() {//item click listener (listitemclick)
        	
        	@Override
        	public void onChildViewAdded(View parent, final View child) {
        		Log.d("ChildViewAdded_goal","addchild");
        		//Called when a new child is added to a parent view.
        		  final TextView txtnum = (TextView) child.findViewById(R.id.ItemTitle);	//find view: txtnum list.xml 		  
        		  final ImageButton check = (ImageButton)child.findViewById(R.id.btn_checkbox);
        		  final ImageButton level = (ImageButton)child.findViewById(R.id.lv);
        		  
        		  //按下level後跳到下一頁
        		  level.setOnClickListener( new Button.OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d("ChildViewAdded_level","click");
						HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		TestFineActivity.select = itemAtPosition;
		        	 
		        		Intent intent = new Intent();
		        		intent.setClass(TestFineActivity.this,goal.class);
		        		Bundle bundle =new Bundle();
		        		Log.e("ChildViewAdded_select", "_txtnum:"+ txtnum.getText().toString());
		        		bundle.putString("KEY_Goal", txtnum.getText().toString());		        		
		        		bundle.putString("KEY_Goal_Max", map2.get("ItemMax").toString());
		        	    bundle.putString("KEY_Goal_Current", map2.get("ItemCurrent").toString());
		        	    bundle.putString("KEY_Goal_Level", map2.get("ItemLevel").toString());
		        		
		        		intent.putExtras(bundle);
		        		Log.d("level_intent", "put bundle");
		        		startActivityForResult(intent,2);
					}
        			  
        			  
        		  });
        		  
        		  //按下list中間的textView也要跳到下一頁
        		  txtnum.setOnClickListener(new OnClickListener() {
  					
  					public void onClick(View v) {
  						// TODO Auto-generated method stub
  						HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		TestFineActivity.select = itemAtPosition;
  						Intent intent = new Intent();
  		        		intent.setClass(TestFineActivity.this,goal.class);
  		        		Bundle bundle =new Bundle();
  		        		bundle.putString("KEY_Goal", txtnum.getText().toString());
  		                bundle.putString("KEY_Goal_Max", map2.get("ItemMax").toString());
		        	    bundle.putString("KEY_Goal_Current", map2.get("ItemCurrent").toString());
		        	    bundle.putString("KEY_Goal_Level", map2.get("ItemLevel").toString());
		        	    bundle.putString("KEY_Goal_Start", map2.get("ItemStart").toString());
  		        		intent.putExtras(bundle);
  		        		Log.d("txtnum_intent", "put bundle");
  		        		startActivityForResult(intent,2);
  					}
  				});
        		  
        		  
        		  //按下check後要做的事情
                  check.setOnClickListener(new Button.OnClickListener(){					
                	@Override
					public void onClick(View v) {						              		
			      		
                		SharedPreferences settings2= getSharedPreferences(PREF,0);
			    		Map<String,?>stmap3 = settings2.getAll();
			    		Integer checked1 = (Integer) stmap3.get("ItemCheck"+txtnum.getText().toString());
		        		
			    		//如果還未勾過check
		        		if(checked1 == 0)
		        		{	Log.d("check=0", "in");	        			
		        			LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  		        	        
		        		    final View textEntryView = inflater.inflate(R.layout.suredialoglayout, null);  			//textEntryView: dialog layout		        	        
		        	        final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
		        	        builder.setCancelable(false);
		        	        builder.setTitle("今日目標達成");  
		        	        builder.setView(textEntryView); 
		        	        
		        	        //**** OK clicked ****///
		        	        builder.setPositiveButton("確定",  
		        	            new DialogInterface.OnClickListener() {  
		        	                 public void onClick(DialogInterface dialog, int whichButton) { 
		        	                	Log.d("check_sure", "click");	
		        	                	HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        			        		
		        			      		SharedPreferences settings2= getSharedPreferences(PREF,0);
		        			    		Map<String,?>stmap4 = settings2.getAll();
		        			    				        			    					        	                	
		     							check.setImageResource(R.drawable.checkbox_checked);
		     							Log.d("check_image","set checked");
		     							
		     							Integer max1 = (Integer) stmap4.get("ItemMax"+txtnum.getText().toString());
		     		        			Integer level1 = (Integer)stmap4.get("ItemLevel"+txtnum.getText().toString());
		     		        		    Integer current1 = (Integer) stmap4.get("ItemCurrent"+txtnum.getText().toString());
		     		        			Log.d("check_Max", "readok");
		     		        		    
		     		        			current1++;//update progress bar
		     		        			   
		     		        			if( (level1*21 + current1) > max1)
		     		        			{ 
		     		        				  max1 ++;//update max date
		     		        			 }
		     		        		
		     		        			if(current1 == 21)
		     		        			{
		     		        				    level1 ++;
		     		        			 	    current1 = level1*21;
		     		        			 	    level.setImageResource(R.drawable.lv3);
//		     		        			 	    level.setImageResource(R.drawable);
		     		        			 	    
		     		        	              
		     		        			 	    //alert dialog
		     		        	       	    	LayoutInflater inflaterPost = LayoutInflater.from(TestFineActivity.this.getBaseContext());  		        	        
		     		        	   		        final View PostTextEntryView = inflaterPost.inflate(R.layout.postdialog, null);  			//textEntryView: dialog layout		        	        
		     		        	   	            final AlertDialog.Builder PostBuilder = new AlertDialog.Builder(TestFineActivity.this);  
		     		        	   	            final EditText edtInput = (EditText)PostTextEntryView.findViewById(R.id.level_up_post);
		     		        	   	            TextView textpost = (TextView)PostTextEntryView.findViewById(R.id.textViewPostSure);//description text
		     		        	   	            EditText sostextpost = (EditText)PostTextEntryView.findViewById(R.id.level_up_post);
		     		          	            
		     		          	                sostextpost.setText("我又持續執行我的目標21天了!!");
		     		        	   	            textpost.setText("Level up!! 快PO到Facebook上與朋友們分享!!");
		     		        	   	            PostBuilder.setCancelable(false);
		     		        	   	            PostBuilder.setTitle("Level Up");  
		     		        	   	            PostBuilder.setView(PostTextEntryView); 
		     		        	   	        
		     		        	   	        //**** OK clicked ****///
		     		        	   	        PostBuilder.setPositiveButton("發文",  
		     		        	   	            new DialogInterface.OnClickListener() {  
		     		        	   	                 public void onClick(DialogInterface dialog, int whichButton) { 
		     		        	   	                			     		        	   	        	        
		     		        	   	                	Log.d("sos", "postclick");	  	            		
		     		        	   	                //level up: put on wall post//
		     		        	   	                	if( !mFacebook.isSessionValid())
		     		        	   	                	{      			
		     		        	   	                		mFacebook.authorize(TestFineActivity.this, new String[] {"publish_stream","read_stream"}, new DialogListener() {
		     		        	   	                			@Override
		     		        	   	                		    public void onComplete(Bundle values) {
		     		        	   	                		     	// TODO Auto-generated method stub
		     		        	   	                		     	SessionStore.save(mFacebook, getApplicationContext());
		     		        	   	                		    }
		     		        	   	                			@Override
		     		        	   	                			public void onFacebookError(FacebookError e) {
		     		        	   	                			}
		     		        	   	                			@Override
		     		        	   	                			public void onError(DialogError e) {
		     		        	   	                				
		     		        	   	                			}
		     		        	   	                			@Override
		     		        	   	                		    public void onCancel() {
		     		        	   	                				
		     		        	   	                			}
		     		        	   	                			}
		     		        	   	                		);    		        	     	        
		     		        	   	                	}
		     		        	   	                	mAsyncRunner.request("me", new IDRequestListener());
		     		        	   	                	
		     		        	   	                	Bundle Postb = new Bundle();
		     		        	   	                	Postb.putString("method", "POST");
		     		        	   	                	Log.d("level_bundle_mothod", "ok"); 
		     		        	   	                	Postb.putString("message", edtInput.getText().toString());
		     		        	   	                	Log.d("level_bundle_msg", edtInput.getText().toString());
		     		        	   	                  	 
		     		        	   	                  	 if(!edtInput.getText().equals(""))
		     		        	   	                  		 mAsyncRunner.request("me/feed",Postb,PostRequest);

		     		        	          			        Log.d("level_post", "ok"); 	                    	
		     		        	   	                 } });  

		     		        	   	        PostBuilder.setNegativeButton("取消",  
		     		        	   	                new DialogInterface.OnClickListener() {  
		     		        	   	                    public void onClick(DialogInterface dialog, int whichButton) {  
		     		        	   	                    	Log.d("level", "cancel");
		     		        	   	                    }  
		     		        	   	                });  
		     		        	   		
		     		        	   	        AlertDialog	PostAlertDialog = PostBuilder.create();
		     		        	   	        PostAlertDialog.show();		     		        	   	        		     		        			 	    
		     		        		     	}	
		     		        			   		     		        			   
		     		        			  Date date = new Date();
		     		                      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		     		                      String newlast = df.format(date);
		     		                   	  Log.d("check_newlast", newlast);
		     		        		    
		     					    	//回存數值
		     	        			    map2.put("ItemCurrent", current1);
		     	        			    map2.put("ItemMax", max1);
		     	        		    	map2.put("ItemLevel", level1);
		     	        		    	map2.put("ItemCheck", 1);
		     	        		    	map2.put("ItemLast", newlast);
		     	        		    	Log.d("check_map2", "ok");
		                        
		                 		       SharedPreferences settings= getSharedPreferences(PREF,0);
		               		 	       Editor editor=settings.edit();
		                      	       editor.putInt("ItemMax"+map2.get("ItemTitle").toString(),max1);
		                      	       editor.putInt("ItemCurrent"+map2.get("ItemTitle").toString(), current1);
		                     	       editor.putInt("ItemLevel"+map2.get("ItemTitle").toString(), level1);
		                     	       editor.putInt("ItemCheck"+map2.get("ItemTitle").toString(), 1);
		                     	       editor.putString("ItemLast"+map2.get("ItemTitle").toString(), newlast.toString());
		               			       editor.commit();
		               			       
		               			       Log.d("check_pref", "ok");
		             		             	                    	
		        	                 } });  

		        	        builder.setNegativeButton("取消",  
		        	                new DialogInterface.OnClickListener() {  
		        	                    public void onClick(DialogInterface dialog, int whichButton) {  
		        	                    	Log.d("check", "cancel");
		        	                    }  
		        	                });  
		        		
		        	        AlertDialog	alertDialog=builder.create();
		        	        alertDialog.show();
		        	        
		        	        Integer checked2 = (Integer) stmap3.get("ItemCheck"+txtnum.getText().toString());
		        	        if(checked2 == 1)
		        	        {
		        	          check.setClickable(false);
             		          Log.d("check_setclick", "ok");   
		        	        }
		        		} 
		        	}

                  });
                  
      			SharedPreferences settings2= getSharedPreferences(PREF,0);
    			Map<String,?>stmap2 = settings2.getAll();
    			Integer checked = (Integer) stmap2.get("ItemCheck"+txtnum.getText().toString());
    			Integer level_num = (Integer) stmap2.get("ItemLevel"+txtnum.getText().toString());
    			level_num++;
    			Integer progress = (Integer) stmap2.get("ItemCurrent"+txtnum.getText().toString());
    			//String level_str = "R.drawable.lv"+level_num.toString();
    			
    			Log.d("checkedtry",stmap2.get("ItemCheck"+txtnum.getText().toString()).toString());
    			
    			if(checked == 1)
                {
                  check.setImageResource(R.drawable.checkbox_checked);
                  check.setClickable(false);
                  
                  
                  Log.d("check=1_setclick", "ok"); 
                }
    			
    			else
    			{
    				check.setImageResource(R.drawable.checkbox);
    				check.setClickable(true);
    				Log.d("check=0_setclick", "ok"); 
    			}
    			
    			switch(level_num){
    			case 0:
    				level.setImageResource(R.drawable.lv1);
    				break;
    			case 2:
    				level.setImageResource(R.drawable.lv2);
    				break;
    			case 3:
    				level.setImageResource(R.drawable.lv3);
    				break;
    			case 4:
    				level.setImageResource(R.drawable.lv4);
    				break;
    			case 5:
    				level.setImageResource(R.drawable.lv5);
    				break;
    			case 6:
    				level.setImageResource(R.drawable.lv6);
    				break;
    			default:
    				level.setImageResource(R.drawable.lv1);
    			}
    			switch(progress){
    			case 0:
    				txtnum.setBackgroundResource(R.drawable.goal_background);
    				break;
    			case 1:
    				txtnum.setBackgroundResource(R.drawable.goal_background1);
    				break;
    			case 2:
    				txtnum.setBackgroundResource(R.drawable.goal_background2);
    				break;
    			case 3:
    				txtnum.setBackgroundResource(R.drawable.goal_background2);
    				break;
    			case 4:
    				txtnum.setBackgroundResource(R.drawable.goal_background4);
    				break;
    			case 5:
    				txtnum.setBackgroundResource(R.drawable.goal_background5);
    				break;
    			case 6:
    				txtnum.setBackgroundResource(R.drawable.goal_background6);
    				break;
    			case 7:
    				txtnum.setBackgroundResource(R.drawable.goal_background7);
    				break;
    			case 8:
    				txtnum.setBackgroundResource(R.drawable.goal_background8);
    				break;
    			case 9:
    				txtnum.setBackgroundResource(R.drawable.goal_background9);
    				break;
    			case 10:
    				txtnum.setBackgroundResource(R.drawable.goal_background10);
    				break;
    			case 11:
    				txtnum.setBackgroundResource(R.drawable.goal_background11);
    				break;
    			case 12:
    				txtnum.setBackgroundResource(R.drawable.goal_background12);
    				break;
    			case 13:
    				txtnum.setBackgroundResource(R.drawable.goal_background13);
    				break;
    			case 14:
    				txtnum.setBackgroundResource(R.drawable.goal_background14);
    				break;
    			case 15:
    				txtnum.setBackgroundResource(R.drawable.goal_background15);
    				break;
    			case 16:
    				txtnum.setBackgroundResource(R.drawable.goal_background16);
    				break;
    			case 17:
    				txtnum.setBackgroundResource(R.drawable.goal_background17);
    				break;
    			case 18:
    				txtnum.setBackgroundResource(R.drawable.goal_background18);
    				break;
    			case 19:
    				txtnum.setBackgroundResource(R.drawable.goal_background19);
    				break;
    			case 20:
    				txtnum.setBackgroundResource(R.drawable.goal_background20);
    				break;
    			default:
    				txtnum.setBackgroundResource(R.drawable.goal_background);
    			}
    			
            }

			@Override
			public void onChildViewRemoved(View arg0, View arg1) {			//REMOVE list item...
				// TODO Auto-generated method stub
				
			}};
					
			list.setOnHierarchyChangeListener(listitemclick );
		//******* End of List Item Click ********// 
        
			//******* Array List: list item ******//
			final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
			listItem1 = listItem;
			SharedPreferences settings1= getSharedPreferences(PREF,0);
			Map<String,?>stmap = settings1.getAll();
      	
			for(String dataKey : stmap.keySet())   
			{   
				String k1=dataKey.substring(0,9);
				String k2=dataKey.substring(9);
				Log.d("list_goalk1",k1 ); 
				Log.d("list_goalk2",k2 ); 
				if(k1.equals("ItemTitle"))
				{
					String titlek = stmap.get(dataKey).toString();
				    String goalmax = stmap.get("ItemMax"+k2).toString();
				    String goalcurrent = stmap.get("ItemCurrent"+k2).toString();
				    String goallevel = stmap.get("ItemLevel"+k2).toString(); //set level image
				    Log.d("goal","502: goalLevel = "+goallevel);
				    //level.setImageResource(R.drawable.)
				    String goalcheck = stmap.get("ItemCheck"+k2).toString();
				    String goalstart = stmap.get("ItemStart"+k2).toString();
				    String goallast = stmap.get("ItemLast"+k2).toString();
				    
					HashMap<String, Object> map = new HashMap<String, Object>();  
					map.put("ItemTitle", titlek); 
					map.put("ItemMax",goalmax);
					map.put("ItemCurrent",goalcurrent);
					map.put("ItemLevel",goallevel);
					map.put("ItemCheck",goalcheck);
					map.put("ItemStart",goalstart);
					map.put("ItemLast",goallast);
					
					listItem.add(map); 											
				}
			}//end of for-loop		  
		  //******* Simple Adapter: listItemAdapter ******//
          final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,  
                new String[] {"ItemTitle"},   
                new int[] {R.id.ItemTitle}  
          );
          
          listItemAdapter1 = listItemAdapter;
          list.setAdapter(listItemAdapter); 
          Log.d("list_read","ok" );
               
          
          //****** ACTION BAR: ADD NEW ITEM ******//
          ActionBarBtn_add.setOnClickListener(new Button.OnClickListener(){
        	  
        	  public void onClick(View V){			//add new goal (action bar add button)
    		    LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  
    	        
    		    final View textEntryView = inflater.inflate(R.layout.dialoglayout, null);  			//textEntryView: dialog layout
    	        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput); 		//edtInput: new name of goal
      	        
    	        final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
    	        builder.setCancelable(false);    
    	        builder.setTitle("新增目標");  
    	        builder.setView(textEntryView); 
    	        
    	        //**** OK clicked ****///
    	        builder.setPositiveButton("確定",  
    	            new DialogInterface.OnClickListener() {  
    	                 public void onClick(DialogInterface dialog, int whichButton) { 
    	                    	
    	                    	String edtInputName = edtInput.getText().toString();
    	                    	
    	                    	if(edtInputName.equals(""))
    	                		{        	            			
    	                			Toast.makeText(TestFineActivity.this, "未輸入目標名稱", Toast.LENGTH_SHORT).show();
    	                  		}
    	                    	        	                		        	            			        	                		       	                  		
    	                    	else{
    	                          
    			        			  Date date2 = new Date();
    			                      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    			                      String startday = df.format(date2);
    			                   	  Log.e("startday", startday);
    	                    	  
    	                    	  HashMap<String, Object> map = new HashMap<String, Object>();  
    	                          map.put("ItemTitle", edtInput.getText() );
    	                          map.put("ItemMax", 0 );
    	                          map.put("ItemCurrent", 0 );
    	                          map.put("ItemLevel", 0 );
    	                          map.put("ItemCheck", 0 );
    	                          map.put("ItemStart", startday);
    	                          map.put("ItemLast", "2000-11-11");

    	                          listItem.add(map); 
                                  list.setAdapter(listItemAdapter); 
    	                         
    	                  		  SharedPreferences settings= getSharedPreferences(PREF,0);
    	                		  Editor editor=settings.edit();
    	                          editor.putString("ItemTitle"+edtInput.getText().toString(), edtInput.getText().toString());
    	                          editor.putInt("ItemMax"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemCurrent"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemLevel"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemCheck"+edtInput.getText().toString(), 0);
    	                          editor.putString("ItemStart"+edtInput.getText().toString(), startday);
    	                          editor.putString("ItemLast"+edtInput.getText().toString(), "2000-11-11");
    	                		  editor.commit();
    	                    	}}  
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
        //******END OF ACTION BAR: ADD NEW ITEM ******//
        
    }  
    
    //更新主畫面List Item
    public void updateStatus() 
    {
		//******* Array List: list item ******//
		final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		listItem1 = listItem;
		SharedPreferences settings1= getSharedPreferences(PREF,0);
		Map<String,?>stmap = settings1.getAll();
  	
		for(String dataKey : stmap.keySet())   
		{   
			String k1 = dataKey.substring(0,9);
			String k2 = dataKey.substring(9);

			Log.d("goal",k1 ); 
			Log.d("goal",k2 ); 
			
			if(k1.equals("ItemTitle"))
			{
				String titlek = stmap.get(dataKey).toString();
			    String goalmax = stmap.get("ItemMax"+k2).toString();
			    String goalcurrent = stmap.get("ItemCurrent"+k2).toString();
			    String goallevel = stmap.get("ItemLevel"+k2).toString(); //level image
			    Log.d("goal","429: goalLevel = "+goallevel);
			    
			    String goalcheck = stmap.get("ItemCheck"+k2).toString();
			    String goalstart = stmap.get("ItemStart"+k2).toString();
			    String goallast = stmap.get("ItemLast"+k2).toString();
			    
				HashMap<String, Object> map = new HashMap<String, Object>();  
				map.put("ItemTitle", titlek);  
				map.put("ItemMax",goalmax);
				map.put("ItemCurrent",goalcurrent);
				map.put("ItemLevel",goallevel);
				map.put("ItemCheck",goalcheck);
				map.put("ItemStart",goalstart);
				map.put("ItemLast",goallast);
				
				listItem.add(map); 	
			}
		}//end of for-loop
	  
	  //******* Simple Adapter: listItemAdapter ******//
      final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,  
            new String[] {"ItemTitle"},   
            new int[] {R.id.ItemTitle}  
      );
      
      listItemAdapter1 = listItemAdapter;
      list.setAdapter(listItemAdapter); 
      
      Log.d("updateStatus", "ok"); 
    }
    
    
 // TODO Auto-generated method stub 刪除目標項
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebook.authorizeCallback(requestCode, resultCode, data);
        Log.e("resultok","123");
        
        if(resultCode==RESULT_OK)
        {
        	Log.e("resultok",TestFineActivity.select.get("ItemTitle").toString());
        	
           final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem1,R.layout.list,  
                    new String[] {"ItemTitle"},   
                    new int[] {R.id.ItemTitle}  
              );
    		if(select.get("ItemTitle") != null){
    	    Log.e("deleteOK","delete!!");
    		listItem1.remove(select);
    		setTitle("刪除了" + select.get("ItemTitle"));
    		list.setAdapter(listItemAdapter); 
            SharedPreferences settings= getSharedPreferences(PREF,0);
    		Editor editor=settings.edit();
    		editor.remove("ItemCurrent"+select.get("ItemTitle"));
    		editor.remove("ItemMax"+select.get("ItemTitle"));
    		editor.remove("ItemLevel"+select.get("ItemTitle"));
    		editor.remove("ItemCheck"+select.get("ItemTitle"));
    		editor.remove("ItemStart"+select.get("ItemTitle"));
    		editor.remove("ItemLast"+select.get("ItemTitle"));
    		editor.remove("ItemTitle"+select.get("ItemTitle"));   		
            editor.commit();
    		}
        }
    }
    
  //level up PO文
    public BaseRequestListener PostRequest= new BaseRequestListener()
	{
		@Override
		public void onComplete(String response, Object state) {
			TestFineActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	            	//TODO: put toast
	            	Toast toast = Toast.makeText(TestFineActivity.this, "發文成功!!", Toast.LENGTH_SHORT);
	        		toast.show();
	            	Log.d("level_toast","ok");
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
    
    //假資料
    public void FakeData()
    {
    	String data1, data2, data3;
    	data1 = "12點前睡覺";
    	data2 = "每天吃一份水果";
    	data3 = "寫code 4個小時以上";
    	
        String startday1 = "2011-11-29";
        String startday2 = "2011-12-13";
        String startday3 = "2011-11-22";
     	//Log.e("startday", startday);
    	
    	SharedPreferences settingsfake= getSharedPreferences(PREF,0);
		Editor editorfake=settingsfake.edit();
        //data1
		editorfake.putString("ItemTitle"+ data1, data1);
        editorfake.putInt("ItemMax"+data1, 18);
        editorfake.putInt("ItemCurrent"+data1, 6);
        editorfake.putInt("ItemLevel"+data1, 0);
        editorfake.putInt("ItemCheck"+data1, 0);
        editorfake.putString("ItemStart"+ data1, startday1);
        editorfake.putString("ItemLast"+data1, "2011-12-28");
        
        //data2
		editorfake.putString("ItemTitle"+ data2, data2);
        editorfake.putInt("ItemMax"+data2, 16);
        editorfake.putInt("ItemCurrent"+data2, 16);
        editorfake.putInt("ItemLevel"+data2, 0);
        editorfake.putInt("ItemCheck"+data2, 0);
        editorfake.putString("ItemStart"+ data2, startday2);
        editorfake.putString("ItemLast"+data2, "2011-12-28");
        
        //data3
		editorfake.putString("ItemTitle"+ data3, data3);
        editorfake.putInt("ItemMax"+data3, 41);
        editorfake.putInt("ItemCurrent"+data3, 20);
        editorfake.putInt("ItemLevel"+data3, 1);
        editorfake.putInt("ItemCheck"+data3, 0);
        editorfake.putString("ItemStart"+ data3, startday3);
        editorfake.putString("ItemLast"+data3, "2011-12-28");//today time
        
        editorfake.commit();
    }
     
}    
    