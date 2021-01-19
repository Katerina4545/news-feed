package com.example.nasafeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nasafeed.api.model.DateDTO;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable disposable = new CompositeDisposable();
    RecyclerView recyclerView;
    Adapter adapter;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list);

        adapter = new Adapter();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //здесь получаем приложение (у него внутри интерфейс взаимодействия с сервером)
        App app = (App) getApplication();

        //обращение к серверу за списком дат
        disposable.add(app.getNasaService().getApi().getDatesWithPhoto()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<DateDTO>, Throwable>() {
                    @Override
                    public void accept(List<DateDTO> dates, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Toast.makeText(MainActivity.this, "Data loading error", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<OneNode> nodes = new ArrayList<>();
                            for(int i=0; i<dates.size();i++){
                                nodes.add(new OneNode(dates.get(i)));
                            }
                            adapter.setNodes(nodes);
                        }
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private class OneNode {
        DateDTO date;
        //PhotoDTO photo;

        OneNode(DateDTO dateDTO){
            date = dateDTO;
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        //список записей
        ArrayList<OneNode> nodes = new ArrayList<>();

        public void setNodes(List<OneNode> nodes){
            this.nodes.clear();
            this.nodes.addAll(nodes);
            notifyDataSetChanged();
       }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_date, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.bind(nodes.get(i));
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        DateDTO dateDTO;

        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //при клике на элемент вызываем след экран
                    PhotoListActivity.start(view.getContext(), dateDTO.getDate());
                }
            });
        }

        public void bind(OneNode node) {
            dateDTO = node.date;
            text.setText(node.date.getDate());
        }
    }
}