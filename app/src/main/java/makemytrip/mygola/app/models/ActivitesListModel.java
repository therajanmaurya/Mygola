package makemytrip.mygola.app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deves on 11/7/2015.
 */
public class ActivitesListModel
{
	List<ActivityModel> activities = new ArrayList<ActivityModel>(){{new ActivityModel();}};

	enum SORT_TYPE
	{
		PRICE,
		NAME,
		RATING
	}


	public List<ActivityModel> getActivities()
	{
		return activities;
	}

	public void setActivities(List<ActivityModel> activities)
	{
		this.activities = activities;
	}

	public void sort(SORT_TYPE param)
	{
		switch (param)
		{
			case PRICE:
				break;

			case NAME:
				break;

			case RATING:
				break;

			default:
				throw new IllegalArgumentException("Invalid sort parameter. Must be of type " +
						getClass().getSimpleName() + ".SORT_TYPE");
		}
	}
}
