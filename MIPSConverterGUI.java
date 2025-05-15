import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class MIPSConverterGUI {
    // creating maps for registers opcodes
    private static final Map<String, String> registerMap = new HashMap<>();
    private static final Map<String, String> opcodeMap = new HashMap<>();
    private static final Map<String, String> functMap = new HashMap<>();
    private static final long START_ADDRESS = 0x00400000L; // starting memory address for instruv
    static {
        // initializing register map with registernames
        registerMap.put("$zero", "00000");
        registerMap.put("$at", "00001");
        registerMap.put("$v0", "00010");
        registerMap.put("$v1", "00011");
        registerMap.put("$a0", "00100");
        registerMap.put("$a1", "00101");
        registerMap.put("$a2", "00110");
        registerMap.put("$a3", "00111");
        registerMap.put("$t0", "01000");
        registerMap.put("$t1", "01001");
        registerMap.put("$t2", "01010");
        registerMap.put("$t3", "01011");
        registerMap.put("$t4", "01100");
        registerMap.put("$t5", "01101");
        registerMap.put("$t6", "01110");
        registerMap.put("$t7", "01111");
        registerMap.put("$s0", "10000");
        registerMap.put("$s1", "10001");
        registerMap.put("$s2", "10010");
        registerMap.put("$s3", "10011");
        registerMap.put("$s4", "10100");
        registerMap.put("$s5", "10101");
        registerMap.put("$s6", "10110");
        registerMap.put("$s7", "10111");
        registerMap.put("$t8", "11000");
        registerMap.put("$t9", "11001");
        registerMap.put("$k0", "11010");
        registerMap.put("$k1", "11011");
        registerMap.put("$gp", "11100");
        registerMap.put("$sp", "11101");
        registerMap.put("$fp", "11110");
        registerMap.put("$ra", "11111");
        // setting up the opcodes for the instr
        opcodeMap.put("add", "000000");
        opcodeMap.put("sub", "000000");
        opcodeMap.put("and", "000000");
        opcodeMap.put("or", "000000");
        opcodeMap.put("sll", "000000");
        opcodeMap.put("srl", "000000");
        opcodeMap.put("sllv", "000000");
        opcodeMap.put("srlv", "000000");
        opcodeMap.put("addi", "001000");
        opcodeMap.put("andi", "001100");
        opcodeMap.put("lw", "100011");
        opcodeMap.put("sw", "101011");
        opcodeMap.put("beq", "000100");
        opcodeMap.put("bne", "000101");
        opcodeMap.put("blez", "000110");
        opcodeMap.put("bgtz", "000111");
        opcodeMap.put("j", "000010");
        opcodeMap.put("jal", "000011");
        // setting function codes for R-type instruction
        functMap.put("add", "100000");
        functMap.put("sub", "100010");
        functMap.put("and", "100100");
        functMap.put("or", "100101");
        functMap.put("sll", "000000");
        functMap.put("srl", "000010");
        functMap.put("sllv", "000100");
        functMap.put("srlv", "000110");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MIPSConverterGUI::createAndShowGUI); // starting GUI creation
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MIPS to Machine Code Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closing operation
        frame.setSize(850, 300); // setting the size
        frame.setLocationRelativeTo(null); // centering the window
        // creating input panel with label
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        JLabel inputLabel = new JLabel("MIPS Assembly", SwingConstants.CENTER);
        inputLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // setting font for input label
        JTextArea inputArea = new JTextArea(12, 25); // text area for entering assembly code
        inputArea.setLineWrap(true); // enabling line wrapping
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // setting border
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER); // scroll pane for text area
        // creating output panel for showing the results
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        JLabel outputLabel = new JLabel("Address           Machine Code", SwingConstants.CENTER);
        outputLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // setting font for output label
        JTextArea outputArea = new JTextArea(12, 25); // text area for displaying machine code output
        outputArea.setEditable(false); // making output area non-editable
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // setting monospaced font for output area
        outputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // setting border for output area
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER); // scroll pane for output area
        // creating convert button
        JButton convertButton = new JButton("Convert");
        convertButton.setPreferredSize(new Dimension(100, 40)); // setting button size
        convertButton.setFont(new Font("SansSerif", Font.BOLD, 15)); // setting font
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // vertical box layout
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(convertButton);
        buttonPanel.add(Box.createVerticalGlue());
        // arranging input button and output panels in a horizontal layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS)); // horizontal box layout
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // adding padding
        centerPanel.add(inputPanel); // adding input panel
        centerPanel.add(Box.createHorizontalStrut(20)); // adding horizontal space
        centerPanel.add(buttonPanel); // adding button panel
        centerPanel.add(Box.createHorizontalStrut(20)); // adding horizontal space
        centerPanel.add(outputPanel); // adding output panel
        frame.add(centerPanel); // adding center panel to the frame
        frame.setVisible(true); // making the frame visible
        // setting action for convert button
        convertButton.addActionListener((ActionEvent e) -> {
            String assemblyCode = inputArea.getText();
            String[][] result = convertAssemblyToColumns(assemblyCode); // converting assembly code to columns
            String[] addresses = result[0]; // getting addresses
            String[] hexs = result[1]; // getting hex codes
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < addresses.length; i++) {
                builder.append(String.format("%-14s %s\n", addresses[i], hexs[i])); // formatting the output
            }
            outputArea.setText(builder.toString()); // setting the formatted output to the output area
        });
    }

    private static String[][] convertAssemblyToColumns(String assembly) {
        String[] lines = assembly.split("\n"); // splitting the assembly code into lines
        Map<String, Integer> labels = new HashMap<>(); // map to store labels and their line numbers
        ArrayList<String> instr = new ArrayList<>(); // list to store the instr
        int instrC = 0;
        // processing each line to extract labels and instruction
        for (String line : lines) {
            line = line.trim(); // removing leading spaces
            if (line.isEmpty())
                continue; // skip empty lines
            if (line.contains(":")) { // processing label
                String label = line.substring(0, line.indexOf(":")).trim(); // extracting label
                labels.put(label, instrC); // storing label and line number
                String rest = line.substring(line.indexOf(":") + 1).trim(); // extracting instruction
                if (!rest.isEmpty()) {
                    instr.add(rest); // adding instruction to the list
                    instrC++;
                }
            } else {
                instr.add(line); // adding instruction without label
                instrC++;
            }
        }
        ArrayList<String> addrList = new ArrayList<>(); // liso store addresses
        ArrayList<String> codeList = new ArrayList<>(); // list to store machine codes
        long address = START_ADDRESS; // initializing address
        // converting each instruction to machine code
        for (int i = 0; i < instr.size(); i++) {
            String instruction = instr.get(i).trim();
            if (instruction.isEmpty())
                continue; // skip empty instr
            String bin = convertInstruction(instruction, i, labels, instr, address); // converting to binary code
            String hex = binaryToHex(bin); // converting binary code to hex
            addrList.add(String.format("0x%08X", address)); // formatting address as hex
            codeList.add(hex); // adding hex code to the list
            address += 4; // incrementing address by 4
        }
        return new String[][] { addrList.toArray(new String[0]), codeList.toArray(new String[0]) }; // returningaddressesand
                                                                                                    // hex
    }

    // function to convert an instruction to its binary code representation
    private static String convertInstruction(String instruction, int currind, Map<String, Integer> labels,
            ArrayList<String> instr, long address) {
        String[] parts = instruction.replaceAll(",", "").replaceAll("\\s+", " ").split(" "); // splittinginstruction
        String opcode = parts[0].toLowerCase(); // getting opcode
        if (opcode.equals("add") || opcode.equals("sub") || opcode.equals("and") || opcode.equals("or")) {
            String rd = getRegister(parts[1]); // getting destinationregister
            String rs = getRegister(parts[2]);
            String rt = getRegister(parts[3]);
            return opcodeMap.get(opcode) + rs + rt + rd + "00000" + functMap.get(opcode);
        } else if (opcode.equals("sll") || opcode.equals("srl")) {
            String rd = getRegister(parts[1]); // getting destination register
            String rt = getRegister(parts[2]);
            String shamt = String.format("%5s", Integer.toBinaryString(Integer.parseInt(parts[3]))).replace(' ', '0'); // shift
            return opcodeMap.get(opcode) + "00000" + rt + rd + shamt + functMap.get(opcode); // shift instruction format
        } else if (opcode.equals("sllv") || opcode.equals("srlv")) {
            String rd = getRegister(parts[1]); // getting destination register
            String rt = getRegister(parts[2]); // getting target register
            String rs = getRegister(parts[3]); // getting source register
            return opcodeMap.get(opcode) + rs + rt + rd + "00000" + functMap.get(opcode); // R-type format for sllv/srlv
        } else if (opcode.equals("addi") || opcode.equals("andi")) {
            String rt = getRegister(parts[1]);
            String rs = getRegister(parts[2]);
            int imm = Integer.decode(parts[3]);
            String immediate = toSignedBinary(imm, 16); // convertingto signed binary
            return opcodeMap.get(opcode) + rs + rt + immediate; // I-type format for addi/andi
        } else if (opcode.equals("lw") || opcode.equals("sw")) {
            String rt = getRegister(parts[1]); // getting target register
            String offsetBase = parts[2]; // getting offset and base register
            int left = offsetBase.indexOf('(');
            int right = offsetBase.indexOf(')');
            int offset = Integer.parseInt(offsetBase.substring(0, left)); // extracting offset
            String base = getRegister(offsetBase.substring(left + 1, right)); // getting base register
            String immediate = toSignedBinary(offset, 16); // converting offset to binary
            return opcodeMap.get(opcode) + base + rt + immediate; // I-type format for lw/sw
        } else if (opcode.equals("beq") || opcode.equals("bne")) {
            String rs = getRegister(parts[1]); // getting source register
            String rt = getRegister(parts[2]); // getting target register
            String label = parts[3];
            int trgind = labels.get(label);
            int offset = trgind - (currind + 1); // calculating offset
            String immediate = toSignedBinary(offset, 16); // converting offset to binary
            return opcodeMap.get(opcode) + rs + rt + immediate; // I-type format for beq/bne
        } else if (opcode.equals("blez") || opcode.equals("bgtz")) {
            String rs = getRegister(parts[1]);
            String rt = "00000"; // setting rt to 00000 for these instr
            String label = parts[2]; // getting label
            int trgind = labels.get(label);
            int offset = trgind - (currind + 1); // calculating offset
            String immediate = toSignedBinary(offset, 16); // converting offset to binary
            return opcodeMap.get(opcode) + rs + rt + immediate; // I-type format for blez/bgtz
        } else if (opcode.equals("j") || opcode.equals("jal")) {
            String label = parts[1];
            int trgind = labels.get(label);
            long trgadd = START_ADDRESS + trgind * 4; // calculating target address
            long addr = (trgadd >> 2) & 0x03FFFFFFL; // shifting address and masking
            String addressBits = String.format("%26s", Long.toBinaryString(addr)).replace(' ', '0'); // formattingaddress
                                                                                                     // bits
            return opcodeMap.get(opcode) + addressBits; // J-type format for j/jal
        }
        return "00000000000000000000000000000000"; // default zero instruction
    }

    // function to convert binary string to hexadecimal string
    private static String binaryToHex(String binary) {
        long decimal = Long.parseLong(binary, 2); // converting binary to decimal
        return String.format("%08X", decimal); // formatting decimal to hex
    }

    // function to get the binary representation
    private static String getRegister(String reg) {
        return registerMap.getOrDefault(reg, "00000"); // returning the binary value of the register
    }

    // function to convert integer to signed binary string
    private static String toSignedBinary(int value, int bits) {
        int mask = (1 << bits) - 1; // reating a mask for the specified bits
        return String.format("%" + bits + "s", Integer.toBinaryString(value & mask)).replace(' ', '0'); // onverting to
                                                                                                        // signed binary
    }
}
