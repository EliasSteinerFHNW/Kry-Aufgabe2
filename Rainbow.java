package programmieraufgabe2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Rainbow {
    
  private final static int TABLE_LENGTH = 2000;
  private final static int TABLE_DEPTH = 2000;

  
static ArrayList<String> startValues = new ArrayList<>();

static ArrayList<String> endValues = new ArrayList<>();


  public static void main(String[] args)
    { 
    
      // genereates HAshtable absed on starting PWD
     generateHashTable("0000000");

      System.out.println(findPWD("d56a37fb6b08aa709fe90e12ca59e12"));
     

    }
  
  public static void generateHashTable(String initalPwd){
    // creates an arraylist of startvalues and corresponding endvalues in second arraylist
      for (int i = 0; i <= TABLE_LENGTH; i++) {
        startValues.add(initalPwd);
        endValues.add(generateEndHash(initalPwd));
        //this does increase the the  value by 1 in base 36 as a next startvalue
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
      }
  }



  public static String findPWD(String pwd){
  String init = pwd;
  int k =0;
  //check if the pwd value is found in the array containing all endvalues
  //if not hash and reduce the pwd  and check again untill 2000 steps are made
   while (!(endValues.contains(pwd)) && k <2000){
    pwd = getMd5(pwd);
    pwd = reduce(pwd, k);
    System.out.println("trying " + pwd + " at index " + k);

    // if current pwd is found in endvalue arraylist, get the corresponding startvalue and reduce + hash  startvalue untill the pwd is reached
    //return the last entry before as the correct password
    if (endValues.contains(pwd)) {
      System.out.println("pwd: "+ pwd + "was found in endtables");
        int j =0;
    String startvalue = startValues.get(endValues.indexOf(pwd));
    ArrayList<String> startValuesChain = new ArrayList<>();
    
    while (!startvalue.equals(pwd) ){
    startvalue = getMd5(startvalue);
    startvalue = reduce(startvalue, j);
    startValuesChain.add(startvalue);
    j++;

    }

    return "Password for Hash:  " +  init  +" is: " +startValuesChain.get(startValuesChain.size()-1);
    }
    k++;
   }
    String msg= "found no match for " + init +" in hashtable endvalues ";
    return msg;
  }
  
  

  public static String generateEndHash(String startString){
    String value = startString;

    for (int i =0; i<TABLE_DEPTH; i++){
      value = getMd5(value);
      value = reduce(value, i);
    } 

    return value;
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
  throw new IllegalArgumentException ("incorrect char entered");
}

  }




public static String reduce (String hash, int step){
  List<String> reducedHash = new ArrayList<>();
  int l = 7;

  // implements the reduce function based on the slides
  //converts the hex value to a natural number
BigInteger num = new BigInteger(hash, 16);
num = num.add(new BigInteger(Integer.toString(step)));
hash = num.toString(16);
for (int i = 1; i<=l;i++){
num = new BigInteger(hash, 16);

//bigint with radix 36 is used to output a base 36 number
BigInteger mod = num.remainder(BigInteger.valueOf(36));
BigInteger div = num.divide(BigInteger.valueOf(36));
reducedHash.add(mod.toString(36));
hash = div.toString(16);
}

//reverse the lsit to output the correct order of chars in string
ListIterator<String> iterator=reducedHash.listIterator(reducedHash.size());
 String rslt ="";
      while(iterator.hasPrevious())
      {
        rslt+=(iterator.previous());
      }
  return rslt;
}





public static String getMd5(String input)
    {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}

