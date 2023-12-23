LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;


ENTITY debug_portmap IS
PORT (
	RESET : IN std_logic; 
	Clk   : IN std_logic;
	dig0 : OUT std_logic_vector(6 DOWNTO 0);
	dig1 : OUT std_logic_vector(6 DOWNTO 0); 
	dig2 : OUT std_logic_vector(6 DOWNTO 0); 
	dig3 : OUT std_logic_vector(6 DOWNTO 0); 
	dig4 : OUT std_logic_vector(6 DOWNTO 0); 
	dig5 : OUT std_logic_vector(6 DOWNTO 0); 
	switch : IN std_logic_vector(9 DOWNTO 0); 
	led_switch : OUT std_logic_vector(9 DOWNTO 0); 
	button : IN std_logic_vector(3 DOWNTO 1) 
);
END debug_portmap;

Architecture structure of debug_portmap is


COMPONENT Debug IS 
PORT (
    Debug_data : IN std_logic_vector(15 DOWNTO 0);
	micro_addr : OUT  integer range 0 to 511;
	cont_micro_addr : IN std_logic_vector(15 DOWNTO 0); 
	RESET : IN std_logic; 
	branch_conditions : IN std_logic_vector(3 DOWNTO 0);
	Debug_addr_REG : OUT std_logic_vector(5 DOWNTO 0);
	Debug_data_REG : IN std_logic_vector(15 DOWNTO 0);
	dig0 : OUT std_logic_vector(6 DOWNTO 0);
	dig1 : OUT std_logic_vector(6 DOWNTO 0); 
	dig2 : OUT std_logic_vector(6 DOWNTO 0); 
	dig3 : OUT std_logic_vector(6 DOWNTO 0); 
	dig4 : OUT std_logic_vector(6 DOWNTO 0); 
	dig5 : OUT std_logic_vector(6 DOWNTO 0); 
	Debug_addr : OUT std_logic_vector(11 DOWNTO 0);
	switch : IN std_logic_vector(9 DOWNTO 0); 
	led_switch : OUT std_logic_vector(9 DOWNTO 0); 
	button : IN std_logic_vector(3 DOWNTO 1) 
    );
  END COMPONENT Debug;
  
COMPONENT MainMemory IS 
PORT (
	Clk   : IN std_logic;
	Reset : IN std_logic; -- low active
	Debug_addr : IN std_logic_vector(11 DOWNTO 0);
	bus_A : IN std_logic_vector(15 DOWNTO 0); --Bus A first 12 bits is memory adress
	button : IN std_logic_vector(3 DOWNTO 1);
	Bus_B : IN std_logic_vector(15 DOWNTO 0); --Bus B data going into mainMemory (DATA IN with respect to main memory)
	Bus_D : OUT std_logic_vector(15 DOWNTO 0); --Bus D from memory (DATA OUT with respect to main memory)
	M_Rd : IN std_logic; -- Memory Read
	M_Wr : IN std_logic; -- Memory Write
	Debug_data : OUT std_logic_vector(15 DOWNTO 0)
	--sel_D : IN std_logic_vector(1 DOWNTO 0)--bits to select rd/wr main memory registers
	
      );
END COMPONENT mainMemory; 

COMPONENT Microstore IS 
PORT ( pc : IN integer  range 0 to 511;
		micro_addr : IN integer range 0 to 511;
		button : IN std_logic_vector(3 DOWNTO 1);
		cont_micro_addr : OUT std_logic_vector(15 DOWNTO 0); 
        microcode : OUT std_logic_vector(15 DOWNTO 0));
END COMPONENT Microstore;

COMPONENT RegisterFile IS 
PORT (
		Clk : IN std_logic;
		Reset : IN std_logic;
		Debug_addr_REG : IN std_logic_vector(5 DOWNTO 0);
		Debug_data_REG : OUT std_logic_vector(15 DOWNTO 0); 
		button : IN std_logic_vector(3 DOWNTO 1);
		Bus_A : OUT std_logic_vector(15 DOWNTO 0); --bus to rs1 --Bus_A
		Sel_A : IN std_logic_vector(5 DOWNTO 0);--sel_A
		Bus_B : OUT std_logic_vector(15 DOWNTO 0); --bus to rs2 --Bus_B
		Sel_B : IN std_logic_vector(5 DOWNTO 0);--sel_B
		Bus_C : IN std_logic_vector(15 DOWNTO 0);--alu output --Bus_C
		Bus_D : IN std_logic_vector(15 DOWNTO 0);--read from memory --Bus_D
		I_Index : IN integer;
		IR_Bit: OUT std_logic
      );
END COMPONENT RegisterFile;

COMPONENT ALU IS 
PORT (
	Clk     : IN std_logic;
	Reset : IN std_logic; -- low active
	Bus_A : IN std_logic_vector(15 DOWNTO 0); --Bus A from registerFile
	Bus_B : IN std_logic_vector(15 DOWNTO 0); --Bus B from registerFile
	opvalue : IN std_logic_vector(2 DOWNTO 0); -- op code for ALU
	Bus_C : OUT std_logic_vector(15 DOWNTO 0); --Bus C to registerFile
	Branch_Conditions : OUT std_logic_vector(3 DOWNTO 0) -- branch conditions bits from ALU
      );
END COMPONENT ALU;

SIGNAL Debug_addr : std_logic_vector(11 DOWNTO 0);
SIGNAL Debug_data_REG : std_logic_vector(15 DOWNTO 0);
SIGNAL Debug_addr_REG : std_logic_vector(5 DOWNTO 0);
SIGNAL branch_conditions : std_logic_vector(3 DOWNTO 0);
SIGNAL cont_micro_addr : std_logic_vector(15 DOWNTO 0);
SIGNAL micro_addr : integer range 0 to 511; 
SIGNAL Debug_data : std_logic_vector(15 DOWNTO 0);
SIGNAL bus_A : std_logic_vector(15 DOWNTO 0);
SIGNAL Bus_B : std_logic_vector(15 DOWNTO 0);
SIGNAL Bus_D : std_logic_vector(15 DOWNTO 0);
SIGNAL M_Rd : std_logic;
SIGNAL M_Wr : std_logic;
SIGNAL pc : integer range 0 to 511;
SIGNAL microcode : std_logic_vector(15 DOWNTO 0);
SIGNAL Sel_A : std_logic_vector(5 DOWNTO 0);
SIGNAL Sel_B : std_logic_vector(5 DOWNTO 0);
SIGNAL Bus_C :  std_logic_vector(15 DOWNTO 0);
SIGNAL I_Index : integer;
SIGNAL IR_Bit : std_logic;
SIGNAL opvalue : std_logic_vector(2 DOWNTO 0);
BEGIN

  dbg: Debug
    PORT MAP (
      Debug_data    => Debug_data,
      micro_addr => micro_addr,
      cont_micro_addr   => cont_micro_addr,
      RESET=> RESET,
      branch_conditions      => branch_conditions,
      Debug_addr_REG   => Debug_addr_REG,    
      Debug_data_REG      => Debug_data_REG,
      dig0   => dig0,
      dig1      => dig1,
      dig2   => dig2,
      dig3  => dig3,
      dig4      => dig4,
      dig5 => dig5,
      Debug_addr    => Debug_addr,
      switch     => switch,
      led_switch    => led_switch,
      button  => button  
    );
  
  MM: MainMemory
    PORT MAP (
      Clk   => Clk,
      Reset => RESET,
      Debug_addr     => Debug_addr,
      bus_A  => bus_A,  
	  button => button,
      Bus_B     => Bus_B,
      Bus_D => Bus_D,
      M_Rd     => M_Rd,
      M_Wr  => M_Wr,
      Debug_data  => Debug_data
    );

  Ms: Microstore
    PORT MAP (
      pc    => pc,
      micro_addr => micro_addr,
	  button => button,
      cont_micro_addr   => cont_micro_addr,
      microcode=> microcode
    );
  
  RF: RegisterFile
    PORT MAP (
      Clk   => Clk,
      Reset => RESET,
      Debug_addr_REG     => Debug_addr_REG,
      Debug_data_REG  => Debug_data_REG,    
      button     => button,
      Bus_A  => Bus_A,
      Sel_A     => Sel_A,
      Bus_B  => Bus_B,
      Sel_B  => Sel_B,
      Bus_C     => Bus_C,
      Bus_D => Bus_D,
      I_Index    => I_Index,
      IR_Bit     => IR_Bit
    );
	
  cpu: ALU
	PORT MAP (
		Clk   => Clk,
		Reset => RESET,
		Bus_A => Bus_A,
		Bus_B => Bus_B,
		opvalue => opvalue,
		Bus_C => Bus_C,
		Branch_Conditions => branch_conditions 
    );
END ARCHITECTURE structure;
