library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.numeric_std.all;

entity Processor is
  	port (
    		clk: in std_logic;
		reset: in std_logic
		--microcode: in std_logic_vector (15 downto 0);
		--pc: out integer range 0 to 511
  	);
end entity Processor;

architecture structure of Processor is

component dataPath IS
  	PORT (
    		Clk : IN std_logic;
		Reset : IN std_logic;
		Sel_A : IN std_logic_vector(5 DOWNTO 0);--sel_A
		Sel_B : IN std_logic_vector(5 DOWNTO 0);--sel_B
		Bus_AMem : OUT std_logic_vector(15 downto 0);
		Bus_BMem : OUT std_logic_vector(15 downto 0);
		Bus_D : IN std_logic_vector(15 DOWNTO 0);--read from memory --Bus_D
		I_Index : IN integer;
		IR_Bit: OUT std_logic;
		opvalue : IN std_logic_vector(2 DOWNTO 0); -- op code for ALU
		Branch_Conditions : OUT std_logic_vector(3 DOWNTO 0) -- branch conditions bits from ALU
  	);
end component dataPath;

component Control is
  	port (
    		clk : in std_logic;
    		reset : in std_logic; 
    		microcode: in std_logic_vector (15 downto 0);
		opvalue: out std_logic_vector (2 downto 0);
		Sel_A: out std_logic_vector (5 downto 0);
		Sel_B: out std_logic_vector (5 downto 0);
		M_Rd: out std_logic;
		M_Wr: out std_logic;
		pc: out integer range 0 to 511;
		branch_conditions: in std_logic_vector(3 downto 0);
		IR_Bit: in std_logic;
		I_Index: out integer
 	 );
end component Control;

component mainMemory is
    port (
	Clk   : IN std_logic;
	Reset : IN std_logic; -- low active
	Bus_A : IN std_logic_vector(15 DOWNTO 0); --Bus A first 12 bits is memory adress
	Bus_B : IN std_logic_vector(15 DOWNTO 0); --Bus B data going into mainMemory (DATA IN with respect to main memory)
	Bus_D : OUT std_logic_vector(15 DOWNTO 0); --Bus D from memory (DATA OUT with respect to main memory)
	M_Rd : IN std_logic; -- Memory Read
	M_Wr : IN std_logic -- Memory Write
	
      );
end component mainMemory;

component microstore is
	port (pc : in integer range 0 to 511;
        microcode : out std_logic_vector(15 downto 0));
end component;

SIGNAL Sel_A, Sel_B: std_logic_vector (5 downto 0);
SIGNAL Bus_B, Bus_D, Bus_AMem, Bus_BMem, microcode: std_logic_vector (15 downto 0);
SIGNAL opvalue: std_logic_vector (2 downto 0);
SIGNAL branch_conditions: std_logic_vector (3 downto 0);
SIGNAL I_Index: integer;
SIGNAL pc: integer range 0 to 511;
SIGNAL ready, M_Rd, M_Wr, IR_Bit: std_logic;

begin
  
DP: datapath port map(Clk, Reset, Sel_A, Sel_B, Bus_AMem, Bus_BMem, Bus_D, I_Index, IR_Bit, opvalue, branch_conditions);
CU: control port map(clk, reset, microcode, opvalue, Sel_A, Sel_B, M_Rd, M_Wr, pc, branch_conditions, IR_Bit, I_Index);
MM: mainmemory port map(Clk, Reset, Bus_AMem, Bus_BMem, Bus_D, M_Rd, M_Wr);
MS: microstore port map(pc, microcode);

END ARCHITECTURE structure;

