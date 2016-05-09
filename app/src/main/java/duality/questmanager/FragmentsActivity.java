package duality.questmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.fragments.BasicTaskListFragment;
import duality.questmanager.fragments.CreateTaskFragment;

public class FragmentsActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private Drawer navigation = null;

    private SectionDrawerItem menu = new SectionDrawerItem().withName(R.string.menu__section_name).withTextColor(R.color.material_drawer_primary_text);
    private PrimaryDrawerItem inbox = new PrimaryDrawerItem().withName(R.string.menu__point1).withIdentifier(1);
    private PrimaryDrawerItem outbox = new PrimaryDrawerItem().withName(R.string.menu__point2).withIdentifier(2);
    private PrimaryDrawerItem unaccepted = new PrimaryDrawerItem().withName(R.string.menu__point3).withIdentifier(3);



    private QuestDatabaseHelper db;
    private RecyclerView rv;
    private ArrayList<Task> task;

    private EditText createTask;

    private BasicTaskListFragment taskListDoneFragment;
    private BasicTaskListFragment taskListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_activity);
        db = new QuestDatabaseHelper(this.getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigation = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.navbar_header)
                .withDisplayBelowStatusBar(true)
                .withSliderBackgroundColorRes(R.color.material_drawer_background)


//                .withAccountHeader(headerResult)
                .addDrawerItems(
                        inbox,
                        outbox,
                        unaccepted
//                        new SectionDrawerItem().withName(R.string.account__section_name).withTextColor(R.color.material_drawer_primary_text),
//                        new ProfileDrawerItem()
//                                .withName("Java-Help")
//                                .withEmail("java-help@mail.ru")
//                                .withIcon(R.drawable.vk),
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        selectItem((int)drawerItem.getIdentifier());
                        return true;
                    }
                })
                .build();



        ///////////
//        CreateTaskFragment createTaskFragment = new CreateTaskFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, createTaskFragment).commit();
        /////////////

        task = db.getAllTasks();


        taskListFragment = BasicTaskListFragment.newInstance(task);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, taskListFragment).commit();

        taskListDoneFragment = BasicTaskListFragment.newInstance(task);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_2, taskListDoneFragment).commit();

    }

    public void onAddQuestClick(View view) {
        createTask = (EditText) findViewById(R.id.createTask);
        String title = createTask.getText().toString();
        db.addTask(title,title,22);
        task.add(new Task(title,22));
        taskListDoneFragment.refresh(db.getAllTasks());

    }
    private void selectItem(int position) {
        switch(position) {
            case 1:
                Intent intent = new Intent(this, FragmentsActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent2 = new Intent(this, FragmentsActivity.class);
                startActivity(intent2);
                break;
            default:
                System.out.print("click default");
                break;
        }

    }
    @Override
    public void onBackPressed() {
        if (navigation!= null && navigation.isDrawerOpen()) {
            navigation.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
