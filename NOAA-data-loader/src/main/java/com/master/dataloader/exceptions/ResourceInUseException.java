package com.master.dataloader.exceptions;

public class ResourceInUseException extends RuntimeException{
    public ResourceInUseException(String resourceName, String resourceId) {
        super(String.format("Resource %s with id %s cannot be deleted because it is used by other resources", resourceName, resourceId));
    }
}
