package edu.andrews.cptr252.lisabeth.contacts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder>{
    private  List<InfoContact> contactList;

    AdapterContact(List<InfoContact> list){
        contactList = list;
    }

    public void setContactList(List<InfoContact> contactList){
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        InfoContact c = contactList.get(position);
        holder.name.setText(c.getName());
        holder.phone.setText(c.getPhone());

        File imgFile = new File(c.getPhoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.photo.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView name;

        TextView phone;

        ContactViewHolder(View v){
            super(v);
            photo = v.findViewById(R.id.imageView);
            name = v.findViewById(R.id.txtName);
            phone = v.findViewById(R.id.txtPhone);
        }
    }
}
