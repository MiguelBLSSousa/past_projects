-- Project          : VHDL Hybrid accumulator-2operand machine
--                    Digital Hardware by group 
-- 
-- File             : temp_.vhd
--
-- Related File(s)  : 
--%r0		- %00 always 0 register
--%r59-r%62	- %wr/rd main memory write/read registers
--%r63		- %rd standard destination register from ALU

LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;

--GENERATE COMMAND IS MISSING!!!!!!!!!!!!!!!!!!!!!

ENTITY RegisterFile IS
	PORT (
		Clk : IN std_logic;
		Reset : IN std_logic;
		temp_output1 : OUT std_logic_vector(15 DOWNTO 0); --bus to rs1 --Bus_A
		temp_sel_output1 : IN std_logic_vector(5 DOWNTO 0);--sel_A
		temp_output2 : OUT std_logic_vector(15 DOWNTO 0); --bus to rs2 --Bus_B
		temp_sel_output2 : IN std_logic_vector(5 DOWNTO 0);--sel_B
		temp_ALU_output : IN std_logic_vector(15 DOWNTO 0);--alu output --Bus_C
		temp_Memory_bus_OUT : IN std_logic_vector(15 DOWNTO 0);--read from memory --Bus_D
		temp_Memory_bus_IN : OUT std_logic_vector(15 DOWNTO 0);--write to memory --Bus_A(bus to main memory)
		temp_sel_Memory_bus : IN std_logic_vector(1 DOWNTO 0);--4 register selector --sel_D
		temp_move : IN std_logic; --when 1 move command is done
		temp_Rd_M : IN std_logic; --when 1 main memory bus is read
		temp_Wr_M : IN std_logic  --when 1 main memory bus is written(Bus_A)
      );
END ENTITY RegisterFile;

ARCHITECTURE bhv OF RegisterFile IS
	TYPE register_file_type IS ARRAY (63 DOWNTO 0) OF std_logic_vector(15 DOWNTO 0);--create register file data type
	SIGNAL register_file : register_file_type :=(OTHERS=>(OTHERS=>'0'));--set every adress and every value in adress to 0
BEGIN

PROCESS(clk, reset)

BEGIN
	IF reset = '0' THEN
		--loop to reset all registers to 0
		
		register_file <= (OTHERS=>(OTHERS=>'0'));
		

	ELSIF falling_edge(clk) THEN
		register_file(0) <= (OTHERS=>'0'); --%r0 constant zero

		IF temp_move = '1' THEN
			--move command
			register_file(to_integer(unsigned(temp_sel_output2))) <= register_file(to_integer(unsigned(temp_sel_output1)));
		ELSIF temp_Rd_M = '1' THEN --make sure timing is correct with memory
			--output of memory into register(59-62)
			register_file(59+to_integer(unsigned(temp_sel_output2))) <= temp_Memory_bus_OUT;
		ELSIF temp_Wr_M = '1' THEN --make sure timing is correct with memory
			--input to memory from register(59-62)
			temp_Memory_bus_IN <= register_file(59+to_integer(unsigned(temp_sel_output2)));
		ELSE
			--read Bus_C
			register_file(63) <= temp_ALU_output;
		END IF;
	END IF;
END PROCESS;
--set to Bus_A and Bus_B BUT NOT Bu_A for main memory!!!
temp_output1 <= register_file(to_integer(unsigned(temp_sel_output1)));
temp_output2 <= register_file(to_integer(unsigned(temp_sel_output2)));
END;
