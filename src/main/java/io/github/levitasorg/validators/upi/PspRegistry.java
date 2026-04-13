package io.github.levitasorg.validators.upi;

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
 * Registry of known UPI PSP (Payment Service Provider) handles, loaded from the
 * bundled {@code levitas/upi-handles.json} resource.
 *
 * <p>Handles are stored and looked up in lowercase. All lookups are thread-safe.
 *
 * <p>Usage:
 * <pre>{@code
 * Optional<String> psp = PspRegistry.getPspName("okhdfcbank");  // "Google Pay (HDFC)"
 * Set<String> handles = PspRegistry.getAllHandles();
 * }</pre>
 */
public final class PspRegistry {

    private PspRegistry() {
        // utility class
    }

    private static final Map<String, String> HANDLE_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        try (InputStream in = PspRegistry.class.getClassLoader()
                .getResourceAsStream("levitas/upi-handles.json")) {
            if (in == null) {
                throw new IllegalStateException("Bundled resource levitas/upi-handles.json not found on classpath");
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(in);
            JsonNode handles = root.get("handles");
            if (handles != null) {
                handles.fields().forEachRemaining(entry ->
                        map.put(entry.getKey().toLowerCase(), entry.getValue().asText()));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load levitas/upi-handles.json", e);
        }
        HANDLE_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Returns the PSP display name for the given UPI handle.
     *
     * <p>The lookup is case-insensitive — the handle is normalised to lowercase before lookup.
     *
     * @param handle the UPI handle (the part after '@'); may be null
     * @return an {@link Optional} containing the PSP name, or empty if not recognised
     */
    public static Optional<String> getPspName(String handle) {
        if (handle == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(HANDLE_MAP.get(handle.toLowerCase()));
    }

    /**
     * Returns an unmodifiable set of all known UPI handles (in lowercase).
     *
     * @return immutable set of handle strings
     */
    public static Set<String> getAllHandles() {
        return HANDLE_MAP.keySet();
    }
}
