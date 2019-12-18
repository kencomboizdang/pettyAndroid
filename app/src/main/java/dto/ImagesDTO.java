package dto;

public class ImagesDTO {
    String id;
    String img;

    public ImagesDTO(String id, String img) {
        this.id = id;
        this.img = img;
    }

    public ImagesDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
