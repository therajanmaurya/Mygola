package makemytrip.mygola.app.rest;

import makemytrip.mygola.app.models.APIHitsModel;
import makemytrip.mygola.app.models.ActivitesListModel;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by deves on 11/7/2015.
 */
public interface MygolaService
{

	@GET("/mygola?type=json")
	Call<ActivitesListModel> getActivitiesList(@Query("query") String query);

	@GET("/mygola?type=json")
	Call<APIHitsModel> getApiHits(@Query("query") String query);
}
