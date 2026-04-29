package es.upm.miw.base64url;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public final class Base64UrlGenerator {

    private static final int UUID_BYTES = 16;
    private static final int TOKEN_BYTES = 32; // 256 bits

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    private Base64UrlGenerator() {
        // Utility class
    }

    public static String encode() {
        return encode(UUID.randomUUID());
    }

    public static String encode(UUID value) {
        ByteBuffer buffer = ByteBuffer.allocate(UUID_BYTES)
                .putLong(value.getMostSignificantBits())
                .putLong(value.getLeastSignificantBits());

        return ENCODER.encodeToString(buffer.array());
    }

    public static UUID decode(String value) {
        byte[] bytes = DECODER.decode(value);

        if (bytes.length != UUID_BYTES) {
            throw new IllegalArgumentException("Invalid UUID Base64 value");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        return new UUID(
                buffer.getLong(),
                buffer.getLong()
        );
    }

    public static String token() {
        byte[] bytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);

        return ENCODER.encodeToString(bytes);
    }
}