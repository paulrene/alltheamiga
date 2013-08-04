package com.alltheamiga.utils;

import miggy.cpu.DecodedInstruction;
import miggy.cpu.Disassembler;
import miggy.cpu.InstructionDecoder;

public class StringDisassembler implements Disassembler {

    StringBuilder builder = new StringBuilder();
    
    public int disassemble(int address) {
        DecodedInstruction di = InstructionDecoder.decode(address);
        builder.append(di.toString()).append("\n");
        return address + 2 + di.src().offset() + di.dst().offset();
    }
    
    public String toString() {
        return builder.toString();
    }

}
