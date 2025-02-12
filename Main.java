import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try{
            String firstName = "", lastName = "", address = "", city = "", state = "", age = "";   
            boolean inString = false; //flag to notify if parsing within an active string
            List<Person> people = new ArrayList<>(); //stores each person's information from the input
            Map<String, Integer> addressCountMap = new HashMap<>(); //to identify the number of occupants

            FileReader inputFile = new FileReader("input_file.txt");
            FileWriter outputSorted = new FileWriter("output_sorted.txt");
            
            Integer commaCount=0; //allocating data to the correct variable
            int i;
            while((i = inputFile.read()) != -1){ //-1 is returned at EOF
                char ch = (char)i;

                if(ch == '"'){
                    inString = !inString;
                }
                else if(ch == ',' && !inString){
                    commaCount+=1; //only count commas outside of quotes
                }
                else if(ch==',' && inString){ 
                    continue; //ignore commas inside strings
                }
                else if(commaCount == 0){
                    firstName += ch;
                }
                else if(commaCount == 1){
                    lastName += ch;
                }
                else if(commaCount == 2){
                    address += ch;

                }
                else if(commaCount == 3){
                    city += ch;
                }
                else if(commaCount == 4){
                    state += ch;
                }
                else if(ch == '\n'){ //end of line, clean and input the data
                    commaCount = 0;
                    age = age.trim().replace("\"", "");
                    address = address.trim().replace(".","").toUpperCase();
                    city = city.trim().toUpperCase();
                    state = state.trim().toUpperCase();
                    String fullAddress = address + ", " + city + ", " + state;
                    if((address.charAt(address.length()-1)) == ' '){
                        address = address.substring(0,address.length()-2);
                    }
                    //add person to people list
                    people.add(new Person(firstName, lastName, address, city, state, age));
                     //add the address to the map with it's value being the # of occupants
                    addressCountMap.put(fullAddress, addressCountMap.getOrDefault(fullAddress,0)+1);
                    
                    //reset the variables
                    firstName="";
                    lastName="";
                    address="";
                    city="";
                    state="";
                    age="";
                }
                else if(commaCount == 5){
                    age += ch;
                }
            }
            inputFile.close();

            //uses the address map to output the address and # of occupants
            for(Map.Entry<String,Integer> entry : addressCountMap.entrySet()){
                String outputLine = entry.getKey() + ", occupants: " + entry.getValue() + "\n";
                outputSorted.write(outputLine);
            }
            outputSorted.write("--------------------------------\n");
            //sorts the people array by last name then first name
            people.sort(Comparator.comparing(Person::getLastName).thenComparing(Person::getFirstName));
            //outputs the information of everybody 18 years or older
            for(Person p : people){
                if(p.getAge()>=18){
                    String outputLine = p.getFirstName() + ", " + p.getLastName() + ", " + p.getAddress() + ", " + p.getCity() + ", " + p.getState() + ", " + String.valueOf(p.getAge()) + "\n";
                    outputSorted.write(outputLine);
                }
            }
            outputSorted.close();

        }
        catch (IOException e) {
            System.out.println("There are some IOException");
        }
    }
}
