import java.io.*;
import java.util.*;

public class Split {
   
   public static void main(String[] args) throws FileNotFoundException {
      
      Scanner input = new Scanner(new File("Cleaned_Data.csv"));
      String[] names = new String[118];
      String[] results = new String[118];
      int index = 0;
      
      while(input.hasNextLine()) {
         
         String line = input.nextLine();
         String[] cells = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
         names[index] = cells[0];
         results[index] = cells[3];
         index++;
      
      }
      
      for(int i = 0; i < index; i++) {
         System.out.println(names[i]);
         System.out.println(results[i]);
      }
      
   }
   
}