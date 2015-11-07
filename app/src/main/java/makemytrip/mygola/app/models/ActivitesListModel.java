package makemytrip.mygola.app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deves on 11/7/2015.
 */
public class ActivitesListModel
{
	List<ActivityModel> activities = new ArrayList<>();

	public List<ActivityModel> getActivities()
	{
		return activities;
	}

	public void setActivities(List<ActivityModel> activities)
	{
		this.activities = activities;
	}
}
