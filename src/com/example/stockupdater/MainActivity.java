package com.example.stockupdater;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



public class MainActivity extends Activity implements OnItemSelectedListener
{

	
	int flag=0;
	int flag_Company=0;
	int flag_Index=0;
	String stock_Data;
	FileOutputStream fos=null;
	private String[] Stock = { "LSE", "NSE", "BSE", "NYSE",
			   "TSE", "HKE", "SSE", "NZSE","JSE", "TSX","RTS","ASX","FWB","NASDAQ" };

	ArrayList<String> stockList=new ArrayList<String>();
	ArrayAdapter<String> adapter_Stock;
	ListView lv;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		final Spinner et1=(Spinner)findViewById(R.id.spinner1);
		final EditText et2=(EditText)findViewById(R.id.editText2);
		final Button bt1=(Button)findViewById(R.id.button1);
		final Button bt2=(Button)findViewById(R.id.Button01);
		final Button bt3=(Button)findViewById(R.id.Button02);
		
		adapter_Stock=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Stock);
		
		adapter_Stock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		
		et1.setAdapter(adapter_Stock);
		et1.setOnItemSelectedListener(this);
		bt2.setEnabled(false);
		bt3.setEnabled(false);
		
		bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				try
				{
					String fileName="table1.txt";
					
					String Company_Name=(String)et1.getSelectedItem().toString();
					
				if(!(Company_Name.equals("")) && !( et2.getText().toString().equals("")))
				{	
					//=et1.getText().toString().toUpperCase();
					String Index=et2.getText().toString().toUpperCase();
					
					String str=Company_Name+" "+Index;
					
					if(flag==0)
						fos=openFileOutput(fileName, Context.MODE_PRIVATE);
					else
						fos=openFileOutput(fileName, Context.MODE_APPEND);
					
					fos.write(str.getBytes());
				//	fos.write(Index.getBytes());
					fos.write(" ".getBytes());
					fos.close();
					bt2.setEnabled(true);
					bt3.setEnabled(true);
					
					Toast.makeText(MainActivity.this, "Data Saved: "+(String)et1.getSelectedItem()+" "+et2.getText().toString(),Toast.LENGTH_SHORT ).show();
					
					
				}
				else
					Toast.makeText(MainActivity.this,"Enter valid details",Toast.LENGTH_SHORT ).show();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					

				}
			}
		});
		
		
		bt2.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				
				// TODO Auto-generated method stub
				//et1.set("");
				et2.setText("");
				adapter_Stock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				et1.setFocusable(true);
				et2.setBackgroundColor(Color.GREEN);
				et2.setTextColor(Color.parseColor("#bdbdbd"));
				flag=1;			
			}
		});
		
		bt3.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				String table_name="table1.txt";
				
				try
				{
					FileInputStream fis=openFileInput(table_name);
					InputStreamReader isr=new InputStreamReader(fis);
					StringBuilder sb=new StringBuilder();
					char[] data=new char[2048];
					
					int l;
					while((l=isr.read(data))!=-1)
					{
						sb.append(data,0,l);
						
					}
					//sb.append("\0",0,l);
					stock_Data=sb.toString();
					//Toast.makeText(MainActivity.this, stock_Data,Toast.LENGTH_SHORT ).show();
					String[] stock=stock_Data.split(" ");
					String[] company=new String[10];
					String[] index=new String[10];
					int m=0,n=0;
					for(int i=0;i<stock.length;i++)
					{
						if(i%2==0)
						{
							company[m++]=stock[i];
							
						}
						else
						{
							index[n++]=stock[i];
							
						}
					}
					
					Bundle b=new Bundle();
					b.putStringArray("Exchange", company);
					b.putStringArray("Company", index);
					b.putInt("m", m);
					Intent it=new Intent(MainActivity.this,UpdateIntent.class);
					
					it.putExtras(b);
					startActivity(it);
					
				}
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
			}
		});
					
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
