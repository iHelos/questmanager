package duality.questmanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import duality.questmanager.R;
import duality.questmanager.Task;

/**
 * Created by anna on 20.05.16.
 */
public class TaskListFragmentDone extends BasicTaskListFragment {
    @Override
    protected void getLayout(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.task_list_done_fragment, container, false);
    }


    public static TaskListFragmentDone newInstance(ArrayList<Task> task) {
        TaskListFragmentDone myFragment = new TaskListFragmentDone();

        Bundle args = new Bundle();
        args.putParcelableArrayList("Tasks", task);
        myFragment.setArguments(args);

        return myFragment;
    }





}