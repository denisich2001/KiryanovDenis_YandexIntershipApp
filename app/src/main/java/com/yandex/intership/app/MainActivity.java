package com.yandex.intership.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {

    RecyclerView companiesListView;
    TextView headView;
    //TextView useFavorite;
    SearchView searchView;

    //для проверки:
    ArrayList<Integer> favoriteIds;

    CompanyAdapter companyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ПРОВЕРИТЬ СОЕДИНЕНИЕ, ЕСЛИ НЕТ - ПОПРОСИТЬ ВЫЙТИ
        searchView = (SearchView) findViewById(R.id.search_bar);
        //useFavorite = findViewById(R.id.useFavorite);
        headView = findViewById(R.id.headView);
        companiesListView = findViewById(R.id.companiesList);
        this.favoriteIds = new ArrayList<>();
        Company.setContext(this);

        loadData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        companiesListView.setLayoutManager(layoutManager);
        companiesListView.setHasFixedSize(true);

        companyAdapter = new CompanyAdapter(MainActivity.this,  Company.getCompanyList().size(), Company.getCompanyList());
        companiesListView.setAdapter(companyAdapter);
        Company.setCompanyAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //здесь изменяем companiesListToShow =
                companyAdapter.getFilter().filter(newText);
                return false;
            }
        });

        /*useFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "--1--"+Company.getCompanyFavoriteList().size(),Toast.LENGTH_LONG).show();

                //companyListToShow = Company.getCompanyFavoriteList();
//                CompanyAdapter newCompanyAdapter = new CompanyAdapter(MainActivity.this, companyListToShow.size(),companyListToShow);
//                CompanyAdapter newCompanyAdapter = new CompanyAdapter(MainActivity.this, companyListToShow.size(),companyListToShow);

  //              companiesListView.setAdapter(newCompanyAdapter);
 //               Company.setCompanyAdapter(newCompanyAdapter);
  //              Toast.makeText(MainActivity.this, "--2--"+Company.getCompanyFavoriteList().size(),Toast.LENGTH_LONG).show();
                companyListToShow = Company.getCompanyFavoriteList();
                companyAdapter.notifyDataSetChanged();
  //              Toast.makeText(MainActivity.this, "--3--"+Company.getCompanyFavoriteList().size(),Toast.LENGTH_LONG).show();

            }
        });
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyListToShow = Company.getCompanyList();
                CompanyAdapter newCompanyAdapter = new CompanyAdapter(MainActivity.this, companyListToShow.size(),companyListToShow);
                companiesListView.setAdapter(newCompanyAdapter);
                Company.setCompanyAdapter(newCompanyAdapter);
                newCompanyAdapter.notifyDataSetChanged();
            }
        });*/

    }

    private void loadData(){
        Resources resources = MainActivity.this.getResources();
        String[] companiesNames = resources.getStringArray(R.array.companiesNames);
        String[] companiesSymbols = resources.getStringArray(R.array.companiesSymbols);

        for(int i=0;i<companiesNames.length;++i){
            Company.addCompany(new Company(companiesNames[i], companiesSymbols[i]));
        }
        Company.updateAllPrices();
    }

   public void onAddToFavoriteClicked(View view) {
        //Toast.makeText(this, String.valueOf(view.getId()),Toast.LENGTH_SHORT).show();
     //   if(!this.favoriteIds.contains(view.getId()))
     //       this.favoriteIds.add(view.getId());
 //  /     else
 //           this.favoriteIds.remove(this.favoriteIds.get(view.getId()));
    }
    //public void onFavoriteListClicked(View view){
        /*Toast.makeText(this, String.valueOf(Company.getCompanyFavoriteList().size())+" -Входим",Toast.LENGTH_SHORT).show();
        companyAdapter.getItem
        CompanyAdapter newCompanyAdapter = new CompanyAdapter(MainActivity.this, Company.getCompanyFavoriteList().size(), Company.getCompanyFavoriteList(), this.favoriteIds);
        companiesListView.setAdapter(newCompanyAdapter);
        Company.setCompanyAdapter(newCompanyAdapter);
        newCompanyAdapter.notifyDataSetChanged();
        Toast.makeText(this,"Элементов в id: " + String.valueOf(favoriteIds.size()),Toast.LENGTH_SHORT).show();
         */
   // }
}