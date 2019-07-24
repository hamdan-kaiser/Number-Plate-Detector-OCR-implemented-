package hamdan.JuniorDesign.DigitalNumPlateDetector;

/**
 * Created by Virus on 4/9/2019.
 */
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> ID;
    private ArrayList<String> Name;
    private ArrayList<String> time;

    public ListAdapter(
            Context context2,
           ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> time
    )
    {

        this.context = context2;
        this.ID = id;
        this.Name = name;
        this.time = time;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.items, null);

            holder = new Holder();

            holder.ID_TextView =  child.findViewById(R.id.textViewID);
            holder.Name_TextView =  child.findViewById(R.id.textViewNAME);
            holder.Time_TextView = child.findViewById(R.id.textViewTime);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.ID_TextView.setText(ID.get(position));
        holder.Name_TextView.setText(Name.get(position));
        holder.Time_TextView.setText(time.get(position));

        return child;
    }

    public class Holder {

        TextView ID_TextView;
        TextView Name_TextView;
        TextView Time_TextView;
    }

}