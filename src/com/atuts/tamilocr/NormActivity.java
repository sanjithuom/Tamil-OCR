package com.atuts.tamilocr;

import com.atuts.tamilocr.R;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NormActivity extends Activity{												//parent class for Gallery and Camera Activity
	private Button convert;
	private Button copy;
	private ImageButton rotateC;
	private ImageButton rotateA;
	protected ImageView image;
	private TextView text;
	protected Bitmap img;
	private Bitmap resized;
	private Bitmap imgCopy;
	protected PhotoViewAttacher attacher;												//used for photo zoom
	private ImageHandler imgHandler;
	private Converter converter;
	private ProgressDialog progress;
	protected int n = 0; 																//used for rotating angle
	protected boolean isToastShowed = false;											//Long press to turn 90 message indicator
	protected boolean isCamera;
	protected void onCreate(Bundle savedInstanceState, int layoutID) {					//onCreate method for activity, layoutID is used to specify the layout
		super.onCreate(savedInstanceState);
		setContentView(layoutID);		
		image = (ImageView) this.findViewById(R.id.image);
		rotateC = (ImageButton) this.findViewById(R.id.rotateC);
		rotateC.setLongClickable(true);
		rotateA = (ImageButton) this.findViewById(R.id.rotateA);
		rotateA.setLongClickable(true);
		convert = (Button) this.findViewById(R.id.convert);
		copy = (Button) this.findViewById(R.id.copy);
		text = (TextView) this.findViewById(R.id.text);
		imgHandler = new ImageHandler();
		Typeface type = Typeface.createFromAsset(getAssets(),"LATHA.TTF"); 				//get Tamil font from asset	
		text.setTypeface(type);															//set Tamil font for EditText
		text.setVisibility(View.INVISIBLE);												//initially set editText
		text.setKeyListener(null);
		progress = new ProgressDialog(this);
		converter = new Converter(getFilesDir().getPath());
		hideConv(true);																	//initially hide imageview, convert and rotate buttons
		hideText(true);
		convert.setOnClickListener(new View.OnClickListener() {							//invoked when convert button is clicked
			
            public void onClick(View v) {
            	if (imgCopy != null){													//if the image is rotated copy imgCopy to img
            		img=imgCopy.copy(Bitmap.Config.ARGB_8888, true);					//tesseract API only accept ARGB_8888 config
            		imgCopy.recycle();
            		imgCopy = null;
            	}
            	resized = imgHandler.resize(img, isCamera);								//enlarge the image
            	if(resized != null){
            		
            		new Task().execute();																			
            	}
            }
        });
        
		copy.setOnClickListener(new View.OnClickListener() {							//copy to clipboard

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				int sdk = android.os.Build.VERSION.SDK_INT;
				if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {					//the ClipboardManager in API 10 or below is different from the newer version			    
					android.text.ClipboardManager clipboard = 
				    		(android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				    clipboard.setText(text.getText());
				} else {
				    android.content.ClipboardManager clipboard = 
				    		(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
				    android.content.ClipData clip = 
				    		android.content.ClipData.newPlainText("TamilOCR",text.getText());
				    clipboard.setPrimaryClip(clip);
				}
				
			}	
			
		});
		
		rotateC.setOnClickListener(new View.OnClickListener() {							//invoked when rotate clockwise is clicked
			@Override
			public void onClick(View v) {			
				
				rotateClick((++n)%360);													//rotate by 1 degree in clock wise
				toast();
			}
		});
			
		rotateC.setOnLongClickListener(new View.OnLongClickListener() {					//invoked when rotate clockwise is long clicked
			@Override
			public boolean onLongClick(View v) {
				rotateLongClick(90);													//rotate by 90 degree in clock wise
		    	return true;
			}
		});
		
		rotateA.setOnClickListener(new View.OnClickListener() {							//invoked when rotate anti-clockwise is clicked
			@Override
			public void onClick(View v) {			
				rotateClick((--n)%360);													//rotate by 1 degree in anti-clock wise
				toast();
			}
		});
			
		rotateA.setOnLongClickListener(new View.OnLongClickListener() {					//invoked when rotate anti-clockwise is long clicked
			@Override
			public boolean onLongClick(View v) {
					rotateLongClick(270);												//rotate by 90 degree in anti-clock wise
		    		return true;
			}
		});

	}
	public void rotateLongClick(int n){													//rotating method when long pressed
		img=imgHandler.rotate(img, n);
    	image.setImageBitmap(img);
    	n = 0;
    	imgCopy=img.copy(Bitmap.Config.ARGB_8888, true);								//tesseract-api only accept ARGB_8888 config
		attacher.update();																//updating the photo zoom attacher
	}
	public void rotateClick(int n){														//rotating method for normal click
		imgCopy=imgHandler.rotate(img, n);
	    image.setImageBitmap(imgCopy);
	    attacher.update();																//updating the photo zoom attacher
	}
	public void hideConv(boolean b){													//used to control the visibility of imageView, convert and rotate buttons
		if(b){																			//hide
			rotateC.setVisibility(View.INVISIBLE);
			rotateA.setVisibility(View.INVISIBLE);
			convert.setVisibility(View.INVISIBLE);
			image.setVisibility(View.INVISIBLE);
		}
		else{																			//show
			rotateC.setVisibility(View.VISIBLE);
			rotateA.setVisibility(View.VISIBLE);
			convert.setVisibility(View.VISIBLE);
			image.setVisibility(View.VISIBLE);
		}
	}
	public void hideText(boolean b){
		if(b){
			text.setVisibility(View.INVISIBLE);
			copy.setVisibility(View.INVISIBLE);
		}else{
			text.setVisibility(View.VISIBLE);
			copy.setVisibility(View.VISIBLE);
		}
	}
	public void toast(){
		if(!isToastShowed){
			Context context = getApplicationContext();
			CharSequence text = "Long Press for rotate 90 degree";
			int duration = Toast.LENGTH_SHORT;
	
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			isToastShowed=true;
		}
	}
	private class Task extends AsyncTask<Void, Void, Void>{

		String recognizedText;
		@Override
	    protected void onPostExecute(Void result)
	    {
	        super.onPostExecute(result);
	        text.setText(recognizedText);
    		hideText(false);															//text view is set to visible
    		progress.dismiss();
    		img.recycle();																//freeing the space allocated for bitmap
    		resized.recycle();
    		resized=null;
    		img = null;
    		n = 0;																		//setting rotate angle back to normal
	    }

	    @Override
	    protected void onPreExecute()
	    {
	        
	        super.onPreExecute();
	        progress.setTitle("Converting");
	        progress.setMessage("Wait while Converting...");
	        progress.setCancelable(false);
	        progress.show();
	        hideConv(true);																//hiding imageview, convert and rotate buttons after conversion
	    }
	    @Override
		protected Void doInBackground(Void... params) {
	    	recognizedText= converter.convert(resized);									//convert the image
			return null;
		}
		
	}
}
