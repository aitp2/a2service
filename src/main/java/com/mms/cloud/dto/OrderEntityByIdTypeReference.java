package com.mms.cloud.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mms.cloud.document.response.GetByIdResponse;

/**
 * Type reference required to make use of Jackson and nested entities.
 */
public class OrderEntityByIdTypeReference extends TypeReference<GetByIdResponse<OrderEntity>>{
}
