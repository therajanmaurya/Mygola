package makemytrip.mygola.app.fragments;// Created by Sanat Dutta on 2/17/2015.

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
}
