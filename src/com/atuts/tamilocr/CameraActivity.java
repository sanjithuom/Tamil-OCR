package com.atuts.tamilocr;

import com.atuts.tamilocr.R;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CameraActivity extends NormActivity{					//Activity for Camera tab
	private Button camera;
	private static final int CAMERA_PIC_REQUEST = 2;  				//Constant value for Camera Intent request
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.cameralayout);
		camera = (Button) this.findViewById(R.id.camera);	
		isCamera = true;
		camera.setOnClickListener(new View.OnClickListener() {		//Invoked when camera button is clicked
            public void onClick(View v) {
            	if(img == null){
                	hideConv(true);
                }
            	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
        		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        		hideText(true);
                n = 0;
            }
        });
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		
	    if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK && null != data) {  
	    	img = (Bitmap) data.getExtras().get("data"); 
	    	if(img!=null){   	
	    		image.setImageBitmap(img);
	            attacher = new PhotoViewAttacher(image);
	            hideConv(false);
	    	}
	    	 
	    }
	}
	
}
