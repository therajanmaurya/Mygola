package makemytrip.mygola.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.models.ActivityModel;

/**
 * Created by rajanmaurya
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

	private final String LOG_TAG = getClass().getSimpleName();

	List<ActivityModel> activityModelList; // change list after fetch data
	Context context;
	private final int VIEW_ITEM = 1;
	private final int VIEW_PROG = 0;
	private String drawableimage = "drawable://" + R.color.colorPrimary ;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	/**
	 * Provide a reference to the type of views that you are using (custom ViewHolder)
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ImageView image;
		private final TextView name;
		private final TextView actual_price;


		public ViewHolder(View v) {
			super(v);


			image = (ImageView) v.findViewById(R.id.image);
			name  = (TextView) v.findViewById(R.id.name);
			actual_price  = (TextView) v.findViewById(R.id.actual_price);


		}

		public ImageView getCoverpost() {
			return image;
		}

		public TextView getName(){
			return name;
		}

		public TextView getActual_price(){
			return actual_price;
		}

	}


	public ActivityAdapter(Context activity, List<ActivityModel> activityModelList) {
		this.context = activity;
		this.activityModelList = activityModelList;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.white)
				.showImageForEmptyUri(R.color.white)
				.showImageOnFail(R.color.white)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}



	@Override
	public int getItemCount() {
		return activityModelList.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View v = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.recyclerview_item, viewGroup, false);

		return new ViewHolder(v);
	}



	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {

			try
			{
				ImageLoader.getInstance().displayImage(drawableimage, viewHolder.image, options, animateFirstListener);
			}catch (Exception e)
			{
				Log.i(LOG_TAG, "Exception Occured: " + e.getMessage());
			}


			try
			{
				viewHolder.getName().setText(activityModelList.get(position).getName());
				viewHolder.getActual_price().setText("₹ " + activityModelList.get(position).getActual_price());
			}catch(NullPointerException e){
				Log.i(LOG_TAG, "Exception Occured: " + e.getMessage());
			}

	}


	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
