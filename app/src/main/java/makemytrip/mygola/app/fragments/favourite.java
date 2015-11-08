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
public class favourite extends Fragment implements RecyclerItemClickListner.OnItemClickListener, RetrievalCallback<ActivityModel>
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


        cities = Arrays.asList(context.getResources().getStringArray(R.array.cities));
        sort_options = Arrays.asList(context.getResources().getStringArray(R.array.sort_options));


        loadFromDatabase();


/*
		citySelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				cities));
		sortSelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				sort_options));*/
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


}
