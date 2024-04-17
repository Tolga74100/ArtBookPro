package com.tolga.artbookpro.art;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.ExtractedText;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tolga.artbookpro.R;
import com.tolga.artbookpro.adapter.ArtAdapter;
import com.tolga.artbookpro.adapter.PlaceAdapter;
import com.tolga.artbookpro.databinding.ActivityArtBinding;
import com.tolga.artbookpro.databinding.ActivityMapsBinding;
import com.tolga.artbookpro.maps.MapsActivity;
import com.tolga.artbookpro.model.Art;
import com.tolga.artbookpro.model.ArtAndPlace;
import com.tolga.artbookpro.model.Place;
import com.tolga.artbookpro.roomdb.ArtDao;
import com.tolga.artbookpro.roomdb.ArtDatabase;
import com.tolga.artbookpro.roomdb.PlaceDao;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtActivity extends AppCompatActivity {
    ArrayList<ArtAndPlace> artAndPlaceArrayList;

    private ActivityArtBinding binding;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> mapActivityResulLouncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;

    ArtDatabase artdb;
    ArtDao artDao;
    PlaceDao placeDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ArtAndPlace selectedArtAndPlace;
    Art selectedArt;

    public Button goToMaps;
    Place place;
    ArtAndPlace artAndPlace;

    PlaceAdapter placeAdapter;
    ArrayList<Place> placeArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLouncher();
        registerMapLouncher();

        artAndPlaceArrayList = new ArrayList<>();
        placeArrayList = new ArrayList<>();


        Intent intent = getIntent();
        String info = intent.getStringExtra("info");






        /*if (info.equals("new")){
            binding.nameText.setText("");
            binding.artistText.setText("");
            binding.yearText.setText("");
            binding.button.setVisibility(View.VISIBLE);
            binding.imageView.setImageResource(R.drawable.select);

        }else{
            int artId = intent.getIntExtra("artId",0);
            binding.button.setVisibility(View.INVISIBLE);

            /*try {
                Cursor cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?", new String[] {String.valueOf(artId)});
                int artNameIx = cursor.getColumnIndex("artname");
                int painterNameIx = cursor.getColumnIndex("paintername");
                int yearIx = cursor.getColumnIndex("year");
                int imageIx = cursor.getColumnIndex("image");

                while (cursor.moveToNext()){
                    binding.nameText.setText(cursor.getString(artNameIx));
                    binding.artistText.setText(cursor.getString(painterNameIx));
                    binding.yearText.setText(cursor.getString(yearIx));

                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                }

                cursor.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

        if (info.equals("new")){

            binding.nameText.setText("");
            binding.artistText.setText("");
            binding.yearText.setText("");
            binding.button.setVisibility(View.VISIBLE);
            binding.goToMaps.setVisibility(View.VISIBLE);
            binding.artDeleteButton.setVisibility(View.GONE);
            binding.imageView.setImageResource(R.drawable.select);

        }else {
            selectedArtAndPlace = (ArtAndPlace) intent.getSerializableExtra("art");
            binding.button.setVisibility(View.GONE);
            binding.goToMaps.setVisibility(View.GONE);
            binding.artDeleteButton.setVisibility(View.VISIBLE);

            binding.nameText.setText(selectedArtAndPlace.art.artName);
            binding.artistText.setText(selectedArtAndPlace.art.artistName);
            binding.yearText.setText(selectedArtAndPlace.art.year);
            binding.mapTextView.setText(selectedArtAndPlace.art.locationName);
            //placeList.get(place.placeid).mapName





            byte[] bytes = selectedArtAndPlace.art.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.imageView.setImageBitmap(bitmap);



        }

        artdb = Room.databaseBuilder(getApplicationContext(),ArtDatabase.class,"Arts").build();
        artDao = artdb.artDao();
        placeDao = artdb.placeDao();





    }



public void setLocaationData(View view ){
        Intent setArtAdapterIntent = getIntent();
       Serializable infoMaps =  setArtAdapterIntent.getSerializableExtra("art");
    Intent setLocationDataintent = new Intent(ArtActivity.this, MapsActivity.class);
    setLocationDataintent.putExtra("placeinfo","placeold");
    setLocationDataintent.putExtra("place", infoMaps);
    startActivity(setLocationDataintent);


}





    public  void save(View view){


        Bitmap smallImage = makeSmallerImage(selectedImage, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        Art art = new Art(
                binding.nameText.getText().toString(),
                binding.artistText.getText().toString(),
                binding.yearText.getText().toString(),
                binding.mapTextView.getText().toString(), byteArray);

        // placeDao.insert(place);
        compositeDisposable.add(artDao.artInsert(art)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ArtActivity.this::handleResponse));
    }

    private void handleResponse(){
        Intent intent = new Intent(ArtActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goLocation(View  view) {

           goToMaps=findViewById(R.id.goToMaps);
           Intent goLocationIntent = new Intent(ArtActivity.this, MapsActivity.class);
           goLocationIntent.putExtra("placeinfo", "placenew");
           mapActivityResulLouncher.launch(goLocationIntent);



    }

    public void registerMapLouncher(){
        mapActivityResulLouncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == RESULT_OK) {
                    Intent intentFromResult = o.getData();
                    if (intentFromResult != null) {
                        String mapdata = intentFromResult.getStringExtra("name");
                        binding.mapTextView.setText(mapdata);
                    }
                }

            }
        });
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        binding.mapsTextView.setText(data.getExtras().getString("name"));

    }

    /* public void save1(View view) {
            String name = binding.nameText.getText().toString();
            String artistName = binding.artistText.getText().toString();
            String year = binding.yearText.getText().toString();

            Bitmap smallImage = makeSmallerImage(selectedImage, 300);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            byte[] byteArray = outputStream.toByteArray();

            try {

                database.execSQL("CREATE TABLE IF NOT EXISTS arts(id INTEGER PRIMARY KEY, artname VARCHAR, paintername VARCHAR, year VARCHAR, image BLOB)");

                String sqlString = "INSERT INTO arts(artname, paintername, year, image) VALUES(?, ?, ?, ? )";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1, name);
                sqLiteStatement.bindString(2, artistName);
                sqLiteStatement.bindString(3, year);
                sqLiteStatement.bindBlob(4, byteArray);
                sqLiteStatement.execute();
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent intent = new Intent(ArtActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }*/
    public Bitmap makeSmallerImage(Bitmap image, int maximumSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio >1){
            width = maximumSize;
            height = (int) (width/bitmapRatio);
        }else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return image.createScaledBitmap(image, width,height, true);
    }

    public void selectImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(ArtActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ArtActivity.this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }

        } else {
            if (ContextCompat.checkSelfPermission(ArtActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ArtActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }

    }

    public void registerLouncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                     Uri imageData = intentFromResult.getData();
                     //binding.imageView.setImageURI(imageData);

                        try {
                            if (Build.VERSION.SDK_INT >=28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(ArtActivity.this.getContentResolver(), imageData);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            }else {
                                selectedImage = MediaStore.Images.Media.getBitmap(ArtActivity.this.getContentResolver(), imageData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                        }catch (Exception e) {

                        }
                    }
                }

            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    //permission granted
                    Intent intentToGaleriy = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGaleriy);
                }else {
                    //permission denied
                    Toast.makeText(ArtActivity.this, "Permission needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}