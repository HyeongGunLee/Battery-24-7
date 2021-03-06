package waggle.wagglebattery.activity;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import waggle.data.WaggleLocationInfo;
import waggle.utility.BackPressCloseHandler;
import waggle.utility.DownloadDataTask;
import waggle.waggle.wagglebattery.adapter.WaggleEmergencyListViewAdapter;
import waggle.waggle.wagglebattery.adapter.WaggleListViewAdapter;
import waggle.wagglebattery.R;
import waggle.wagglebattery.layout.WaggleHomeLayout;
import waggle.wagglebattery.layout.WaggleListLayout;
import waggle.wagglebattery.layout.WaggleMapLayout;

/**
 * This class is main class for android application.
 * This activity is started at first. Main activity has a navigation bar.
 * TODO: Main view have to show the warning that is about low battery.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private BackPressCloseHandler mBackPressCloseHandler;

    // 리스트뷰
    private ArrayList<WaggleLocationInfo> mWaggleLocationInfo;
    private WaggleEmergencyListViewAdapter mAdapter;
    private ContentValues mColumns = new ContentValues();

    private Integer[] mImgId = {R.drawable.waggle1, R.drawable.waggle2, R.drawable.waggle3};
    private  FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면 전환을 위해 필요한 프래그먼트 매니저
        manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, new WaggleHomeLayout()).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // register FCM "news" topic
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        // BackPressCloseHandler makes back button to be used to close application when clicks twice.
        mBackPressCloseHandler = new BackPressCloseHandler(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * This function is operated when the user press the back button.
     * onBackPressed function call the BackPressCloseHandler Class function to make the application
     * be closed when the button is closed twice.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mBackPressCloseHandler.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wagglelist_layout) {
            manager.beginTransaction().replace(R.id.content_main, new WaggleListLayout()).commit();
        } else if (id == R.id.nav_wagglemap_layout) {
            manager.beginTransaction().replace(R.id.content_main, new WaggleMapLayout()).commit();
        } else if (id == R.id.nav_wagglehome_layout) {
            manager.beginTransaction().replace(R.id.content_main, new WaggleHomeLayout()).commit();
        }
        // 네비게이션 바에 메뉴를 더 추가하고 싶을 경우 여기에 추가

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
