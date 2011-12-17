package test.android;

import java.text.DecimalFormat;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class goal extends Activity {
	Button btn_sos;
	TextView goal_name;//Penguin
	public String ngoal;//Penguin
	//public String ncurrrent;//Penguin
	//Action bar的delete Button
	private Button ActionBar_delete_button;
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d("goal","Goal on create!!");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.goal);
        Log.d("goal","find layout");
        
        //findView
        btn_sos=(Button)findViewById(R.id.button1);	//sos
        goal_name=(TextView)findViewById(R.id.detail_goal_name);//Penguin
        ActionBar_delete_button = (Button)findViewById(R.id.deleteGoal);
        
        showResults(savedInstanceState);////Penguin

        //指定AcitonBar 的那顆垃圾桶按下去後要幹嘛
        ActionBar_delete_button.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(goal.this, "垃圾桶被按了", Toast.LENGTH_SHORT).show();
			}
		});
        
        
        btn_sos.setOnClickListener(new Button.OnClickListener()
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
		String goal = bundle.getString("KEY_Goal");
		Log.d("goal", goal);
		//String currrent=bundle.getString("KEY_currrent");
		
		goal_name.setText(goal);
		//textView6.setText(currrent);
		//ngoal=bundle.getString("KEY_Goal");
		//ncurrrent=bundle.getString("KEY_currrent");
	}
	
}