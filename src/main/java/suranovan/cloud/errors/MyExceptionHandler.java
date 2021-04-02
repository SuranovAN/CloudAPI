package suranovan.cloud.errors;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import suranovan.cloud.model.response.Error;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MyExceptionHandler extends BasicErrorController {

    public MyExceptionHandler(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    @Override
    public ResponseEntity error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            return ResponseEntity.status(status).body(new Error("Error from server", 0));

        } else if (status.equals(HttpStatus.BAD_REQUEST)) {
            return ResponseEntity.status(status).body(new Error("Bad request from body", 1));
        }

        return super.error(request);
    }
}
