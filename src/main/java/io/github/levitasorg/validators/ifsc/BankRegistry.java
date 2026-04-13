package io.github.levitasorg.validators.ifsc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Registry of known Indian bank codes loaded from the bundled {@code levitas/ifsc-banks.json} resource.
 *
 * <p>The registry is loaded once at class initialization and stored as an immutable map.
 * All lookups are thread-safe.
 *
 * <p>Usage:
 * <pre>{@code
 * Optional<String> name = BankRegistry.getBankName("HDFC");  // "HDFC Bank"
 * Set<String> codes = BankRegistry.getAllBankCodes();
 * }</pre>
 */
public final class BankRegistry {

    private BankRegistry() {
        // utility class
    }

    private static final Map<String, String> BANK_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        try (InputStream in = BankRegistry.class.getClassLoader()
                .getResourceAsStream("levitas/ifsc-banks.json")) {
            if (in == null) {
                throw new IllegalStateException("Bundled resource levitas/ifsc-banks.json not found on classpath");
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(in);
            JsonNode banks = root.get("banks");
            if (banks != null) {
                banks.fields().forEachRemaining(entry ->
                        map.put(entry.getKey(), entry.getValue().asText()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load levitas/ifsc-banks.json", e);
        }
        BANK_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Returns the display name of the bank identified by the given 4-character bank code.
     *
     * @param bankCode the 4-character IFSC bank code (e.g. {@code "HDFC"}); may be null
     * @return an {@link Optional} containing the bank name, or empty if not found
     */
    public static Optional<String> getBankName(String bankCode) {
        if (bankCode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(BANK_MAP.get(bankCode.toUpperCase()));
    }

    /**
     * Returns an unmodifiable set of all bank codes known to this registry.
     *
     * @return immutable set of 4-character bank code strings
     */
    public static Set<String> getAllBankCodes() {
        return BANK_MAP.keySet();
    }
}
