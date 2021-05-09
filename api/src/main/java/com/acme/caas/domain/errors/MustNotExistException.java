package com.acme.caas.domain.errors;

import com.redislabs.modules.rejson.Path;

public class MustNotExistException extends IllegalArgumentException{

    public String redisKey;

    public MustNotExistException(String redisKey, Path path, Throwable cause) {
        super("The redis key [" + redisKey + "] must not exist at path: " + path.toString(), cause);
        this.redisKey = redisKey;
    }
}
