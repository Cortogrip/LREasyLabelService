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
 * Label
 */
public class Label {

  @JsonProperty("xPos")
  protected int xPos;

  @JsonProperty("yPos")
  protected int yPos;

  @JsonProperty("lines")
  protected List<String> lines;

  /**
   * Add a text line to print
   * @param line
   */
  public void addLine(String line){
    lines.add(line);
  }

  /**
   * Get the message formatted for printing
   * @return
   */
  public String getText(){

    StringBuffer sb = new StringBuffer();
    for (String line:lines) {
      sb.append(line+ "\n");
    }

    return sb.toString();
  }

  public int getxPos() {
    return xPos;
  }

  public void setxPos(int xPos) {
    this.xPos = xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public void setyPos(int yPos) {
    this.yPos = yPos;
  }

  public List<String> getLines() {
    return lines;
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
  }
}
