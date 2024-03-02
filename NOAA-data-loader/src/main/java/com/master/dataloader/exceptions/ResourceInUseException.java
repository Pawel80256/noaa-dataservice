package com.master.dataloader.exceptions;

public class ResourceInUseException extends RuntimeException{
    public ResourceInUseException(String resourceName) {
        super(String.format("Resource %s cannot be deleted because it is used by other resources", resourceName));
    }
}
