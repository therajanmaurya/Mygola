package makemytrip.mygola.app.fragments;// Created by Sanat Dutta on 2/17/2015.

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;

import java.util.Arrays;
import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.adapters.ActivityAdapter;
import makemytrip.mygola.app.models.ActivitesListModel;
import makemytrip.mygola.app.models.ActivityModel;
import makemytrip.mygola.app.rest.MygolaService;
import makemytrip.mygola.app.util.ApiUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Aradh Pillai on 1/10/15.
 */
/*
* this fragment is using for chat tab option
* */
public class home extends Fragment implements RetrievalCallback<ActivityModel>,
		Callback<ActivitesListModel>
{

    private String TAG = getClass().getSimpleName();

	private Context context;

	private String bucket="mygola";
	private int entityId;
	private Spinner citySelectSpinner, sortSelectSpinner;
	private RecyclerView activityRecyclerView;
	private Retrofit retrofit;
	private int pageNumber = 0;
	private ProgressBar progressBar;
	ActivityAdapter activityAdapter;
	private ActivitesListModel activitiyList = new ActivitesListModel();
	private List<String> cities = Arrays.asList("Delhi", "Bangalore", "Mumbai");
	private List<String> sort_options = Arrays.asList("Price", "Name", "Rating");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	    context = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);


/*
		FButton fButton = (FButton)view.findViewById(R.id.openfav);
		fButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(),"Fav" , Toast.LENGTH_SHORT).show();
			}
		});
*/

        Log.i(TAG, "onCreateView");

        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		activityRecyclerView = (RecyclerView) view.findViewById(R.id.activityListRecyclerView);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		activityRecyclerView.setLayoutManager(layoutManager);
		activityRecyclerView.setHasFixedSize(true);
//		activityRecyclerView.setVisibility(View.GONE);


		retrofit = new Retrofit.Builder()
				.baseUrl(ApiUtils.getApiBaseUrl())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		citySelectSpinner = (Spinner) view.findViewById(R.id.citySelectSpinner);
		sortSelectSpinner = (Spinner) view.findViewById(R.id.sortDelectSpinner);

		citySelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				/*ActivitesListModel tmp = activitiyList;
				activitiyList.getActivities().clear();
				for (ActivityModel activity : tmp.getActivities())
				{
					if (activity.getCity().equals(cities.get(position)))
						activitiyList.getActivities().add(activity);
				}
				setUpAdapter();*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		loadActivities();

		setUpAdapter();
/*
		citySelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				cities));
		sortSelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				sort_options));*/
	}

	private void loadActivities()
	{
		MygolaService mygolaService = retrofit.create(MygolaService.class);

		Call<ActivitesListModel> activitiesCall = mygolaService.getActivitiesList("list_activity");
//		Call<APIHitsModel> apiHitsModelCall = mygolaService.getApiHits("api_hits");

		activitiesCall.enqueue(this);
	}

	private void setUpAdapter()
	{

		//TODO - create adapter and assign it to the recyclerview.



		//activityRecyclerView.setLayoutManager(new LinearLayoutManager(context));

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
				Log.d(TAG, "Activity retrieved from database: " + activity.getData().getName());
				activitiyList.getActivities().add(activity.getData());
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
		}

		Log.i(TAG, "Notifying adapter.");
		activityAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResponse(Response<ActivitesListModel> response, Retrofit retrofit)
	{
		Log.d(TAG, "onResponse. URL: ");
		if (response.isSuccess())
		{
			Log.d(TAG, "isSuccess");
			activitiyList = response.body();

			int ActivityId=0;
			NoSQLEntity<ActivityModel> noSQLEntity = null;
			for (ActivityModel activity : activitiyList.getActivities())
			{
				Log.d(TAG, "Saving Activity: Id " + ActivityId + ", Name: " + activity.getName());
				noSQLEntity = new NoSQLEntity<>(bucket, "" + ActivityId++);
				noSQLEntity.setData(activity);
				NoSQL.with(context).using(ActivityModel.class).save(noSQLEntity);
			}

			progressBar.setVisibility(View.GONE);
			activityRecyclerView.setVisibility(View.VISIBLE);
			activityAdapter = new ActivityAdapter(context, activitiyList.getActivities());
			activityRecyclerView.setAdapter(activityAdapter);
		}
		else
		{
			Log.e(TAG, response.message());
		}

		NoSQL.with(context).using(ActivityModel.class)
				.bucketId(bucket)
				.retrieve(this);

	}

	@Override
	public void onFailure(Throwable t)
	{
		Log.e(TAG, "onFailure: Activities list fetching failed: " + t.getMessage());

		NoSQL.with(context).using(ActivityModel.class)
				.bucketId(bucket)
				.retrieve(this);

	}

}
