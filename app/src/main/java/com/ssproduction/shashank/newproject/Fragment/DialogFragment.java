package com.ssproduction.shashank.newproject.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ssproduction.shashank.newproject.ChatImgShowActivity;
import com.ssproduction.shashank.newproject.R;
import com.ssproduction.shashank.newproject.models.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends Fragment {


    private static final int GALLERY_PIC = 1;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private String currentUser;
    private String user_id;
    private CardView mBottomSheet;


    private ArrayList<String> images;


    public DialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        currentUser = bundle.getString("current_user");
        user_id = bundle.getString("user_id");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dialog, container, false);


        LinearLayout camera = (LinearLayout) view.findViewById(R.id.botm_sheet_camera);
        LinearLayout share = (LinearLayout) view.findViewById(R.id.botm_sheet_share);
        LinearLayout document = (LinearLayout) view.findViewById(R.id.botm_sheet_document);
        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.botm_sheet_gallery);
        LinearLayout media = (LinearLayout) view.findViewById(R.id.botm_sheet_media);
        LinearLayout contacts = (LinearLayout) view.findViewById(R.id.botm_sheet_contacts);


        dialog = new ProgressDialog(getActivity());

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "camera", Toast.LENGTH_SHORT).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "share", Toast.LENGTH_SHORT).show();

            }
        });

        document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "document", Toast.LENGTH_SHORT).show();

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "gallery", Toast.LENGTH_SHORT).show();

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PIC);
                view.setVisibility(View.GONE);
            }
        });

        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "media", Toast.LENGTH_SHORT).show();

            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Contacts", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PIC && resultCode == RESULT_OK) {

            String image = data.getData().toString();
            String userId = user_id;
            String current_user = currentUser;

            final Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            final byte[] myData = baos.toByteArray();

            byte[] myByte = myData;

            Intent imgPreIntent = new Intent(getActivity(), ChatImgShowActivity.class);
            imgPreIntent.putExtra("image_info", image);
            imgPreIntent.putExtra("user_id", userId);
            imgPreIntent.putExtra("current_user", current_user);
            imgPreIntent.putExtra("myData", myByte);
            startActivity(imgPreIntent);


        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
