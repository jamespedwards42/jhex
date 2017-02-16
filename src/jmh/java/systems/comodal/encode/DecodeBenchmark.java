package systems.comodal.encode;

import com.google.common.io.BaseEncoding;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
@Threads(1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
public class DecodeBenchmark {

  private static final int NUM_ELEMENTS = 1 << 21;
  private static final int MASK = NUM_ELEMENTS - 1;
  private static final int ELEMENT_LENGTH = 32;
  private final String[] hexStrings = new String[NUM_ELEMENTS];
  @Param({
      "JHEX_TO_CHAR_ARRAY",
      "JHEX_CHAR_AT",
      "JHEX_CHAR_ITERATOR",
      "JHEX_TO_CHAR_ARRAY_CHECKED",
      "JHEX_CHAR_AT_CHECKED",
      "JHEX_CHAR_ITERATOR_CHECKED",
      "GUAVA",
      "COMMONS_CODEC",
      "JMX_DATATYPE_CONVERTER",
      "BC"
  })
  private DecodeFactory decodeType;
  private Function<String, byte[]> decodeFunction;

  static void shuffleArray(final Object[] array) {
    final ThreadLocalRandom rand = ThreadLocalRandom.current();
    for (int i = array.length - 1; i > 0; --i) {
      int index = rand.nextInt(i + 1);
      final Object swap = array[i];
      array[i] = array[index];
      array[index] = swap;
    }
  }

  @Setup(Level.Iteration)
  public void setup(final ThreadState threadState) {
    threadState.index = 0;
    if (decodeFunction == null) {
      decodeFunction = decodeType.createDecodeFunction();
      final ThreadLocalRandom random = ThreadLocalRandom.current();
      for (int i = 0; i < NUM_ELEMENTS; i++) {
        final byte[] element = new byte[ELEMENT_LENGTH];
        random.nextBytes(element);
        hexStrings[i] = JHex.encode(element);
      }
    } else {
      shuffleArray(hexStrings);
    }
  }

  @Benchmark
  public byte[] run(final ThreadState threadState) {
    return decodeFunction.apply(hexStrings[threadState.index++ & MASK]);
  }

  public enum DecodeFactory {

    JHEX_TO_CHAR_ARRAY {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decodeToCharArray;
      }
    },
    JHEX_CHAR_AT {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decode;
      }
    },
    JHEX_CHAR_ITERATOR {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decodePrimIter;
      }
    },
    JHEX_TO_CHAR_ARRAY_CHECKED {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decodeCheckedToCharArray;
      }
    },
    JHEX_CHAR_AT_CHECKED {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decodeChecked;
      }
    },
    JHEX_CHAR_ITERATOR_CHECKED {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return JHex::decodePrimIterChecked;
      }
    },
    COMMONS_CODEC {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return hex -> {
          try {
            return Hex.decodeHex(hex.toCharArray());
          } catch (DecoderException e) {
            throw new RuntimeException(e);
          }
        };
      }
    },
    GUAVA {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        final BaseEncoding coder = BaseEncoding.base16().lowerCase();
        return coder::decode;
      }
    },
    JMX_DATATYPE_CONVERTER {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return DatatypeConverter::parseHexBinary;
      }
    },
    BC {
      @Override
      public Function<String, byte[]> createDecodeFunction() {
        return org.bouncycastle.util.encoders.Hex::decode;
      }
    };

    public abstract Function<String, byte[]> createDecodeFunction();
  }
}
