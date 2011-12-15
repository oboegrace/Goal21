package test.android;

import java.text.DecimalFormat;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class goal extends Activity {
	Button btn_sos;
	TextView goal_name;//Penguin
	public String ngoal;//Penguin
	//public String ncurrrent;//Penguin

	
	public void onCreate(Bundle savedInstanceState) {
		Log.d("goal","Goal on create!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal);
        Log.d("goal","find layout");
        btn_sos=(Button)findViewById(R.id.button1);	//sos
        goal_name=(TextView)findViewById(R.id.detail_goal_name);//Penguin
        showResults(savedInstanceState);////Penguin

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