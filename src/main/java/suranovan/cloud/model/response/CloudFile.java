package suranovan.cloud.model.response;

import java.util.Objects;

public class CloudFile {
    private String name;
    private Integer size;

    public CloudFile(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(name, cloudFile1.name) && Objects.equals(size, cloudFile1.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }
}
