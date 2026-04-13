package com.example.demo;

import io.github.levitasorg.validators.aadhaar.AadhaarMasker;
import io.github.levitasorg.validators.aadhaar.AadhaarValidator;
import io.github.levitasorg.validators.gstin.GstinInfo;
import io.github.levitasorg.validators.gstin.GstinValidator;
import io.github.levitasorg.validators.ifsc.IfscValidator;
import io.github.levitasorg.validators.pan.PanEntityType;
import io.github.levitasorg.validators.pan.PanValidator;
import io.github.levitasorg.validators.upi.VpaValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller exposing validation endpoints using the levitas-validators library.
 *
 * <p>Available endpoints:
 * <ul>
 *   <li>{@code GET /validate/gstin/{gstin}} — validates GSTIN and returns parsed info</li>
 *   <li>{@code GET /validate/pan/{pan}} — validates PAN and returns entity type</li>
 *   <li>{@code GET /validate/ifsc/{ifsc}} — validates IFSC and returns bank name</li>
 *   <li>{@code GET /validate/vpa/{vpa}} — validates UPI VPA and returns PSP name</li>
 *   <li>{@code POST /mask/aadhaar} — masks an Aadhaar number</li>
 * </ul>
 */
@RestController
@RequestMapping
public class ValidationController {

    /**
     * Validates a GSTIN and returns its parsed components.
     *
     * <p>Example: {@code GET /validate/gstin/29AAAAA0000A1ZX}
     *
     * @param gstin the GSTIN to validate (path variable)
     * @return JSON with {@code valid}, {@code info} (if valid), or {@code error} (if invalid)
     */
    @GetMapping("/validate/gstin/{gstin}")
    public ResponseEntity<Map<String, Object>> validateGstin(@PathVariable String gstin) {
        Map<String, Object> response = new HashMap<>();
        boolean valid = GstinValidator.isValid(gstin);
        response.put("valid", valid);
        if (valid) {
            GstinInfo info = GstinValidator.parse(gstin);
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("stateCode", info.getStateCode().getCode());
            infoMap.put("stateName", info.getStateCode().getDisplayName());
            infoMap.put("pan", info.getPan());
            infoMap.put("entityCode", info.getEntityCode());
            infoMap.put("entityType", info.getEntityType().name());
            response.put("info", infoMap);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Validates a PAN and returns its entity type.
     *
     * <p>Example: {@code GET /validate/pan/AAAPA1234A}
     *
     * @param pan the PAN to validate (path variable)
     * @return JSON with {@code valid} and {@code entityType} (if valid)
     */
    @GetMapping("/validate/pan/{pan}")
    public ResponseEntity<Map<String, Object>> validatePan(@PathVariable String pan) {
        Map<String, Object> response = new HashMap<>();
        boolean valid = PanValidator.isValid(pan);
        response.put("valid", valid);
        if (valid) {
            PanValidator.getEntityType(pan).ifPresent(entityType -> {
                response.put("entityType", entityType.name());
                response.put("entityDescription", entityType.getDescription());
            });
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Validates an IFSC code and returns the bank name.
     *
     * <p>Example: {@code GET /validate/ifsc/HDFC0001234}
     *
     * @param ifsc the IFSC to validate (path variable)
     * @return JSON with {@code valid}, {@code bankCode}, and {@code bankName} (if known)
     */
    @GetMapping("/validate/ifsc/{ifsc}")
    public ResponseEntity<Map<String, Object>> validateIfsc(@PathVariable String ifsc) {
        Map<String, Object> response = new HashMap<>();
        boolean valid = IfscValidator.isValid(ifsc);
        response.put("valid", valid);
        if (valid) {
            IfscValidator.getBankCode(ifsc).ifPresent(code -> response.put("bankCode", code));
            IfscValidator.getBankName(ifsc).ifPresent(name -> response.put("bankName", name));
            IfscValidator.getBranchCode(ifsc).ifPresent(branch -> response.put("branchCode", branch));
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Validates a UPI VPA and returns the PSP name.
     *
     * <p>Example: {@code GET /validate/vpa/user@okhdfcbank}
     *
     * @param vpa the VPA to validate (path variable)
     * @return JSON with {@code valid}, {@code handle}, and {@code pspName} (if known)
     */
    @GetMapping("/validate/vpa/{vpa}")
    public ResponseEntity<Map<String, Object>> validateVpa(@PathVariable String vpa) {
        Map<String, Object> response = new HashMap<>();
        boolean valid = VpaValidator.isValid(vpa);
        response.put("valid", valid);
        if (valid) {
            VpaValidator.getUsername(vpa).ifPresent(u -> response.put("username", u));
            VpaValidator.getHandle(vpa).ifPresent(h -> response.put("handle", h));
            VpaValidator.getPspName(vpa).ifPresent(p -> response.put("pspName", p));
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Masks an Aadhaar number for safe display or logging.
     *
     * <p>Example: {@code POST /mask/aadhaar} with body {@code {"aadhaar": "234123412346"}}
     *
     * @param request request body containing the {@code aadhaar} field
     * @return JSON with {@code masked} (in {@code "XXXX-XXXX-NNNN"} format)
     */
    @PostMapping("/mask/aadhaar")
    public ResponseEntity<Map<String, Object>> maskAadhaar(
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String aadhaar = request.get("aadhaar");
        if (aadhaar == null || aadhaar.isBlank()) {
            response.put("error", "aadhaar field is required");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            String masked = AadhaarMasker.mask(aadhaar);
            response.put("masked", masked);
            response.put("valid", AadhaarValidator.isValid(aadhaar));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
