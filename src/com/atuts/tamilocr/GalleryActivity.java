package com.atuts.tamilocr;

import com.atuts.tamilocr.R;

import uk.co.senab.photoview.PhotoViewAttacher;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class GalleryActivity extends NormActivity{						//Activity for Gallery tab
	private Button open;
	private static int RESULT_LOAD_IMAGE = 1;							//Constant value for Gallery Intent request
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.gallerylayout);
		open = (Button) this.findViewById(R.id.open);
		isCamera = false;
		open.setOnClickListener(new View.OnClickListener() {			//Invoked when open button is clicked
            public void onClick(View v) {
                if(img == null){
                	hideConv(true);
                }
           		 Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
                    startActivityForResult(i, RESULT_LOAD_IMAGE);                   
                    hideText(true);
                    n = 0;
            }
        });
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            img= BitmapFactory.decodeFile(picturePath);
            image.setImageBitmap(img);
            attacher = new PhotoViewAttacher(image);
            if(img!=null){
            	hideConv(false);
            }
        }
	}
	
}
