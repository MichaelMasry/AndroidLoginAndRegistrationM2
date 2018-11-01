package info.androidhive.loginandregistration.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import info.androidhive.loginandregistration.R;

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder>{


    private Context context;
    private List<Landmark> landmarkList;

    public LandmarkAdapter(Context context, List<Landmark> landmarkList) {
        this.context = context;
        this.landmarkList = landmarkList;
    }

    @NonNull
    @Override
    public LandmarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_layout,null);
        return new LandmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LandmarkViewHolder landmarkViewHolder, int i) {
        Landmark landmark = landmarkList.get(i);
        if (landmarkViewHolder!=null && landmarkViewHolder.textViewName!=null) {
            landmarkViewHolder.textViewName.setText("SkiEgypt");
            landmarkViewHolder.textViewDistance.setText("55 KM");
            landmarkViewHolder.textViewRate.setText("4.8");
            //     landmarkViewHolder.imageView.setImageDrawable(context.getResources().getDrawable());
        }else{
            System.out.println("Tel3et b null");
        }
    }

    @Override
    public int getItemCount() {
        return landmarkList.size();
    }


    class LandmarkViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName,textViewDistance,textViewRate;

        public LandmarkViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textView_title);
            textViewDistance = (TextView) itemView.findViewById(R.id.textViewShortDesc);
            textViewRate = (TextView) itemView.findViewById(R.id.textViewRating);
        }
    }
}
