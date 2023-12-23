-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : mainMemory.vhd
--
-- Related File(s)  : 


LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY mainMemory IS
    PORT (
	Clk     : IN std_logic;
	Reset : IN std_logic; -- low active
	Bus_A : IN std_logic_vector(15 DOWNTO 0); --Bus A first 12 bits is memory adress
	Bus_B : IN std_logic_vector(15 DOWNTO 0); --Bus B data going into mainMemory
	Bus_D : OUT std_logic_vector(15 DOWNTO 0); --Bus D from memory
	M_Rd : IN std_logic; -- Memory Read
	M_Wr : IN std_logic; -- Memory Write
	sel_D : IN std_logic_vector(1 DOWNTO 0)--bits to select rd/wr main memory registers
	
      );
END ENTITY mainMemory;

ARCHITECTURE bhv OF mainMemory IS

BEGIN

PROCESS(clk, reset)

BEGIN


END PROCESS;
END;
