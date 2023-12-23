#include <iostream>
#include <fstream>
#include <bits/stdc++.h>
#include <bitset>
#include <string>
#include <sstream>
#include <list>

using namespace std;
ofstream outFile;

///This code converts an .txt file with assembly into a .txt file in the format of microcode_bhv
///Look at the documentation line codes

string register2Bin(string& inputString){
        string registerString;
        stringstream ss;
        ss << inputString;
        int tempint;
        ss >> tempint;
        if(0>tempint ){
            cout<<"Register is out of scope (too low)\nPlease use register 0-63 for register  operations\n";
            registerString = "000000";
        }
        else if(tempint>63){
            cout<<"Register is out of scope(too high)\nPlease use register 0-63 for register  operations\n";
            registerString = "000000";
        }
        else{
        std::bitset<6> br(tempint);
        registerString = br.to_string<char,std::string::traits_type,std::string::allocator_type>();
        }
        return registerString;
}

string memRegister2Bin(string& inputString){
        std::string memRegisterString;
        stringstream ss;
        ss << inputString;
        int tempint;
        ss >> tempint;
        if(59>tempint ){
            cout<<"memRegister is out of scope (too low)\nPlease use register 59-62 for memory operations\n";
            memRegisterString = "00";
        }
        else if(tempint>62){
            cout<<"memRegister is out of scope(too high)\nPlease use register 59-62 for memory operations\n";
            memRegisterString = "00";
        }
        else{
        tempint = tempint -59;
        std::bitset<2> bmr(tempint);
        memRegisterString = bmr.to_string<char,std::string::traits_type,std::string::allocator_type>();
        }
        return memRegisterString;
}

string memory2Bin(string& inputString){
        string memoryString;
        stringstream ss;
        ss << inputString;
        int tempint;
        ss >> tempint;
        if(0>tempint ){
            cout<<"Memory Address is out of scope (too low)\n Please use address 0-4095 for memory operations\n";
            memoryString = "000000000000";
        }
        else if(tempint>4095){
            cout<<"Memory Address is out of scope(too high)\n Please use address 0-4095 for memory operations\n";
            memoryString = "000000000000";
        }
        else{
        std::bitset<12> bm(tempint);
        memoryString = bm.to_string<char,std::string::traits_type,std::string::allocator_type>();
        }
        return memoryString;
}

string constant2Bin(string& inputString){
        string constantString;
        stringstream ss;
        ss << inputString;
        int tempint;
        ss >> tempint;
        if(0>tempint ){
            cout<<"Memory Address is out of scope (too low)\n Please use address 0-2047 for memory operations\n";
            constantString = "00000000000";
        }
        else if(tempint>2047){
            cout<<"Memory Address is out of scope(too high)\n Please use address 0-2047 for memory operations\n";
            constantString = "00000000000";
        }
        else{
        std::bitset<11> bm(tempint);
        constantString = bm.to_string<char,std::string::traits_type,std::string::allocator_type>();
        }
        return constantString;
}


string parts2Bin(string &op, string &rs1, string &rs2){

        string MachineCode;
        string temp1;
        string temp2;

        if (op.compare("LOAD")== 0) {
            MachineCode="00";
            //rs1 => r address
            //rs2 => r rs/rd
            MachineCode = MachineCode + "00" + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("STORE")== 0) {
            MachineCode="01";
            //rs1 => r address
            //rs2 => r rs/rd
            MachineCode = MachineCode + "00" + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("MOVE")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1000";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("AND")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1001";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("OR")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1010";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("ADD")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1011";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("SFTL")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1100";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("SFTR")== 0) {
            //rs1 => rs1
            //rs2 => rs2
            MachineCode="1101";
            MachineCode = MachineCode + register2Bin(rs2) + register2Bin(rs1);
        }
        else if (op.compare("INV")== 0) {
            //rs1 => rs1
            //4 zeros
            MachineCode="111000";
            MachineCode = MachineCode + "0000" + register2Bin(rs1);
        }
        else if (op.compare("TCMP")== 0) {
            //rs1 => rs1
            //4 zeros
            MachineCode="111001";
            MachineCode = MachineCode + "0000" + register2Bin(rs1);
        }
        else if (op.compare("GEN")== 0) {
            //rs1 => constant
            MachineCode="11101";
            MachineCode = MachineCode + constant2Bin(rs1);
        }
        else if (op.compare("BA")== 0) {
            //rs1 => condition
            //rs2 => jump tag
            MachineCode="11110";
            MachineCode = MachineCode + "00000" + register2Bin(rs1);

        }
        else if (op.compare("BSB")== 0) {
            //rs1 => condition
            //rs2 => jump tag
            MachineCode="11111";
            if (rs1.compare("Z")){
                MachineCode = MachineCode + "00";

            }
            else if(rs1.compare("N")){
                MachineCode = MachineCode + "01";

            }
            else if(rs1.compare("V")){
                MachineCode = MachineCode + "10";

            }
            else if(rs1.compare("C")){
                MachineCode = MachineCode + "11";

            }
            else{
                cout<<"Branch condition does not exist\nPlease use Z, N, V and C"<<endl;
            }

            MachineCode = MachineCode + "000" + register2Bin(rs2);

        }
        else{
            cout<<"ERROR: op does not exist \n";
        }
        return MachineCode;
}

string Assembly2Bin(string &s){
    string outputString;
    vector<string> v;
    stringstream ss(s);

    while (ss.good()) {
        string substr;
        getline(ss, substr, ',');
        v.push_back(substr);

    }
    //for (size_t i = 0; i < v.size(); i++){cout<<v[i]<<endl;} //function to display all v[] components.
    outputString = parts2Bin(v[0],v[1],v[2]);
    return outputString;
}


int main()
{
    std::string content;
    std::string line;
    ifstream inFile;

    ///Fill in your local path to your source.txt and target.txt
    inFile.open("D:\\intelFPGA_lite\\18.1\\work\\processor\\Assembly Examples\\source.txt");
    outFile.open("D:\\intelFPGA_lite\\18.1\\work\\processor\\Assembly Examples\\target.txt");
    if (!inFile) {
        cout << "Unable to open input file"<<endl;
    }
    if (!outFile) {
        cout << "Unable to open output file"<<endl;
    }
    cout << "ARCHITECTURE behaviour OF microstore IS \n TYPE instr_mem IS ARRAY (0 TO 511) OF std_logic_vector(15 DOWNTO 0); \n CONSTANT instr_store : instr_mem := (OTHERS=>(OTHERS=>'0')); \n";
    outFile << "ARCHITECTURE behaviour OF microstore IS \n TYPE instr_mem IS ARRAY (0 TO 511) OF std_logic_vector(15 DOWNTO 0); \n CONSTANT instr_store : instr_mem := (OTHERS=>(OTHERS=>'0')); \n";


    int address = 0;
    string STRING;

        while (!inFile.eof()) {

        getline(inFile,STRING); // Saves the line in STRING.
        //Remove tabs, spaces and set all letters to uppercase:
        STRING.erase(std::remove(STRING.begin(), STRING.end(), '\t'), STRING.end());
        STRING.erase(std::remove(STRING.begin(), STRING.end(), ' '), STRING.end());
        transform(STRING.begin(), STRING.end(), STRING.begin(), ::toupper);

            if(!STRING.empty()){
                ///Remove comment below to display assembly code:
                //cout << STRING <<endl;
                cout << address << " => \""<< Assembly2Bin(STRING) << "\"," << endl;
                outFile << address << " => \""<< Assembly2Bin(STRING) << "\"," << endl;
            }
            else{

            }
            address++;
        }

    ///Change the integer to you total amount of addresses in microstore to fill up the whole microstore_bhv
    while (address < 512){
        cout<<address<<" => \"----------------\","<<endl;
        outFile<<address<<" => \"----------------\","<<endl;
        address++;
    }


    cout << "BEGIN \n microcode <= instr_store(pc); \n END ARCHITECTURE behaviour; \n";
    outFile << "BEGIN \n microcode <= instr_store(pc); \n END ARCHITECTURE behaviour; \n";
    inFile.close();
    outFile.close();

    return 0;
}

