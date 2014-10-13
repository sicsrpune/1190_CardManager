package com.cardmanager;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Update2 extends Activity {
	
	private ArrayList<Card> cards;
	ListView listView;
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allcardno);
		pref = getSharedPreferences("users.dat", MODE_PRIVATE);
		String email = pref.getString("username", "null");//getIntent().getExtras().getString("email");
		String type = getIntent().getExtras().getString("cardtype");
		DBHelper handler = new DBHelper(getBaseContext(),DBHelper.DATABASE_NAME);
		cards = handler.getUserCards(email, type);
		ArrayList<String> names = new ArrayList<String>();
		for(Card c: cards)
		{
			names.add(c.getCard_name());
		}
		//Log.e("Cards", names.get(0));
		CustomAdapter ca = new CustomAdapter(getApplicationContext(), names);
		listView = (ListView) findViewById(R.id.all_cards);
		listView.setAdapter(ca);
		final Context context = this;
		listView.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				final Card c = cards.get(position);
				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle("Update Details");
				alertDialogBuilder.setMessage("Input text to search");
				LinearLayout card_entry = (LinearLayout) findViewById(R.layout.activity_card_entry);
				View view = card_entry.inflate(getApplicationContext(), R.layout.activity_card_entry, null);
				final EditText card_number;
				final EditText card_name;
				final EditText card_holder_name;
				final EditText validity_date;
				final EditText cvv;
				final EditText pin;
				final EditText password;
				final EditText bank_name;
				final RadioGroup rg;
				card_number = (EditText) view.findViewById(R.id.Card_number);
				card_name = (EditText) view.findViewById(R.id.Card_name);
				card_holder_name = (EditText) view.findViewById(R.id.Card_holder_name);
				validity_date = (EditText) view.findViewById(R.id.Validity_date);
				cvv = (EditText) view.findViewById(R.id.Card_cvv);
				pin = (EditText) view.findViewById(R.id.card_pin);
				pin.setInputType(InputType.TYPE_CLASS_TEXT);
				password = (EditText) view.findViewById(R.id.Card_pass);
				password.setInputType(InputType.TYPE_CLASS_TEXT);
				bank_name = (EditText) view.findViewById(R.id.Card_bank_name);
				rg =(RadioGroup) view.findViewById(R.id.radioGroup1);
				card_number.setText(c.getCard_no());
				card_name.setText(c.getCard_name());
				card_holder_name.setText(c.getCard_holder_name());
				cvv.setText(c.getCard_cvv());
				pin.setText(c.getCard_pin());
				password.setText(c.getCard_password());
				bank_name.setText(c.getCard_bank());
				validity_date.setText(c.getCard_validity());
				String radioValue = c.getCard_type();
				switch(radioValue)
				{
					case "Debit Card": rg.check(R.id.radio0); 
										break;
					case "Credit Card": rg.check(R.id.radio1);
										break;
					case "Net Banking": rg.check(R.id.radio3);
										break;
					case "Shopping Card": rg.check(R.id.radio4);
										break;
					case "Others": rg.check(R.id.radio5);
										break;
				}
				alertDialogBuilder.setView(view);
				final Context context = getApplicationContext();
				
				alertDialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String get_card_no = card_number.getText().toString();
						String get_card_name = card_name.getText().toString();
						String get_card_holder_name = card_holder_name.getText().toString();
						String get_card_validity = validity_date.getText().toString();
						String get_card_cvv = cvv.getText().toString();
						String get_card_pin = pin.getText().toString();
						String get_card_password = password.getText().toString();
						String get_card_bank = bank_name.getText().toString();
						String get_card_type = "";
						if(rg.getCheckedRadioButtonId()!=-1){
						    int id= rg.getCheckedRadioButtonId();
						    View radioButton = rg.findViewById(id);
						    int radioId = rg.indexOfChild(radioButton);
						    RadioButton btn = (RadioButton) rg.getChildAt(radioId);
						    get_card_type=btn.getText().toString();
						}
						Card card = new Card(get_card_no, get_card_name, get_card_holder_name, get_card_validity, get_card_password, get_card_pin, get_card_cvv, get_card_bank, get_card_type, c.getCard_user_email());
					    card.setCard_id(c.getCard_id());
					    DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DATABASE_NAME);
						dbHelper.update(card);
						finish();
					    
					}
				});	
				
				AlertDialog al = alertDialogBuilder.create();
				al.show();
				
			}

			
			
		});
	}
	
	@Override
	public boolean  onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.about:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("About Us");
			builder.setMessage("Application made by:Sreejib Das using this application you can manage your all type of cards like Debit Card, Credit Card, Shopping Card, Net Banking and Others.");
			builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface d, int arg1) {
					// TODO Auto-generated method stub
					d.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		return true;
	}
	
}
