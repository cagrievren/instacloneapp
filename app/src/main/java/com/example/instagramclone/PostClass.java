package com.example.instagramclone;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostClass extends ArrayAdapter<String> {

    private final ArrayList<String> uyeEmail;
    private final ArrayList<String> uyeResim;
    private final ArrayList<String> uyeYorum;
    private final Activity context;

    public PostClass(ArrayList<String> uyeEmail, ArrayList<String> uyeResim, ArrayList<String> uyeYorum, Activity context) {
        super(context, R.layout.custom_view, uyeEmail);
        this.uyeEmail = uyeEmail;
        this.uyeResim = uyeResim;
        this.uyeYorum = uyeYorum;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view, null, true);

        TextView uyeEmailText = customView.findViewById(R.id.uyeEmailTextViewCustomView);
        ImageView resimView = customView.findViewById(R.id.imageViewCustomView);
        TextView uyeYorumText = customView.findViewById(R.id.uyeYorumtextViewCustomView);


        uyeEmailText.setText(uyeEmail.get(position));
        Picasso.get().load(uyeResim.get(position)).into(resimView);
        uyeYorumText.setText(uyeYorum.get(position));

        return customView;
    }
}
