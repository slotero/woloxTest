package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.controller.exception.NotFoundException;
import co.com.wolox.technicaltest.model.album.AlbumActions;
import co.com.wolox.technicaltest.model.album.Action;
import co.com.wolox.technicaltest.repository.AlbumActionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static co.com.wolox.technicaltest.model.album.Action.READ;

@Service
public class AlbumActionServiceImplement implements InterfaceAlbumActionService {


    final
    AlbumActionsRepository albumActionsRepository;

    public AlbumActionServiceImplement(AlbumActionsRepository albumActionsRepository) {
        this.albumActionsRepository = albumActionsRepository;
    }

    @Override
    public AlbumActions create(AlbumActions albumActions) {
        return albumActionsRepository.save(albumActions);
    }

    @Override
    public Optional<AlbumActions> findByAlbumIdAndUserId(Long albumId, Long userId) {
        return albumActionsRepository.findByAlbumIdAndUserId(albumId, userId);
    }

    @Override
    public AlbumActions update(AlbumActions albumActions) {
        AlbumActions albumActionsDB = findByAlbumIdAndUserId(albumActions.getAlbumId(), albumActions.getUserId())
                    .orElseThrow(NotFoundException::new);
        albumActionsDB.setRead(albumActions.getRead());
        albumActionsDB.setRead(albumActions.getWrite());
        return albumActionsRepository.save(albumActionsDB);
    }

    @Override
    public Optional<List<AlbumActions>> findByAlbumIdAndActions(Long albumId, Action action) {
        return (action == READ)
                ?albumActionsRepository.findByAlbumIdAndRead(albumId, true)
                :albumActionsRepository.findByAlbumIdAndWrite(albumId, true);
    }
}
