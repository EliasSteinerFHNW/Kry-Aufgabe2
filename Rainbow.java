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
      // generates HAshtable based on starting PWD and TableLength
    generateHashTable("0000000");
      // tries to find password matching provided hash in hashtable
    System.out.println(findPWD("1d56a37fb6b08aa709fe90e12ca59e12"));
    }
  
  public static void generateHashTable(String initalPwd){
    // creates an arraylist of startvalues and corresponding endvalues in second arraylist
      for (int i = 0; i < TABLE_LENGTH; i++) {
        //.out.println("Generating Hashtable: "+ i + " out of "+ TABLE_LENGTH);
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
      System.out.println("Hashtable generated");
  }


public static String findPWD(String hash) {
    String initHash = hash;
    
    // 1. Walk backward through possible columns (from last column to first)
    for (int i = 0; i < TABLE_DEPTH; i++) {
        String currentHash = initHash;
        String reduced = "";
        
        // Compute starting step for the look-ahead sequence
        int startStep = TABLE_DEPTH - 1 - i; 
        
        // Complete the chain from the assumed step to the very end
        for (int k = startStep; k < TABLE_DEPTH; k++) {
            reduced = reduce(currentHash, k);
            currentHash = getMd5(reduced);
        }

        // Check if this look-ahead endpoint matches a stored end value
        if (endValues.contains(reduced)) {
            int chainIndex = endValues.indexOf(reduced);
            String currentPWD = startValues.get(chainIndex);
            
            // 2. Reconstruct the chain forward from the matching Start Value
            for (int k = 0; k < TABLE_DEPTH; k++) {
                String hashedPWD = getMd5(currentPWD);
                
                // If the hashes match, we successfully cracked it!
                if (hashedPWD.equals(initHash)) {
                    return "Password for Hash: " + initHash + " is: " + currentPWD;
                }
                
                // Move to the next link in the chain
                currentPWD = reduce(hashedPWD, k);
            }
        }
    }

    return "Could not find password in hashtable for Hash: " + initHash;
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

// ensure its always 7 chars long
      String finalResult = rslt;
  while (finalResult.length() < l) {
    finalResult = "0" + finalResult;
  }
  return finalResult;
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

