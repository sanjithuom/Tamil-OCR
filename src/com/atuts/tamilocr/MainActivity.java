package com.atuts.tamilocr;

import java.io.*;

import com.atuts.tamilocr.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity{									//main activity class which holds all the tabs
	private String path;																//path for private storage space for the app in device
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		
		path = getFilesDir().getPath();
		TabHost tabHost = getTabHost(); 
		File f= new File(path +"/tessdata", "tam.traineddata");
		if(!f.exists()){														//if traineddata is not available copy it into the device
			try {
				copyAssets();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Gallery tab
		Intent intentGallery = new Intent().setClass(this, GalleryActivity.class);
		TabSpec tabSpecGallery = tabHost
		  .newTabSpec("Gallery")
		  .setContent(intentGallery);
		tabSpecGallery.setIndicator("Gallery", getResources().getDrawable(R.drawable.gallery_tab));
 
		// Camera tab
		Intent intentCamera = new Intent().setClass(this, CameraActivity.class);
		TabSpec tabSpecCamera = tabHost
		  .newTabSpec("Camera")
		  .setContent(intentCamera);
		tabSpecCamera.setIndicator("Camera", getResources().getDrawable(R.drawable.camera_tab));
		
		tabHost.addTab(tabSpecGallery);
		tabHost.addTab(tabSpecCamera);

		tabHost.setCurrentTab(0);												//Gallery Tab is the starting tab
	}
	private void copyAssets() throws IOException{								//copying assets into device
	    AssetManager assetManager = getAssets();
	    InputStream in = null;
	    OutputStream out = null;
	    String filename="tam.traineddata";
	    File dir = new File(path+"/tessdata");									//creating tessdata folder, tesseract API requires the traineddata to be stored inside this folder
        dir.mkdirs();
	    File outFile = new File(path+"/tessdata", filename);					//create tam.traineddata file in the private storage it should be inside the folder tessdata
        out = new FileOutputStream(outFile);	
        for(int i = 1; i < 9; i++){												//files are broken into 1MB chunks, in Android 2.2 files which are greater than 1MB cannot be uncompressed from APK
        	String inFile = i+ ".dat";											//chunks are stored in assets in this format 1.dat, 2.dat, 3.dat, ..... 
	        in = assetManager.open(inFile);
	        copyFile(in, out);													//copy the chunk into traineddata file
        }
	    in.close();
	    out.flush();
	    out.close();  
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {	//used to copy the files
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}



}
