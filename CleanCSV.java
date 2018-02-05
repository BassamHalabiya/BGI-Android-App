import java.io.*;
import java.util.*;

public class CleanCSV {
   
   public static void main(String[] args) throws FileNotFoundException {
      Scanner input = new Scanner(new File("Disease_results.csv"));
      PrintStream output = new PrintStream(new File("Cleaned_Data.csv"));
      replace(input, output);
   }

   public static void replace(Scanner input, PrintStream output) {
      while(input.hasNextLine()) {
         String line = input.nextLine();
         String cleanLine = "";
         cleanLine = line.replaceAll("\'", "");
         cleanLine = cleanLine.replaceAll("-", " ");
         cleanLine = cleanLine.replaceAll("/", " or ");
         if (cleanLine.contains("?")) {
            if (cleanLine.contains("Wolfram")) {
               cleanLine = cleanLine.replaceAll("\\?", " ");
            } else if (cleanLine.contains("poprotein")) {
               cleanLine = cleanLine.replaceAll("\\?", "beta ");
            }
         }
         /*if (line.contains("-")) {
            cleanLine = line.replaceAll("-", " ");
         }
         if (line.contains("/")) {
            cleanLine = line.replaceAll("/", " or ");
         }
         if (line.contains("\\?") && line.contains("Wolfram")) {
            cleanLine = line.replaceAll("\\?", " ");
         } else if (cleanLine.contains("\\?") && cleanLine.contains("poprotein")) {
            cleanLine = cleanLine.replaceAll("\\?", "beta ");
         }*/
         output.println(cleanLine);
      }
   }
}