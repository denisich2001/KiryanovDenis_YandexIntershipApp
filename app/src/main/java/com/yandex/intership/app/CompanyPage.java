package com.yandex.intership.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyPage extends AppCompatActivity {

    TextView nameCompany;
    TextView symbol;
    TextView symbolCompany;
    TextView price;
    TextView priceCompany;
    TextView change;
    //TextView changeCompanyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);

        nameCompany = findViewById(R.id.nameCompany);
        symbol = findViewById(R.id.symbol);
        symbolCompany = findViewById(R.id.symbolCompany);
        priceCompany = findViewById(R.id.priceCompany);
        price = findViewById(R.id.price);
        change = findViewById(R.id.change);
        //changeCompanyView = findViewById(R.id.changeCompany);

        Intent intentCompaniesList = getIntent();

        if(intentCompaniesList.hasExtra("position")){
            Company company = (Company)intentCompaniesList.getParcelableExtra("company");
            company.updatePrice();

            //this.changeCompanyView.setText(company.getPriceChangeField());
            this.nameCompany.setText(company.getName());
            this.symbolCompany.setText(company.getSymbol());
            this.priceCompany.setText(company.getPriceField());
        }
    }
}