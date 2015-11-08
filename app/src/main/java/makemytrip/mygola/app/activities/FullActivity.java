package makemytrip.mygola.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class FullActivity extends AppCompatActivity implements RetrievalCallback<ActivityModel>, View.OnClickListener
{
	private final String LOG_TAG = getClass().getSimpleName();
	public static final String EXTRA_POST = "ACTIVITY_ID";

	private ActivityModel activity;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions displayImageOptions;

	Toolbar toolbar;
	CollapsingToolbarLayout collapsingToolbar;
	ImageView image;

	TextView distv ,loctv,citytv,pritv,rattv , destv;
	RatingBar ratingBar ;
	FloatingActionButton sharefav , smsfav, favouritefav;

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

		activity = new ActivityModel();
		final Intent intent = getIntent();
		activity.setId(intent.getExtras().getInt(EXTRA_POST));

		NoSQL
				.with(this)
				.using(ActivityModel.class)
				.bucketId("activities")
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
		toolbar = (Toolbar) findViewById(R.id.titleToolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar().setTitle("");
		collapsingToolbar =
				(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbar.setTitle("Mygola");


		image = (ImageView) findViewById(R.id.fullImage);
		distv = (TextView)findViewById(R.id.discount_text);
		loctv = (TextView)findViewById(R.id.location_text);
		citytv = (TextView)findViewById(R.id.city_text);
		pritv = (TextView)findViewById(R.id.price_text);
		destv = (TextView)findViewById(R.id.description_text);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);

		sharefav = (FloatingActionButton)findViewById(R.id.share);
		smsfav = (FloatingActionButton)findViewById(R.id.sms);
		favouritefav = (FloatingActionButton)findViewById(R.id.favourite);

		sharefav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Mygola.com  " + activity.getName());
				shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getDescription() + " with Discount " + activity.getDiscount() +
				" at Effective price: " + "₹ " +activity.getActual_price() + " at Location " + activity.getLocation());
				startActivity(Intent.createChooser(shareIntent, "Share By Mygola"));
				FullActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R
						.anim.slide_out_right);
			}
		});

		smsfav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", "");
				smsIntent.putExtra("sms_body", " Mygola.com " +activity.getName() +  " :" + activity.getDescription() + " with Discount " + activity.getDiscount() +
						" at Effective price: " + "₹ " +activity.getActual_price() + " at Location " + activity.getLocation());
				startActivity(smsIntent);
			}
		});

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

		if (id == android.R.id.home) {
			finish();
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
			activity.setId(Integer.parseInt(noSQLEntities.get(0).getId()));
			toolbar.setTitle(activity.getName());
			collapsingToolbar.setTitle(activity.getName());

			ImageLoader.getInstance().displayImage(activity.getImage(),
					image, displayImageOptions, animateFirstListener);

			distv.setText("Discount: " + activity.getDiscount());
			loctv.setText("Location: " + activity.getLocation());
			citytv.setText("City: " + activity.getCity());
			pritv.setText("Effective Price: " + "₹" + activity.getActual_price());
			destv.setText(activity.getDescription());
			ratingBar.setNumStars(5);
			ratingBar.setRating(activity.getRating());
			favouritefav.setOnClickListener(this);

		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.favourite:
				activity.setFavorite(!activity.isFavorite());
				if (activity.isFavorite())
				{
					NoSQLEntity<ActivityModel> noSQLEntity = new NoSQLEntity<ActivityModel>
							("favorites", ""+activity.getId());
					noSQLEntity.setData(activity);
					NoSQL.with(this)
							.using(ActivityModel.class)
							.save(noSQLEntity);
				}
				else
				{
					NoSQL.with(this)
							.using(ActivityModel.class)
							.bucketId("favorites")
							.entityId(""+activity.getId())
							.delete();
				}
				break;
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
