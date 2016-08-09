
package com.example.stockupdater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class UpdateIntent extends Activity {
 
    /** Called when the activity is first created. */
	
	ArrayList<HashMap<String,String>> stockList=new ArrayList<HashMap<String, String>>();
	String serverURL="";
	int flag=0;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
     
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_intent);
        
        
        
        //Receiving the data from MainActivity
        
        Bundle b=this.getIntent().getExtras();
        String Exchange[]=b.getStringArray("Exchange");
        String Company[]=b.getStringArray("Company");
        
        String url="";
        int n=b.getInt("m");
        
        for(int i=0;i<n;i++)
        {
        	url=url+Exchange[i]+":"+Company[i]+",";
        	
        }
        
        
       // url+=Exchange[n-1]+":"+Company[n-1];
        
         
        serverURL = "http://finance.google.com/finance/info?client=ig&q="+url;
        Toast.makeText(UpdateIntent.this, serverURL, Toast.LENGTH_SHORT).show();
        final Handler handler=new Handler();
        Timer timer=new Timer();
        
        TimerTask doAsynchronousTask=new TimerTask() 
        {
			
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				handler.post(new Runnable() 
				{
					
					@Override
					public void run() 
					{
						// TODO Auto-generated method stub
						
						new LongOperation().execute(serverURL);
					}
				});
				
			}
		};
        
		
		timer.schedule(doAsynchronousTask, 0, 2000);
        
		
         
    }
     
     
    // Class with extends AsyncTask class
    private class LongOperation  extends AsyncTask<String, Void, Void>
    {
         
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(UpdateIntent.this);
         
       // TextView uiUpdate = (TextView) findViewById(R.id.textView1);
        
        void Sleep(int ms)
        {
            try
            {
                Thread.sleep(ms);
            }
            catch (Exception e)
            {
            }
        }
         
        protected void onPreExecute() {
            // NOTE: You can call UI Element here.
             
            //UI Element
            if(flag==0)
            {
	            Dialog.setMessage("Downloading source..");
	            Dialog.show();
	            flag=1;
            }
        }
 
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {
                 
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
                 
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            }
             
            return null;
        }
         
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.
             
            // Close progress dialog
        	 		Dialog.dismiss();
             
            if (Error != null) {
                 
            	Toast.makeText(UpdateIntent.this, "Error",Toast.LENGTH_SHORT).show();;
                 
            } 
            else
            {
                 
            	Content=Content.replace('/', ' ');
            	//Toast.makeText(UpdateIntent.this, Content,Toast.LENGTH_SHORT).show();
            	try
				{
					JSONArray jArray=new JSONArray(Content);
					
					for(int i=0;i<jArray.length();i++)
						stockList.clear();
					
					
					for(int i=0;i<jArray.length();i++)
					{
						
						JSONObject c=jArray.getJSONObject(i);
						
						String Exchange=c.getString("e");
						String Company=c.getString("t");
						String Price=c.getString("l_cur");
						String Change=c.getString("c");
						String k=c.getString("cp");
						String ChangePer=k+"%";
						
						HashMap<String,String> map=new HashMap<String, String>();
						
						map.put("e",Exchange);
						map.put("t", Company);
						map.put("l_cur",Price);
						map.put("c", Change);
						map.put("cp", ChangePer);
						
						stockList.add(map);
						
					}
				} 
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ListView lv=(ListView)findViewById(R.id.listView1);
				
				ListAdapter adapter = new SimpleAdapter(UpdateIntent.this, stockList,R.layout.list,new String[] { "e","t","l_cur","c","cp" }, new int[] {  R.id.textView2,R.id.textView1,R.id.TextView01,R.id.textView3,R.id.textView4});
				
				lv.setAdapter(adapter);
				
				Sleep(1000);
				
				
             }
        }
         
    }
}