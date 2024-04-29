package edu.andrews.cptr252.lisabeth.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private ImageButton photo;
    private EditText name;

    private EditText address;
    //ArrayList<String> paymentList = new ArrayList<>();
    private Spinner payment;
    //private EditText payment;
    private Button save;
    private InfoContact contact;

    private final String IMAGE_DIR = "/contactPhotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        contact = getIntent().getParcelableExtra("contact");



        photo = findViewById(R.id.btnImage);
        name = findViewById(R.id.editName);

        address = findViewById(R.id.editAddress);
//        payment = findViewById(R.id.editPayment);
        save = findViewById(R.id.btnSave);
        payment = findViewById(R.id.editPayment);

        String[] categories = {"Payments Made", "Overdue", "Waiting for Payment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        payment.setAdapter(adapter);

        name.setText(contact.getName());
     //   payment.setText(contact.getPhone());
        try {
            payment.setSelection(Integer.parseInt(contact.getPhone()));

        }
        catch (Exception e){
            //Toast.makeText(this, "Num selected: "+e.toString(), Toast.LENGTH_SHORT).show();
            //name.setText(e.toString());
            payment.setSelection(0);
        }

        address.setText(contact.getAddress());

        File imgFile = new File(contact.getPhoto());
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo.setImageBitmap(bitmap);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setName(name.getText().toString());
                contact.setAddress(address.getText().toString());
                //contact.setPhone(payment.getText().toString());
                contact.setPhone(payment.getSelectedItem().toString());

                if (contact.getName().equals("") || contact.getAddress().equals("")) {
                    Toast.makeText(EditActivity.this, "No Name or Address Entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contact.getPhone().equals("Payments Made")) {
                    photo.setImageResource(R.drawable.happy);
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.happy);

                    saveImage(largeIcon);
                    contact.setPhoto(saveImage(largeIcon));
                    photo.setImageBitmap(largeIcon);


                } else if (contact.getPhone().equals("Overdue")) {
                    photo.setImageResource(R.drawable.madface);
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.madface);

                    saveImage(largeIcon);
                    contact.setPhoto(saveImage(largeIcon));
                    photo.setImageBitmap(largeIcon);
                } else {
                    photo.setImageResource(R.drawable.midface);
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.midface);

                    saveImage(largeIcon);
                    contact.setPhoto(saveImage(largeIcon));
                    photo.setImageBitmap(largeIcon);
                }

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