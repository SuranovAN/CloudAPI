package suranovan.cloud.model.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileName {
    private String fileName;

    public FileName(@JsonProperty("name") String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
