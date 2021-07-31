package com.tambyy.fanoronaakalana.models;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Theme {

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
    private String folder_name;

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

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }
}
