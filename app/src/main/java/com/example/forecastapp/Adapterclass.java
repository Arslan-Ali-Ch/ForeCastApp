package com.example.forecastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Adapterclass extends RecyclerView.Adapter<Adapterclass.viewHolder> {
Context context;
ArrayList<modelclass> arrayList;

    public Adapterclass(Context context, ArrayList<modelclass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.singlerow,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapterclass.viewHolder holder, int position) {
        modelclass md=arrayList.get(position);
        Picasso.with(context)
                .load("https://openweathermap.org/img/w/"+md.getImage()+".png")
                .into(holder.img1);
       //int d=Integer.parseInt(md.getDate());
         Date date=new Date(md.getDate()*1000);
        DateFormat dateFormat=new SimpleDateFormat("EEEE MMM yyyy", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone(md.getTimezone()));
        holder.din.setText(dateFormat.format(date));
        holder.weath.setText(md.getWeather());
       double temp= (md.getTemp()-273.15);
        double math=Math.round(temp*10)/10.0;
        holder.timp.setText(math+" °C");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView din,weath,timp;
        ImageView img1;
        public viewHolder(@NonNull  View itemView) {
            super(itemView);
            din=itemView.findViewById(R.id.day);
            weath=itemView.findViewById(R.id.weather);
            timp=itemView.findViewById(R.id.temp);
            img1=itemView.findViewById(R.id.img);
        }
    }
}

