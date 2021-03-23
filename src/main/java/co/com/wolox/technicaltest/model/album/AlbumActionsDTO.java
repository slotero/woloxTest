package co.com.wolox.technicaltest.model.album;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AlbumActionsDTO extends RepresentationModel<AlbumActionsDTO> {

    @NotNull
    private Long userId;

    @NotNull
    private Long albumId;

    @NotNull
    private Boolean read;

    @NotNull
    private Boolean write;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getWrite() {
        return write;
    }

    public void setWrite(Boolean write) {
        this.write = write;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumActionsDTO)) return false;
        if (!super.equals(o)) return false;
        AlbumActionsDTO that = (AlbumActionsDTO) o;
        return
                Objects.equals(userId, that.userId) && Objects.equals(albumId, that.albumId)
                        && Objects.equals(read, that.read) && Objects.equals(write, that.write);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, albumId, read, write);
    }
}
