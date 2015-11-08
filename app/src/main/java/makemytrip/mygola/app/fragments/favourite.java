package makemytrip.mygola.app.fragments;// Created by Sanat Dutta on 2/17/2015.

import android.content.Context;
import android.content.Intent;
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

import com.colintmiller.simplenosql.DataComparator;
import com.colintmiller.simplenosql.DataFilter;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;

import java.util.Arrays;
import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.activities.FullActivity;
import makemytrip.mygola.app.adapters.ActivityAdapter;
import makemytrip.mygola.app.adapters.RecyclerItemClickListner;
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
public class favourite extends Fragment implements RecyclerItemClickListner.OnItemClickListener, RetrievalCallback<ActivityModel>,
        Callback<ActivitesListModel>
{

    private String TAG = getClass().getSimpleName();

    private Context context;

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

        Intent intent = new Intent(getActivity(), FullActivity.class);
        intent.putExtra(FullActivity.EXTRA_POST, activitiyList.getActivities().get(position)
                .getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);


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
        activityRecyclerView.addOnItemTouchListener(new RecyclerItemClickListner(getActivity(), this));
        activityRecyclerView.setHasFixedSize(true);
//		activityRecyclerView.setVisibility(View.GONE);


        retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.getApiBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cities = Arrays.asList(context.getResources().getStringArray(R.array.cities));
        sort_options = Arrays.asList(context.getResources().getStringArray(R.array.sort_options));


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

    private void loadFromDatabase()
    {
        NoSQL.with(context).using(ActivityModel.class)
                .bucketId(bucket)
                .filter(new DataFilter<ActivityModel>()
                {
                    @Override
                    public boolean isIncluded(NoSQLEntity<ActivityModel> item)
                    {
                        Log.d(TAG, "isIncluded");
                        if (item != null && item.getData() != null && citySelectSpinner
                                .getSelectedItemPosition()!=0)
                        {
                            return item.getData().getCity().equals(cities.get(citySelectSpinner
                                    .getSelectedItemPosition()));
                        }
                        return true;
                    }
                })
                .orderBy(new DataComparator<ActivityModel>()
                {
                    @Override
                    public int compare(NoSQLEntity<ActivityModel> lhs, NoSQLEntity<ActivityModel> rhs)
                    {
                        Log.d(TAG, "compare(): ");
                        if (lhs != null && lhs.getData() != null) {
                            if (rhs != null && rhs.getData() != null) {
                                switch (sortSelectSpinner.getSelectedItemPosition())
                                {
                                    case 0:
                                        Log.d(TAG, "case 0, Price: " + lhs.getData()
                                                .getActual_price() + " v/s " + rhs.getData()
                                                .getActual_price());
                                        return lhs
                                                .getData()
                                                .getActual_price() >
                                                rhs
                                                        .getData()
                                                        .getActual_price() ? 1 : -1;

                                    case 1:
                                        Log.d(TAG, "case 1, Name: " + lhs.getData()
                                                .getName() + " v/s " + rhs.getData()
                                                .getName());
                                        return lhs.getData().getName().compareToIgnoreCase(rhs.getData().getName
                                                ());

                                    case 2:
                                        Log.d(TAG, "case 2, Rating: " + lhs.getData()
                                                .getRating() + " v/s " + rhs.getData()
                                                .getRating());
                                        return lhs
                                                .getData()
                                                .getRating() >
                                                rhs
                                                        .getData()
                                                        .getRating() ? 1 : -1;

                                    default:
                                        return -1;
                                }
                            } else {
                                return 1;
                            }
                        } else if (rhs != null && rhs.getData() != null) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                })
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


        }
        else
        {
            Log.e(TAG, response.message());
        }

        loadFromDatabase();

    }

    @Override
    public void onFailure(Throwable t)
    {
        Log.e(TAG, "onFailure: Activities list fetching failed: " + t.getMessage());

        loadFromDatabase();

    }


}
