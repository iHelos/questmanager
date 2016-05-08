package duality.questmanager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import duality.questmanager.content.QuestDatabaseHelper;
import duality.questmanager.fragments.BasicTaskListFragment;
import duality.questmanager.fragments.CreateTaskFragment;

public class FragmentsActivity extends FragmentActivity {

    private QuestDatabaseHelper db;
    private RecyclerView rv;
    private ArrayList<Task> task;

    private EditText createTask;

    private BasicTaskListFragment taskListDoneFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_activity);
        db = new QuestDatabaseHelper(this.getApplicationContext());

//        ArrayList<Task> task = new ArrayList<>();
//        task.add(new Task("Горим очень сильно", R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task.add(new Task("Lavery Maiss", R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task.add(new Task("Lillie Watts",R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task.add(new Task("Меня должно быть не видно", R.drawable.coin, R.drawable.info, R.string.base_cost));


//        BasicTaskListFragment taskListFragment = BasicTaskListFragment.newInstance(task);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, taskListFragment).commit();

        ///////////
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createTaskFragment).commit();
        /////////////


//        ArrayList<Task> task_done = new ArrayList<>();
//        task_done.add(new Task("Горим очень сильно", R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task_done.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));
//        task_done.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));

        task = db.getAllTasks();
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
}
