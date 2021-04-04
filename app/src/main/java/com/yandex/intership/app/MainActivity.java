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
    TextView useFavorite;

    CompanyAdapter companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ПРОВЕРИТЬ СОЕДИНЕНИЕ, ЕСЛИ НЕТ - ПОПРОСИТЬ ВЫЙТИ

        useFavorite = findViewById(R.id.useFavorite);
        headView = findViewById(R.id.headView);
        companiesListView = findViewById(R.id.companiesList);

        Company.setContext(this);

        loadData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        companiesListView.setLayoutManager(layoutManager);
        companiesListView.setHasFixedSize(true);

        ArrayList<Company> companyList = Company.getCompanyList();
        companyAdapter = new CompanyAdapter(MainActivity.this, companyList.size(), companyList);
        companiesListView.setAdapter(companyAdapter);
        Company.setCompanyAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();

        /*useFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ArrayList<Company> companiesFavoriteList = Company.getCompanyFavoriteList();
                    companyAdapter = new CompanyAdapter(MainActivity.this, companiesFavoriteList.size(), companiesFavoriteList);
                    companiesListView.setAdapter(companyAdapter);
                    Company.setCompanyAdapter(companyAdapter);
                }
                else{
                    ArrayList<Company> companyList = Company.getCompanyList();
                    companyAdapter = new CompanyAdapter(MainActivity.this, Company.getCompanyList().size(), Company.getCompanyList());
                    companiesListView.setAdapter(companyAdapter);
                    Company.setCompanyAdapter(companyAdapter);

                }
                companyAdapter.notifyDataSetChanged();
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

    }
}