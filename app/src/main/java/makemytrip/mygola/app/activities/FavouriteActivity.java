package makemytrip.mygola.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.adapters.ActivityAdapter;
import makemytrip.mygola.app.adapters.RecyclerItemClickListner;
import makemytrip.mygola.app.models.ActivitesListModel;
import retrofit.Retrofit;

/**
 * Created by rajan on 8/11/15.
 */
public class FavouriteActivity extends AppCompatActivity implements RecyclerItemClickListner.OnItemClickListener {



	private Context context;
	Toolbar toolbar;
	private String bucket="activities";
	private int entityId;
	private RecyclerView activityRecyclerView;
	private Retrofit retrofit;
	private int pageNumber = 0;
	private ProgressBar progressBar;
	ActivityAdapter activityAdapter;
	private ActivitesListModel activitiyList = new ActivitesListModel();
	private List<String> cities;
	private List<String> sort_options;


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
		setContentView(R.layout.activity_favourite);


		context = getApplicationContext();
		/**
		 * Setting up the Support ToolBar and  CollapsingToolbarLayout
		 */
		toolbar = (Toolbar) findViewById(R.id.toolbar1);
		toolbar.setTitle("Favourite");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



		progressBar = (ProgressBar) findViewById(R.id.progress_favourite);
		progressBar.setVisibility(View.VISIBLE);

		activityRecyclerView = (RecyclerView) findViewById(R.id.recycler_favourite);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		activityRecyclerView.setLayoutManager(layoutManager);
		activityRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getApplicationContext(), this));
		activityRecyclerView.setHasFixedSize(true);



	}



	private void setUpAdapter(ActivitesListModel activitiyList)
	{

		//TODO - create adapter and assign it to the recyclerview.
		progressBar.setVisibility(View.GONE);
		activityRecyclerView.setVisibility(View.VISIBLE);
		activityAdapter = new ActivityAdapter(context, activitiyList.getActivities());
		activityRecyclerView.setAdapter(activityAdapter);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
