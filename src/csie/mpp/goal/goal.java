package csie.mpp.goal;

import java.text.DecimalFormat;
import java.util.HashMap;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class goal extends Activity {
	ImageButton btn_sos;
	TextView goal_name, goal_max, goal_current, goal_level, goal_start;
	public String ngoal;
	String goal, max, current, level, start;

 
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
				//Toast.makeText(goal.this, "垃圾桶被按了", Toast.LENGTH_SHORT).show();
				// test fine activity callForResult
				// 這邊回傳的時候加上goal# & goal name, testfineactivity再做delete goal的動作
				//setResult(RESULT_OK, 
				// 彈出視窗詢問是否真的要刪掉此目標？資料記錄將無法恢復
				//finished();
    		    LayoutInflater inflater = LayoutInflater.from(goal.this.getBaseContext());  
    	        
    		    final View textEntryView = inflater.inflate(R.layout.deletesuredialog, null);  	
    	        final AlertDialog.Builder builder = new AlertDialog.Builder(goal.this);  
    	        builder.setCancelable(false);  
    	       // builder.setIcon(R.drawable.icon);  
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
				

        
        
        btn_sos.setOnClickListener(new ImageButton.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		Log.d("goal","SOS clicked");
//        	Intent intent = new Intent();
//        	intent.setClass(goal.this,TestFineActivity.class);
//        	startActivity(intent);
        		
//        		
        	}
        	       	
        });
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
		//String currrent=bundle.getString("KEY_currrent");
		
		goal_name.setText(goal);
	    goal_max.setText(max);
	    goal_current.setText(current);
	    goal_start.setText(start);
		//textView6.setText(currrent);
		//ngoal=bundle.getString("KEY_Goal");
		//ncurrrent=bundle.getString("KEY_currrent");
	}
	
}