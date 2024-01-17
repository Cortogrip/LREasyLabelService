/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label.label;

// Imports

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * LabelPage
 */
public class LabelPage {

  @JsonProperty(value = "file_name", required = false)
  protected String fileName;

  @JsonProperty("labels")
  protected List<Label> labels;


}
