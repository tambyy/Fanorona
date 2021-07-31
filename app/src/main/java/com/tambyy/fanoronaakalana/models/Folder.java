package com.tambyy.fanoronaakalana.models;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
    foreignKeys = @ForeignKey(
        entity = Folder.class,
        parentColumns = "id",
        childColumns = "parent_id",
        onDelete = CASCADE
    )
)
public class Folder {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date created_at;

    private String name;

    @ColumnInfo(name = "parent_id", index = true)
    private Long parent_id;

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

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parentId) {
        this.parent_id = parentId;
    }
}
