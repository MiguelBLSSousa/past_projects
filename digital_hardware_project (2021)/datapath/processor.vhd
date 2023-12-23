-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : processor.vhd
--
-- Related File(s)  : 


LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

ENTITY processor IS
    PORT (
        Clk : IN std_logic;
        Reset : IN std_logic

      );
END ENTITY processor;

ARCHITECTURE structure OF processor IS

COMPONENT controlUnit IS
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
  END COMPONENT controlUnit;

COMPONENT dataPath IS
    PORT (
	Clk : IN std_logic;
	Reset : IN std_logic; -- low active
	Branch_Conditions : OUT std_logic_vector(3 DOWNTO 0); -- branch conditions bits from ALU
	M_Rd : IN std_logic; -- Memory Read
	M_Wr : IN std_logic; -- Memory Write
	R_mv : IN std_logic; -- Register Move
	operation_value : IN std_logic_vector(3 DOWNTO 0); --
	sel_A : IN std_logic_vector(5 DOWNTO 0); -- !!! was 15 downto 0, but i think it should be
	sel_B : IN std_logic_vector(5 DOWNTO 0); -- !!! ^^^
	sel_D : IN std_logic_vector(1 DOWNTO 0)--bits to select rd/wr main memory registers
	
    );
  END COMPONENT dataPath;

	SIGNAL sel_A, sel_B : std_logic_vector(5 DOWNTO 0);
	SIGNAL sel_D : std_logic_vector(1 DOWNTO 0);
	SIGNAL operation_value, Branch_Conditions : std_logic_vector(3 DOWNTO 0);
	SIGNAL M_Rd, M_Wr, R_mv : std_logic;

BEGIN

 ctrl: controlUnit
    PORT MAP(
	Clk => Clk,
	Reset => Reset, -- low active
	Branch_Conditions => Branch_Conditions, -- branch conditions bits from ALU
	M_Rd => M_Rd, -- Memory Read
	M_Wr => M_Wr, -- Memory Write
	R_mv => R_mv, -- Register Move
	operation_value => operation_value,
	sel_A => sel_A,
	sel_B => sel_B,
	sel_D => sel_D --bits to select rd/wr main memory registers 
	
    );


END ARCHITECTURE structure;
