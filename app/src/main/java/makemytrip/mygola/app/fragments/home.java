package makemytrip.mygola.app.fragments;// Created by Sanat Dutta on 2/17/2015.

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.hoang8f.widget.FButton;
import makemytrip.mygola.app.R;

/**
 * Created by Aradh Pillai on 1/10/15.
 */
/*
* this fragment is using for chat tab option
* */
public class home extends Fragment {

    private String TAG = getClass().getSimpleName();

	private Context context = getActivity();

	private Spinner citySelectSpinner, sortSelectSpinner;
	private RecyclerView activityRecyclerView;
	private List<String> cities = Arrays.asList("Delhi", "Bangalore", "Mumbai");
	private List<String> sort_options = Arrays.asList("Price", "Name", "Rating");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

		citySelectSpinner = (Spinner) view.findViewById(R.id.citySelectSpinner);
		sortSelectSpinner = (Spinner) view.findViewById(R.id.sortDelectSpinner);
/*
		citySelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				cities));
		sortSelectSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_item,
				sort_options));*/
	}
}
