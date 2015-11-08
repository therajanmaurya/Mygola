package makemytrip.mygola.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;

import java.util.Arrays;
import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.adapters.ActivityAdapter;
import makemytrip.mygola.app.adapters.RecyclerItemClickListner;
import makemytrip.mygola.app.models.ActivitesListModel;
import makemytrip.mygola.app.models.ActivityModel;
import retrofit.Retrofit;

/**
 * Created by rajan on 8/11/15.
 */
public class FavouriteActiviyt extends AppCompatActivity implements RecyclerItemClickListner.OnItemClickListener,RetrievalCallback<ActivityModel>
{



	private String TAG = getClass().getSimpleName();

	private Context context;

	private String bucket="favorites";
	private int entityId;
	private RecyclerView activityRecyclerView;
	private Retrofit retrofit;
	private int pageNumber = 0;
	private ProgressBar progressBar;
	ActivityAdapter activityAdapter;
	private ActivitesListModel activitiyList = new ActivitesListModel();
	private List<String> cities;
	private List<String> sort_options;
	Toolbar toolbar;

	@Override
	public void onItemClick(View childView, int position) {

		Intent intent = new Intent(this, FullActivity.class);
		intent.putExtra(FullActivity.EXTRA_POST, activitiyList.getActivities().get(position)
				.getId());
		startActivity(intent);


	}

	@Override
	public void onItemLongPress(View childView, int position) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_favourite);

		context = getApplicationContext();
		toolbar = (Toolbar) findViewById(R.id.toolbar1);
				toolbar.setTitle("Favourite");
				setSupportActionBar(toolbar);
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		activityRecyclerView = (RecyclerView)findViewById(R.id.activityListRecyclerView);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		activityRecyclerView.setLayoutManager(layoutManager);
		activityRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getApplicationContext(), this));
		activityRecyclerView.setHasFixedSize(true);
//		activityRecyclerView.setVisibility(View.GONE);


		cities = Arrays.asList(context.getResources().getStringArray(R.array.cities));
		sort_options = Arrays.asList(context.getResources().getStringArray(R.array.sort_options));


		loadFromDatabase();


	}


	private void loadFromDatabase()
	{
		NoSQL.with(context).using(ActivityModel.class)
				.bucketId(bucket)
				.retrieve(this);
	}
	@Override
	public void retrievedResults(List<NoSQLEntity<ActivityModel>> noSQLEntities)
	{
		Log.d(TAG, "retrievedResults");
		try
		{
			activitiyList.getActivities().clear();
			for (NoSQLEntity<ActivityModel> activity : noSQLEntities)
			{
				Log.d(TAG, "Activity retrieved from database: " + activity.getData().getName() +
						" ID: " + activity.getId());
				activity.getData().setId(Integer.parseInt(activity.getId()));
				activitiyList.getActivities().add(activity.getData());
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}

		progressBar.setVisibility(View.GONE);
		activityRecyclerView.setVisibility(View.VISIBLE);

		if (activityAdapter==null)
		{
			activityAdapter = new ActivityAdapter(context, activitiyList.getActivities());
			activityRecyclerView.setAdapter(activityAdapter);
		}else
		{
			Log.i(TAG, "Notifying adapter.");
			activityAdapter.notifyDataSetChanged();
		}


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

}
