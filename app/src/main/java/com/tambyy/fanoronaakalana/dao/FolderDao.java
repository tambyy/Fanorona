package com.tambyy.fanoronaakalana.dao;

import com.tambyy.fanoronaakalana.models.Folder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM Folder WHERE id = :id")
    Folder getFolder(Long id);

    @Insert
    long insertFolder(Folder folder);

    @Update
    int updateFolder(Folder folder);

    @Query("DELETE FROM Folder WHERE id = :folderId")
    int deleteFolder(long folderId);

    @Query("SELECT * FROM Folder WHERE parent_id = :parentId")
    List<Folder> getChildFolders(Long parentId);

    @Query("SELECT * FROM Folder WHERE parent_id IS NULL")
    List<Folder> getRootFolders();
}
