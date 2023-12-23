LIBRARY IEEE;
USE IEEE.std_logic_1164.ALL;
USE IEEE.numeric_std.ALL;


ENTITY Debug IS
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
END Debug;

ARCHITECTURE bhv of Debug IS
BEGIN
	

	PROCESS(RESET, button, switch)
	  FUNCTION SLV2hex (inp : std_logic_vector) RETURN character IS
    CONSTANT inp_a : std_logic_vector(3 DOWNTO 0) := inp;
  BEGIN
    CASE inp_a IS
      WHEN "0000" => RETURN '0';
      WHEN "0001" => RETURN '1';        
      WHEN "0010" => RETURN '2';
      WHEN "0011" => RETURN '3';
      WHEN "0100" => RETURN '4';
      WHEN "0101" => RETURN '5';
      WHEN "0110" => RETURN '6';
      WHEN "0111" => RETURN '7';
      WHEN "1000" => RETURN '8';
      WHEN "1001" => RETURN '9';        
      WHEN "1010" => RETURN 'A';
      WHEN "1011" => RETURN 'B';
      WHEN "1100" => RETURN 'C';
      WHEN "1101" => RETURN 'D';        
      WHEN "1110" => RETURN 'E';
      WHEN "1111" => RETURN 'F';                        
      WHEN OTHERS => RETURN '0'; REPORT "unexpected string in SLV2hex" SEVERITY warning;
    END CASE;  
  END SLV2hex;
	
	FUNCTION HEX2disp(char : character) RETURN std_logic_vector IS 
	 CONSTANT inp_b : character := char; 
	BEGIN
	 CASE inp_b IS 
		WHEN '0' => RETURN "1000000";
		WHEN '1' => RETURN "1111001";
		WHEN '2' => RETURN "0100100";
		WHEN '3' => RETURN "0110000";
		WHEN '4' => RETURN "0011001";
		WHEN '5' => RETURN "0010010";
		WHEN '6' => RETURN "0000010";
		WHEN '7' => RETURN "1111000";
		WHEN '8' => RETURN "0000000";
		WHEN '9' => RETURN "0010000";
		WHEN 'A' => RETURN "0001000";
		WHEN 'B' => RETURN "0000011";
		WHEN 'C' => RETURN "1000110";
		WHEN 'D' => RETURN "0100001";
		WHEN 'E' => RETURN "0000110";
		WHEN 'F' => RETURN "0001110"; 
		when others => Return "0000000";
	  END CASE;
	 END HEX2disp;
		
	VARIABLE address_R : integer := 0;
	VARIABLE address_M : integer := 0;	
	VARIABLE all_digits : integer; 
	VARIABLE bin_addr_M0 : std_logic_vector(12 DOWNTO 0); 
	VARIABLE bin_addr_M1 : std_logic_vector(12 DOWNTO 0); 
	VARIABLE bin_micro_addr : std_logic_vector(11 DOWNTO 0);
	VARIABLE var_debug_addr : std_logic_vector(11 DOWNTO 0);
	VARIABLE Var_debug_addr_REG : std_logic_vector(5 DOWNTO 0);
	VARIABLE var_micro_addr : integer range 0 to 511;
		BEGIN
		
		IF RESET='0' THEN
		Debug_addr <= (others => '0');
		Debug_addr_REG <= (others => '0');
		micro_addr <= 0;
		dig0 <= (others => '0');
		dig1 <= (others => '0');
		dig2 <= (others => '0');
		dig3 <= (others => '0');
		dig4 <= (others => '0');
		dig5 <= (others => '0');

		ELSIF switch(9)='1' THEN 
			CASE switch(3) IS
				WHEN '0' => all_digits:=1;
				WHEN '1' => all_digits:=8;
				when others => null;
			END CASE;
		
			CASE switch(4) IS 
				WHEN '0' => all_digits:=all_digits*1;
				WHEN '1' => all_digits:=all_digits*4;
				when others => null;
			END CASE;
			
			CASE switch(5) IS
				WHEN '0' => all_digits:=all_digits*1;
				WHEN '1' => all_digits:=all_digits*4;
				when others => null;
			END CASE;
			
			CASE switch(6) IS
				WHEN '0' => all_digits:=all_digits*1;
				WHEN '1' => all_digits:=all_digits*2;
				when others => null;
			END CASE;
			
			CASE switch(7) IS 
				WHEN '0' => all_digits:=all_digits*1;
				WHEN '1' => all_digits:=all_digits*2;
				when others => null;
			END CASE;
			
			CASE switch(8) IS 
				WHEN '0' => all_digits:=all_digits*1;
				WHEN '1' => all_digits:=all_digits*2;
				when others => null;
			END CASE;
			
			CASE switch(2 DOWNTO 1) IS 
				WHEN "00" =>
					IF falling_edge(button(1)) and button(3 DOWNTO 2)="11" THEN
					address_M := address_M + all_digits;
					ELSIF falling_edge(button(2)) and button(3)='1' and button(1)='1' THEN 
					address_M := address_M - all_digits;
					ELSE 
					END IF; 
					Debug_addr <= std_logic_vector(to_unsigned(address_M, 12)); --Convert address_M to an 11 bit unsigned std_logic_vector.
					var_debug_addr := std_logic_vector(to_unsigned(address_M, 12));
					bin_addr_M0 := var_debug_addr & '0';	--Concatenate with 0.
					bin_addr_M1 := var_debug_addr & '1'; 	--Concatenate with 1. 
						CASE switch(0) IS
						WHEN '0' =>
						dig0 <= HEX2disp(SLV2hex(bin_addr_M0(3 DOWNTO 0))); --Display the first address at the first three digits. 
						dig1 <= HEX2disp(SLV2hex(bin_addr_M0(7 DOWNTO 4)));
						dig2 <= HEX2disp(SLV2hex(bin_addr_M0(11 DOWNTO 8)));
						dig3 <= HEX2disp(SLV2hex(bin_addr_M1(3 DOWNTO 0))); --Display the second address at the second three digits. 
						dig4 <= HEX2disp(SLV2hex(bin_addr_M1(7 DOWNTO 4)));
						dig5 <= HEX2disp(SLV2hex(bin_addr_M1(11 DOWNTO 8)));
						WHEN '1' => 				--If switch(0) is 1, display the content of a memory address. 
						dig0 <= HEX2disp(SLV2hex(Debug_data(3 DOWNTO 0)));
						dig1 <= HEX2disp(SLV2hex(Debug_data(7 DOWNTO 4)));
						dig2 <= HEX2disp(SLV2hex(Debug_data(11 DOWNTO 8)));
						dig3 <= HEX2disp(SLV2hex(Debug_data(15 DOWNTO 12)));
 						when others => null;
						END CASE;
					
				WHEN "01" => 
					IF falling_edge(button(1)) and button(3 DOWNTO 2)="11" THEN
					address_R := address_R + all_digits;
					ELSIF falling_edge(button(2)) and button(3)='1' and button(1)='1' THEN
					address_R := address_R - all_digits;
					ELSE 
					END IF;
					Debug_addr_REG <= std_logic_vector(to_unsigned(address_R, 6));
					Var_debug_addr_REG := std_logic_vector(to_unsigned(address_R, 6));
						CASE switch(0) IS 
						WHEN '0' => 
						dig0 <= HEX2disp(SLV2hex(Var_debug_addr_REG(3 DOWNTO 0)));
						dig1 <= HEX2disp(SLV2hex("00" & Var_debug_addr_REG(5 DOWNTO 4)));
						WHEN '1' => 
						dig0 <= HEX2disp(SLV2hex(Debug_data_REG(3 DOWNTO 0)));
						dig1 <= HEX2disp(SLV2hex(Debug_data_REG(7 DOWNTO 4)));
						dig2 <= HEX2disp(SLV2hex(Debug_data_REG(11 DOWNTO 8)));
						dig3 <= HEX2disp(SLV2hex(Debug_data_REG(15 DOWNTO 12)));
						when others => null;
						END CASE;
				WHEN "10" =>
				dig0 <= HEX2disp(SLV2hex(BRANCH_CONDITIONS));	

				
				WHEN "11" => 
					IF falling_edge(button(1)) and button(3 DOWNTO 2)="11" THEN
					var_micro_addr := var_micro_addr + all_digits;
					micro_addr <= var_micro_addr;
					ELSIF falling_edge(button(2)) and button(3)='1' and button(1)='1' THEN 
					var_micro_addr := var_micro_addr - all_digits;
					micro_addr <= var_micro_addr;
					ELSE 
					END IF;
					bin_micro_addr := std_logic_vector(to_unsigned(var_micro_addr, 12));
						CASE switch(0) IS 
						WHEN '0' =>
						dig0 <= HEX2disp(SLV2hex(bin_micro_addr(3 DOWNTO 0)));
						dig1 <= HEX2disp(SLV2hex(bin_micro_addr(7 DOWNTO 4)));
						dig2 <= HEX2disp(SLV2hex(bin_micro_addr(11 DOWNTO 8)));
						WHEN '1' => 
						dig0 <= HEX2disp(SLV2hex(cont_micro_addr(3 DOWNTO 0)));
						dig1 <= HEX2disp(SLV2hex(cont_micro_addr(7 DOWNTO 4)));
						dig2 <= HEX2disp(SLV2hex(cont_micro_addr(11 DOWNTO 8)));
						dig3 <= HEX2disp(SLV2hex(cont_micro_addr(15 DOWNTO 12))); 
						when others => null;
						END CASE;
				when others => null;
				END CASE;
		ELSE 		
		END IF;			
	END process; 		
END bhv;		

