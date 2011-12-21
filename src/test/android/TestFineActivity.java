package test.android;

import java.util.ArrayList;
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
    //    ImageButton check;
    CheckBox checkBox1;				//
    TextView ItemTitle;
    public static final String PREF="GoalList";
    //TextView Current;
    //int Current =0;
    
    HashMap<String, Object> select=new HashMap<String, Object>();	//array list: select
    private static final int EDIT=1;	//for startActivityForResult
    //**** On Create ****//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);		//for self define title bar?
        setContentView(R.layout.main);							//set content view: main.xml
        
        //**** find view ****//
        Btadd=(Button)this.findViewById(R.id.button1);					//find view: btadd
        ActionBarBtn_add=(Button)this.findViewById(R.id.addGoal);		//find view: action bar button
        final ListView list = (ListView) findViewById(R.id.list01); 	//(?) why use 'final'? listview(list01) of main.xml
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
	      		  select = itemAtPosition;
	  
	      		  Intent intent = new Intent();
	      		  intent.setClass(TestFineActivity.this,goal.class);
	      		  Bundle bundle =new Bundle();
	      		  bundle.putString("KEY_Goal", map2.get("ItemTitle").toString());
	      		  //Integer.toString(Current);

	      		  //Log.d("goal", "1");
	      		  //bundle.putString("KEY_Current", Integer.toString(Current));
	      		  //Log.d("goal"," Current");
	      		  //Log.d("goal", "2");
	      		  intent.putExtras(bundle);
	      		  Log.d("goal", "3");
	      		  //startActivityForResult(intent, EDIT);
	      		  startActivity(intent);
	      		  Log.d("goal", "4");	
			}

        	

        	}
        );      
        //******* List Item Click ********//        
        ListView.OnHierarchyChangeListener listitemclick = new ListView.OnHierarchyChangeListener() {//item click listener (listitemclick)
        	@Override
        	public void onChildViewAdded(View parent, View child) {		//Called when a new child is added to a parent view.
        		//Button delbtn = (Button) child.findViewById(R.id.deletebutton);//delete button

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
						//HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(arg2);
		        		//HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(arg2);
		        		//  select= itemAtPosition;
		    
		        		Intent intent = new Intent();
		        		intent.setClass(TestFineActivity.this,goal.class);
		        		Bundle bundle =new Bundle();
		        		bundle.putString("KEY_Goal", txtnum.getText().toString());
		        		
		        		//Integer.toString(Current);

		        		  //Log.d("goal", "1");
		        		  //bundle.putString("KEY_Current", Integer.toString(Current));
		        		  //Log.d("KEY_Current"," Current");
		        		  //Log.d("goal", "2");
		        		  intent.putExtras(bundle);
		        		  Log.d("goal", "put bundle");
		        		  startActivity(intent);
		        		  //Log.d("goal", "4");
					}
        			  
        			  
        		  });
        		  
        		  //按下list中間的textView也要跳到下一頁
        		  txtnum.setOnClickListener(new OnClickListener() {
  					
  					public void onClick(View v) {
  						// TODO Auto-generated method stub
  						Intent intent = new Intent();
  		        		intent.setClass(TestFineActivity.this,goal.class);
  		        		Bundle bundle =new Bundle();
  		        		bundle.putString("KEY_Goal", txtnum.getText().toString());
  		        		intent.putExtras(bundle);
  		        	   // Log.d("goal", "put bundle");
  		        		startActivity(intent);
  						
  					}
  				});
        		  
        		  //TODO: check whether it is done, then R.checkbox_checked 
                  check.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						Log.d("goal","checked"+txtnum.getText().toString());
						check.setImageResource(R.drawable.checkbox_checked);
					
						// TODO:
							//remember this item (today) is checked
							//current++
							//if current == max 
							//max++
							//if current == 21
							//level++
							//current = 0
					}
                	  
                	  
                  });
                  
                  
                  
                  
        	 }

			@Override
			public void onChildViewRemoved(View arg0, View arg1) {			//REMOVE list item...
				// TODO Auto-generated method stub
				
			}};
			
			
			list.setOnHierarchyChangeListener(listitemclick );
		//******* End of List Item Click ********// 
        
			//******* Array List: list item ******//
			final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  

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
					String titlek=stmap.get(dataKey).toString();
					//String textk=stmap.get("Current"+k2).toString();
					HashMap<String, Object> map = new HashMap<String, Object>();  
					map.put("ItemTitle", titlek);  
					//map.put("Current",textk);
					listItem.add(map); 	
				}
			}//end of for-loop
		  
		  //******* Simple Adapter: listItemAdapter ******//
          final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,  
                new String[] {"ItemTitle"},   
                new int[] {R.id.ItemTitle}  
          );
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
    	        builder.setPositiveButton("ok",  
    	            new DialogInterface.OnClickListener() {  
    	                 public void onClick(DialogInterface dialog, int whichButton) { 
    	                    	
    	                    	String edtInputName=edtInput.getText().toString();
    	                    	
    	                    	if(edtInputName.equals(""))
    	                		{        	            			
    	                			Toast.makeText(TestFineActivity.this, "未輸入目標名稱", Toast.LENGTH_SHORT).show();
    	                  		}
    	                    	        	                		        	            			        	                		       	                  		
    	                    	else{
    	                    	  HashMap<String, Object> map = new HashMap<String, Object>();  
    	                          map.put("ItemTitle",edtInput.getText() );  
    	                         // map.put("Current", 0 );  
    	                          listItem.add(map);  
    	                          listItemAdapter.notifyDataSetChanged();
    	                          
    	                  		  SharedPreferences settings= getSharedPreferences(PREF,0);
    	                		  Editor editor=settings.edit();
    	                          editor.putString("ItemTitle"+edtInput.getText().toString(), edtInput.getText().toString());

    	                		  editor.commit();
    	                    	}}  
    	                });  

    	        builder.setNegativeButton("Cancel",  
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

    
    

}    
    



