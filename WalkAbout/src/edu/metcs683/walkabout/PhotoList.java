package edu.metcs683.walkabout;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.metcs683.walkabout.controller.PhotoListController;
import edu.metcs683.walkabout.model.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * User interface for the photo list screen.
 * 
 * @author Ryszard Kilarski
 * 
 */
public class PhotoList extends Activity {

	private PhotoListController controller;
	private GridView photoList;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri imageURI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		initializeUI();
		controller = new PhotoListController(getApplicationContext(), this);
		loadData();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String fnyi = getString(R.string.fnyi);
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.camera:
			String cameraMessage = getString(R.string.camera_not_available_text);
			try {
				if (Camera.getNumberOfCameras() > 0) {
					intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// Intent intent = new
					// Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

					// create a file to save the image
					imageURI = getOutputImageFileUri();
					// set the image file name
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);

					// start the image capture Intent
					startActivityForResult(intent,
							CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

					loadData();
				} else {
					Toast.makeText(getApplicationContext(), cameraMessage,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), cameraMessage,
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.import_image:
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 0);
			loadData();
			break;
		case R.id.edit_waypoint:
			intent = new Intent(this, WaypointDetail.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle bundle = new Bundle();
			long id = this.getIntent().getLongExtra("waypointId", 0);
			bundle.putLong("waypointId", id);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.delete_waypoint:
			String title = "Delete Waypoint";
			String message = "This action will delete the current waypoint and all associated images.  Are you sure?";
			String yes = getString(R.string.yes);
			String no = getString(R.string.no);
			new AlertDialog.Builder(this)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									deleteWaypoint();
									finish();
								}
							})
					.setNegativeButton(no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Do nothing.
								}
							}).show();
			break;
		case R.id.map_waypoint:
			Toast.makeText(getApplicationContext(), fnyi, Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void deleteWaypoint() {
		long id = this.getIntent().getLongExtra("waypointId", 0);
		controller.deleteWaypoint(id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String fnyi = getString(R.string.fnyi);

		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Image saved to:\n" + imageURI.toString(),
						Toast.LENGTH_LONG).show();
				// Save image to database.
				long id = this.getIntent().getLongExtra("waypointId", 0);
				Image image = new Image(0, id, imageURI.toString());
				controller.saveImage(image);

				loadData();
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		}
		if ((resultCode == RESULT_OK) && (data != null)
				&& (data.getAction() == Intent.ACTION_PICK)) {
			Toast.makeText(getApplicationContext(), fnyi, Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(this, "Image saved to:\n" + data.getData(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void initializeUI() {
		photoList = (GridView) findViewById(R.id.photoList);

		photoList.setOnItemClickListener(new ImageClickHandler());
	}

	private void loadData() {
		Intent intent = this.getIntent();
		long id = intent.getLongExtra("waypointId", 0);
		List<Image> list = controller.getImageList(id);
		photoList.setAdapter(new ImageAdapter(this, list));
		this.setTitle(controller.getWaypointDescription(id));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_photo_list, menu);
		return true;
	}

	private class ImageClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Toast.makeText(getApplicationContext(), "" + position,
					Toast.LENGTH_SHORT).show();

			// Get the image from the adapter...
			Image image = (Image) parent.getAdapter().getItem(position);
			// ...and get its URI.
			Uri uri = Uri.parse(image.getImageURI());
			Intent viewImageIntent = new Intent(
					android.content.Intent.ACTION_VIEW);
			viewImageIntent.setDataAndType(uri, "image/jpeg");
			startActivity(viewImageIntent);
		}
	}

	/**
	 * Adapter class for the images.
	 */
	private class ImageAdapter extends BaseAdapter {
		private Context context;
		List<Image> list;

		public ImageAdapter(Context context, List<Image> imageList) {
			this.context = context;
			this.list = imageList;
		}

		public int getCount() {
			return list.size();
		}

		public Image getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageURI(Uri.parse(list.get(position).getImageURI()));
			return imageView;
		}

	}

	/** Create a File for saving an image or video */
	private static File getOutputImageFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"WalkAbout");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("WalkAbout", "failed to create directory "
						+ mediaStorageDir.getParentFile());
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}

	/**
	 * Create file URI for image.
	 * 
	 * @return
	 */
	private static Uri getOutputImageFileUri() {
		return Uri.fromFile(getOutputImageFile());
	}
}