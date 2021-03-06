package edu.metcs683.walkabout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import edu.metcs683.walkabout.controller.WaypointPhotoDeleteController;
import edu.metcs683.walkabout.model.Image;
import edu.metcs683.walkabout.uihelper.ErrorDisplay;
import edu.metcs683.walkabout.uihelper.ImageAdapter;

/**
 * User interface for the delete images from waypoints functionality.
 * 
 * @author Ryszard Kilarski (U81-39-8560)
 * 
 */
public class WaypointPhotoDelete extends Activity {

	private Button cancelButton;
	private WaypointPhotoDeleteController controller;
	private TextView sourceWaypoint;
	private Button okButton;
	private GridView photoList;
	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Image> selectedPhotos = new HashMap<Integer, Image>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_waypoint_photo_delete, menu);
		return true;
	}

	/**
	 * Load the data into the UI.
	 */
	private void initializeUI() {
		setContentView(R.layout.activity_waypoint_photo_delete);
		// Attach to UI elements
		photoList = (GridView) findViewById(R.id.photoList);
		sourceWaypoint = (TextView) findViewById(R.id.textMoveFrom);

		cancelButton = (Button) findViewById(R.id.cancelButton);
		okButton = (Button) findViewById(R.id.okButton);

		// Attach handlers
		cancelButton.setOnClickListener(new CancelButtonHandler());
		okButton.setOnClickListener(new OKButtonHandler());
		photoList.setOnItemClickListener(new ImageClickHandler());
	}

	/**
	 * Get data from intent and load into the form.
	 */
	private void loadData() {
		try {
			final Intent intent = getIntent();
			final long sourceWaypointId = intent.getLongExtra("waypointId", 0);

			sourceWaypoint.setText(controller.getWaypointDescription(sourceWaypointId));

			final List<Image> list = controller.getImageList(sourceWaypointId);
			photoList.setAdapter(new ImageAdapter(this, list));
		} catch (Exception ex) {
			Context context = getApplicationContext();
			ErrorDisplay.displayMessage(this, context, context.getString(R.string.error_message_load_data), ex);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeUI();
		initializeControllers();
		loadData();
	}

	/**
	 * Set up any needed controllers
	 */
	private void initializeControllers() {
		controller = new WaypointPhotoDeleteController(getApplicationContext(), this);
	}

	/**
	 * Handler for the Cancel button.
	 */
	private class CancelButtonHandler implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			finish();
			overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
		}
	}

	/**
	 * Click handler for when selecting/unselecting an image in the list.
	 */
	private class ImageClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// Get the image from the adapter...
			final Image image = (Image) parent.getAdapter().getItem(position);

			// And change the cell color to white or cyan, if selected.
			if (selectedPhotos.containsKey(position)) {
				selectedPhotos.remove(position);
				View view = photoList.getChildAt(position);
				view.setBackgroundColor(Color.WHITE);
			} else {
				selectedPhotos.put(position, image);
				View view = photoList.getChildAt(position);
				view.setBackgroundColor(Color.CYAN);

			}
		}
	}

	/**
	 * Handler for the OK button.
	 */
	private class OKButtonHandler implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			try {
				// Delete the selected images.
				for (Image image : selectedPhotos.values()) {
					controller.deleteImage(image.getId());
				}

				final Intent intent = getIntent();
				final long sourceWaypoint = intent.getLongExtra("waypointId", 0);
				final Intent returnIntent = new Intent();

				returnIntent.putExtra("waypointId", sourceWaypoint);
				setResult(Activity.RESULT_OK, returnIntent);
			} catch (Exception ex) {
				Context context = getApplicationContext();
				ErrorDisplay.displayMessage(WaypointPhotoDelete.this, context,
						context.getString(R.string.error_message_delete_photos), ex);
			}

			finish();
			overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
		}
	}

}
