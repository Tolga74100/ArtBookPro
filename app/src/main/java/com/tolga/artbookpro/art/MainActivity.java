package com.tolga.artbookpro.art;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tolga.artbookpro.adapter.ArtAdapter;
import com.tolga.artbookpro.R;
import com.tolga.artbookpro.databinding.ActivityMainBinding;
import com.tolga.artbookpro.model.Art;
import com.tolga.artbookpro.model.ArtAndPlace;
import com.tolga.artbookpro.roomdb.ArtDao;
import com.tolga.artbookpro.roomdb.ArtDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ArrayList<ArtAndPlace> artAndPlaceArrayList;
    ArtAdapter artAdapter;
    ArtDatabase artdb;
    ArtDao artDao;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        artAndPlaceArrayList = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artAdapter = new ArtAdapter(artAndPlaceArrayList);
        binding.recyclerView.setAdapter(artAdapter);

        artdb = Room.databaseBuilder(getApplicationContext(),ArtDatabase.class,"Arts").build();
        artDao = artdb.artDao();

        compositeDisposable.add(artDao.getArtAndPlace()
                .subscribeOn(Schedulers.io())
                . observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::handlerResponse));

        //getData();

    }

    private  void handlerResponse(List<ArtAndPlace> artAndPlaceList){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArtAdapter placeAdapter = new ArtAdapter(artAndPlaceList);
        binding.recyclerView.setAdapter(placeAdapter);
    }

    /*private void getData(){
        try {
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM arts", null);
            int nameIx = cursor.getColumnIndex("artname");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()){
                String name = cursor.getString(nameIx);
                int id = cursor.getInt(idIx);
                Art art = new Art(name,id);
                artArryList.add(art);
            }

            artAdapter.notifyDataSetChanged();
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.art_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_art){
            Intent intent = new Intent(this, ArtActivity.class);
            intent.putExtra("info", "new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}