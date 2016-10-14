package com.fabahaba.encode;

import java.util.Arrays;
import java.util.PrimitiveIterator;

public final class JHex {

  private JHex() {}

  private static final char[] LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f'};

  private static final char[] UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F'};

  static final byte[] LOWER_BYTES = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f'};

  static final byte[] UPPER_BYTES = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F'};

  static final int[] DIGITS = new int[103];

  static {
    Arrays.fill(DIGITS, -1);
    for (final char c : LOWER) {
      DIGITS[c] = Character.digit(c, 16);
    }
    for (int i = 10;i < UPPER.length;++i) {
      final char c = UPPER[i];
      DIGITS[c] = Character.digit(c, 16);
    }
  }

  public static String encode(final byte[] data) {
    return new String(encode(data, LOWER));
  }

  public static String encodeUpper(final byte[] data) {
    return new String(encode(data, UPPER));
  }

  private static char[] encode(byte[] data, char[] alpha) {
    final int len = data.length;
    final char[] hex = new char[len << 1];
    for (int i = 0, h = 0, d;i < len;) {
      d = data[i++] & 0xff;
      hex[h++] = alpha[d >>> 4];
      hex[h++] = alpha[d & 0xf];
    }
    return hex;
  }

  static byte[] encode(byte[] data, byte[] alpha) {
    final int len = data.length;
    final byte[] hex = new byte[len << 1];
    for (int i = 0, h = 0, d;i < len;) {
      d = data[i++] & 0xff;
      hex[h++] = alpha[d >>> 4];
      hex[h++] = alpha[d & 0xf];
    }
    return hex;
  }

  public static char[] encodeChars(final byte[] data) {
    return encode(data, LOWER);
  }

  public static char[] encodeUpperChars(final byte[] data) {
    return encode(data, UPPER);
  }

  public static String encode(final byte[] data, int offset, final int len) {
    return new String(encode(data, offset, len, LOWER));
  }

  public static String encodeUpper(final byte[] data, int offset, final int len) {
    return new String(encode(data, offset, len, UPPER));
  }

  private static char[] encode(final byte[] data, int offset, final int len, final char[] alpha) {
    if (len == 0) {
      return new char[0];
    }
    final char[] hex = new char[len << 1];
    for (int i = 0, d;;++offset) {
      d = data[offset] & 0xff;
      hex[i++] = alpha[d >>> 4];
      hex[i++] = alpha[d & 0xf];
      if (i == hex.length) {
        return hex;
      }
    }
  }

  public static byte[] encodeBytes(final byte[] data) {
    return encode(data, LOWER_BYTES);
  }

  public static byte[] encodeUpperBytes(final byte[] data) {
    return encode(data, UPPER_BYTES);
  }

  static byte[] encode(final byte[] data, int offset, final int len, final byte[] alpha) {
    if (len == 0) {
      return new byte[0];
    }
    final byte[] hex = new byte[len << 1];
    for (int i = 0, d;;++offset) {
      d = data[offset] & 0xff;
      hex[i++] = alpha[d >>> 4];
      hex[i++] = alpha[d & 0xf];
      if (i == hex.length) {
        return hex;
      }
    }
  }

  public static String encodeReverse(final byte[] data, int offset, final int len) {
    return new String(encodeReverse(data, offset, len, LOWER));
  }

  public static String encodeUpperReverse(final byte[] data, int offset, final int len) {
    return new String(encodeReverse(data, offset, len, UPPER));
  }

  private static char[] encodeReverse(final byte[] data, int offset, final int len,
      final char[] alpha) {
    if (len == 0) {
      return new char[0];
    }
    final char[] hex = new char[len << 1];
    for (int i = 0, d;;--offset) {
      d = data[offset] & 0xff;
      hex[i++] = alpha[d >>> 4];
      hex[i++] = alpha[d & 0xf];
      if (i == hex.length) {
        return hex;
      }
    }
  }

  static byte[] encodeReverse(final byte[] data, int offset, final int len, final byte[] alpha) {
    if (len == 0) {
      return new byte[0];
    }
    final byte[] hex = new byte[len << 1];
    for (int i = 0, d;;--offset) {
      d = data[offset] & 0xff;
      hex[i++] = alpha[d >>> 4];
      hex[i++] = alpha[d & 0xf];
      if (i == hex.length) {
        return hex;
      }
    }
  }

  public static char[] encodeChars(final byte[] data, int offset, final int len) {
    return encode(data, offset, len, LOWER);
  }

  public static char[] encodeUpperChars(final byte[] data, int offset, final int len) {
    return encode(data, offset, len, UPPER);
  }

  public static boolean isValid(final String hex) {
    if (hex == null) {
      return false;
    }
    final int len = hex.length();
    if ((len & 1) != 0) {
      return false;
    }
    if (len == 0) {
      return true;
    }
    final char[] chars = hex.toCharArray();
    int index = 0;
    do {
      char chr = chars[index++];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        return false;
      }
      chr = chars[index++];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        return false;
      }
    } while (index < len);
    return true;
  }

  public static boolean isLengthValid(final String hex) {
    return hex != null && (hex.length() & 1) == 0;
  }

  public static byte[] decode(final String hex) {
    return decode(hex.toCharArray());
  }

  public static byte[] decode(final char[] chars) {
    final byte[] data = new byte[chars.length >> 1];
    for (int i = 0, c = 0;;++c) {
      data[i++] = (byte) (DIGITS[chars[c]] << 4 | DIGITS[chars[++c]]);
      if (i == data.length) {
        return data;
      }
    }
  }

  public static byte[] decode(final byte[] chars) {
    final byte[] data = new byte[chars.length >> 1];
    for (int i = 0, c = 0;;++c) {
      data[i++] = (byte) (DIGITS[chars[c]] << 4 | DIGITS[chars[++c]]);
      if (i == data.length) {
        return data;
      }
    }
  }

  public static byte[] decodeChecked(final String hex) {
    return decodeChecked(hex.toCharArray());
  }

  public static byte[] decodeChecked(final char[] chars) {
    if (chars.length == 0) {
      return new byte[0];
    }
    if ((chars.length & 1) != 0) {
      throw new IllegalStateException("Hex encoding must have an even length.");
    }
    final byte[] data = new byte[chars.length >> 1];
    for (int i = 0, c = 0;;++c) {
      char chr = chars[c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c, chars));
      }
      int bite = DIGITS[chr] << 4;
      chr = chars[++c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c, chars));
      }
      data[i++] = (byte) (bite | DIGITS[chr]);
      if (i == data.length) {
        return data;
      }
    }
  }

  public static byte[] decodeChecked(final byte[] chars) {
    if (chars.length == 0) {
      return new byte[0];
    }
    if ((chars.length & 1) != 0) {
      throw new IllegalStateException("Hex encoding must have an even length.");
    }
    final byte[] data = new byte[chars.length >> 1];
    for (int i = 0, c = 0;;++c) {
      byte chr = chars[c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c));
      }
      int bite = DIGITS[chr] << 4;
      chr = chars[++c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c));
      }
      data[i++] = (byte) (bite | DIGITS[chr]);
      if (i == data.length) {
        return data;
      }
    }
  }

  private static String formatExMsg(final int pos, final char[] hex) {
    return formatExMsg(pos, new String(hex));
  }

  private static String formatExMsg(final int pos, final String hex) {
    return String.format("Invalid character for hex encoding at position %d for '%s'.", pos, hex);
  }

  private static String formatExMsg(final int pos) {
    return String.format("Invalid character for hex encoding at position %d.", pos);
  }

  public static void decode(final String hex, final byte[] out, int offset) {
    decode(hex.toCharArray(), out, offset);
  }

  public static void decode(final char[] chars, final byte[] out, int offset) {
    for (int c = 0;;++offset) {
      out[offset] = (byte) (DIGITS[chars[c++]] << 4 | DIGITS[chars[c++]]);
      if (c == chars.length) {
        return;
      }
    }
  }

  public static void decodeChecked(final String hex, final byte[] out, int offset) {
    decodeChecked(hex.toCharArray(), out, offset);
  }

  public static void decodeChecked(final char[] chars, final byte[] out, int offset) {
    if (chars.length == 0) {
      return;
    }
    if ((chars.length & 1) != 0) {
      throw new IllegalStateException("Hex encoding must have an even length.");
    }
    for (int c = 0;;++offset) {
      char chr = chars[c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c, chars));
      }
      int bite = DIGITS[chr] << 4;
      chr = chars[++c];
      if (chr >= DIGITS.length || DIGITS[chr] == -1) {
        throw new IllegalStateException(formatExMsg(c, chars));
      }
      out[offset] = (byte) (bite | DIGITS[chr]);
      if (++c == chars.length) {
        return;
      }
    }
  }

  public static byte[] decodePrimIter(final CharSequence hex) {
    final byte[] data = new byte[hex.length() >> 1];
    final PrimitiveIterator.OfInt chars = hex.chars().iterator();
    int index = 0;
    do {
      data[index++] = (byte) (DIGITS[chars.nextInt()] << 4 | DIGITS[chars.nextInt()]);
    } while (index < data.length);
    return data;
  }

  public static void decodePrimIter(final CharSequence hex, final byte[] out, int offset) {
    final PrimitiveIterator.OfInt chars = hex.chars().iterator();
    final int max = offset + (hex.length() >> 1);
    do {
      out[offset++] = (byte) (DIGITS[chars.nextInt()] << 4 | DIGITS[chars.nextInt()]);
    } while (offset < max);
  }
}
