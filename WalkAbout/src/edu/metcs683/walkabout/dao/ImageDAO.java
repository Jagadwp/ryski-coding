package edu.metcs683.walkabout.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import edu.metcs683.walkabout.model.Image;

/**
 * Data access object for the image. This class controls the
 * loading/saving/querying of images in the database.
 * 
 * @author Ryszard Kilarski (U81-39-8560)
 *  
 */
public class ImageDAO extends Database<Image> {

	private static final String CLASSNAME = ImageDAO.class.getSimpleName();
	private static final String[] COLUMN_LIST = new String[] { "_id", "waypointId", "imageURI" };
	private static final String DATABASE_TABLE_NAME = "waypoint_image";

	public ImageDAO(Context context) {
		super(context);
	}

	/**
	 * Given an id, delete the item from the database and the filesystem.
	 * 
	 * @param id
	 */
	@Override
	public void delete(long id) {
		Image image = get(id);
		File file = new File(Uri.parse(image.getImageURI()).getPath());
		file.delete();

		final SQLiteDatabase db = getWritableDatabase();
		db.delete(DATABASE_TABLE_NAME, "_id=" + id, null);
		db.close();
	}

	/**
	 * Delete all rows from the database.
	 */
	@Override
	public void deleteAll() {
		List<Image> list = getAll();
		for (Image image : list) {
			delete(image.getId());
		}
	}

	/**
	 * Given an id, delete all items associated with that id from the database.
	 * 
	 * @param id
	 */
	@Override
	public void deleteAll(long id) {
		List<Image> list = getAll(true, id);
		for (Image image : list) {
			delete(image.getId());
		}
	}

	/**
	 * Given an id, get the item from the database.
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Image get(long id) {
		Cursor cursor = null;
		Image image = null;
		try {
			final SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(true, DATABASE_TABLE_NAME, COLUMN_LIST, "_id = '" + id + "'", null, null, null, null,
					null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				image = getImageFromCursor(cursor);
			}
		} catch (final SQLException e) {
			Log.v("ProviderWidgets", CLASSNAME, e);
		} finally {
			if ((cursor != null) && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return image;
	}

	/**
	 * Get all of these items from the database, in no given order.
	 * 
	 * @param orderAscending
	 * @return
	 */
	@Override
	public List<Image> getAll() {
		return getAll(true);
	}

	/**
	 * Get all of these items from the database, in a given order.
	 * 
	 * @param orderAscending
	 * @return
	 */
	@Override
	public List<Image> getAll(boolean orderAscending) {
		return null;
	}

	/**
	 * Get all the items associated with an id, in a given order.
	 * 
	 * @param orderAscending
	 * @param id
	 * @return
	 */
	@Override
	public List<Image> getAll(boolean orderAscending, long id) {
		final List<Image> images = new ArrayList<Image>();
		Cursor cursor = null;
		try {
			String orderBy = null;
			if (!orderAscending) {
				orderBy = COLUMN_LIST[0] + " DESC";
			}
			final SQLiteDatabase db = getReadableDatabase();
			cursor = db.query(DATABASE_TABLE_NAME, COLUMN_LIST, "waypointId = '" + id + "'", null, null, null, orderBy);
			final int numRows = cursor.getCount();
			cursor.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				final Image image = getImageFromCursor(cursor);
				images.add(image);
				cursor.moveToNext();
			}
		} catch (final SQLException e) {
			Log.v("ProviderWidgets", CLASSNAME, e);
		} finally {
			if ((cursor != null) && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return images;
	}

	/**
	 * Insert the object into the database, and return its new id.
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public long insert(Image image) {
		final ContentValues values = getContentValuesFromImage(image);
		final SQLiteDatabase db = getWritableDatabase();
		final long countInserted = db.insert(DATABASE_TABLE_NAME, null, values);
		db.close();
		return countInserted;
	}

	/**
	 * Update the object in the database.
	 * 
	 * @param object
	 */
	@Override
	public void update(Image image) {
		final ContentValues values = getContentValuesFromImage(image);
		final SQLiteDatabase db = getWritableDatabase();
		db.update(DATABASE_TABLE_NAME, values, "_id=" + image.getId(), null);
		db.close();
	}

	/**
	 * Build a content value for a given image.
	 * 
	 * @param waypoint
	 * @return
	 */
	private ContentValues getContentValuesFromImage(Image image) {
		final ContentValues values = new ContentValues();
		values.put(COLUMN_LIST[1], image.getWaypointId());
		values.put(COLUMN_LIST[2], image.getImageURI());
		return values;
	}

	/**
	 * Get the image from a Sql cursor.
	 * 
	 * @param cursor
	 * @return
	 */
	private Image getImageFromCursor(Cursor cursor) {
		return new Image(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
	}

}