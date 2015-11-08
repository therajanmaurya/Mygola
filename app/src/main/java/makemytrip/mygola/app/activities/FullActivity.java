package makemytrip.mygola.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.colintmiller.simplenosql.DataFilter;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.models.ActivityModel;

public class FullActivity extends AppCompatActivity implements RetrievalCallback<ActivityModel>
{
	private final String LOG_TAG = getClass().getSimpleName();
	public static final String EXTRA_POST = "POST_DETAILS";

	private ActivityModel activity;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions displayImageOptions;

	Toolbar toolbar;
	CollapsingToolbarLayout collapsingToolbar;
	TextView ratingTextView, pricingTextView, descriptionTextView;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_activity);

		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.colorPrimary)
				.showImageForEmptyUri(R.color.colorPrimary)
				.showImageOnFail(R.color.colorPrimary)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		final Intent intent = getIntent();
		activity.setId(Integer.parseInt(intent.getExtras().getString("ACTIVITY_ID")));

		NoSQL
				.with(this)
				.using(ActivityModel.class)
				.bucketId("mygola")
				.filter(new DataFilter<ActivityModel>()
				{
					@Override
					public boolean isIncluded(NoSQLEntity<ActivityModel> item)
					{
						Log.d(LOG_TAG, "isIncluded: " + item.getId() + ", " + activity.getId());
						return item.getId().equals(""+activity.getId());
					}
				})
				.retrieve(this);

		/**
		 * Setting up the Support ToolBar and  CollapsingToolbarLayout
		 */
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setTitle("");
		collapsingToolbar =
				(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle("Mygola");

		ratingTextView = (TextView)findViewById(R.id.ratingTextView);
		pricingTextView = (TextView)findViewById(R.id.pricing);
		descriptionTextView = (TextView) findViewById(R.id.description);
		image = (ImageView) findViewById(R.id.fullImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_full, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void retrievedResults(List<NoSQLEntity<ActivityModel>> noSQLEntities)
	{
		if (noSQLEntities != null)
		{
			activity = noSQLEntities.get(0).getData();
			toolbar.setTitle(activity.getName());
			ratingTextView.setText(activity.getRating() + "");
			pricingTextView.setText("₹" + activity.getActual_price() + "@" + "10" + "%");
			descriptionTextView.setText(activity.getDescription());
			ImageLoader.getInstance().displayImage(activity.getImage(),
					image, displayImageOptions, animateFirstListener);
		}
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
	{

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
