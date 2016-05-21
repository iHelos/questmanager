package duality.questmanager;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.fragments.BasicTaskListFragment;
import duality.questmanager.fragments.CreateTaskFragment;
import duality.questmanager.fragments.InfoTaskFragment;
import duality.questmanager.fragments.ReportTaskFragment;
import duality.questmanager.fragments.TaskListFragmentDone;

public class FragmentsActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private Drawer navigation = null;

    private DrawerLayout mDrawer;


    private QuestDatabaseHelper db;
    private RecyclerView rv;
    private ArrayList<Task> inputTask;
    private ArrayList<Task> outputTask;

    private EditText createTask;
    private NavigationView nvDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences.updateLocaleIfNeeded(this);
        setContentView(R.layout.fragments_activity);
        db = new QuestDatabaseHelper(this.getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        inputTask = db.getAllTasks(false);
        outputTask = db.getAllTasks(true);
        // Inflate the header view at runtime
//        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
// We can now look up items within the header if needed
//        ImageView ivHeaderPhoto = headerLayout.findViewById(R.id.imageView);

        setupDrawerContent(nvDrawer);
//        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);


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

    public void onAddQuestClick(View view) {
        createTask = (EditText) findViewById(R.id.createTask);
        String title = createTask.getText().toString();
        //db.addTask(title, title, 22, false);
        inputTask.add(new Task(title, 22));
        //taskListDoneFragment.refresh(db.getAllTasks());

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
                    fragment = BasicTaskListFragment.newInstance(inputTask);
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_second_fragment:
                    fragment = CreateTaskFragment.newInstance();
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_third_fragment:
                    fragment = TaskListFragmentDone.newInstance(outputTask);
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_fourth_fragment:
                    fragment = Preferences.newInstance();
                    setFragment(fragment, menuItem);
                    break;
                case R.id.nav_fifth_fragment:
                    LoginActivity.quit(this);
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
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }


    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (navigation != null && navigation.isDrawerOpen()) {
            navigation.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void onAddClick(View v) {
        Fragment fragment = CreateTaskFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        setTitle("Создать задание");
        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);

    }

    public void onTaskClick(View v) {
        Fragment fragment = InfoTaskFragment.newInstance(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
//        setTitle("Создать задание");
        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);
    }

    public void onReadyTask(View v) {
        Fragment fragment = ReportTaskFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        setTitle("Отчет по заданию");
        nvDrawer.getMenu().findItem(R.id.nav_first_fragment).setChecked(false);

    }

    public void onCancelTask(View v) {

    }

    public void onReportSendClick(View v) {

    }

}
