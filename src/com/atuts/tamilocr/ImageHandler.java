package com.atuts.tamilocr;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageHandler {
	
	public Bitmap resize(Bitmap img, boolean isCamera){							//enlarge the image
		if(img != null){
			
			double num= Math.max(img.getWidth(), img.getHeight());
			double factor= 2048.0/num;											//factor to zoom the image 
			
			return Bitmap.createScaledBitmap(img,(int)(img.getWidth()*factor), 
					(int)(img.getHeight()*factor), true)
					.copy(Bitmap.Config.ARGB_8888, true);
			
			
		}
        return img;
	}
	
	public Bitmap rotate(Bitmap img, int i){						//rotate the img by i degrees
		if(i != 0){
			Matrix matrix = new Matrix();
	    	matrix.postRotate(i);
	    	Bitmap rotatedBitmap = Bitmap.createBitmap(img , 0, 0, 
	    			img .getWidth(), img .getHeight(), matrix, true);
	    	img=rotatedBitmap.copy(Bitmap.Config.ARGB_8888, true);
	    	rotatedBitmap.recycle();
		}
    	return img;
	}
	
}
