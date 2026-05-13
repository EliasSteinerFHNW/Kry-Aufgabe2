import java.util.ArrayList;
import java.util.List;


public class Raibow {
    


  public static void main(String[] args)
    {     String initalPwd = "0000000";
      List<String> initalPWList = new ArrayList<>();
     initalPWList.add(initalPwd);
      for (int i = 0; i < 8899000; i++) {
            
            
            char prev = initalPwd.charAt(initalPwd.length() - 1);
            char[] prevPWD = initalPwd.toCharArray();
            if (prev=='z'){
              int index = initalPwd.length() - 1;
              while (initalPwd.charAt(index)=='z'){
                prevPWD[index] = 'a';
                prevPWD[index-1] = increment (initalPwd.charAt(index-1));
                initalPwd= String.valueOf(prevPWD);
                index --;
              }
            }else{
            prevPWD[initalPwd.length() - 1] = increment (prev);
            initalPwd= String.valueOf(prevPWD);
            }
           initalPWList.add(initalPwd);
      }
    }
  
  public static char increment(char prevChar){

  if (Character.isLetter(prevChar)){
      if (prevChar == 'z'){
        return'0';
      }
      else{
       return ((char) (prevChar + 1));
      }
    }
  else if (Character.isDigit(prevChar)){
    if (Character.getNumericValue(prevChar) == 9){
     return 'a';
    }else{
     int i = Character.getNumericValue(prevChar)+1;
     return  String.valueOf(i).charAt(0);
  }}
else{
  throw new IllegalArgumentException ("no correct char entered");
}

  }
}




