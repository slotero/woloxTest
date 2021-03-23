package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.model.album.Album;

import java.util.List;

public interface InterfaceAlbumService {

    List<Album> findAll();

    Album findById(Long id);

    List<Album> findByUserId(Long userId);


}
