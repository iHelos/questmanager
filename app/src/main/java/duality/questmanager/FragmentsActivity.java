package duality.questmanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.fragments.BasicTaskListFragment;
import duality.questmanager.fragments.CreateTaskFragment;
import duality.questmanager.fragments.InfoTaskFragment;
import duality.questmanager.fragments.TaskListFragmentDone;
import duality.questmanager.gcm.MessageGCMListener;

public class FragmentsActivity extends AppCompatActivity {


    public static final String BankSPTag = "BankSPTag";
    public static final String EmailSPTag = "EmailSPTag";

    public static final String START_FRAGMENT = "StartFragmentActivity";

    private Toolbar toolbar = null;


    private DrawerLayout mDrawer;


    private QuestDatabaseHelper db;
    private RecyclerView rv;
    private ArrayList<Task> inputTask;
    private ArrayList<Task> outputTask;

    private EditText createTask;
    public NavigationView nvDrawer;
    private TextView myCoinCost;
    private TextView myEmail;


    private static Boolean isTasksForMe = true;

    public static void setTaskForMe(boolean x)
    {
        isTasksForMe = x;
    }
//    private BroadcastReceiver MessageInput = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String idStr = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_ID);
//            String title = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_TITLE);
//            String priceStr = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_PRICE);
//            String date = intent.getStringExtra(MessageGCMListener.RECIEVE_TASK_DATE);
//
//            int id = Integer.parseInt(idStr);
//            int price = Integer.parseInt(priceStr);
//
//            Task task = new Task(id, title, price, date);
//            inputTask.add(task);
//        }
//    };

//    private BroadcastReceiver MessageOutput = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String idStr = intent.getStringExtra(CreateTaskService.CREATE_TASK_RESULT);
//            String title = intent.getStringExtra(CreateTaskService.CREATE_TASK_TITLE);
//            String priceStr = intent.getStringExtra(CreateTaskService.CREATE_TASK_PRICE);
//            String date = intent.getStringExtra(CreateTaskService.CREATE_TASK_DATE);
//
//            int id = Integer.parseInt(idStr);
//            int price = Integer.parseInt(priceStr);
//
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            String bank = sharedPreferences.getString(BankSPTag, "");
//            Integer bankInt = Integer.parseInt(bank);
//            bankInt-=price;
//            sharedPreferences.edit().putString(FragmentsActivity.BankSPTag, bankInt+"").apply();
//            myCoinCost.setText(bankInt+"");
//
//
//            Task task = new Task(id, title, price, date, 0);
//            outputTask.add(task);
//        }
//    };

        private BroadcastReceiver BankRefresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String bank = sharedPreferences.getString(BankSPTag, "");

            myCoinCost.setText(bank);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Preferences.updateTheme(this);
        super.onCreate(savedInstanceState);
        Preferences.updateLocaleIfNeeded(this);
        setContentView(R.layout.fragments_activity);

        db = new QuestDatabaseHelper(this.getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setNavigationIcon(R.drawable.menu);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        myEmail = (TextView) findViewById(R.id.myEmail);
        myCoinCost = (TextView) findViewById(R.id.myCoinCost);

        View headerLayout = nvDrawer.getHeaderView(0);
        myEmail = (TextView) headerLayout.findViewById(R.id.myEmail);
        myCoinCost = (TextView) headerLayout.findViewById(R.id.myCoinCost);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bank = sharedPreferences.getString(BankSPTag, "");
        String email = sharedPreferences.getString(EmailSPTag, "");
        myCoinCost.setText(bank);
        myEmail.setText(email);

        inputTask = db.getAllTasks(false);
//        outputTask = db.getAllTasks(true);


//        LocalBroadcastManager.getInstance(this).registerReceiver(MessageOutput,
//                new IntentFilter(CreateTaskService.CREATE_TASK_SUCCESS));
//        LocalBroadcastManager.getInstance(this).registerReceiver(MessageInput,
//                new IntentFilter(MessageGCMListener.RECIEVE_TASK_SUCCESS));


        if (savedInstanceState == null) {

            nvDrawer.getMenu().performIdentifierAction(R.id.nav_first_fragment, 0);

        }


        mDrawer.addDrawerListener(setupDrawerToggle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(BankRefresh,
                new IntentFilter(MessageGCMListener.NEW_BANK));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bank = sharedPreferences.getString(BankSPTag, "");
        String email = sharedPreferences.getString(EmailSPTag, "");
        myCoinCost.setText(bank);
        myEmail.setText(email);

        Intent intent = getIntent();
        String message = intent.getStringExtra(START_FRAGMENT);
        intent.putExtra(START_FRAGMENT, "");

        if (message != null && message.equals(Preferences.FragmentTAG))
        {
            Fragment fragment = Preferences.newInstance();
            MenuItem item = (MenuItem) nvDrawer.getMenu().findItem(R.id.nav_fourth_fragment);
            setFragment(fragment, item);
        }



    }


    public ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.refresh_started,  R.string.refresh_finished){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            public void onDrawerStateChanged(int newState) {
                //super.onDrawerOpened(drawerView);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
    }



    @Override
    protected void onStop()
    {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(MessageOutput);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BankRefresh);
        super.onStop();
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_first_fragment:
                    inputTask = db.getAllTasks(false);
                    fragment = TaskListFragmentDone.newInstance(inputTask);
                    //isTasksForMe = true;
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_second_fragment:
                    fragment = CreateTaskFragment.newInstance();
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_third_fragment:
                    outputTask = db.getAllTasks(true);
                    fragment = BasicTaskListFragment.newInstance(outputTask);
                   // isTasksForMe = false;
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_fourth_fragment:
                    fragment = Preferences.newInstance();
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_fifth_fragment:
                    LoginActivity.quit(this);
                    this.finish();
                    break;
                default:
                    fragment = BasicTaskListFragment.newInstance(inputTask);
                    setFragment(fragment, menuItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFragment(Fragment fragment, MenuItem menuItem) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
        View temp = this.getCurrentFocus();
        if (temp != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        //setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    public void onAddClick(View v) {
        Fragment fragment = CreateTaskFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        setTitle(R.string.menu_create_task);
        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);

    }

    public void onTaskClick(View v) {

       // int id = Integer.parseInt(((TextView) v.findViewById(R.id.task_id)).getText().toString());
        int id = v.getId();
        Fragment fragment = InfoTaskFragment.newInstance(id, isTasksForMe);
        FragmentManager fragmentManager = getSupportFragmentManager();
       // fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
        setTitle(R.string.header_report_for_task);

        //ft.addToBackStack(null);

        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);
    }


    public void onReadyTaskClick(View v) {
//        Fragment fragment = ReportTaskFragment.newInstance();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.replace(R.id.fragment_container, fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//        setTitle(R.string.header_report_for_task);
//        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);

    }
    public void onViewReportClick(View v) {

    }

    public void onCancelTask(View v) {

    }

    public void onReportSendClick(View v) {

    }

    private int getFragmentCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentCount()>1) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

}
