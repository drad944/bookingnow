package com.pitaya.bookingnow.app.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitaya.bookingnow.app.R;

public class ButtonPreference extends Preference {
    private Button mBtn;
    private TextView mTitle;
    private String title = "";
    private String btnlabel = "";
    private View.OnClickListener mListener;

    public ButtonPreference(Context context) {
    	super(context);
    }
    
    public ButtonPreference(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }
    
    public ButtonPreference(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    }

    @Override    
    protected View onCreateView(ViewGroup parent) {
	     LayoutInflater layout = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	     View viewGroup = layout.inflate(R.layout.btnpreference, null);
	     mBtn =  (Button)viewGroup.findViewById(R.id.btn);
	     mTitle = (TextView)viewGroup.findViewById(R.id.title);
		 this.mBtn.setText(this.title);
		 this.mTitle.setText(this.btnlabel);
	     mBtn.setOnClickListener(mListener);
	     return viewGroup;
     }
    
    protected void onBindView(View view) {
    	 super.onBindView(view);
	     mBtn =  (Button)view.findViewById(R.id.btn);
	     mTitle = (TextView)view.findViewById(R.id.title);
    }

	 public void setClickListener(View.OnClickListener l) {
	     this.mListener = l;
	 }
	 
	 public void setValues(String [] values){
		 this.title = values[1];
		 this.btnlabel = values[0];
	 }

}
