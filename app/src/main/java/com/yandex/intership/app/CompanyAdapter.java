package com.yandex.intership.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private static int viewHolderCount;
    private int companyItems;
    private Context context;
    private ArrayList<Company> companyList;
    public CompanyAdapter(Context context, int companyItems, ArrayList<Company> companyList){
        this.companyItems = companyItems;
        this.context = context;
        this.companyList = companyList;
        viewHolderCount = 0;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        CompanyViewHolder viewHolder = new CompanyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
       holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return companyItems;
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView symbol;
        TextView name;
        ToggleButton addToFavoriteView;
        TextView price;
        TextView change;
        Company company;

        public CompanyViewHolder(View itemView) {
            super(itemView);

            symbol = (TextView) itemView.findViewById(R.id.symbol);
            name = (TextView) itemView.findViewById(R.id.name);
            addToFavoriteView = (ToggleButton) itemView.findViewById(R.id.addToFavorite);
            price = (TextView) itemView.findViewById(R.id.price);
            change = (TextView) itemView.findViewById(R.id.change);

            addToFavoriteView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int positionTappedIndex = getLayoutPosition();
                    Company company = Company.getCompany(positionTappedIndex);
                    if(isChecked){
                        company.addToFavorite();
                    }
                    else if(!isChecked){
                        company.removeFromFavorite();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionTappedIndex = getLayoutPosition();

                    Class companyPageActivity = CompanyPage.class;
                    Intent companyActivityIntent = new Intent(context, companyPageActivity);
                    companyActivityIntent.putExtra("position", String.valueOf(positionTappedIndex));
                    companyActivityIntent.putExtra("company", company);
                    context.startActivity(companyActivityIntent);
                }
            });

        }

        public void bind(int companyIndex) {
            this.company = companyList.get(companyIndex);
            this.name.setText(this.company.getName());
            this.symbol.setText(this.company.getSymbol());
            this.price.setText(String.valueOf(this.company.getCurrentPrice()));
            this.change.setText(this.company.getPriceChangeField());
            if(company.getPriceChangeField().charAt(0)=='-')
                this.change.setTextColor(Color.RED);
            else this.change.setTextColor(Color.parseColor("#007F16"));
            if(Company.getCompanyFavoriteList().contains(this.company))
                this.addToFavoriteView.setChecked(true);
            else{
                this.addToFavoriteView.setChecked(false);
            }

        }
    }
}
