package com.fabahaba.encode;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class HexEncodeTest {

  static final String TEST_HEX =
      "4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b";

  @Test
  public void decodeEncodeLower() {
    final byte[] lower = JHex.decode(TEST_HEX);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeBytesEncodeLower() {
    final byte[] lower = JHex.decode(TEST_HEX.getBytes(StandardCharsets.US_ASCII));
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeByteBufferEncodeLower() {
    final byte[] lower = JHex.decode(ByteBuffer.wrap(TEST_HEX.getBytes(StandardCharsets.US_ASCII)));
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeCheckedEncodeLower() {
    final byte[] lower = JHex.decodeChecked(TEST_HEX);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeCheckedBytesEncodeLower() {
    final byte[] lower = JHex.decodeChecked(TEST_HEX.getBytes(StandardCharsets.US_ASCII));
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeCheckedByteBufferEncodeLower() {
    final byte[] lower = JHex
        .decodeChecked(ByteBuffer.wrap(TEST_HEX.getBytes(StandardCharsets.US_ASCII)));
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeOffsetEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decode(TEST_HEX, lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeOffsetCheckedEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decodeChecked(TEST_HEX, lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeOffsetCheckedBytesEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decodeChecked(TEST_HEX.getBytes(StandardCharsets.US_ASCII), lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeOffsetCheckedByteBufferEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decodeChecked(ByteBuffer.wrap(TEST_HEX.getBytes(StandardCharsets.US_ASCII)), lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodeEncodeLowerOffset() {
    final byte[] lower = JHex.decode(TEST_HEX);
    assertEquals(TEST_HEX, JHex.encode(lower, 0, lower.length));
  }

  @Test
  public void encodeReverse() {
    final byte[] lower = JHex.decode(TEST_HEX);
    final byte[] reverse = copyReverse(lower);
    assertEquals(TEST_HEX, JHex.encodeReverse(reverse, 31, 32));
  }

  @Test
  public void decodeEncodeUpper() {
    final byte[] lower = JHex.decode(TEST_HEX);
    final String upperHex = TEST_HEX.toUpperCase(Locale.ENGLISH);
    assertEquals(upperHex, JHex.encodeUpper(lower));
    final byte[] upper = JHex.decode(upperHex);
    assertEquals(TEST_HEX, JHex.encode(upper));
    assertEquals(upperHex, JHex.encodeUpper(upper));
  }

  @Test
  public void decodePrimIterOffsetEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decodePrimIter(TEST_HEX, lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodePrimIterEncodeLower() {
    final byte[] lower = JHex.decodePrimIter(TEST_HEX);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodePrimIterEncodeReverse() {
    final byte[] lower = JHex.decodePrimIter(TEST_HEX);
    final byte[] reverse = copyReverse(lower);
    assertEquals(TEST_HEX, JHex.encodeReverse(reverse, 31, 32));
  }

  @Test
  public void decodePrimIterEncodeUpper() {
    final byte[] lower = JHex.decodePrimIter(TEST_HEX);
    final String upperHex = TEST_HEX.toUpperCase(Locale.ENGLISH);
    assertEquals(upperHex, JHex.encodeUpper(lower));
    final byte[] upper = JHex.decodePrimIter(upperHex);
    assertEquals(TEST_HEX, JHex.encode(upper));
    assertEquals(upperHex, JHex.encodeUpper(upper));
  }

  @Test
  public void decodePrimIterCheckedOffsetEncodeLower() {
    final byte[] lower = new byte[TEST_HEX.length() >> 1];
    JHex.decodePrimIterChecked(TEST_HEX, lower, 0);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  @Test
  public void decodePrimIterCheckedEncodeLower() {
    final byte[] lower = JHex.decodePrimIterChecked(TEST_HEX);
    assertEquals(TEST_HEX, JHex.encode(lower));
  }

  static byte[] copyReverse(final byte[] data) {
    return copyReverse(data, 0, data.length);
  }

  static byte[] copyReverse(final byte[] data, int offset, final int len) {
    offset += len;
    final byte[] bytes = new byte[len];
    for (int i = 0;i < len;) {
      bytes[i++] = data[--offset];
    }
    return bytes;
  }
}
