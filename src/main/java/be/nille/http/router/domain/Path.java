package be.nille.http.router.domain;

public class Path {

    private final String value;

    private Path(String value) {
        this.value = value;
    }

    public static Path create(String path){
        return new Path(path);
    }


    public String getValue() {
        return value;
    }
}
