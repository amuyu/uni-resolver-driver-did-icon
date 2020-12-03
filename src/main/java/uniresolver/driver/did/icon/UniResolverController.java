package uniresolver.driver.did.icon;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniresolver.result.ResolveResult;

import java.util.Map;

@RestController
@RequestMapping("1.0")
@RequiredArgsConstructor
public class UniResolverController {

    final UniResolverService uniResolverService;

    @GetMapping(value = "/identifiers/{did}", produces = "application/ld+json;profile=\"https://w3id.org/did-resolution\"")
    public ResponseEntity getDid(@PathVariable String did) {
        try {
            ResolveResult result = uniResolverService.resolve(did);
            String json = result.toJson();
            if (json == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No resolve result for " + did);
            }
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Driver reported for " + did + ": " + e.getMessage());
        }
    }

    @GetMapping(value = "/properties", produces = "application/json;")
    public ResponseEntity getDid() {
        try {
            Map<String, Object> result = uniResolverService.properties();
            String json = new Gson().toJson(result);
            if (json == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No properties.");
            }
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Properties problem: " + e.getMessage());
        }
    }
}
