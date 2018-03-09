package com.mark.foodorderapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.mark.foodorderapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 08/03/2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DATABASEB_NAME="order.db";
    private static final int DATABASEB_VER=1;

    public Database(Context context) {
        super(context, DATABASEB_NAME,null, DATABASEB_VER);
    }

    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlselect={"foodid","foodname","quantity","price","discount"};
        String sqlTable="orderdetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlselect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do
            {
                result.add(new Order(c.getString(c.getColumnIndex("foodid")),
                        c.getString(c.getColumnIndex("foodname")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart (Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (foodid,foodname,quantity,price,discount) VALUES('%s','%s','%s','%s','%s');",
                order.getFoodID(),
                order.getFoodName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void emptyCart ()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM orderdetail");
        db.execSQL(query);
    }

}
