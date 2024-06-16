package com.example.cakeshop;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Order implements Serializable , Parcelable {
    //感觉可以用表连接，代码质量好低呜呜呜
    int id;
    String cakeId;
    String Uid;
    String productDesc;
    String price;
    String detail;
    int count;
    String name;
    int status;
    String createTime;

    protected Order(Parcel in) {
        id = in.readInt();
        cakeId = in.readString();
        Uid = in.readString();
        productDesc = in.readString();
        price = in.readString();
        detail = in.readString();
        count = in.readInt();
        name = in.readString();
        status = in.readInt();
        createTime = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", cakeId='" + cakeId + '\'' +
                ", Uid='" + Uid + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", price='" + price + '\'' +
                ", detail='" + detail + '\'' +
                ", count=" + count +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCakeId() {
        return cakeId;
    }

    public void setCakeId(String cakeId) {
        this.cakeId = cakeId;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cakeId);
        dest.writeString(Uid);
        dest.writeString(productDesc);
        dest.writeString(price);
        dest.writeString(detail);
        dest.writeInt(count);
        dest.writeString(name);
        dest.writeInt(status);
        dest.writeString(createTime);
    }
}
