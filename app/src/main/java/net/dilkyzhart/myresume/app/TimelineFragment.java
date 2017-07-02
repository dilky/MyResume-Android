package net.dilkyzhart.myresume.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.dilkyzhart.myresume.app.comm.FormatUtils;
import net.dilkyzhart.myresume.app.firebase.ReceiveValueListener;
import net.dilkyzhart.myresume.app.firebase.models.MyTimeline;
import net.dilkyzhart.myresume.app.firebase.models.PostInfo;

import java.util.ArrayList;

/**
 * Created by dilky on 2017. 6. 29..
 */

public class TimelineFragment extends Fragment {
    public static TimelineFragment newInstance() {
        TimelineFragment f = new TimelineFragment();
        return f;
    }

    private View contentView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_timeline, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        recyclerView =(RecyclerView) contentView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext()
                , DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.gray_box_20));
        recyclerView.addItemDecoration(dividerItemDecoration);

        MyTimeline.Read(new ReceiveValueListener() {
            @Override
            public void onDataReceive(Object data) {

                recyclerView.setAdapter(new PostAdapter(getActivity(), (ArrayList) data));
            }
        });

    }

    class PostAdapter extends RecyclerView.Adapter {

        private ArrayList<PostInfo> items;
        private Context context;
        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public PostAdapter(Context context, ArrayList items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = View.inflate(context, R.layout.fragment_timeline_post, null);

            PostViewHolder holder = new PostViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            postViewHolder.tvRate.setText(items.get(position).rate);
            postViewHolder.tvTitle.setText(items.get(position).title);
            postViewHolder.tvPeriod.setText(FormatUtils.getDate(items.get(position).period));
            postViewHolder.tvDescription.setText(items.get(position).description);
            postViewHolder.tvBelongTo.setText(items.get(position).belong_to);

            //setAnimation(postViewHolder.tvTitle, position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class PostViewHolder extends RecyclerView.ViewHolder {
            public TextView tvRate;
            public TextView tvTitle;
            public TextView tvPeriod;
            public TextView tvDescription;
            public TextView tvBelongTo;
            public LinearLayout imageGroup;

            public PostViewHolder(View view) {
                super(view);
                tvRate = (TextView) view.findViewById(R.id.rate);
                tvTitle = (TextView) view.findViewById(R.id.title);
                tvPeriod = (TextView) view.findViewById(R.id.period);
                tvDescription = (TextView) view.findViewById(R.id.description);
                tvBelongTo = (TextView) view.findViewById(R.id.belong_to);
                imageGroup = (LinearLayout) view.findViewById(R.id.imageGroup);
            }
        }

//        private void setAnimation(View viewToAnimate, int position) {
//            // 새로 보여지는 뷰라면 애니메이션을 해줍니다
//            if (position > lastPosition) {
//                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
//                viewToAnimate.startAnimation(animation);
//                lastPosition = position;
//            }
//        }
    }
}
