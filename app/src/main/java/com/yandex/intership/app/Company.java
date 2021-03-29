package com.yandex.intership.app;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

public class Company implements Parcelable{
    final static private String apiKey = "c1g686n48v6p69n8n1ig";
    final static private String apiPrefix = "https://finnhub.io/api/v1/";
    final static private ArrayList<Company> companyList = new ArrayList<>();
    final static private ArrayList<Company> companyFavoriteList = new ArrayList<>();
    static private CompanyAdapter companyAdapter;
    static private Context context;
    static private int companiesNumber = 0;

    private String name;
    private boolean favorite;
    private String symbol;
    private double currentPrice;
    private double priceChange;

    Company(String name, String symbol){
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = 0.00;
        this.priceChange = 0.00;
        this.favorite = false;
    }
    public static void setCompanyAdapter(CompanyAdapter companyAdapter){
        Company.companyAdapter = companyAdapter;
    }
    public static void setContext(Context context) {
        Company.context = context;
    }

    public static void addCompany(Company company){
        companyList.add(company);
        companiesNumber++;
    }
    public static Company getCompany(int position){
        return companyList.get(position);
    }

    public static ArrayList<Company> getCompanyList() {
        return companyList;
    }
    public static ArrayList<Company> getCompanyFavoriteList() {
        return companyFavoriteList;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceField(){
        String change = String.format("%.2f",this.currentPrice);
        return change;
    }
    public String getPriceChangeField(){
        String change = String.format("%.2f",this.priceChange);
        return change;
    }
    public String getPriceChangePercentage(){
        String changePercentage = String.format("%.2f",this.priceChange/this.currentPrice);
        return changePercentage;
    }
    public Boolean isFavorite(){
        return this.favorite;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void addToFavorite(){
        this.favorite = true;
        if(!Company.companyFavoriteList.contains(this))Company.companyFavoriteList.add(this);
    }
    public void removeFromFavorite(){
        this.favorite = false;
        if(Company.companyFavoriteList.contains(this))Company.companyFavoriteList.remove(this);
    }

    public double getCurrentPrice() {
        return this.currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    protected Company(Parcel in) {
        name = in.readString();
        symbol = in.readString();
        currentPrice = in.readDouble();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public static void updateAllPrices(){;
        //Тут выполняется запрос на изменение цены по api у всех компаний
        for(int i=0;i<Company.companiesNumber;++i) {
            Company company = companyList.get(i);
            company.updatePrice(new PriceResponseListener() {
                @Override
                public void onError(String message) {}

                @Override
                public void onResponse(double price, double change) {
                    company.setCurrentPrice(price);
                    company.setPriceChange(change);
                    companyAdapter.notifyDataSetChanged();//вынеси это в main-----------
                }
            });
        }
    }

    public void updatePrice(){
        updatePrice(new PriceResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(double price, double change) {
                currentPrice = price;
                priceChange = change;
                companyAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updatePrice(PriceResponseListener volleyResponseListener){
        //Выполняет запрос только на цену акции
        //Нужно для обновления цен на MainActivity
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = apiPrefix+"quote?"+"symbol="+this.symbol+"&token="+apiKey;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject priceInfo) {
                        try {
                            double price = priceInfo.getDouble("c");
                            double previousPrice = priceInfo.getDouble("pc");
                            double change = price - previousPrice;
                            volleyResponseListener.onResponse(price, change);
                        } catch (JSONException e) {
                            Toast.makeText(context, "Попробуйте позже", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Ошибка сети",Toast.LENGTH_LONG).show();
                        volleyResponseListener.onError("Ошибка сети");
                    }
            }
        );
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeDouble(currentPrice);
    }

    public interface PriceResponseListener{
        void onError(String message);

        void onResponse(double currentPrice, double priceChange);
    }
}
