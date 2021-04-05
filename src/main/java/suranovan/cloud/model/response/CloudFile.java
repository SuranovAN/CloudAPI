package suranovan.cloud.model.response;

import java.util.Objects;

public class CloudFile {
    private String filename;
    private Integer size;

    public CloudFile(String name, Integer size) {
        this.filename = name;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloudFile cloudFile1 = (CloudFile) o;
        return Objects.equals(filename, cloudFile1.filename) && Objects.equals(size, cloudFile1.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, size);
    }
}
