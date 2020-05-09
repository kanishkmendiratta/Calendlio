package com.example.internshipassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingListAdapter extends ArrayAdapter<bookingListModel> implements DialogBox.ExampleDialogListener {

    private Context ctx;
    private int mresource;

    public BookingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<bookingListModel> objects) {
        super(context, resource, objects);
        ctx=context;
        mresource=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView des,start,end,created,modified;
        Button delete,edit;

        LayoutInflater inflater=LayoutInflater.from(ctx);
        convertView=inflater.inflate(mresource,parent,false);

        final SharedPreferences user=ctx.getSharedPreferences("user",Context.MODE_PRIVATE);
        des=convertView.findViewById(R.id.meetDes);
        delete=convertView.findViewById(R.id.deleteBooking);
        edit=convertView.findViewById(R.id.update);
        start=convertView.findViewById(R.id.startTime);
        end=convertView.findViewById(R.id.endTime);
        created=convertView.findViewById(R.id.createTime);
        modified=convertView.findViewById(R.id.modifiedTime);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox exampleDialog = new DialogBox(getItem(position).getId());
                exampleDialog.show(((FragmentActivity)ctx).getSupportFragmentManager(), "example dialog");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface apiInterface;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://calendlio.sarayulabs.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiInterface = retrofit.create(ApiInterface.class);

                Call<JsonObject> call=apiInterface.delete("Token "+user.getString("Auth",""),"/api/bookings/"+getItem(position).getId());

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.isSuccessful()){
                            remove(getItem(position));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });

        bookingListModel b1=getItem(position);

        des.setText(b1.getDescription());
        start.setText(b1.convertDateTime(b1.getStart()));
        end.setText(b1.convertDateTime(b1.getEnd()));
        created.setText(b1.convertDateTime(b1.getCreated()));
        modified.setText(b1.convertDateTime(b1.getModified()));

        return convertView;
    }

    @Override
    public void applyTexts(String newDescrip,String id) {
        System.out.println(newDescrip);
    }
}
