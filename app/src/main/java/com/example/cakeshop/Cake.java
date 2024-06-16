package com.example.cakeshop;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Cake implements Parcelable, Serializable {
    private int id;
    private String img;
    private int image;
    private String desc;
    private String price;
    private String detail;
    private int count;
    private String name;

    protected Cake(Parcel in) {
        id = in.readInt();
        img = in.readString();
        desc = in.readString();
        price = in.readString();
        detail = in.readString();
        count = in.readInt();
        name = in.readString();
    }

    public static final Creator<Cake> CREATOR = new Creator<Cake>() {
        @Override
        public Cake createFromParcel(Parcel in) {
            return new Cake(in);
        }

        @Override
        public Cake[] newArray(int size) {
            return new Cake[size];
        }
    };

    @Override
    public String toString() {
        return "Cake{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", detail='" + detail + '\'' +
                ", count=" + count +
                ", name='" + name + '\'' +
                '}';
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(img);
        dest.writeString(desc);
        dest.writeString(price);
        dest.writeString(detail);
        dest.writeInt(count);
        dest.writeString(name);
    }
}
