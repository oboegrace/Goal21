package csie.mpp.goal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
    CheckBox checkBox1;				//
    TextView ItemTitle;
    public static final String PREF="GoalList";
    //TextView Current;
    //int Current =0;
    ArrayList<HashMap<String, Object>> listItem1;
    SimpleAdapter listItemAdapter1;
    public static  HashMap<String, Object> select=new HashMap<String, Object>();	//array list: select
    private static final int EDIT=1;	//for startActivityForResult
    static ListView list;
    //**** On Create ****//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);		//for self define title bar?
        setContentView(R.layout.main);							//set content view: main.xml
        
        //**** find view ****//
       // Btadd=(Button)this.findViewById(R.id.button1);					//find view: btadd
        ActionBarBtn_add=(Button)this.findViewById(R.id.addGoal);		//find view: action bar button
        list = (ListView) findViewById(R.id.list01); 	//(?) why use 'final'? listview(list01) of main.xml
        
        //******* Item on Click Listener ******//
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
	      		  Log.d("goal","item clicked (arg2):"+arg2+" (arg3):"+arg3);
	      		  
	      		  //ListView list = (ListView)arg1;
	      		  //* get item Position *//
	      		  HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(arg2);
	      		  HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(arg2);
	      		  TestFineActivity.select = itemAtPosition;
	              Log.e("select","selectok");
	      		  Intent intent = new Intent();
	      		  intent.setClass(TestFineActivity.this,goal.class);
	      		  Bundle bundle =new Bundle();
	      		  bundle.putString("KEY_Goal", map2.get("ItemTitle").toString());
	      		  bundle.putString("KEY_Goal_Max", map2.get("ItemMax").toString());
	      		  bundle.putString("KEY_Goal_Current", map2.get("ItemCurrent").toString());
	      		  bundle.putString("KEY_Goal_Level", map2.get("ItemLevel").toString());
	 
	      		  intent.putExtras(bundle);
	      		  Log.d("goal", "3");
	      		  //startActivityForResult(intent, EDIT);
	      		  startActivityForResult(intent,2);
	      		  Log.d("goal", "4");	
			}

        	

        	}
        );      
        
        //******* List Item Click ********//        
        ListView.OnHierarchyChangeListener listitemclick = new ListView.OnHierarchyChangeListener() {//item click listener (listitemclick)
        	
        	@Override
        	public void onChildViewAdded(View parent, final View child) {
        		//Called when a new child is added to a parent view.
        		  final TextView txtnum = (TextView) child.findViewById(R.id.ItemTitle);	//find view: txtnum list.xml > item title (?) what this for?
        		  Log.d("goal","onChildViewAdded");
        		  
        		  final ImageButton check = (ImageButton)child.findViewById(R.id.btn_checkbox);
        		  final ImageButton level = (ImageButton)child.findViewById(R.id.lv);
        		  
        		  //按下level後跳到下一頁
        		  level.setOnClickListener( new Button.OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d("goal","item clicked");
						HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		TestFineActivity.select = itemAtPosition;
		        	 
		        		Intent intent = new Intent();
		        		intent.setClass(TestFineActivity.this,goal.class);
		        		Bundle bundle =new Bundle();
		        		Log.e("select_txtnum", txtnum.getText().toString());
		        		bundle.putString("KEY_Goal", txtnum.getText().toString());		        		
		        		bundle.putString("KEY_Goal_Max", map2.get("ItemMax").toString());
		        	    bundle.putString("KEY_Goal_Current", map2.get("ItemCurrent").toString());
		        	    bundle.putString("KEY_Goal_Level", map2.get("ItemLevel").toString());
		        		
		        		  intent.putExtras(bundle);
		        		  Log.d("goal", "put bundle");
		        		  //startActivity(intent);
		        		  startActivityForResult(intent,2);
		        		  //Log.d("goal", "4");
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
  		        	   // Log.d("goal", "put bundle");
  		        		startActivityForResult(intent,2);
  					}
  				});
        		  
        		  
        		  //TODO: check whether it is done, then R.checkbox_checked 
                  check.setOnClickListener(new Button.OnClickListener(){
					
                	  @Override
					public void onClick(View v) {
						
  						HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(list.indexOfChild(child));
		        		TestFineActivity.select = itemAtPosition;
		        		
		        		if((Integer) map2.get("ItemCheck") == 0)
		        		{
						
						Log.d("goal","checked"+txtnum.getText().toString());
						check.setImageResource(R.drawable.checkbox_checked);
					
						// TODO:
							//remember this item (today) is checked
						Log.d("check","check clicked");
						

		        		Integer max1 = (Integer) map2.get("ItemMax");
		        		Log.e("checkMax", max1.toString());
		        		Integer level1 = (Integer) map2.get("ItemLevel");
		        		Integer current1 = (Integer) map2.get("ItemCurrent");
		        		
		        		current1++;
		        		if(current1 > max1)
		        		{ // max++
		        			max1 ++;
		        		}
		        		
		        		if(current1 == 21)
		        		{
		        			//level++, current = 0
		        			level1 ++;
		        			current1 = 0;
		        		}
		        		
		        		//回存數值
		        		map2.put("ItemCurrent", current1);
		        		map2.put("ItemMax", max1);
		        		map2.put("ItemLevel", level1);
		        		map2.put("ItemCheck", 1);
                        
		        		//listItem1.add(map2); 
                       // list.setAdapter(listItemAdapter1); 
                       // listItemAdapter1.notifyDataSetChanged();
                       
                		SharedPreferences settings= getSharedPreferences(PREF,0);
              		    Editor editor=settings.edit();
                        editor.putInt("ItemMax"+map2.get("ItemTitle").toString(),max1);
                        editor.putInt("ItemCurrent"+map2.get("ItemTitle").toString(), current1);
                        editor.putInt("ItemLevel"+map2.get("ItemTitle").toString(), level1);
                        editor.putInt("ItemCheck"+map2.get("ItemTitle").toString(), 1);
              		    editor.commit();
              		    
              		    check.setClickable(false);
		        		}
					}               	  
                  });
                  
      			SharedPreferences settings2= getSharedPreferences(PREF,0);
    			Map<String,?>stmap2 = settings2.getAll();
    			Integer checked = (Integer) stmap2.get("ItemCheck"+txtnum.getText().toString());
    			Log.e("checkedtry",stmap2.get("ItemCheck"+txtnum.getText().toString()).toString());
    			if(checked == 1)
                {
                  check.setImageResource(R.drawable.checkbox_checked);
                  check.setClickable(false);
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
			listItem1=listItem;
			SharedPreferences settings1= getSharedPreferences(PREF,0);
			Map<String,?>stmap = settings1.getAll();
      	
			for(String dataKey : stmap.keySet())   
			{   
				String k1=dataKey.substring(0,9);
				String k2=dataKey.substring(9);
				//Btdel.setText(k1);
				Log.d("goal",k1 ); 
				Log.d("goal",k2 ); 
				if(k1.equals("ItemTitle"))
				{
					String titlek = stmap.get(dataKey).toString();
				    String goalmax = stmap.get("ItemMax"+k2).toString();
				    String goalcurrent = stmap.get("ItemCurrent"+k2).toString();
				    String goallevel = stmap.get("ItemLevel"+k2).toString(); 
				    String goalcheck = stmap.get("ItemCheck"+k2).toString();
				    String goalstart = stmap.get("ItemStart"+k2).toString();

					HashMap<String, Object> map = new HashMap<String, Object>();  
					map.put("ItemTitle", titlek);  
					map.put("ItemMax",goalmax);
					map.put("ItemCurrent",goalcurrent);
					map.put("ItemLevel",goallevel);
					map.put("ItemCheck",goalcheck);
					map.put("ItemStart",goalstart);
					
					listItem.add(map); 	
				}
			}//end of for-loop
		  
		  //******* Simple Adapter: listItemAdapter ******//
          final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,  
                new String[] {"ItemTitle"},   
                new int[] {R.id.ItemTitle}  
          );
          listItemAdapter1=listItemAdapter;
          list.setAdapter(listItemAdapter); 
          
          
    
          
          
          //****** ACTION BAR: ADD NEW ITEM ******//
          ActionBarBtn_add.setOnClickListener(new Button.OnClickListener(){
        	  
        	  public void onClick(View V){			//add new goal (action bar add button)
    		    LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  
    	        
    		    final View textEntryView = inflater.inflate(R.layout.dialoglayout, null);  			//textEntryView: dialog layout
    	        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput); 		//edtInput: new name of goal
    	        //final EditText edtInput1=(EditText)textEntryView.findViewById(R.id.Current); 
    	        
    	        final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
    	        builder.setCancelable(false);  
    	       // builder.setIcon(R.drawable.icon);  
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
    	                          
    	                    	  long timeNow = System.currentTimeMillis(); 
    	                    	  Calendar calendar = Calendar.getInstance();
    	                    	  calendar.setTimeInMillis(timeNow);
    	                    	  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	                    	  String startday = df.format(calendar.getTime());
    	                    	 //tv4.setText("DATE="+bf);
    	                    	  
    	                    	  HashMap<String, Object> map = new HashMap<String, Object>();  
    	                          map.put("ItemTitle", edtInput.getText() );
    	                          map.put("ItemMax", 0 );
    	                          map.put("ItemCurrent", 0 );
    	                          map.put("ItemLevel", 0 );
    	                          map.put("ItemCheck", 0 );
    	                          map.put("ItemStart", startday);
    	                                              
    	                         // map.put("Current", 0 );  
    	                          listItem.add(map); 
                                  list.setAdapter(listItemAdapter); 
    	                          //listItemAdapter.notifyDataSetChanged();
    	                         
    	                  		  SharedPreferences settings= getSharedPreferences(PREF,0);
    	                		  Editor editor=settings.edit();
    	                          editor.putString("ItemTitle"+edtInput.getText().toString(), edtInput.getText().toString());
    	                          editor.putInt("ItemMax"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemCurrent"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemLevel"+edtInput.getText().toString(), 0);
    	                          editor.putInt("ItemCheck"+edtInput.getText().toString(), 0);
    	                          editor.putString("ItemStart"+edtInput.getText().toString(), startday);
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
 // TODO Auto-generated method stub
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultok","123");
        
        if(resultCode==RESULT_OK)
        {
        	Log.e("resultok",TestFineActivity.select.get("ItemTitle").toString());
        	
        	//mText.setText(data.getExtras().getString("Msg"));
        	//mHandler.postDelayed(mRunable, 2000);
        	
           final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem1,R.layout.list,  
                    new String[] {"ItemTitle"},   
                    new int[] {R.id.ItemTitle}  
              );
    		if(select.get("ItemTitle") != null){
    	    Log.e("deleteOK","delete!!");
    		listItem1.remove(select);
    		setTitle("刪除了" + select.get("ItemTitle"));
    		//listItemAdapter.notifyDataSetChanged();
    		list.setAdapter(listItemAdapter); 
            SharedPreferences settings= getSharedPreferences(PREF,0);
    		Editor editor=settings.edit();
    		editor.remove("ItemCurrent"+select.get("ItemTitle"));
    		editor.remove("ItemMax"+select.get("ItemTitle"));
    		editor.remove("ItemLevel"+select.get("ItemTitle"));
    		editor.remove("ItemCheck"+select.get("ItemTitle"));
    		editor.remove("ItemStart"+select.get("ItemTitle"));
    		editor.remove("ItemTitle"+select.get("ItemTitle"));   		
            editor.commit();
    		}
        }
    }
    

}    
    



