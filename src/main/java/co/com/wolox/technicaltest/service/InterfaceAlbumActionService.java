package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.model.album.AlbumActions;
import co.com.wolox.technicaltest.model.album.Action;

import java.util.List;
import java.util.Optional;

public interface InterfaceAlbumActionService {

    Optional<AlbumActions> findByAlbumIdAndUserId(Long albumId, Long userId);

    AlbumActions create(AlbumActions albumActions);

    AlbumActions update(AlbumActions albumActions);

    Optional<List<AlbumActions>> findByAlbumIdAndActions(Long albumId, Action action);


}
