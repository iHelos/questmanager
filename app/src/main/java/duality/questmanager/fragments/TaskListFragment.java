package duality.questmanager.fragments;

import android.os.Bundle;

import java.util.ArrayList;

import duality.questmanager.R;
import duality.questmanager.Task;


public class TaskListFragment extends BasicTaskListFragment {
    public static TaskListFragment newInstance() {
        TaskListFragment catFragment = new TaskListFragment();

        ArrayList<Task> task = new ArrayList<>();
        task.add(new Task("Горим очень сильно", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lavery Maiss", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lillie Watts", R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Lillie Watts",R.drawable.coin, R.drawable.info, R.string.base_cost));
        task.add(new Task("Меня должно быть не видно",R.drawable.coin, R.drawable.info, R.string.base_cost));



        Bundle args = new Bundle();
        args.putParcelableArrayList("Tasks", task);
        catFragment.setArguments(args);

        System.out.println("New Instance!!!"+catFragment.getArguments());
        return catFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        newInstance();
        super.onCreate(savedInstanceState);


    }
}
