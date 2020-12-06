package com.lia.yilirestaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lia.yilirestaurant.activity.FoodDetailActivity;
import com.lia.yilirestaurant.R;
import com.lia.yilirestaurant.bean.FoodBean;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    List<FoodBean.Food> foodList;
    List<FoodItems> foodItemsList = new ArrayList<>();
    FoodItemButtononClick foodItemButtononClick = null;
    Context context;

    public interface FoodItemButtononClick {
        void buttonAddClick(FoodBean.Food food, int Count);

        void buttonSubClick(FoodBean.Food food, int count);
    }

    public FoodAdapter(List<FoodBean.Food> foodList, Context context, FoodItemButtononClick foodItemButtononClick) {
        this.foodList = foodList;
        this.context = context;
        this.foodItemButtononClick = foodItemButtononClick;
        addFoodItems(foodList);
    }

    public void addFoodItems(List<FoodBean.Food> foodList) {
        for (FoodBean.Food food : foodList) {
            this.foodItemsList.add(new FoodItems(Integer.parseInt(food.getFoodId())));
        }
    }

    public void addFood(List<FoodBean.Food> foodList) {
        this.foodList.addAll(foodList);
        addFoodItems(foodList);
        notifyDataSetChanged();
    }

    public void clearFoodItemsCount() {
        for (FoodItems foodItems : foodItemsList) {
            foodItems.count = 0;
        }
        notifyDataSetChanged();
    }

    public void clearFood() {
        foodItemsList.clear();
        foodList.clear();
        notifyDataSetChanged();
    }

    public String getOrderItems() {
        List<FoodItems> orderItems = new ArrayList<>();
        for (FoodItems foodItems : foodItemsList) {
            if (foodItems.count > 0) {
                orderItems.add(foodItems);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(orderItems);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, final int position) {
        Glide.with(context).load(foodList.get(position).getImgUrl()).placeholder(R.mipmap.placeholder).error(R.mipmap.error).into(holder.pFoodIm);
        holder.pNameTv.setText(foodList.get(position).getName());
        holder.pContentTv.setText(foodList.get(position).getDes());
        holder.pPriceTv.setText(foodList.get(position).getPrice() + "元/份");
        holder.pCountTv.setText(String.valueOf(foodItemsList.get(position).count));
        holder.pSubIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.pCountTv.getText().equals("0")) {
                    foodItemsList.get(position).count -= 1;
                    holder.pCountTv.setText(String.valueOf(foodItemsList.get(position).count));
                    foodItemButtononClick.buttonSubClick(foodList.get(position), foodItemsList.get(position).count);
                }
            }
        });
        holder.pAddIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodItemsList.get(position).count += 1;
                holder.pCountTv.setText(String.valueOf(foodItemsList.get(position).count));
                foodItemButtononClick.buttonAddClick(foodList.get(position), foodItemsList.get(position).count);
            }
        });
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("foodId", foodList.get(position).getFoodId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }


    static class FoodViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_item;
        ImageView pFoodIm;
        TextView pNameTv;
        TextView pContentTv;
        TextView pPriceTv;
        TextView pCountTv;
        ImageButton pSubIb;
        ImageButton pAddIb;

        public FoodViewHolder(@NonNull View view) {
            super(view);
            rl_item = view.findViewById(R.id.rl_item);
            pFoodIm = view.findViewById(R.id.iv_item_food);
            pNameTv = view.findViewById(R.id.tv_item_name);
            pContentTv = view.findViewById(R.id.tv_item_content);
            pPriceTv = view.findViewById(R.id.tv_item_price);
            pCountTv = view.findViewById(R.id.tv_item_count);
            pSubIb = view.findViewById(R.id.ib_item_sub);
            pAddIb = view.findViewById(R.id.ib_item_add);

        }
    }

    static class FoodItems {
        int count;
        int foodId;


        public FoodItems(int foodId) {
            this.foodId = foodId;
            this.count = 0;
        }

        public int getFoodId() {
            return foodId;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }


    }
}
