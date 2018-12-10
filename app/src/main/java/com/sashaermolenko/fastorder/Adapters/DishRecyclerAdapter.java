package com.sashaermolenko.fastorder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sashaermolenko.fastorder.DishActivity;
import com.sashaermolenko.fastorder.Items.CartItem;
import com.sashaermolenko.fastorder.Items.DishItem;
import com.sashaermolenko.fastorder.MainActivity;
import com.sashaermolenko.fastorder.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishRecyclerAdapter extends RecyclerView.Adapter<DishRecyclerAdapter.RecyclerViewHolder> {
    private Context context;
    ArrayList<DishItem> items = new ArrayList<>();

    public DishRecyclerAdapter(Context context) {
        this.context = context;
        if(MenuRecyclerAdapter.categoty == 0)
            this.items = DishActivity.items_1;
        else if(MenuRecyclerAdapter.categoty == 1)
            this.items = DishActivity.items_2;
        else if(MenuRecyclerAdapter.categoty == 2)
            this.items = DishActivity.items_3;
        else if(MenuRecyclerAdapter.categoty == 3)
            this.items = DishActivity.items_4;
    }
    
    public void addAll(List<DishItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dishitem, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final DishItem dishItem = items.get(position);

        holder.bind(dishItem);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = dishItem.getExpandable();
                dishItem.setExpanded(!expanded);
                notifyItemChanged(position);
                dishItem.setVisOfFullName(!expanded);
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer amount = Integer.valueOf(holder.amount.getText().toString()) + 1;
                Integer pr = Integer.valueOf(dishItem.getPrice()) * amount;
                holder.total_price.setText(Integer.toString(pr));
                holder.amount.setText(Integer.toString(amount));
                dishItem.setAmount(1);
                //notifyItemChanged(position);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer amount = Math.max(1, Integer.valueOf(holder.amount.getText().toString()) - 1);
                Integer pr = Integer.valueOf(dishItem.getPrice()) * amount;
                holder.total_price.setText(Integer.toString(pr));
                holder.amount.setText(Integer.toString(amount));
                if(dishItem.getAmount() != 1) dishItem.setAmount(-1);
                //notifyItemChanged(position);
            }
        });
        holder.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dishItem.setComment(holder.comment.getText().toString());

                String title = dishItem.getName();
                String imgUrl = dishItem.getURL();
                int id = items.size();
                int amount = dishItem.getAmount();
                String price = dishItem.getPrice();
                String desc = dishItem.getDescription();
                String comment = dishItem.getComment();
                CartItem cartItem = new CartItem(title, imgUrl, id, price, desc, comment, amount);
                MainActivity.cartItems.add(cartItem);

                boolean expanded = dishItem.getExpandable();
                dishItem.setExpanded(!expanded);
                notifyItemChanged(position);
                dishItem.setVisOfFullName(!expanded);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title, price, description, total_price, total, amount;
        private Button plus, minus, purchase;
        private ImageView image;
        private View subItem;
        private EditText comment;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            subItem = itemView.findViewById(R.id.sub_item);

            amount = (TextView) itemView.findViewById(R.id.dish_amount);
            comment = (EditText) itemView.findViewById(R.id.comment);
            plus = (Button) itemView.findViewById(R.id.btn_plus);
            minus = (Button) itemView.findViewById(R.id.btn_minus);
            purchase = (Button) itemView.findViewById(R.id.purchase);
            title = (TextView) itemView.findViewById(R.id.title);
            total = (TextView) itemView.findViewById(R.id.total);
            total_price = (TextView) itemView.findViewById(R.id.total_price);
            price = (TextView) itemView.findViewById(R.id.price);
            description = (TextView) itemView.findViewById(R.id.description);
            title.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            total.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            total_price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            price.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));
            description.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));

            image = (ImageView) itemView.findViewById(R.id.imgD);

            //image.setImageResource(R.drawable.no_img); //
        }

        public void bind(final DishItem recyclerItem) {
            boolean expanded = recyclerItem.getExpandable();


            comment.setText(recyclerItem.getComment());
            title.setText(recyclerItem.getName());
            price.setText(recyclerItem.getPrice());
            description.setText(recyclerItem.getDescription());
            // TODO добавить текст button.setText();
            Picasso.with(context).load(recyclerItem.getURL()).into(image);

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            price.setVisibility(expanded ? View.INVISIBLE : View.VISIBLE);

            Integer am = Integer.valueOf(amount.getText().toString());
            Integer pr = Integer.valueOf(recyclerItem.getPrice()) * am;
            total_price.setText(Integer.toString(pr));
        }
    }
}
