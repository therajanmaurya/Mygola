package makemytrip.mygola.app.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.colintmiller.simplenosql.DataFilter;
import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import makemytrip.mygola.app.R;
import makemytrip.mygola.app.fragments.home;
import makemytrip.mygola.app.models.ActivityModel;
import makemytrip.mygola.app.tabbarview.TabBarView;
import makemytrip.mygola.app.util.CustomViewPager;

/* created by Aradh Pillai on 05/10/15
*
* this class is our home Screen in which all tabs has define
* Toolbar I have used
* Customized ViewPager
* CardStack
*/
public class MainScreenActivity extends AppCompatActivity
{

    //this context using in
    public static Context context;
    public static CustomViewPager mViewPager;
    /*Toolbar use and set the tab dynamically
       setting custom design to Toolbar
    */
    Toolbar toolbar;
    private String TAG = "MainScreenActivity";
    private TabBarView mTabBarView;
    private MainScreenPagerAdapter mMainScreenPagerAdapter;
    //Data
    private int PAGE_COUNT = 1;

    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "MainScreenActivity: onCreate()");
        context = this;
        //using sharedPreference to check whether this is using first time on not...
        //even this app is starting first time than from here it will call the OnBoardActivity and updated the value
        //even this app is already started than this screen will work...

        //from here it's started

        //setting the layout
        setContentView(R.layout.activity_home);

        //checking sdk version and giving shadow effect

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.shadow_prelollipop).setVisibility(View.GONE);
        } else {
            findViewById(R.id.shadow_prelollipop).setVisibility(View.VISIBLE);
        }


        //Setting Custom Toolbar
        setToolBar();

        //Setting all 3 Customized Tabs
        setUpCustomTabs();

        //Setting the pagerListener
        setPagerListener();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
       // searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				return false;
			}

			@Override
			public boolean onQueryTextChange(final String s) {
				Log.d(TAG, "onQueryTextChanged, " + s);
				NoSQL.with(context)
						.using(ActivityModel.class)
						.bucketId("mygola")
						.filter(new DataFilter<ActivityModel>() {
							@Override
							public boolean isIncluded(NoSQLEntity<ActivityModel> item) {
								Log.d(TAG, "isIncluded");
								if (item != null && item.getData() != null) {
									try {
										Log.d(TAG, "Split-string[0]: " + s.split("=")[0]);
										Log.d(TAG, "Split-string[1]: " + s.split("=")[1]);
										switch (s.split("=")[0].toLowerCase()) {
											case "name":
												Log.d(TAG, "Compare name: " + item.getData().getName());
												return item.getData().getName().contains(s.split("=")
														[1]);

											case "price":
												return item.getData().getActual_price() < Integer.parseInt(s
														.split("=<")[1]);

											case "rating":
												return item.getData().getRating() > Double.parseDouble(s
														.split("=>")[1]);
										}
									} catch (ArrayIndexOutOfBoundsException ex) {
										Log.e(TAG, ex.getMessage());
										return false;
									}
								}
								return false;
							}
						})
						.retrieve(new RetrievalCallback<ActivityModel>() {
							@Override
							public void retrievedResults(List<NoSQLEntity<ActivityModel>> noSQLEntities) {
								Log.d(TAG, "retrievedResults: ");
								String[] results = new String[noSQLEntities.size()];
								int i = 0;
								for (NoSQLEntity<ActivityModel> activity : noSQLEntities) {
									results[i] = activity.getData().getName();
									Log.d(TAG, "Result: " + results[i]);
									i++;
								}
								searchView.setSuggestions(results);
							}
						});
				return true;
			}
		});

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
			@Override
			public void onSearchViewShown() {
				//Do some magic
			}

			@Override
			public void onSearchViewClosed() {
				//Do some magic
			}
		});
    }

    //using to set custom design , actions and tabView into toolbar
    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //setting TabBarView
        View customTabView = mLayoutInflater.inflate(R.layout.custom_tab_view, null);
        mTabBarView = (TabBarView) customTabView.findViewById(R.id.customTabBar);
        mTabBarView.setStripHeight(7);

        mTabBarView.setStripColor(getResources().getColor(R.color.white));

        //setting the properties of ActionBar

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        //Setting the Customized Toolbar into toolbar object
        toolbar.addView(customTabView);
    }


    private void setUpCustomTabs() {

        //setting of ViewPager
        mMainScreenPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainScreenPagerAdapter);

        //Setting the CustomizedViewPager into Toolbar for tabOption
        mTabBarView.setViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar card_stack_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

		if(id == R.id.favourite)
		{
			Intent intent = new Intent(this , FavouriteActiviyt.class);
			startActivity(intent);
		}

        return super.onOptionsItemSelected(item);
    }


    private void setPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabBarView.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, "Page: " + position);
                mTabBarView.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mTabBarView.onPageScrollStateChanged(state);
            }
        });
    }

//	@Override
//	public boolean onQueryTextSubmit(final String s)
//	{
//
//		return false;
//	}
//
//	@Override
//	public boolean onQueryTextChange(final String s)
//	{
//		Log.d(TAG, "onQueryTextChanged, " + s);
//		NoSQL.with(this)
//				.using(ActivityModel.class)
//				.bucketId("mygola")
//				.filter(new DataFilter<ActivityModel>()
//				{
//					@Override
//					public boolean isIncluded(NoSQLEntity<ActivityModel> item)
//					{
//						Log.d(TAG, "isIncluded");
//						if (item != null && item.getData() != null)
//						{
//							try
//							{
//								Log.d(TAG, "Split-string[0]: " + s.split("=")[0]);
//								Log.d(TAG, "Split-string[1]: " + s.split("=")[1]);
//								switch (s.split("=")[0].toLowerCase())
//								{
//									case "name":
//										Log.d(TAG, "Compare name: " + item.getData().getName());
//										return item.getData().getName().contains(s.split("=")
//												[1]);
//
//									case "price":
//										return item.getData().getActual_price() < Integer.parseInt(s
//												.split("=<")[1]);
//
//									case "rating":
//										return item.getData().getRating() > Double.parseDouble(s
//												.split("=>")[1]);
//								}
//							}
//							catch (ArrayIndexOutOfBoundsException ex)
//							{
//								Log.e(TAG, ex.getMessage());
//								return false;
//							}
//						}
//						return false;
//					}
//				})
//				.retrieve(new RetrievalCallback<ActivityModel>()
//				{
//					@Override
//					public void retrievedResults(List<NoSQLEntity<ActivityModel>> noSQLEntities)
//					{
//						Log.d(TAG, "retrievedResults: ");
//						String[] results = new String[noSQLEntities.size()];
//						int i = 0;
//						for (NoSQLEntity<ActivityModel> activity : noSQLEntities)
//						{
//							results[i] = activity.getData().getName();
//							Log.d(TAG, "Result: " + results[i]);
//							i++;
//						}
//						searchView.setSuggestions(results);
//					}
//				});
//		return true;
//	}


	// class is implemented with IconTabProvider Interface as well as extends with FragmentStateAdapter for ViewPager
    //with different tabIcons
    public class MainScreenPagerAdapter extends FragmentStatePagerAdapter implements TabBarView.IconTabProvider {


        //Defining the array for Tab icons..which is going to call dynamically and load it into tabBar of toolbar
        private int[] tab_icons = {
                R.drawable.ic_connect,
        };

        public MainScreenPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        //this method is returning the ref of our fragments
        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new home();
                default:
                    return null;
            }
        }

        //returning the number of pages
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        //this is TabBarView.IconTabProvider's method to return the position of icon to load into tabBar of Toolbar
        @Override
        public int getPageIconResId(int position) {
            return tab_icons[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";
            }
            return null;
        }
    }

}