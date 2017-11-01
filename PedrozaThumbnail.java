package com.pedroza.PedrozaThumbnail;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.runtime.util.BoundingBox;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.MediaUtil;
import android.content.Context;

import android.media.ThumbnailUtils;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore; 
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.os.Build.VERSION;
import java.io.File;
import java.io.FileOutputStream;


/**Welcome to my fourth extension ever created.
* This extension provides tools to extract thumbnails from an image or a video.
* @author Carlos Pedroza
*/

@DesignerComponent(
	version = 1,
	description = "Simple tool developed by Carlos Pedroza to extract thumbnails from an image or a video.",
	category = ComponentCategory.EXTENSION,
	nonVisible = true,
	iconName = "https://drive.google.com/uc?export=download&id=0B27zfJqMN6bjdUgwcjFVbm5SSTA")

@SimpleObject(external = true)
public class PedrozaThumbnail extends AndroidNonvisibleComponent implements Component {
	private ComponentContainer container;
	private Context context;
	private static final String LOG_TAG = "PedrozaThumbnail Extension";
	public static final int VERSION = 1;
	
	private String fileName = "thumbnail.jpg";
	private Drawable drawable;
	
	public PedrozaThumbnail (ComponentContainer container) {
		super(container.$form());
		this.container = container;
		context = (Context) container.$context();
		Log.d(LOG_TAG, "PedrozaThumbnail created");
	}
	
	/**
	* Method for getting a thumbnail from a video.
	* @param String path: Text with the path to the video to create the new thumbnail.
	* @param int kind: Number with the specifications of the new thumbnail to be created. It can be Mini (1) or Micro (3).
	* @param int quality: Number with the desired quality of the image from 1 to 100.
	* @return a string with the path of the new thumbnail created.
	*/
	@SimpleFunction(description = "Method for getting a thumbnail from a video. Parameters:"
	+ " String path: Text with the path to the video to create the new thumbnail."
	+ " Number kind: Number with the specifications of the new thumbnail to be created."
	+ " Use 1 for MiniKind or 3 for MicroKind."
	+ " Number quality: Number with the desired quality of the image from 1 to 100."
	+ " Returns a string with the path of the new thumbnail created.")
	public String GetFromVideo (String path, int kind, int quality) {
		String result = "";
		if (android.os.Build.VERSION.SDK_INT >= 8){
			String mPath = Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
			try {
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,kind);
				File imageFile = new java.io.File(mPath);
				FileOutputStream outputStream = new FileOutputStream(imageFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
				outputStream.flush();
				outputStream.close();
				result = (imageFile.toString());
			} catch (Throwable e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
		return result;
    }
	
	/**
	* Method for getting a thumbnail from an image.
	* @param String path: Text with the path to the image to create the new thumbnail.
	* @param int width: Number with the desired width in pixels (px).
	* @param int height: Number with the desired height in pixels (px).
	* @param int quality: Number with the desired quality of the image from 1 to 100.
	* @return a string with the path of the new thumbnail created.
	*/
	@SimpleFunction(description = "Method for getting a thumbnail from an image. Parameters:"
	+ " String path: Text with the path to the image to create the new thumbnail."
	+ " Number width: Number with the desired width in pixels (px)."
	+ " Number height: Number with the desired height in pixels (px)."
	+ " Number quality: Number with the desired quality of the image from 1 to 100."
	+ " Returns a string with the path of the new thumbnail created.")
	public String GetFromImage (String path, int width, int height, int quality) {
		String result = "";
		if (android.os.Build.VERSION.SDK_INT >= 8){
			String mPath = Environment.getExternalStorageDirectory().toString() + File.separator + fileName;
			try {
				String picturePath = (path == null) ? "" : path;
				drawable = MediaUtil.getBitmapDrawable(form, picturePath);
				Bitmap thumb = ThumbnailUtils.extractThumbnail(((BitmapDrawable)drawable).getBitmap(),width,height);
				File imageFile = new java.io.File(mPath);
				FileOutputStream outputStream = new FileOutputStream(imageFile);
				thumb.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
				outputStream.flush();
				outputStream.close();
				result = (imageFile.toString());
			} catch (Throwable e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
		return result;
    }
	
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns number 1 to be used in the kind parameter")
	public int MiniKind () {
		int result = 0;
		if (android.os.Build.VERSION.SDK_INT >= 8){
			result = MediaStore.Video.Thumbnails.MINI_KIND;
		}
		return result;
    }
	@SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns number 3 to be used in the kind parameter")
	public int MicroKind () {
		int result = 0;
		if (android.os.Build.VERSION.SDK_INT >= 8){
			result = MediaStore.Video.Thumbnails.MICRO_KIND;
		}
		return result;
    }
}