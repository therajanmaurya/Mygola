package makemytrip.mygola.app.models;

/**
 * Created by deves on 11/7/2015.
 */
public class APIHitsModel
{
	int api_hits;

	public int getApi_hits()
	{
		return api_hits;
	}

	public void setApi_hits(String api_hits)
	{
		this.api_hits = Integer.parseInt(api_hits);
	}
}
