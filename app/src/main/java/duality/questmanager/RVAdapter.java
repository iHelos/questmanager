package duality.questmanager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TaskViewHolder> {

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView taskTitle;
        ImageView coin;
        ImageView info;
        TextView coinCost;
        TextView id;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            taskTitle = (TextView)itemView.findViewById(R.id.task_title);
            coin = (ImageView)itemView.findViewById(R.id.coin);
            info = (ImageView)itemView.findViewById(R.id.info);
            coinCost =  (TextView)itemView.findViewById(R.id.coin_cost);
//            id =  (TextView)itemView.findViewById(R.id.task_id);
        }
    }

    List<Task> tasks;

    public RVAdapter(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_list_item, viewGroup, false);
        TaskViewHolder pvh = new TaskViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.taskTitle.setText(tasks.get(i).title);
        taskViewHolder.info.setImageResource(tasks.get(i).iconId);
        taskViewHolder.coinCost.setText(String.valueOf(tasks.get(i).coinCost));
        //taskViewHolder.id.setText(String.valueOf(tasks.get(i).id));
        taskViewHolder.cv.setId(tasks.get(i).id);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}