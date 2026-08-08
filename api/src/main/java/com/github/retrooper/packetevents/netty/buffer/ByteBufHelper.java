/*
 * (头部 License 保持不变)
 */

package com.github.retrooper.packetevents.netty.buffer;

import com.github.retrooper.packetevents.PacketEvents;
import org.jetbrains.annotations.ApiStatus;

import java.nio.charset.Charset;

public class ByteBufHelper {
    private static volatile ByteBufOperator operator;

    @ApiStatus.Internal
    public static void clearOperatorCache() {
        operator = null;
    }

    private static ByteBufOperator op() {
        ByteBufOperator current = operator;
        if (current == null) {
            current = PacketEvents.getAPI().getNettyManager().getByteBufOperator();
            operator = current;
        }
        return current;
    }


    public static int capacity(Object buffer) {
        return op().capacity(buffer);
    }

    public static Object capacity(Object buffer, int capacity) {
        return op().capacity(buffer, capacity);
    }

    public static int readerIndex(Object buffer) {
        return op().readerIndex(buffer);
    }

    public static Object readerIndex(Object buffer, int readerIndex) {
        return op().readerIndex(buffer, readerIndex);
    }

    public static int writerIndex(Object buffer) {
        return op().writerIndex(buffer);
    }

    public static Object writerIndex(Object buffer, int writerIndex) {
        return op().writerIndex(buffer, writerIndex);
    }

    public static int readableBytes(Object buffer) {
        return op().readableBytes(buffer);
    }

    public static int writableBytes(Object buffer) {
        return op().writableBytes(buffer);
    }

    public static Object clear(Object buffer) {
        return op().clear(buffer);
    }

    public static String toString(Object buffer, int index, int length, Charset charset) {
        return op().toString(buffer, index, length, charset);
    }

    public static byte readByte(Object buffer) {
        return op().readByte(buffer);
    }

    public static void writeByte(Object buffer, int value) {
        op().writeByte(buffer, value);
    }

    public static boolean readBoolean(Object buffer) {
        return op().readBoolean(buffer);
    }

    public static void writeBoolean(Object buffer, boolean value) {
        op().writeBoolean(buffer, value);
    }

    public static short readUnsignedByte(Object buffer) {
        return op().readUnsignedByte(buffer);
    }

    public static char readChar(Object buffer) {
        return op().readChar(buffer);
    }

    public static void writeChar(Object buffer, int value) {
        op().writeChar(buffer, value);
    }

    public static short readShort(Object buffer) {
        return op().readShort(buffer);
    }

    public static int readUnsignedShort(Object buffer) {
        return op().readUnsignedShort(buffer);
    }

    public static void writeShort(Object buffer, int value) {
        op().writeShort(buffer, value);
    }

    public static void writeShortLE(Object buffer, int value) {
        op().writeShortLE(buffer, value);
    }

    public static int readMedium(Object buffer) {
        return op().readMedium(buffer);
    }

    public static void writeMedium(Object buffer, int value) {
        op().writeMedium(buffer, value);
    }

    public static int readInt(Object buffer) {
        return op().readInt(buffer);
    }

    public static void writeInt(Object buffer, int value) {
        op().writeInt(buffer, value);
    }

    public static long readUnsignedInt(Object buffer) {
        return op().readUnsignedInt(buffer);
    }

    public static long readLong(Object buffer) {
        return op().readLong(buffer);
    }

    public static void writeLong(Object buffer, long value) {
        op().writeLong(buffer, value);
    }

    public static float readFloat(Object buffer) {
        return op().readFloat(buffer);
    }

    public static void writeFloat(Object buffer, float value) {
        op().writeFloat(buffer, value);
    }

    public static double readDouble(Object buffer) {
        return op().readDouble(buffer);
    }

    public static void writeDouble(Object buffer, double value) {
        op().writeDouble(buffer, value);
    }

    public static Object getBytes(Object buffer, int index, byte[] destination) {
        return op().getBytes(buffer, index, destination);
    }

    public static short getUnsignedByte(Object buffer, int index) {
        return op().getUnsignedByte(buffer, index);
    }

    public static boolean isReadable(Object buffer) {
        return op().isReadable(buffer);
    }

    public static Object copy(Object buffer) {
        return op().copy(buffer);
    }

    public static Object duplicate(Object buffer) {
        return op().duplicate(buffer);
    }

    public static boolean hasArray(Object buffer) {
        return op().hasArray(buffer);
    }

    public static byte[] array(Object buffer) {
        return op().array(buffer);
    }

    public static Object retain(Object buffer) {
        return op().retain(buffer);
    }

    public static Object retainedDuplicate(Object buffer) {
        return op().retainedDuplicate(buffer);
    }

    public static Object readSlice(Object buffer, int length) {
        return op().readSlice(buffer, length);
    }

    public static Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length) {
        return op().readBytes(buffer, destination, destinationIndex, length);
    }

    public static Object readBytes(Object buffer, int length) {
        return op().readBytes(buffer, length);
    }

    public static Object writeBytes(Object buffer, Object src) {
        return op().writeBytes(buffer, src);
    }

    public static void readBytes(Object buffer, byte[] bytes) {
        op().readBytes(buffer, bytes);
    }

    public static void writeBytes(Object buffer, byte[] bytes) {
        op().writeBytes(buffer, bytes);
    }

    public static void writeBytes(Object buffer, byte[] bytes, int offset, int length) {
        op().writeBytes(buffer, bytes, offset, length);
    }

    public static boolean release(Object buffer) {
        return op().release(buffer);
    }

    public static int refCnt(Object buffer) {
        return op().refCnt(buffer);
    }

    public static Object skipBytes(Object buffer, int length) {
        return op().skipBytes(buffer, length);
    }

    public static Object markReaderIndex(Object buffer) {
        return op().markReaderIndex(buffer);
    }

    public static Object resetReaderIndex(Object buffer) {
        return op().resetReaderIndex(buffer);
    }

    public static Object markWriterIndex(Object buffer) {
        return op().markWriterIndex(buffer);
    }

    public static Object resetWriterIndex(Object buffer) {
        return op().resetWriterIndex(buffer);
    }

    public static Object allocateNewBuffer(Object buffer) {
        return op().allocateNewBuffer(buffer);
    }

    public static int getByteSize(int value) {
        for (int i = 1; i < 5; ++i) {
            if ((value & -1 << i * 7) == 0) {
                return i;
            }
        }
        return 5;
    }

    public static int readVarInt(Object buffer) {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte(buffer);
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public static void writeVarInt(Object buffer, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeByte(buffer, value);
                return;
            }
            writeByte(buffer, (value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public static byte[] copyBytes(Object buffer) {
        byte[] bytes = new byte[readableBytes(buffer)];
        getBytes(buffer, readerIndex(buffer), bytes);
        return bytes;
    }
}