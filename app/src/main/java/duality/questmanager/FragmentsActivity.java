package duality.questmanager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import duality.questmanager.fragments.BasicTaskListFragment;
import duality.questmanager.fragments.CreateTaskFragment;

public class FragmentsActivity extends FragmentActivity {

    private RecyclerView rv;
    private List<Task> task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_activity);

        ArrayList<Task> task = new ArrayList<>();
        task.add(new Task("Горим очень сильно", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lavery Maiss", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lillie Watts",R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Меня должно быть не видно", R.drawable.coin, R.drawable.info, R.string.base_cost));


//        BasicTaskListFragment taskListFragment = BasicTaskListFragment.newInstance(task);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, taskListFragment).commit();

        ///////////
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createTaskFragment).commit();
        /////////////


        ArrayList<Task> task_done = new ArrayList<>();
        task_done.add(new Task("Горим очень сильно", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task_done.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task_done.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));

        BasicTaskListFragment taskListDoneFragment = BasicTaskListFragment.newInstance(task_done);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_2, taskListDoneFragment).commit();

    }

}
