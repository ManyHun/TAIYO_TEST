package com.erp.taiyo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.erp.taiyo.R;
import com.erp.taiyo.item.MenuIListtem;

import java.util.ArrayList;


public class MenuListAdapter extends BaseAdapter implements Filterable {
    Button btnMenu;
    private ArrayList<MenuIListtem> ListArray = new ArrayList<>();
    private ArrayList<MenuIListtem> FilterListArray = ListArray;
    Filter listFilter;
    String filiterName;
    View convertView2;
    LayoutInflater inflater2;

    @Override
    public Filter getFilter()
    {
        if(listFilter == null)
        {
            listFilter = new ListFilter();
        }
        return listFilter;
    }


    public void filterDate(String filter)
    {
        filiterName = filter;
    }

    @Override
    public int getCount(){
        return FilterListArray.size();
    }

    @Override
    public MenuIListtem getItem(int position) {
        return FilterListArray.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_menu_lv_list, parent, false);
        }
        btnMenu = (Button) convertView.findViewById(R.id.btn_menu1); //모델명


        MenuIListtem FilterListArray = getItem(position);

        btnMenu.setText(FilterListArray.getStrMenu());


        FilterListArray = ListArray.get(position);

        if(FilterListArray != null){
            convertView.setBackgroundColor((position & 1) == 1 ? Color.rgb(255, 255, 230) : Color.WHITE);
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), ""+position, Toast.LENGTH_SHORT).show();
                if(getItem(position).getStrMenu().equals("영업"));
                {
                    Intent intent = null;
                    String activity = "com.erp.hetn.activity." + "MainWahousingActivity";
                    try {

                      //  intent = new Intent(context, MainWahousingActivity.class);

                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return convertView;
    }

    public void addItem(CharSequence Menu
                        )
    {
        //ListArray.add(item);
        MenuIListtem Item = new MenuIListtem();

        Item.setStrMenu((String) Menu);


        ListArray.add(Item);
    }

    public void clearItem(){
        ListArray.clear();
    }

    public void remove(int position)
    {
        ListArray.remove(position);
    }



    private  class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0)
            {
                results.values = ListArray;
                results.count = ListArray.size();

            }
            else
            {
                ArrayList<MenuIListtem> itemList = new ArrayList<MenuIListtem>();

//                for (MenuIListtem item : ListArray) {
//                    if (filiterName.toString().equals("LOT")) {
//                        if (item.getStrLot().toUpperCase().contains(constraint.toString().toUpperCase())) {
//                            itemList.add(item);
//                        }
//                    }
//                    if (filiterName.toString().equals("공정명")) {
//                        if (item.getStrNamek().toUpperCase().contains(constraint.toString().toUpperCase())) {
//                            itemList.add(item);
//                        }
//                    }
//                }

                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            FilterListArray = (ArrayList<MenuIListtem>) results.values;

            if(results.count > 0)
            {
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }
        }
    }
}

