-- In this version scancode is read on the negative edge of kbclock.
-- It counts the number of negative edges. IF 11 edges are detected the byte is read and byte_read is '1'.
LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
ENTITY testbench IS
END testbench;

ARCHITECTURE test OF testbench IS

constant t: time := 20 ns; -- set clock time
constant count: integer := 1000; -- set clock cycle amount
signal i: integer := 0; -- loop variable

signal clk : std_logic := '0'; -- input
signal reset : std_logic := '0'; -- input
--signal microcode : std_logic_vector(15 downto 0); -- input

BEGIN

processor: entity work.processor port map(clk => clk, reset => reset);

reset <= '0', '1' after t/2; -- reset = 0 for first clock cycle, then 1

process begin
	clk <= '0';
	wait for t/2;
	clk <= '1';
	wait for t/2;

	if (i = count) then
		wait;
	else
		i <= i + 1; 
	end if;
end process;

--process begin
--	reset <= '0', '1' after t/2; -- reset = 0 for first clock cycle, then 1
--	microcode <= "1110100000000001"; -- instruction = generate constant integer 1
--	wait for 2 ms;
--	microcode <= "1000111111000001"; -- instruction = move generated constant to r1
--	wait for 2 ms;
---	microcode <= "1110000000000001"; -- instruction = invert constant saved to r1
--	wait for 2 ms;
--	microcode <= "1000111111000001"; -- instruction = move inverted constant to r1
--	wait for 2 ms;
--	microcode <= "1110100000000001"; -- instruction = generate constant integer 1
--	wait for 2 ms;
--	microcode <= "1000111111000010"; -- instruction = move generated constant to r2
--	wait for 2 ms;
--	microcode <= "1011000010000001"; -- instruction = add r1 and r2
--	wait for 2 ms;
--	microcode <= "1000111111000011"; -- instruction = move generated result to r3
--	wait for 2 ms;
--	microcode <= "1110100110010001"; -- instruction = generate constant integer 401
--	wait for 2 ms;
--	microcode <= "1000111111000100"; -- instruction = move generated constant to r4
---	wait for 2 ms;
--	microcode <= "1110100000010001"; -- instruction = generate constant integer 17
--	wait for 2 ms;
--	microcode <= "1000111111000101"; -- instruction = move generated constant to r5
--	wait for 2 ms;
--	microcode <= "1001000100000101"; -- instruction = r4 AND r5
--	wait for 2 ms;
--	microcode <= "1000111111000110"; -- instruction = move generated result to r6
--	wait for 2 ms;
--	microcode <= "1110100000001010"; -- instruction = generate constant integer 10
--	wait for 2 ms;
--	microcode <= "1000111111000111"; -- instruction = move generated constant to r7
--	wait for 2 ms;
--	microcode <= "1010000101000111"; -- instruction = r5 OR r7
--	wait for 2 ms;
--	microcode <= "1000111111001000"; -- instruction = move generated result to r8
--	wait for 2 ms;
--	microcode <= "1100000010001000"; -- instruction = shift r8 r3 amount of times to the left
--	wait for 2 ms;
--	microcode <= "1000111111001001"; -- instruction = move generated result to r9
--	wait for 2 ms;
--	microcode <= "1101000010001001"; -- instruction = shift r9 r3 amount of times to the right
--	wait for 2 ms;
--	microcode <= "1000111111001010"; -- instruction = move generated result to r10
--	wait for 2 ms;
--	microcode <= "1110010000001010"; -- instruction = invert and add 1 to r10 
--	wait for 2 ms;
--	microcode <= "1000111111001011"; -- instruction = move result to r11
--	wait for 2 ms;
--	microcode <= "0100001011000010"; -- instruction = store r11 to address stored in r2 aka m1
--	wait for 2 ms;
--	microcode <= "0000001100000010"; -- instruction = load m1 to r12
--	wait for 2 ms;
--	microcode <= "----------------";
--	wait;
--end process;

END test;