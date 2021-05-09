package com.acme.caas.domain.errors;

import com.acme.caas.web.rest.errors.ErrorConstants;
import com.redislabs.modules.rejson.Path;
import org.zalando.problem.Status;

public class MustExistException extends IllegalArgumentException{

    public String redisKey;

    public MustExistException(String redisKey, Path path, Throwable cause) {
        super("The redis key [" + redisKey + "] must exist at path: " + path.toString(), cause);
        this.redisKey = redisKey;
    }

    public MustExistException(String redisKey, Throwable cause) {
        super("The redis key [" + redisKey + "] must exist at the root", cause);
        this.redisKey = redisKey;
    }
}
