library ieee;
use ieee.std_logic_1164.all;
use std.textio.all;

entity textio is
end entity;

architecture syn of textio is
begin
  process is
    variable line_v : line;
    file read_file : text;
    file write_file : text;
    variable slv_v : std_logic_vector(44 * 4 - 1 downto 0);
  begin
    file_open(read_file, "source.txt", read_mode);
    file_open(write_file, "target.txt", write_mode);
    while not endfile(read_file) loop
      readline(read_file, line_v);
      hread(line_v, slv_v);
      report "slv_v: " & to_hstring(slv_v);
      hwrite(line_v, slv_v);
      writeline(write_file, line_v);
    end loop;
    file_close(read_file);
    file_close(write_file);
    wait;
  end process;
end architecture;
