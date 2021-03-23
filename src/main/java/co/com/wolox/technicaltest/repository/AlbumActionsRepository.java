package co.com.wolox.technicaltest.repository;

import co.com.wolox.technicaltest.model.album.AlbumActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumActionsRepository extends JpaRepository<AlbumActions, Long> {

    Optional<AlbumActions> findByAlbumIdAndUserId(Long albumId, Long userId);

    Optional<List<AlbumActions>> findByAlbumIdAndRead(Long albumId, Boolean  read);

    Optional<List<AlbumActions>> findByAlbumIdAndWrite(Long albumId, Boolean  write);
}
