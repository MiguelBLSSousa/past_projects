-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : controlUnit.vhd
--
-- Related File(s)  : 


LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY controlUnit IS
    PORT (
	Clk : IN std_logic;
	Reset : IN std_logic; -- low active
	Branch_Conditions : IN std_logic_vector(3 DOWNTO 0); -- branch conditions bits from ALU
	M_Rd : OUT std_logic; -- Memory Read
	M_Wr : OUT std_logic; -- Memory Write
	R_mv : OUT std_logic; -- Register Move
	operation_value : OUT std_logic_vector(3 DOWNTO 0); -- !!! shouldnt this be 3 downto 0?
	sel_A : OUT std_logic_vector(5 DOWNTO 0); -- !!! was 15 downto 0, but i think it should be
	sel_B : OUT std_logic_vector(5 DOWNTO 0); -- !!! ^^^
	sel_D : OUT std_logic_vector(1 DOWNTO 0)--bits to select rd/wr main memory registers
	
      );
END ENTITY controlUnit;

ARCHITECTURE bhv OF controlUnit IS

BEGIN

PROCESS(clk, reset)

BEGIN


END PROCESS;
END;
