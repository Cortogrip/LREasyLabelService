/*----------------------------------------------------------------------------------------------------------------------
 * Copyright 2022 - Sopra Steria
 * NOTICE:  All information  contained herein is, and  remains  the property  of Soptra Steria  and  its suppliers,  if
 * any.  The  intellectual  and technical  concepts contained herein are proprietary to Soptra Steria and its suppliers
 * and may  be covered  by European  and  Foreign Patents, and are protected  by trade secret or copyright law.
 * Dissemination  of  this information  or reproduction of  this  material is strictly forbidden unless  prior  written
 * permission  is  obtained   from Sopra Steria.
 * ---------------------------------------------------------------------------------------------------------------------
 * Created by Sopra Steria on 29/12/2022
 *
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label.configuration;

// Imports
import lombok.Data;

/**
 * Retry
 */
@Data
public class Retry {

    /**
     * Maximum number.
     */
    private int nbMax;

    /**
     * timeout.
     */
    private Long timeoutInMs;

}
