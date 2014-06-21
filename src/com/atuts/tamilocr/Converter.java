package com.atuts.tamilocr;

import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Converter{	
	private String path;
	public Converter(String path){
		this.path = path;
	}
	public String convert(Bitmap img){					//convert img to text
		TessBaseAPI baseApi = new TessBaseAPI();
    	baseApi.init(path, "tam");
    	// Eg. baseApi.init("/mnt/sdcard/tesseract/tessdata/tam.traineddata", "tam");
    	baseApi.setImage(img);
    	String recognizedText = baseApi.getUTF8Text();
    	baseApi.end();
    	recognizedText = fix(recognizedText);
    	return recognizedText;
    	
	}
	public String fix(String st){
		String[] str=st.split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<str.length;i++){
			if(str[i].contains("கி")&&str[i].indexOf("கி")!=str[i].length()-2){
				int n=str[i].indexOf("கி");
				if(str[i].charAt(n+3)!='ழ'&&str[i].charAt(n+3)!='ற'){
					if((n+4)<str[i].length()){
						if(str[i].charAt(n+4)=='ஈ'){
							str[i]=str[i].replace(str[i].substring(n,n+3), str[i].substring(n+2,n+3)+"ொ");
						}
						else{
							str[i]=str[i].replace(str[i].substring(n,n+3), str[i].substring(n+2,n+3)+"ெ");
						}
					}
				}
		    }
			if(str[i].contains("ஈ")){
		    	str[i]=str[i].replace("ஈ", "ா");
		    }
		    if(i==str.length-1){
			sb.append(str[i]);
		    }
		    else{
			sb.append(str[i]+" ");
		    }
		}
		return sb.toString();
	 }
	 
}
