package com.tambyy.fanoronaakalana.models;

import com.tambyy.fanoronaakalana.engine.Engine;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    foreignKeys = @ForeignKey(
        entity = Folder.class,
        parentColumns = "id",
        childColumns = "folder_id",
        onDelete = CASCADE
    )
)
public class Game {

    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * Creation date
     */
    private Date created_at;

    /**
     * Name given by user
     */
    private String name;

    /**
     *
     */
    private int image_index;

    /**
     * configs will contains
     * a list of engine actions
     * converted to string
     *
     * It will be be reconverted to engine actions
     * to be shown in an AkalanaView
     */
    private String configs;

    /**
     * Folder Id can be nullable
     */
    @ColumnInfo(name = "folder_id", index = true)
    private Long folder_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_index() {
        return image_index;
    }

    public void setImage_index(int image_index) {
        this.image_index = image_index;
    }

    public String getConfigs() {
        return configs;
    }

    public void setConfigs(String configs) {
        this.configs = configs;
    }

    public Long getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(Long folder_id) {
        this.folder_id = folder_id;
    }
}
