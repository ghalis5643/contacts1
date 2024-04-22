package edu.andrews.cptr252.lisabeth.contacts;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private ImageButton photo;
    private EditText name;

    private EditText address;
    private EditText phone;
    private Button save;
    private InfoContact contact;

    private Drawable happy;

    private final int CAMERA = 1;
    private final int GALLERY = 2;
    private final String IMAGE_DIR = "/contactPhotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contact = getIntent().getParcelableExtra("contact");


        photo = findViewById(R.id.btnImage);
        name = findViewById(R.id.editName);

        address = findViewById(R.id.editAddress);
        phone = findViewById(R.id.editPhone);
        save = findViewById(R.id.btnSave);

        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        address.setText(contact.getAddress());

        File imgFile = new File(contact.getPhoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo.setImageBitmap(bitmap);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setName(name.getText().toString());
                contact.setAddress(address.getText().toString());
                contact.setPhone(phone.getText().toString());

                if(contact.getName().equals("") || contact.getAddress().equals("")){
                    Toast.makeText(EditActivity.this, "No Name or Address Entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contact.getPhone().equals("Y"))
                    photo.setImageResource(R.drawable.happy);
                else if (contact.getPhone().equals("N"))
                    photo.setImageResource(R.drawable.madface);
                else
                    photo.setImageResource(R.drawable.midface);

                Intent i = new Intent();
                i.putExtra("contact", contact);
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }



    private  String saveImage(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String imagePath = "";
        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + IMAGE_DIR);
        if(!directory.exists()){
            if(!directory.mkdirs()){
                Toast.makeText(this, "Error: Failed to create directory", Toast.LENGTH_SHORT).show();
                return "";
            }
        }
        try{
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"},null);
            imagePath = f.getAbsolutePath();
        }catch (IOException e){
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return imagePath;
    }


}