# MIPS to Machine Code Converter (Java GUI)

## What is this?

This is a Java application with a simple graphical interface (GUI) that converts **MIPS assembly instructions** into **32-bit machine code** in hexadecimal format. It shows each instruction's memory address and its corresponding machine code.

I made this tool while studying computer architecture to help understand how MIPS assembly is translated into machine code.

---

## Features

- Converts MIPS assembly code to 32-bit hex machine code
- Supports R-type, I-type, and J-type MIPS instructions
- Handles labels and calculates branch/jump addresses
- Shows memory addresses starting from `0x00400000`
- Simple and easy-to-use GUI built with Java Swing

---

## 🛠️ Supported Instructions

### R-Type:
- `add`, `sub`, `and`, `or`
- `sll`, `srl`, `sllv`, `srlv`

### I-Type:
- `addi`, `andi`
- `lw`, `sw`
- `beq`, `bne`, `blez`, `bgtz`

### J-Type:
- `j`, `jal`

---
## Example

Here is a sample MIPS assembly code and what the output looks like in the app.

### Input:

- add $t0, $t1, $t2
- sub $s1, $s2, $s3
- andi $a0, $a1, 15

### Output:

- Address        Machine Code 
- 0x00400000     012A6020   
- 0x00400004     0x02123022    
- 0x00400008     0x3131000F    

## How to Run:

1. Make sure you have **Java (JDK 8 or later)** installed.
2. Download or clone this project to your computer.
3. Open a terminal or command prompt in the project folder.
4. Compile the Java file:

   ```bash
   javac MIPSConverterGUI.java
