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
    Button Btadd;
    Button ActionBarBtn_add;		//action bar, add new goal button
    ImageButton imagebt;
    ImageButton check;
    CheckBox checkBox1;				//
    TextView ItemTitle;
    
//TextView Current;
//int Current =0;
    
    public static final String PREF="Directoryhw3";	//what is this?
    HashMap<String, Object> select=new HashMap<String, Object>();	//array list: select
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);		//is this for self define title bar?
        setContentView(R.layout.main);							//set content view: main.xml
        
        Btadd=(Button)this.findViewById(R.id.button1);					//find view: btadd
        ActionBarBtn_add=(Button)this.findViewById(R.id.addGoal);		//find view: action bar button
        final ListView list = (ListView) findViewById(R.id.list01); 	//(?) why use 'final'? listview(list01) of main.xml
        
        ListView.OnHierarchyChangeListener listitemclick = new ListView.OnHierarchyChangeListener() {//item click listener (listitemclick)
        	@Override
        	public void onChildViewAdded(View parent, View child) {		//Called when a new child is added to a parent view.
        		//Button delbtn = (Button) child.findViewById(R.id.deletebutton);//delete button
        		//final TextView txtnum = (TextView) child.findViewById(R.id.number);

        		  final TextView txtnum = (TextView) child.findViewById(R.id.ItemTitle);	//find view: txtnum list.xml > item title (?) what this for?
        		  //final TextView Current = (TextView) child.findViewById(R.id.Current);
                  //imagebt = (ImageButton)child.findViewById(R.id.image1);
                  //checkBox1 = (CheckBox)child.findViewById(R.id.checkBox1);
        		  Log.d("goal","onChildViewAdded");
        		  
        		  check = (ImageButton)child.findViewById(R.id.btn_checkbox);
                  //check.setImageDrawable(R.drawable.checkbox);
                  check.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//check.setImageDrawable(R.drawable.checkbox_checked);
						Log.d("goal","checked");
					}
                	  
                	  
                  });
        		  //check.setOnClickListener(new OnClickListener())
                  
                  checkBox1.setOnClickListener(new CheckBox.OnClickListener()//�s�W
                  {
                	  public void onClick(View V)
                	{
                     
                   	  if(checkBox1.isChecked()==true)
                   	     { 
                   	     
                   	 LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  
          	        final View textEntryView = inflater.inflate(R.layout.suredialoglayout, null);           	        
          	      final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
      	        builder.setCancelable(false);  
      	       // builder.setIcon(R.drawable.icon);  
      	        builder.setTitle("你確定你完成了這項工作了嗎?");
      	        
          	        builder.setPositiveButton("確定",  
          	                new DialogInterface.OnClickListener() {  
          	                    public void onClick(DialogInterface dialog, int whichButton) { 
          	                    	checkBox1.setChecked(true);
          	                     
          	                     	/*if(checkBox1.isChecked()==true)
          	                     	{	
          	                     	    Current=Current+1;

          	                     	}*/
          	                    }
          	                  });
          	       
          	        builder.setNegativeButton("取消",  
          	                new DialogInterface.OnClickListener() {  
          	                    public void onClick(DialogInterface dialog, int whichButton) {  
          	                    	
          	                    	checkBox1.setChecked(false);
          	                 
          	                    	
          	                        setTitle("");  
          	                     
          	                    }  
          	                });  
          		
          		AlertDialog	alertDialog=builder.create();
                  alertDialog.show();

          	}
                	}
          	      }); 
               
                  
                  imagebt.setOnClickListener(new Button.OnClickListener()//�s�W
                  {
                  	public void onClick(View V)
                  	{
                  		setTitle("選取了x"+txtnum.getText().toString());
                  	}
                  });  
        	 }

			@Override
			public void onChildViewRemoved(View arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}};
			
			list.setOnHierarchyChangeListener(listitemclick );
           // list.setAdapter(adapter);
        
        //�ͦ��ʺAarray�A�[�J�ƾ�  
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
          
      	SharedPreferences settings1= getSharedPreferences(PREF,0);
      	Map<String,?>stmap = settings1.getAll();
      	
      	for(String dataKey : stmap.keySet())   
      	{   
      		
      		String k1=dataKey.substring(0,9);
      		String k2=dataKey.substring(9);
      		//Btdel.setText(k1);
      		Log.e("xx",k1 ); 
      	Log.e("xx",k2 ); 
      		if(k1.equals("ItemTitle"))
      		{
      			String titlek=stmap.get(dataKey).toString();
      		    //String textk=stmap.get("Current"+k2).toString();
      			HashMap<String, Object> map = new HashMap<String, Object>();  
      	         map.put("ItemTitle", titlek);  
      	        // map.put("Current",textk);
      	         listItem.add(map); 
      			
      		}
      	}
          
          final SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,  
                new String[] {"ItemTitle"},   
                new int[] {R.id.ItemTitle}  
          );    
          list.setAdapter(listItemAdapter); 
          //list.setOnHierarchyChangeListener(listener)
          list.setOnItemClickListener(new OnItemClickListener() {  
        	 
        @Override  
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
             //ListView list = (ListView)arg1;
            HashMap<String, Object> map2 = (HashMap<String, Object>) list.getItemAtPosition(arg2);
            HashMap<String, Object> itemAtPosition = (HashMap<String, Object>) list.getItemAtPosition(arg2);
            select= itemAtPosition;
    
         	Intent intent = new Intent();
         	intent.setClass(TestFineActivity.this,goal.class);
         	Bundle bundle =new Bundle();
         	bundle.putString("KEY_Goal", map2.get("ItemTitle").toString());
         	//Integer.toString(Current);

         	Log.e("test", "1");
         	//bundle.putString("KEY_Current", Integer.toString(Current));
         	Log.e("KEY_Current"," Current");
         	Log.e("test", "2");
            intent.putExtras(bundle);
            Log.e("test", "3");
         	startActivity(intent);
         	Log.e("test", "4");
         	
             
             setTitle("增加新的目標");
          	}  
          });      
    

    //add new goal (action bar add button)
    ActionBarBtn_add.setOnClickListener(new Button.OnClickListener()
    {
    	public void onClick(View V)
    	{
    		    LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  
    	        final View textEntryView = inflater.inflate(R.layout.dialoglayout, null);  
    	        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput); 
    	        //final EditText edtInput1=(EditText)textEntryView.findViewById(R.id.Current); 
    	        
    	        final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
    	        builder.setCancelable(false);  
    	       // builder.setIcon(R.drawable.icon);  
    	        builder.setTitle("增加新的目標");  
    	        
    	        builder.setView(textEntryView);  
    	        builder.setPositiveButton("ok",  
    	                new DialogInterface.OnClickListener() {  
    	                    public void onClick(DialogInterface dialog, int whichButton) { 
    	                    	
    	                    	String edtInputName=edtInput.getText().toString();
    	                    	
    	                    	if(edtInputName.equals(""))
    	                		{        	            			
    	                			Toast.makeText(TestFineActivity.this, "你確定你完成了這項工作了嗎?", Toast.LENGTH_SHORT).show();
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

    	                		  //alertDialog.dismiss();
    	                		  //alertDialog.cancel();
    	                    	}}  
    	                });  

    	        builder.setNegativeButton("Cancel",  
    	                new DialogInterface.OnClickListener() {  
    	                    public void onClick(DialogInterface dialog, int whichButton) {  
    	                        setTitle("");  
    	                    }  
    	                });  
    		
    		AlertDialog	alertDialog=builder.create();
            alertDialog.show();

    		//builder.show(); 

    	}
    });
          
//    Btadd.setOnClickListener(new Button.OnClickListener()//�s�W
//    {
//    	public void onClick(View V)
//    	{
//    		    LayoutInflater inflater = LayoutInflater.from(TestFineActivity.this.getBaseContext());  
//    	        final View textEntryView = inflater.inflate(R.layout.dialoglayout, null);  
//    	        final EditText edtInput=(EditText)textEntryView.findViewById(R.id.edtInput); 
//    	        //final EditText edtInput1=(EditText)textEntryView.findViewById(R.id.Current); 
//    	        
//    	        final AlertDialog.Builder builder = new AlertDialog.Builder(TestFineActivity.this);  
//    	        builder.setCancelable(false);  
//    	       // builder.setIcon(R.drawable.icon);  
//    	        builder.setTitle("增加新的目標");  
//    	        
//    	        builder.setView(textEntryView);  
//    	        builder.setPositiveButton("ok",  
//    	                new DialogInterface.OnClickListener() {  
//    	                    public void onClick(DialogInterface dialog, int whichButton) { 
//    	                    	
//    	                    	String edtInputName=edtInput.getText().toString();
//    	                    	
//    	                    	if(edtInputName.equals(""))
//    	                		{        	            			
//    	                			Toast.makeText(TestFineActivity.this, "你確定你完成了這項工作了嗎?", Toast.LENGTH_SHORT).show();
//    	                  		}
//    	                    	        	                		        	            			        	                		       	                  		
//    	                    	else{
//    	                    	  HashMap<String, Object> map = new HashMap<String, Object>();  
//    	                          map.put("ItemTitle",edtInput.getText() );  
//    	                         // map.put("Current", 0 );  
//    	                          listItem.add(map);  
//    	                          listItemAdapter.notifyDataSetChanged();
//    	                          
//    	                  		  SharedPreferences settings= getSharedPreferences(PREF,0);
//    	                		  Editor editor=settings.edit();
//    	                          editor.putString("ItemTitle"+edtInput.getText().toString(), edtInput.getText().toString());
//    	                       
//    	                	
//    	                		  editor.commit();
//
//    	                		  //alertDialog.dismiss();
//    	                		  //alertDialog.cancel();
//    	                    	}}  
//    	                });  
//
//    	        builder.setNegativeButton("Cancel",  
//    	                new DialogInterface.OnClickListener() {  
//    	                    public void onClick(DialogInterface dialog, int whichButton) {  
//    	                        setTitle("");  
//    	                    }  
//    	                });  
//    		
//    		AlertDialog	alertDialog=builder.create();
//            alertDialog.show();
//
//    		//builder.show(); 
//
//    	}
//    });
        
}   

    
    

}    
    



