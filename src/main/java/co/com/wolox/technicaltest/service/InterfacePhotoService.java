package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.model.photo.Photo;

import java.util.List;

public interface InterfacePhotoService {

    List<Photo> findAll();

    Photo findById(Long id);

    List<Photo> findByUserId(Long userId);

    List<Photo> findByAlbumId(Long albumId);

}
