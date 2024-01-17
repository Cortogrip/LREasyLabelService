/*----------------------------------------------------------------------------------------------------------------------
 * LR Easy Label Print © 2024 by l Simonnet is licensed under Attribution-NonCommercial-NoDerivatives 4.0 International
 *
 * LR Easy Label Print use a deployed service to print labels.
 * See http://localhost:8087/getting_started.html or http://auborddesmauves.fr/products/lrplugins for documentation.
 * ------------------------------------------------------------------------------------------------------------------ */
package com.abdm.imageshop.label.label;

// Imports
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * LabelService
 */
@Slf4j
@Service
public class LabelService {

  public final static String CODE    = "code";
  public final static String STATUS  = "status";
  public final static String MESSAGE = "message";
  public final static String DETAIL  = "detail";

  public final static String OK     = "OK";
  public final static String KO     = "KO";

  // The horizontal reference (from page left)
  @Value("${base.x:30}")
  Integer baseX;

  // The vertical reference (from page bottom)
  @Value("${base.y:15}")
  Integer baseY;

  @Value("${offset.x:200}")
  Integer offsetX;

  @Value("${offset.y:90}")
  Integer offsetY;

  @Value("${lineHeight:10}")
  Integer lineHeight;

  @Value("${fontSize:8}")
  Integer fontSize;


  @Value("${outputDir:}")
  String outputDir;

  /**
   *
   * @param labels
   * @param fileName
   */
  public Map<String, String> printLabels(List <Label> labels, String fileName){

    Map<String, String> serviceState = new HashMap();

    serviceState.put(CODE, "99");
    serviceState.put(STATUS, "");
    serviceState.put(MESSAGE, "");
    serviceState.put(DETAIL, "");


    try (PDDocument doc = new PDDocument()) {

      PDPage page = new PDPage();
      doc.addPage(page);
      PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

      try (PDPageContentStream contents = new PDPageContentStream(doc, page))
      {
        contents.beginText();
        contents.setFont(font, fontSize);

        for (Label label:labels) {
          printLabel(contents, label.getxPos(), label.getyPos(), label.getText());
        }
        contents.endText();
      }

      File outFolder = new File(outputDir);
      if(!outFolder.exists()){
        log.info("Creating {}", outFolder.getAbsoluteFile());
        Files.createDirectory(outFolder.toPath());
        log.info("{} created !", outFolder.getAbsoluteFile());
      }


      doc.save(outputDir + fileName);
      String createdFile = outputDir + fileName;
      String currentDirectory = new File("").getAbsolutePath();
      log.info("{} created in {} ", createdFile, currentDirectory);


      serviceState.put(CODE, "1");
      serviceState.put(STATUS, OK);
      serviceState.put(MESSAGE, fileName );

      String normalized1 = FilenameUtils.normalize(currentDirectory + "/" + createdFile);
      String normalized2 = FilenameUtils.separatorsToUnix(normalized1);

      serviceState.put(DETAIL, normalized2);

    } catch (IOException e) {

      log.warn("Cannot write page [" + e + "]");
      serviceState.put(CODE, "-1");
      serviceState.put(STATUS, KO);
      serviceState.put(MESSAGE, "Cannot write page [" + e + "]");
      serviceState.put(DETAIL, "");
    }

    return serviceState;
  }


  /**
   *
   * @param contents
   * @param xPos
   * @param yPos
   * @param text
   * @throws IOException
   */
  private void printLabel(PDPageContentStream contents, int xPos, int yPos, String text) throws IOException {

    List<String> lines = Arrays.asList( text.split("\n"));
    Collections.reverse(lines);  // Fist line is the bottom one

    int xLabelBase = baseX + (xPos * offsetX);
    int yLabelBase = baseY + (yPos * offsetY);

    contents.newLineAtOffset(xLabelBase, yLabelBase);
    contents.showText(lines.get(0));
    for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
      contents.newLineAtOffset(0, lineHeight);
      contents.showText(lines.get(lineIndex));
    }

    // Reset offset
    for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
      contents.newLineAtOffset(0, -lineHeight);
    }
    contents.newLineAtOffset(-xLabelBase, -yLabelBase); // Reset offset

  }

  /**
   *
   * @param outputDir
   * @param fileName
   */
  private String getOuputFile(String outputDir, String fileName){

    String currentDirectory = new File("").getAbsolutePath();
    currentDirectory = currentDirectory.replaceAll("\\\\", "/");

    // String separator = System.getProperty("file.separator");
    String separator = "/";
    String result = currentDirectory + separator;

    if (outputDir.charAt(0)=='.'){
      result = result + outputDir.substring(outputDir.indexOf('/'));
    } else {
      result = result + outputDir;
    }

    result = result + fileName;
    result = result.replaceAll("//", "/");

    return result ;
  }
}
