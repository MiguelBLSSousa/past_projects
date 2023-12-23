-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : registerFile.vhd
--
-- Related File(s)  : 


LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY registerFile IS
    PORT (
	Clk     : IN std_logic;
	Reset : IN std_logic; -- low active
	sel_A : IN std_logic_vector(5 DOWNTO 0); --register adress for bus A
	sel_B : IN std_logic_vector(5 DOWNTO 0); --register adress for bus B
	sel_D : IN std_logic_vector(1 DOWNTO 0);--bits to select rd/wr main memory registers
	Bus_A : OUT std_logic_vector(15 DOWNTO 0); --Bus A going into ALU
	Bus_B : OUT std_logic_vector(15 DOWNTO 0); --Bus B going into ALU
	Bus_C : IN std_logic_vector(15 DOWNTO 0); --Bus C from ALU result
	Bus_D : IN std_logic_vector(15 DOWNTO 0); --Bus D from memory
	M_Rd : IN std_logic; -- Memory Read
	M_Wr : IN std_logic; -- Memory Write
	R_mv : IN std_logic -- Register Move
	
      );
END ENTITY registerFile;

ARCHITECTURE bhv OF registerFile IS

BEGIN

PROCESS(clk, reset)

BEGIN


END PROCESS;
END;
