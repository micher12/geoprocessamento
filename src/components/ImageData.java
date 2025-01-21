package components;

public class ImageData {

    int id;
    int alertCode;
    String detectado;
    String publicado;

    public ImageData(int id, int alertCode, String detectado, String publicado) {
        this.id = id;
        this.alertCode = alertCode;
        this.detectado = detectado;
        this.publicado = publicado;

    }

    public int getId() {
        return id;
    }

    public String getPublicado() {
        return publicado;
    }
    public String getDetectado() {
        return detectado;
    }

    public int getAlertCode() {
        return alertCode;
    }

}
